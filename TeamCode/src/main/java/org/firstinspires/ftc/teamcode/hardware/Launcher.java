package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.configuration.Settings;
import org.firstinspires.ftc.teamcode.software.TrajectoryEngine;

public class Launcher {
	private final TrajectoryEngine trajectoryEngine;
	
	private final Servo horizontalServo;
	private final Servo verticalServo;
	private final Sorter sorter;
	private final SyncBelt belt;
	
	// Cached commanded orientation (radians or degrees depending on Settings)
	private double commandedYaw;
	private double commandedPitch;
	
	public Launcher(Sorter sorter,
	                DcMotor beltRight,
	                DcMotor beltLeft,
	                Servo horizontalServo,
	                Servo verticalServo,
	                TrajectoryEngine trajectoryEngine) {
		this.sorter = sorter;
		this.trajectoryEngine = trajectoryEngine;
		this.horizontalServo = horizontalServo;
		this.verticalServo = verticalServo;
		this.belt = new SyncBelt(beltRight, beltLeft);
		
		// Initialize commanded orientation from hardware
		commandedYaw = servoToYaw(horizontalServo.getPosition());
		commandedPitch = servoToPitch(verticalServo.getPosition());
	}
	
	// Call to zero the launcher at init
	public final void init() {
		commandedYaw = 0;
		commandedPitch = 0;
		setYaw(0);
		setPitch(0);
	}
	
	public final void update() {
		belt.update();
	}
	
	public void launch() {
		if (okayToLaunch()) {
			sorter.ejectBallAtExit();
		}
	}
	
	public void ready() {
		belt.spinUp();
		sorter.rotateNextArtifactToExit();
	}
	
	public void stop() {
		belt.spinDown();
	}
	
	public boolean okayToLaunch() {
		return trajectoryEngine.isOkayToLaunch() &&
				belt.atSpeed() &&
				sorter.isNextArtifactAtExit();
	}
	
	// ---- Orientation handling ----
	
	public double getYaw() {
		return commandedYaw;
	}
	
	public void setYaw(double yaw) {
		commandedYaw = yaw;
		horizontalServo.setPosition(yawToServo(yaw));
	}
	
	public double getPitch() {
		return commandedPitch;
	}
	
	public void setPitch(double pitch) {
		commandedPitch = pitch;
		verticalServo.setPosition(pitchToServo(pitch));
	}
	
	private double yawToServo(double yaw) {
		return (yaw - Settings.Launcher.MIN_YAW) / (Settings.Launcher.MAX_YAW - Settings.Launcher.MIN_YAW);
	}
	
	private double pitchToServo(double pitch) {
		return (pitch - Settings.Launcher.MIN_PITCH) / (Settings.Launcher.MAX_PITCH - Settings.Launcher.MIN_PITCH);
	}
	
	private double servoToYaw(double servoPos) {
		return Settings.Launcher.MIN_YAW + servoPos * (Settings.Launcher.MAX_YAW - Settings.Launcher.MIN_YAW);
	}
	
	private double servoToPitch(double servoPos) {
		return Settings.Launcher.MIN_PITCH + servoPos * (Settings.Launcher.MAX_PITCH - Settings.Launcher.MIN_PITCH);
	}
	
	// ---- SyncBelt ----
	public static class SyncBelt {
		private final DcMotor beltRight;
		private final DcMotor beltLeft;
		
		private long spinupTimestamp = 0;
		private boolean active = false;
		
		// For smoothing
		private int lastRightPos = 0;
		private int lastLeftPos = 0;
		
		public SyncBelt(DcMotor right, DcMotor left) {
			this.beltRight = right;
			this.beltLeft = left;
			right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
			left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		}
		
		public final void spinUp() {
			if (active) return;
			active = true;
			spinupTimestamp = System.currentTimeMillis();
			setBasePower(Settings.Launcher.BELT_MOTOR_SPEED);
		}
		
		public final void spinDown() {
			active = false;
			spinupTimestamp = 0;
			beltRight.setPower(0);
			beltLeft.setPower(0);
		}
		
		public boolean atSpeed() {
			return active &&
					System.currentTimeMillis() - spinupTimestamp > Settings.Launcher.BELT_SPINUP_TIME_MS;
		}
		
		public void update() {
			if (!active) return;
			
			int rightPos = beltRight.getCurrentPosition();
			int leftPos = beltLeft.getCurrentPosition();
			
			int deltaRight = rightPos - lastRightPos;
			int deltaLeft = leftPos - lastLeftPos;
			
			lastRightPos = rightPos;
			lastLeftPos = leftPos;
			
			// Prevent div-by-zero
			if (deltaRight == 0 && deltaLeft == 0) return;
			
			// Compute imbalance ratio
			double avg = (Math.abs(deltaRight) + Math.abs(deltaLeft)) / 2.0;
			double error = (deltaRight - deltaLeft) / avg;
			
			// Proportional correction
			double correction = Settings.Launcher.BELT_SYNC_KP * error;
			
			double base = Settings.Launcher.BELT_MOTOR_SPEED;
			beltRight.setPower(base * (1 - correction));
			beltLeft.setPower(base * (1 + correction));
		}
		
		private void setBasePower(double power) {
			beltRight.setPower(power);
			beltLeft.setPower(power);
		}
	}
}