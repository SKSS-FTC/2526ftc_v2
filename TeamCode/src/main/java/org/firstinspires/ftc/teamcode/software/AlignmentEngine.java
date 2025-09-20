package org.firstinspires.ftc.teamcode.software;

import static org.firstinspires.ftc.teamcode.configuration.Settings.Positions.CLOSE_LAUNCH_ZONE_FRONT_CORNER;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Positions.CLOSE_LAUNCH_ZONE_LEFT_CORNER;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Positions.CLOSE_LAUNCH_ZONE_RIGHT_CORNER;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Positions.FAR_LAUNCH_ZONE_FRONT_CORNER;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Positions.FAR_LAUNCH_ZONE_LEFT_CORNER;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Positions.FAR_LAUNCH_ZONE_RIGHT_CORNER;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResult;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.configuration.MatchSettings;

public class AlignmentEngine {
	private final Drivetrain drivetrain;
	private final GoBildaPinpointDriver pinpoint;
	private final MatchSettings matchSettings;
	private final LimelightManager limelightManager;
	
	public AlignmentEngine(MatchSettings matchSettings, Drivetrain drivetrain, LimelightManager limelightManager, GoBildaPinpointDriver pinpoint) {
		this.drivetrain = drivetrain;
		this.pinpoint = pinpoint;
		this.matchSettings = matchSettings;
		this.limelightManager = limelightManager;
	}
	
	public static boolean isInsideTriangle(Pose2D pose, Pose A, Pose B, Pose C) {
		// Create new Point objects for the pose's x and y
		Pose P = new Pose(pose.getX(DistanceUnit.INCH), pose.getY(DistanceUnit.INCH));
		
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
		pinpoint.getPosition();
		if (!isInLaunchZone()) {
			return;
		}
		
		// This is wrong lol. it should move to have best angle tho
		
		LLResult llResult = limelightManager.detectGoal();
		if (llResult == null || !llResult.isValid()) {
			return;
		}
		
		double Ta = limelightManager.limelight.getLatestResult().getTa();
		
		drivetrain.interpolateToOffset(0, 0, Ta);
	}
	
	public boolean isInLaunchZone() {
		Pose2D pose = pinpoint.getPosition();
		
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
	
	private double wrappedHeading() {
		return (pinpoint.getHeading(AngleUnit.RADIANS) + Math.PI) % (2 * Math.PI) - Math.PI;
	}
}
