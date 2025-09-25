/*
 * MIT License
 *
 * Copyright (c) 2024 ParkCircus Productions; All Rights Reserved
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.Vision.AprilTag;

/*
 * @doc An empirical fly-by launch solution for assessment during DECODE season
 * @author ParkCircus Productions
 * @version 0.0.2
 * @since  2025-09-07
 * @start-config:
 * Motors: motorLeftFront (DcMotorEx), motorLeftBack (DcMotorEx), motorRightFront (DcMotorEx), motorRightBack (DcMotorEx)
 * Sensors: imu (IMU) - An Inertial Measurement Unit
 * Vision: Webcam1 (WebcamName) - A standard webcam
 * @end-config
 * The primary objective of this OpMode is to create a more accurate and robust
 * fly-by launch system. This updated program will dynamically adjust the launcher's
 * velocity based on the robot's real-time position relative to the target,
 * ensuring a consistent shot even with variations in the robot's path.
 * Workflow
 * Initialize hardware including Vision Processor under runOpMode()
 * Add a LAUNCH_VELOCITY_MAP
 * Create a velocity calculation method getLaunchVelocityFromDistance()
 * Update state machine: (DRIVE_TO_PRELAUNCH, SPIN_UP_LAUNCHER, FIRE_ARTIFACT)
 * Refine telemetry for requiredVelocity (setpoint) and launcherVelocity (measured value)
 * @see https://ftc-resources.firstinspires.org/ftc/game/manual
 * The two parameters you can pass to the RevHubOrientationOnRobot constructor are:
 * logoFacingDirection: This specifies which side of the robot the Rev Hub's logo is facing. Options are:
 * RevHubOrientationOnRobot.LogoFacingDirection.UP
 * RevHubOrientationOnRobot.LogoFacingDirection.DOWN
 * RevHubOrientationOnRobot.LogoFacingDirection.LEFT
 * RevHubOrientationOnRobot.LogoFacingDirection.RIGHT
 * RevHubOrientationOnRobot.LogoFacingDirection.FORWARD
 * RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD
 * usbFacingDirection: This specifies which side of the robot the USB ports are facing. The options are:
 * RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
 * RevHubOrientationOnRobot.UsbFacingDirection.UP
 * RevHubOrientationOnRobot.UsbFacingDirection.DOWN
 * RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
 * RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
 * RevHubOrientationOnRobot.UsbFacingDirection.LEFT
 * RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
 * @see https://ftc-docs.firstinspires.org/en/latest/programming_resources/imu/imu.html
 */
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

// Vision-related imports
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

import java.util.List;
import java.util.Locale;

// The FTC SDK does not include a built-in PID controller class.

public class FlyByLaunch extends LinearOpMode {

    // --- ROBOT HARDWARE DECLARATIONS ---
    private DcMotorEx frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
    private DcMotorEx launcherMotor;
    private Servo launchTrigger;
    private IMU imu;

    // --- LAUNCHER CONSTANTS (Tune these!) ---
    private static final double LAUNCHER_SETPOINT_VELOCITY = 1500; // Ticks/second, adjust based on your motor

    // --- PID CONSTANTS (CRITICAL TO TUNE!) ---
    private static final double PID_kP = 0.001;
    private static final double PID_kI = 0.0;
    private static final double PID_kD = 0.0;
    private static final double PID_kF = 0.4;

    // --- FIELD COORDINATES (in inches) ---
    private static final double TARGET_X = 50.0;
    private static final double TARGET_Y = 30.0;
    private static final double PRE_LAUNCH_X = 40.0;
    private static final double PRE_LAUNCH_Y = 20.0;

    // --- POSE ESTIMATION (AprilTag-based) ---
    private double robotX = 0.0;
    private double robotY = 0.0;
    private double robotHeading = 0.0; // This will be updated by either IMU or AprilTag
    private double requiredVelocity = 0.0;
    private double launcherVelocity = 0.0;

    // --- PID Controller Object ---
    private PIDFController launcherPID;

    // --- APRILTAG VARIABLES ---
    private static final boolean USE_WEBCAM = true;
    private final Position cameraPosition = new Position(DistanceUnit.INCH, 0, 0, 0, 0);
    private final YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES, 0, -90, 0, 0);
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    // --- ROBOT STATE VARIABLES ---
    public enum RobotState {
        DRIVE_TO_PRELAUNCH,
        SPIN_UP_LAUNCHER,
        FIRE_ARTIFACT,
        POST_FIRE
    }

    // --- QUADRATIC REGRESSION COEFFICIENTS ---
    // These coefficients are pre-calculated using the DataAnalysis utility.
    private static final double COEFF_A = 0.000302; // Replace with your calculated value
    private static final double COEFF_B = 14.75;    // Replace with your calculated value
    private static final double COEFF_C = 950.0;    // Replace with your calculated value

    @Override
    public void runOpMode() {
        // --- HARDWARE INITIALIZATION ---
        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "front_left");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "front_right");
        backLeftMotor = hardwareMap.get(DcMotorEx.class, "back_left");
        backRightMotor = hardwareMap.get(DcMotorEx.class, "back_right");

        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        launcherMotor = hardwareMap.get(DcMotorEx.class, "launcher_motor");
        launcherMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        launchTrigger = hardwareMap.get(Servo.class, "launch_trigger");

        // --- IMU INITIALIZATION ---
        imu = hardwareMap.get(IMU.class, "imu");
        // FIX: The constructor for RevHubOrientationOnRobot no longer takes no arguments.
        // You MUST specify the logo and USB facing directions.
        // These are example values; you should tune them for your specific robot.
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));

        // Reset the yaw angle to zero. This is the only reset method available now.
        imu.resetYaw();

        // --- PID Controller Initialization ---
        launcherPID = new PIDFController(PID_kP, PID_kI, PID_kD, PID_kF);

        // --- APRILTAG INITIALIZATION ---
        initAprilTag();

        RobotState currentState = RobotState.DRIVE_TO_PRELAUNCH;

        telemetry.addData("Status", "Initialized with PID, IMU, and AprilTag");
        telemetry.update();

        waitForStart();
        launcherPID.reset();

        while (opModeIsActive()) {
            // --- UPDATE ROBOT POSE FROM APRILTAG ---
            updateRobotPoseFromAprilTag();

            // --- MAIN STATE MACHINE ---
            switch (currentState) {
                case DRIVE_TO_PRELAUNCH:
                    telemetry.addData("State", "DRIVE_TO_PRELAUNCH");
                    double distanceToPreLaunch = Math.sqrt(Math.pow(PRE_LAUNCH_X - robotX, 2) + Math.pow(PRE_LAUNCH_Y - robotY, 2));

                    if (distanceToPreLaunch > 5.0) {
                        double angleToTarget = Math.atan2(PRE_LAUNCH_Y - robotY, PRE_LAUNCH_X - robotX);
                        mecanumDrive(Math.cos(angleToTarget), Math.sin(angleToTarget), 0);
                    } else {
                        currentState = RobotState.SPIN_UP_LAUNCHER;
                    }
                    break;

                case SPIN_UP_LAUNCHER:
                    telemetry.addData("State", "SPIN_UP_LAUNCHER");

                    // Calculate the distance to the final target
                    double distanceToTarget = Math.sqrt(Math.pow(TARGET_X - robotX, 2) + Math.pow(TARGET_Y - robotY, 2));

                    // Use the quadratic function to get the required velocity
                    requiredVelocity = getLaunchVelocity(distanceToTarget);

                    // Use the PID controller to set the launcher motor power
                    launcherVelocity = launcherMotor.getVelocity();
                    double motorPower = launcherPID.calculate(requiredVelocity, launcherVelocity);
                    launcherMotor.setPower(motorPower);

                    // Continue driving towards the final target
                    double angleToTarget = Math.atan2(TARGET_Y - robotY, TARGET_X - robotX);
                    mecanumDrive(Math.cos(angleToTarget), Math.sin(angleToTarget), 0);

                    // Check if we are at the target and the launcher is ready
                    if (distanceToTarget < 5.0 && launcherVelocity > (requiredVelocity * 0.95)) {
                        currentState = RobotState.FIRE_ARTIFACT;
                    }
                    break;

                case FIRE_ARTIFACT:
                    telemetry.addData("State", "FIRE_ARTIFACT");
                    launchTrigger.setPosition(1.0);

                    mecanumDrive(0, 0, 0);

                    launcherMotor.setPower(0);
                    currentState = RobotState.POST_FIRE;
                    break;

                case POST_FIRE:
                    telemetry.addData("State", "POST_FIRE");
                    break;
            }

            telemetry.addData("Robot Pos", "(%.2f, %.2f)", robotX, robotY);
            // Updated telemetry to reflect the new logic
            telemetry.addData("Robot Heading (IMU)", "%.2f", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
            telemetry.addData("Robot Heading (Used)", "%.2f", Math.toDegrees(robotHeading));
            telemetry.addData("Required Velocity", "%.2f", requiredVelocity);
            telemetry.addData("Launcher Velocity", "%.2f", launcherVelocity);
            telemetry.update();
        }
        visionPortal.close();
    }

    private void mecanumDrive(double x, double y, double rot) {
        // Use the heading from our combined IMU/AprilTag source
        double botHeading = robotHeading;

        // Rotate the movement vectors by the inverse of the robot's heading to achieve field-centric control
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all powers are normalized to a value between -1 and 1
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rot), 1);
        double frontLeftPower = (rotY + rotX + rot) / denominator;
        double backLeftPower = (rotY - rotX + rot) / denominator;
        double frontRightPower = (rotY - rotX - rot) / denominator;
        double backRightPower = (rotY + rotX - rot) / denominator;

        frontLeftMotor.setPower(frontLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backLeftMotor.setPower(backLeftPower);
        backRightMotor.setPower(backRightPower);
    }

    private void initAprilTag() {
        aprilTag = new AprilTagProcessor.Builder()
                .setCameraPose(cameraPosition, cameraOrientation)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam1"));
        builder.addProcessor(aprilTag);
        visionPortal = builder.build();
    }

    private void updateRobotPoseFromAprilTag() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();

        if (!currentDetections.isEmpty()) {
            // FIX: Corrected the typo from `currentDemos` to `currentDetections`
            AprilTagDetection detection = currentDetections.get(0); // Take the first detected tag

            if (detection.metadata != null) {
                // The detection.robotPose is the pose of the robot *relative to the tag*.
                robotX = detection.ftcPose.x;
                robotY = detection.ftcPose.y;

                // FIX: The IMU interface no longer supports setting a yaw value.
                // We will now directly use the AprilTag's yaw to update our robotHeading.
                robotHeading = detection.ftcPose.yaw;

                telemetry.addData("AprilTag ID", detection.id);
            }
        } else {
            // If no AprilTag is detected, fall back to the IMU for heading.
            robotHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        }
    }

    /**
     * Calculates the required launch velocity based on distance using
     * a pre-calibrated quadratic regression model.
     *
     * @param distance The distance from the robot to the target in inches.
     * @return The required launcher motor velocity in ticks per second.
     */
    private double getLaunchVelocity(double distance) {
        // We use the coefficients derived from the DataAnalysis utility.
        return (COEFF_A * distance * distance) + (COEFF_B * distance) + COEFF_C;
    }
}
