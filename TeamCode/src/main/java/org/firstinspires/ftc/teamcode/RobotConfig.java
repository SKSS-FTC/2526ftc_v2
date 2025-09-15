package org.firstinspires.ftc.teamcode;


import static java.lang.Math.PI;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.pedroPathing.tuning.FollowerConstants;

//USE THIS CLASS TO PUT ALL CONSTANTS LIKE HARDWARE NAMES AND CONSTANTS

public class RobotConfig {
    @Config
    public static class DriveConstants {
        public static  String frontLeftWheelName = "frontLeft";
        public static  String frontRightWheelName = "frontRight";
        public static  String backLeftWheelName = "backLeft";
        public static  String backRightWheelName = "backRight";

        public static String imuName="imu";

        public static  double forwardMultiplier = 0.0029805169566404065;
        public static  double lateralMultiplier = 0.002965149766084306;

        public static  IMU.Parameters practiceIMUOrientation = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                )
        );

        public static  IMU.Parameters compIMUOrientation = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                )
        );

        //IN KILOGRAMS!!!
        public static  double compBotMass = 16.65;
    }
    @Config
    public static class IntakeConstants {
        public static double colorSensorRedToGreenThreshold =.35;
        public static String colorSensor1Name = "leftColorSensor";
        public static String colorSensor2Name = "rightColorSensor";

        public static String intakeServo1Name = "intakeCRServo1";
        public static String intakeServo2Name = "intakeCRServo2";
        public static double intakeServo1UpDirection = 1;
        public static double intakeServo2UpDirection = -1;

        public static int intakeWaitAtBottomTimeMs = 505;
        public static int passThroughTimeMs = 500;
        public static int takeOutOfBucketTimeMs = 750;

        public static String verticalServoName = "verticalServo";
        public static double verticalServoMinDegrees = 0;
        public static double verticalServoMaxDegrees = 300;
        public static double verticalServoUpPosition = .4222;
        public static double verticalServoOutOfSubReadyPos = .1303;
        public static double verticalServoSubmersibleIntakePos = .0;
        public static double verticalServoDownPosition = verticalServoSubmersibleIntakePos;


        public static String extendMotorName = "extendMotor";
        public static int motorMaxPosition = 540;//401;
        public static int motorMinPosition = 40;//22;
        public static double motorSlowRetractionRawPower = .2;
        public static double motorExtendSpeed = 1;
        public static double motorRetractSpeed = -1;
        public static double motorPCoefficient = .005;
        public static int motorDegreeOfError = 15;
        public static int motorClearPos = 347;
        public static double extendMotorTicksPerInch = 53.2;


        public static String distanceSensorName = "intakeDistanceSensor";
        public static double distanceSensorLowThreshold = .8;
        public static double distanceSensorHighThreshold = 3.1;

    }


    @Config
    public static class HangConstants{
        public static String hangLiftName = "hangMotor";
        public static int HangMaxPos = 16100;
        public static int HangMinPos = 0;
        public static int HangTime = 5;
        public static double LeanPos = 0.5;
        public static double LeanPosEnd = -0.5;
        public static double hangUpDirection = -1;

    }

    @Config
    public static class OuttakeConstants{
        public static String armLiftName = "armLift";
        public static int armLiftHighDunkPos = -1400;
        public static int armLiftDefaultPos = 0;
        public static int armLiftError = 30;
        public static int armLiftDirection = -1;

        public static final double highDunkLingerPower = .5;
        public static final int highDunkLingerTimeMs = 125;
        public static final int autoHighDunkLingerTimeMs = 200;

        //Dunk PID coefficients
        //The starting error from the high dunk that the bot starts to use a P controller on the speed of the arm
        //Not used as of now
        public static  int maxDerivativeApplyError = 50;
        public static  double armUpProportionalCoefficient = 1;
        public static  double armUpDerivativeProportionalCoefficient = 0.003;
        public static  double armSwingHighDunkOptimalDerivative = 520;
        public static  double armSwingAntiGravityCoefficient = 0;


        public static double armSwingPower = .75;

        public static  double armSwingGoDownOptimalDerivative = -0;

        public static String armOuttakeName = "armOuttake";
        public static int armSwingHighDunkPos = 344;
        public static int armSwingLowDunkPos = 570;
        public static int armSwingDefaultPos = 50;
        public static int armSwing90DegreesVerticalPos = 110;
        public static int armSwingParallelVerticalPos = 310;
    }
    @Config
    public static class CameraConstants{
        public static  float fx = 790.088f;
        public static  float fy = 790.088f;
        public static  float cx = 320.002f;
        public static  float cy = 240.001f;
    }
    @Config
    public static class AutoPoseStorage {
        public static Pose startPose = new Pose(4.28,0,3*PI/2);
        public static Point blueGoal = new Point(new Pose(25.5,-5.6));

        public static Point path1StartGoal = blueGoal;
        public static double blueGoalHeading = 5*PI/4;


        public static Point rightWhiteSpike = new Point(new Pose(17.5,-17.0));
        public static double rightWhiteSpikeHeading = 3*PI/2;

        public static Point middleWhiteSpike = new Point(new Pose(24.87,-17));
        public static double middleWhiteSpikeHeading = 3*PI/2;

        public static Point leftWhiteSpike = new Point(new Pose(19.5,-33.3));
        public static double leftWhiteSpikeHeading = 0;

        public static Point submersibleStart = new Point(new Pose(12.5,-52.5));
        public static Point submersibleParameterPoint = new Point(new Pose(20.9,-45.4));
        public static double submersibleHeading = PI;

        public static Point hangPoint = new Point(new Pose(6.2,-54.3));
        public static double hangHeading = 0;

    }

    public static class LimelightConstants {
        //TODO: Fill This pose in with the right coordinates relative to the BOT not the camera when the intake is fully in
        public static Pose crossHairPoseOnGround = new Pose(3.0,17.0);

        public static int redSamplePipelineNumber = 0;
        public static int yellowSamplePipelineNumber = 1;
        public static int blueSamplePipelineNumber = 2;
    }


    public static class GlobalConstants {
        public static Pose lastPose = new Pose(8,0,3*PI/2);
        public static double startTime = 0;
    }

    public static class AutoConstants {
        public static double bucketToRightSpikeZPAM = 1.3;
        public static double bucketToMiddleSpikeZPAM = 1.3;
        public static double bucketToLeftSpikeZPAM = 1.3;
        public static int right2SamplesArmWaitTimeMs = 400;
    }

    public static class CycleTimes{
        public static double intakeToHighScore = 4;
        public static double highBucketToSubmersible = 4;
        public static double intakeToLowScore = 3.5;
        public static double lowScoreToSubmersible = 3.5;
        public static double highScoreToHangPos = 4;
        public static double hang1Time = 5;
        public static double fromHang1ToHang2Time = 7;

    }

    public static class Scores{
        public static double highDunk = 8;
        public static double lowDunk = 4;
        public static double touchBar = 3;
        public static double firstHang = 15;
        public static double fullHang = 30;
    }

}
