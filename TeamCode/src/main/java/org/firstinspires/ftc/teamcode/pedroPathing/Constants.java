package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.configuration.Settings;

public class Constants {
	public static final FollowerConstants followerConstants = new FollowerConstants()
			.mass(4)
			.forwardZeroPowerAcceleration(-33.78)
			.lateralZeroPowerAcceleration(-58.36)
			
			.translationalPIDFCoefficients(new PIDFCoefficients(0.07, 0, 0.01, 0.048))
			.secondaryTranslationalPIDFCoefficients(new PIDFCoefficients(0.15, 0, 0.01, 0.02))
			.headingPIDFCoefficients(new PIDFCoefficients(0.6, 0, 0.02, 0.043))
			.secondaryHeadingPIDFCoefficients(new PIDFCoefficients(1.5, 0, 0.01, 0.025))
			.drivePIDFCoefficients(new FilteredPIDFCoefficients(0.7, 0, 0.001, 0.6, 0.03))
			.useSecondaryDrivePIDF(false) // for now
			.useSecondaryHeadingPIDF(true)
			.useSecondaryTranslationalPIDF(true);
	
	public static final MecanumConstants driveConstants = new MecanumConstants()
			.maxPower(1)
			.leftFrontMotorName(Settings.HardwareIDs.FRONT_LEFT_MOTOR)
			.leftRearMotorName(Settings.HardwareIDs.REAR_LEFT_MOTOR)
			.rightFrontMotorName(Settings.HardwareIDs.FRONT_RIGHT_MOTOR)
			.rightRearMotorName(Settings.HardwareIDs.REAR_RIGHT_MOTOR)
			.leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
			.leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
			.rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
			.rightRearMotorDirection(DcMotorSimple.Direction.REVERSE)
			
			.xVelocity(65.46).yVelocity(56.47);
	
	public static PinpointConstants localizerConstants = new PinpointConstants()
			.forwardPodY(6)
			.strafePodX(6)
			.distanceUnit(DistanceUnit.INCH)
			.hardwareMapName(Settings.HardwareIDs.PINPOINT)
			.encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
			.forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
			.strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);
	
	/**
	 * These are the PathConstraints in order:
	 * tValueConstraint, velocityConstraint, translationalConstraint, headingConstraint, timeoutConstraint,
	 * brakingStrength, BEZIER_CURVE_SEARCH_LIMIT, brakingStart
	 * The BEZIER_CURVE_SEARCH_LIMIT should typically be left at 10 and shouldn't be changed.
	 */
	public static PathConstraints pathConstraints = new PathConstraints(
			0.995,
			0.1,
			0.1,
			0.009,
			50,
			1.25,
			10,
			1
	);
	
	public static Follower createFollower(HardwareMap hardwareMap) {
		return new FollowerBuilder(followerConstants, hardwareMap)
				.mecanumDrivetrain(driveConstants)
				.pathConstraints(pathConstraints)
				.pinpointLocalizer(localizerConstants)
				.build();
	}
}