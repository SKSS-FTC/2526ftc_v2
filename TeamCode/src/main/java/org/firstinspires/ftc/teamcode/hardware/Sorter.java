package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.configuration.Settings;

import java.util.Arrays;

/**
 * Controls a 3-slot rotating sorter driven by a single Servo.
 */
public class Sorter {
    private final Servo servo;
    private final double[] slotIntakePositions = new double[3]; // calibrated positions for physical slots at intake
    private final MatchSettings.ArtifactColor[] slots = new MatchSettings.ArtifactColor[3]; // physical slot storage
    private final Object lock = new Object();
    private final double exitOffset; // servo units 0..1
    private volatile double commandedPosition; // last position we commanded the servo to

    /**
     * @param servo servo used to rotate the sorter. not null.
     */
    public Sorter(Servo servo) {
        this.servo = servo;
        for (int i = 0; i < 3; i++) {
            slotIntakePositions[i] = wrapServo(Settings.Hardware.Sorter.SLOT_INTAKE_POSITIONS[i]);
        }
        this.exitOffset = wrapServo(Settings.Hardware.Sorter.EXIT_OFFSET);

        for (int i = 0; i < 3; i++) slots[i] = MatchSettings.ArtifactColor.UNKNOWN;

        // Sync commandedPosition to current hardware reading if available
        double pos = 0.0;
        try {
            pos = servo.getPosition();
        } catch (Exception ignored) {
        }
        this.commandedPosition = wrapServo(pos);
    }

    /* -------------------- Public API -------------------- */

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
        // organize slots around outtake
        rotateSlotToExit(0);
    }

    public void reset() {
        rotateSlotToExit(0);
    }

    public MatchSettings.ArtifactColor[] getSlotsSnapshot() {
        synchronized (lock) {
            return Arrays.copyOf(slots, slots.length);
        }
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
     * Returns false if that slot is occupied.
     */
    public boolean setCurrentlyIntakingArtifact(MatchSettings.ArtifactColor color) {
        if (color == null) color = MatchSettings.ArtifactColor.UNKNOWN;
        int slot = getSlotIndexAtIntake();
        synchronized (lock) {
            if (slots[slot] != MatchSettings.ArtifactColor.UNKNOWN) return false;
            slots[slot] = color;
            return true;
        }
    }

    /**
     * Remove and return the artifact at the exit. Becomes UNKNOWN after removal.
     */
    public MatchSettings.ArtifactColor ejectBallAtExit() {
        int slot = getSlotIndexAtExit();
        synchronized (lock) {
            MatchSettings.ArtifactColor c = slots[slot];
            slots[slot] = MatchSettings.ArtifactColor.UNKNOWN;
            return c;
        }
    }

    /**
     * Command the servo so physical slot `slotIndex` is aligned to the exit.
     * Non-blocking.
     */
    public void rotateSlotToExit(int slotIndex) {
        checkSlotIndex(slotIndex);
        double target = wrapServo(slotIntakePositions[slotIndex] + exitOffset);
        setServoPosition(target);
    }

    /* -------------------- Internal helpers -------------------- */

    /**
     * Command the servo so physical slot `slotIndex` is aligned to the intake.
     * Non-blocking.
     */
    public void rotateSlotToIntake(int slotIndex) {
        checkSlotIndex(slotIndex);
        double target = slotIntakePositions[slotIndex];
        setServoPosition(target);
    }

    /**
     * Rotate a number of intake steps. Positive steps advance slot indices.
     */
    public void rotateIntakeBySteps(int steps) {
        int current = getSlotIndexAtIntake();
        int target = Math.floorMod(current + steps, 3);
        rotateSlotToIntake(target);
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
            servo.setPosition(pos);
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

    @Override
    public String toString() {
        return "Sorter{" +
                "servoPos=" + getCurrentServoPosition() +
                ", intakeIndex=" + getSlotIndexAtIntake() +
                ", exitIndex=" + getSlotIndexAtExit() +
                ", slots=" + Arrays.toString(getSlotsSnapshot()) +
                '}';
    }
}