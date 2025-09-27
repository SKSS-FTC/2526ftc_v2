package org.firstinspires.ftc.teamcode.software;

import static org.firstinspires.ftc.teamcode.configuration.Settings.Field.CLOSE_LAUNCH_ZONE_FRONT_CORNER;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Field.CLOSE_LAUNCH_ZONE_LEFT_CORNER;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Field.CLOSE_LAUNCH_ZONE_RIGHT_CORNER;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Field.FAR_LAUNCH_ZONE_FRONT_CORNER;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Field.FAR_LAUNCH_ZONE_LEFT_CORNER;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Field.FAR_LAUNCH_ZONE_RIGHT_CORNER;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.configuration.Settings;

public class AlignmentEngine {
	private final Drivetrain drivetrain;
	private final Follower follower;
	private final MatchSettings matchSettings;
	private final LimelightManager limelightManager;
	
	public AlignmentEngine(MatchSettings matchSettings, Drivetrain drivetrain, LimelightManager limelightManager, Follower follower) {
		this.drivetrain = drivetrain;
		this.follower = follower;
		this.matchSettings = matchSettings;
		this.limelightManager = limelightManager;
	}
	
	public static boolean isInsideTriangle(Pose pose, Pose A, Pose B, Pose C) {
		// Create new Point objects for the pose's x and y
		Pose P = new Pose(pose.getX(), pose.getY());
		
		// Use the crossProduct method to determine if the point is within the triangle
		double s1 = crossProduct(A, B, P);
		double s2 = crossProduct(B, C, P);
		double s3 = crossProduct(C, A, P);
		
		boolean has_neg = (s1 < 0) || (s2 < 0) || (s3 < 0);
		boolean has_pos = (s1 > 0) || (s2 > 0) || (s3 > 0);
		
		return !(has_neg && has_pos);
	}
	
	public static double crossProduct(Pose A, Pose B, Pose C) {
		return (B.getX() - A.getX()) * (C.getY() - A.getY()) - (B.getY() - A.getY()) * (C.getX() - A.getX());
	}
	
	public void run() {
		Pose currentPose = follower.getPose();
		if (!isInLaunchZone(currentPose)) {
			return;
		}
		
		LLResult llResult = limelightManager.detectGoal();
		
		Pose targetPose = (matchSettings.getAllianceColor() == MatchSettings.AllianceColor.BLUE)
				? Settings.Field.BLUE_GOAL_POSE
				: Settings.Field.RED_GOAL_POSE;
		
		double angleError = angleToTarget(currentPose, targetPose);
		
		
		if (llResult == null || !llResult.isValid()) {
			drivetrain.interpolateToOffset(0, 0, angleError);
			return;
		}
		
		double xError = limelightManager.limelight.getLatestResult().getTx();
		
		drivetrain.interpolateToOffset(xError, 0, angleError);
	}
	
	public boolean isInLaunchZone(Pose pose) {
		// Check if the pose is inside the FAR launch zone
		boolean inFarZone = isInsideTriangle(
				pose,
				FAR_LAUNCH_ZONE_FRONT_CORNER,
				FAR_LAUNCH_ZONE_LEFT_CORNER,
				FAR_LAUNCH_ZONE_RIGHT_CORNER
		);
		
		// Check if the pose is inside the CLOSE launch zone
		boolean inCloseZone = isInsideTriangle(
				pose,
				CLOSE_LAUNCH_ZONE_FRONT_CORNER,
				CLOSE_LAUNCH_ZONE_LEFT_CORNER,
				CLOSE_LAUNCH_ZONE_RIGHT_CORNER
		);
		
		// Return true if the pose is in *either* launch zone
		return inFarZone || inCloseZone;
	}
	
	// returns signed smallest angle (radians) the robot must rotate to face target
	private double angleToTarget(Pose currentPose, Pose targetPose) {
		double dx = targetPose.getX() - currentPose.getX();
		double dy = targetPose.getY() - currentPose.getY();
		
		double desired = Math.atan2(dy, dx); // absolute angle to goal
		double current = currentPose.getHeading();
		
		double error = desired - current;
		while (error > Math.PI) error -= 2 * Math.PI;
		while (error <= -Math.PI) error += 2 * Math.PI;
		return error; // radians, positive -> rotate CCW, negative -> rotate CW
	}
}
