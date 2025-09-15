package org.firstinspires.ftc.teamcode.hardware;

import static org.firstinspires.ftc.teamcode.configuration.Settings.Hardware.Sorter.TOLERANCE;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.configuration.Settings;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Controls a 3-slot rotating sorter driven by a single Servo.
 * <p>
 * Key changes:
 * - non-blocking sealing: actions that require the launcher transfer to be sealed are queued
 * and only executed after the periodic update() performs the seal.
 * - blocking API for background threads: requireSealBlocking()
 *  TODO this was all up-written by chatgpt 5 to make the requireSeal work. will test w/ robot
 */
public class Sorter {
	private final Servo sorterServo;
	private final Servo launcherTransferServo;
	private final ColorSensor colorSensor;
	private final double[] slotIntakePositions = new double[3]; // calibrated positions for physical slots at intake
	private final MatchSettings.ArtifactColor[] slots = new MatchSettings.ArtifactColor[3]; // physical slot storage
	private final Object lock = new Object();
	private final double exitOffset; // servo units 0..1
	// sealing state
	private final Deque<Runnable> postSealQueue = new ArrayDeque<>();
	private final MatchSettings matchSettings;
	private volatile double commandedPosition; // last position we commanded the servo to
	private long lastEjectTimeMs = 0;
	private boolean sealRequested = false;
	private boolean sealed = true; // whether transfer servo is currently considered closed
	
	/**
	 * @param sorterServo servo used to rotate the sorter. not null.
	 */
	public Sorter(Servo sorterServo, Servo launcherTransferServo, ColorSensor sorterColorSensor, MatchSettings matchSettings) {
		this.sorterServo = sorterServo;
		this.matchSettings = matchSettings;
		this.launcherTransferServo = launcherTransferServo;
		this.colorSensor = sorterColorSensor;
		for (int i = 0; i < 3; i++) {
			slotIntakePositions[i] = wrapServo(Settings.Hardware.Sorter.SLOT_INTAKE_POSITIONS[i]);
		}
		this.exitOffset = wrapServo(Settings.Hardware.Sorter.EXIT_OFFSET);
		
		Arrays.fill(slots, MatchSettings.ArtifactColor.UNKNOWN);
		
		// Sync commandedPosition to current hardware reading if available
		double pos = sorterServo.getPosition();
		this.commandedPosition = wrapServo(pos);
	}
	
	private static double wrapServo(double v) {
		v %= 1.0;
		if (v < 0) v += 1.0;
		return v;
	}
	
	private static double circularDistance(double a, double b) {
		double d = Math.abs(a - b);
		return Math.min(d, 1.0 - d);
	}
	
	public void init() {
		// Make sure transfer is closed initially (best-effort)
		try {
			launcherTransferServo.setPosition(Settings.Hardware.Sorter.TRANSFER_SERVO_CLOSED_POSITION);
			sealed = true;
		} catch (Exception ignored) {
			// keep sealed=true; serviceSeal() will ensure hardware eventually matches logical state
		}
		
		// organize slots around outtake
		rotateSlotToExit(0);
	}
	
	/**
	 * Called periodically from op-mode loop. Must be called frequently.
	 * This services any pending seal requests and runs queued actions once sealed.
	 */
	public void update() {
		serviceSeal();
		
		// if there is a color sensed, update that position in the slots to that color
		MatchSettings.ArtifactColor color = colorSensor.getArtifactColor();
		if (color != MatchSettings.ArtifactColor.UNKNOWN) {
			setCurrentlyIntakingArtifact(color);
		}
	}
	
	public MatchSettings.ArtifactColor[] getSlotsSnapshot() {
		synchronized (lock) {
			return Arrays.copyOf(slots, slots.length);
		}
	}
	
	/**
	 * Rotate the next Motif-matching artifact to the exit.
	 * Uses MatchSettings.nextArtifactNeeded as the target.
	 * Does nothing if no matching artifact is present.
	 */
	public void rotateNextArtifactToExit() {
		MatchSettings.ArtifactColor needed = matchSettings.nextArtifactNeeded();
		if (needed == MatchSettings.ArtifactColor.UNKNOWN) return;
		
		synchronized (lock) {
			for (int i = 0; i < slots.length; i++) {
				if (slots[i] == needed) {
					rotateSlotToExit(i);
					return; // rotate only the first matching slot
				}
			}
		}
	}
	
	public boolean isNextArtifactAtExit() {
		MatchSettings.ArtifactColor needed = matchSettings.nextArtifactNeeded();
		if (needed == MatchSettings.ArtifactColor.UNKNOWN) return false;
		
		synchronized (lock) {
			for (int i = 0; i < slots.length; i++) {
				if (slots[i] == needed) {
					// target servo position for this slot at exit
					double targetPos = wrapServo(slotIntakePositions[i] + exitOffset);
					double distance = circularDistance(getCurrentServoPosition(), targetPos);
					// 5 degrees in servo units (1 unit = 360°)
					return distance <= TOLERANCE;
				}
			}
		}
		return false;
	}
	
	public void clearSlots() {
		synchronized (lock) {
			for (int i = 0; i < 3; i++) slots[i] = MatchSettings.ArtifactColor.UNKNOWN;
		}
	}
	
	/**
	 * Return physical slot index currently aligned to intake (0..2).
	 */
	public int getSlotIndexAtIntake() {
		return nearestSlotIndexTo(getCurrentServoPosition());
	}
	
	/**
	 * Return physical slot index currently aligned to exit (0..2).
	 */
	public int getSlotIndexAtExit() {
		double servoPos = getCurrentServoPosition();
		// The slot at exit is the slot whose intake-calibrated position aligns with servoPos - exitOffset.
		return nearestSlotIndexTo(wrapServo(servoPos - exitOffset));
	}
	
	public MatchSettings.ArtifactColor getArtifactAtSlot(int slotIndex) {
		checkSlotIndex(slotIndex);
		synchronized (lock) {
			return slots[slotIndex];
		}
	}
	
	/**
	 * Place a ball into the physical slot currently at intake.
	 */
	public void setCurrentlyIntakingArtifact(MatchSettings.ArtifactColor color) {
		int slot = getSlotIndexAtIntake();
		synchronized (lock) {
			slots[slot] = color;
		}
	}
	
	/**
	 * Remove and return the artifact at the exit. Becomes UNKNOWN after removal.
	 */
	public void ejectBallAtExit() {
		int slot = getSlotIndexAtExit();
		// open transfer immediately
		launcherTransferServo.setPosition(Settings.Hardware.Sorter.TRANSFER_SERVO_OPEN_POSITION);
		
		synchronized (lock) {
			MatchSettings.ArtifactColor c = slots[slot];
			slots[slot] = MatchSettings.ArtifactColor.UNKNOWN;
			lastEjectTimeMs = System.currentTimeMillis();
			// after an eject we are not sealed
			sealed = false;
			// schedule any later sealing requests normally
			// leave previously queued actions in place; they will run after the next seal
			sealRequested = false;
		}
	}
	
	/**
	 * Command the servo so physical slot `slotIndex` is aligned to the exit.
	 * Non-blocking. The actual servo move will happen only after the transfer is sealed.
	 */
	public void rotateSlotToExit(int slotIndex) {
		checkSlotIndex(slotIndex);
		ensureSealedThen(() -> {
			double target = wrapServo(slotIntakePositions[slotIndex] + exitOffset);
			setServoPosition(target);
		});
	}
	
	/* -------------------- Internal helpers -------------------- */
	
	/**
	 * Ensure the sorter is in a state ready to rotate around.
	 * <p>
	 * Non-blocking: schedules `action` to run after sealing completes. If sealing is already
	 * satisfied, the action runs immediately on the caller thread.
	 */
	private void ensureSealedThen(Runnable action) {
		synchronized (lock) {
			long now = System.currentTimeMillis();
			long readyAt = lastEjectTimeMs + Settings.Hardware.Sorter.EJECT_EXIT_TIME_MS;
			if (now >= readyAt) {
				// Cooldown satisfied. Close transfer and run action immediately.
				try {
					launcherTransferServo.setPosition(Settings.Hardware.Sorter.TRANSFER_SERVO_CLOSED_POSITION);
				} catch (Exception ignored) {
				}
				sealed = true;
				sealRequested = false;
				// run action immediately on caller thread
				action.run();
				return;
			}
			
			// Not ready yet. Request sealing via serviceSeal() and queue the action.
			sealRequested = true;
			postSealQueue.add(action);
		}
	}
	
	/**
	 * Command the servo so physical slot `slotIndex` is aligned to the intake.
	 * Non-blocking. The actual servo move will happen only after the transfer is sealed.
	 */
	public void rotateSlotToIntake(int slotIndex) {
		checkSlotIndex(slotIndex);
		ensureSealedThen(() -> {
			double target = slotIntakePositions[slotIndex];
			setServoPosition(target);
		});
	}
	
	private void checkSlotIndex(int slot) {
		if (slot < 0 || slot >= 3) throw new IllegalArgumentException("slot index must be 0..2");
	}
	
	/**
	 * Return the current servo position used for logical calculations.
	 * Uses last commandedPosition for determinism. Change to servo.getPosition() if you prefer hardware read.
	 */
	private double getCurrentServoPosition() {
		return commandedPosition;
	}
	
	private void setServoPosition(double pos) {
		pos = wrapServo(pos);
		try {
			sorterServo.setPosition(pos);
		} catch (Exception ignored) {
			// If servo call fails, still keep logical state deterministic.
		}
		commandedPosition = pos;
	}
	
	/**
	 * Find the physical slot whose intake-calibration position is nearest the given servo position.
	 */
	private int nearestSlotIndexTo(double servoPos) {
		servoPos = wrapServo(servoPos);
		int best = 0;
		double bestDist = Double.POSITIVE_INFINITY;
		for (int i = 0; i < 3; i++) {
			double d = circularDistance(servoPos, slotIntakePositions[i]);
			if (d < bestDist) {
				bestDist = d;
				best = i;
			}
		}
		return best;
	}
	
	/**
	 * Called from update() to actually close the transfer servo once cooldown passes and run queued actions.
	 *
	 * @noinspection MethodWithMoreThanThreeNegations sorry
	 */
	private void serviceSeal() {
		Deque<Runnable> toRun = null;
		synchronized (lock) {
			if (!sealRequested) return;
			long now = System.currentTimeMillis();
			if (now - lastEjectTimeMs < Settings.Hardware.Sorter.EJECT_EXIT_TIME_MS) return;
			
			// time has passed; close transfer and drain queue
			try {
				launcherTransferServo.setPosition(Settings.Hardware.Sorter.TRANSFER_SERVO_CLOSED_POSITION);
			} catch (Exception ignored) {
			}
			
			sealed = true;
			sealRequested = false;
			
			if (!postSealQueue.isEmpty()) {
				toRun = new ArrayDeque<>(postSealQueue);
				postSealQueue.clear();
			}
			
			// wake any blocking waiters
			lock.notifyAll();
		}
		
		// run queued actions outside lock
		if (toRun != null) {
			while (!toRun.isEmpty()) {
				Runnable r = toRun.poll();
				try {
					if (r != null) {
						r.run();
					}
				} catch (RuntimeException ignored) {
					// swallow to ensure other queued actions still run
				}
			}
		}
	}
}