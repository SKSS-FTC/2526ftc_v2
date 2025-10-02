package org.firstinspires.ftc.teamcode.software;

import com.qualcomm.hardware.limelightvision.LLResult;

import org.firstinspires.ftc.teamcode.configuration.Settings;

/**
 * The Trajectory Engine uses a launcher-mounted Limelight to find the direct angular offsets
 * required to aim at a target.
 */
public class TrajectoryEngine {
	
	private final LimelightManager limelightManager;
	
	public TrajectoryEngine(LimelightManager limelightManager) {
		this.limelightManager = limelightManager;
	}
	
	/**
	 * Gets the current aiming offsets from the Limelight camera.
	 *
	 * @return An {@link AimingOffsets} object containing the targeting data.
	 */
	public AimingOffsets getAimingOffsets() {
		LLResult limelightResult = limelightManager.detectGoal();
		
		// If no valid targets are found, return an object indicating no target.
		if (limelightResult.getFiducialResults().isEmpty()) {
			return AimingOffsets.invalid();
		}
		
		double horizontal = limelightResult.getFiducialResults().get(0).getTargetXDegrees();
		double vertical = limelightResult.getFiducialResults().get(0).getTargetYDegrees();
		
		return new AimingOffsets(horizontal, vertical, true);
	}
	
	/**
	 * Determines if the launcher is currently aimed at the target within acceptable tolerance.
	 *
	 * @return True if aimed correctly, false otherwise.
	 */
	public boolean isAimed() {
		AimingOffsets offsets = getAimingOffsets();
		if (!offsets.hasTarget) {
			return false;
		}
		
		boolean yawAligned = Math.abs(offsets.horizontalOffsetDegrees) < Settings.Aiming.MAX_YAW_ERROR;
		boolean pitchAligned = Math.abs(offsets.verticalOffsetDegrees) < Settings.Aiming.MAX_PITCH_ERROR;
		
		return yawAligned && pitchAligned;
	}
	
	/**
	 * A simple data class to hold the aiming offsets from the Limelight.
	 */
	public static class AimingOffsets {
		public final boolean hasTarget;
		public final double horizontalOffsetDegrees;
		public final double verticalOffsetDegrees;
		
		/**
		 * Constructor for a valid offset measurement.
		 */
		public AimingOffsets(double horizontalOffset, double verticalOffset, boolean hasTarget) {
			this.hasTarget = hasTarget;
			this.horizontalOffsetDegrees = horizontalOffset;
			this.verticalOffsetDegrees = verticalOffset;
		}
		
		/**
		 * Constructor for an invalid solution, used when no target is found.
		 */
		public static AimingOffsets invalid() {
			return new AimingOffsets(Double.NaN, Double.NaN, false);
		}
	}
}
