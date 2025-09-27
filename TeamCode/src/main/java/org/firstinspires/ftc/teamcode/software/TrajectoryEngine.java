package org.firstinspires.ftc.teamcode.software;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.configuration.Settings;

public class TrajectoryEngine {
	
	private final Follower follower;
	private final MatchSettings matchSettings;
	private final LimelightManager limelightManager;
	
	public TrajectoryEngine(LimelightManager limelightManager, Follower follower, MatchSettings matchSettings) {
		this.limelightManager = limelightManager;
		this.follower = follower;
		this.matchSettings = matchSettings;
	}
	
	/**
	 * Normalizes an angle to be within the range [-PI, PI].
	 *
	 * @param angle The angle in radians.
	 * @return The normalized angle in radians.
	 */
	private static double normalizeAngle(double angle) {
		return angle - 2 * Math.PI * Math.floor((angle + Math.PI) / (2 * Math.PI));
	}
	
	/**
	 * Performs all calculations to find a valid aiming solution.
	 * This private method is the single source of truth for all aiming logic.
	 *
	 * @return An {@link AimingSolution} object containing the results.
	 */
	private AimingSolution calculateSolution() {
		// 1. Get all sensor data ONCE
		Pose robotPose = follower.getPose();
		LLResult limelightResult = limelightManager.detectGoal();
		
		// 2. Robustly check for a valid target
		if (limelightResult.getFiducialResults().isEmpty()) {
			return new AimingSolution(); // Return invalid solution if no target is seen
		}
		
		// 3. Define the TARGET's fixed world coordinates from a settings file.
		// This is a much more reliable approach than calculating from camera depth.
		double targetX = (matchSettings.getAllianceColor() == MatchSettings.AllianceColor.RED)
				? Settings.Field.RED_GOAL_POSE.getX()
				: Settings.Field.BLUE_GOAL_POSE.getX();
		double targetY = (matchSettings.getAllianceColor() == MatchSettings.AllianceColor.RED)
				? Settings.Field.RED_GOAL_POSE.getY()
				: Settings.Field.BLUE_GOAL_POSE.getY();
		
		// 4. Calculate the vector from the robot to the target in the world frame
		double dx = targetX - robotPose.getX();
		double dy = targetY - robotPose.getY();
		double d = Math.hypot(dx, dy);
		
		// 5. Calculate the required launcher yaw
		double yawWorld = Math.atan2(dy, dx);
		double yawRelative = normalizeAngle(yawWorld - robotPose.getHeading());
		
		// 6. Calculate the required launcher pitch using ballistic equations
		double h = Settings.Aiming.GOAL_HEIGHT - Settings.Aiming.MUZZLE_HEIGHT;
		double v = Settings.Aiming.MUZZLE_TANGENTIAL_MAX_SPEED;
		double g = Settings.Aiming.GRAVITY;
		
		double discriminant = (v * v * v * v) - g * (g * d * d + 2 * h * v * v);
		
		// If the discriminant is negative, the target is out of range
		if (discriminant < 0) {
			return new AimingSolution(); // No ballistic solution, return invalid
		}
		
		// We choose the lower angle for a flatter, faster trajectory
		double sqrtDiscriminant = Math.sqrt(discriminant);
		double pitch = Math.atan((v * v - sqrtDiscriminant) / (g * d));
		
		// 7. Return the complete, valid solution
		return new AimingSolution(yawRelative, pitch, d);
	}
	
	/**
	 * Computes launcher yaw (relative to robot) and pitch to aim at the target.
	 *
	 * @return A double array containing {yawRadians, pitchRadians}. Values will be NaN if no solution exists.
	 */
	public double[] getAimingAngles() {
		AimingSolution solution = calculateSolution();
		return new double[]{solution.yawRadians, solution.pitchRadians};
	}
	
	/**
	 * Determines if all conditions are met to launch the projectile.
	 * This method should be called right before launching.
	 *
	 * @return True if it's safe and logical to launch, false otherwise.
	 */
	public boolean isOkayToLaunch() {
		AimingSolution solution = calculateSolution();
		
		// Check 1: A valid ballistic path must exist.
		return solution.hasSolution;
		
		// Check 2: The launcher must be aimed correctly.
		// You would get the launcher's CURRENT angles from your hardware control loops.
		// double currentLauncherYaw = launcher.getCurrentYaw();
		// double currentLauncherPitch = launcher.getCurrentPitch();
		//
		// boolean isYawAligned = Math.abs(normalizeAngle(solution.yawRadians - currentLauncherYaw)) < Settings.Aiming.maxYawError;
		// boolean isPitchAligned = Math.abs(solution.pitchRadians - currentLauncherPitch) < Settings.Aiming.maxPitchError;
		//
		// if (!isYawAligned || !isPitchAligned) {
		//    return false;
		// }
	}
	
	/**
	 * A simple data class to hold the results of a trajectory calculation.
	 * An "invalid" solution is one where no target was found or no ballistic path exists.
	 */
	public static class AimingSolution {
		public final boolean hasSolution;
		public final double yawRadians; // Launcher yaw relative to the robot's heading
		public final double pitchRadians; // Launcher pitch relative to the horizontal plane
		public final double distance; // Horizontal distance to the target
		
		/**
		 * Constructor for a valid, successful aiming solution.
		 *
		 * @param yaw   The calculated yaw in radians.
		 * @param pitch The calculated pitch in radians.
		 * @param dist  The horizontal distance to the target in inches.
		 */
		public AimingSolution(double yaw, double pitch, double dist) {
			this.hasSolution = true;
			this.yawRadians = yaw;
			this.pitchRadians = pitch;
			this.distance = dist;
		}
		
		/**
		 * Constructor for an invalid solution, used when no target is found or no solution exists.
		 */
		public AimingSolution() {
			this.hasSolution = false;
			this.yawRadians = Double.NaN;
			this.pitchRadians = Double.NaN;
			this.distance = Double.NaN;
		}
	}
}