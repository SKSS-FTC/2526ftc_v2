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

package org.firstinspires.ftc.teamcode.Match.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

// Vision-related imports
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import java.util.List;
import java.util.Locale;

/**
 * {@link TeleOpPreviewEvent} is a modified TeleOp OpMode for a
 * field-centric mecanum robot with a PID-controlled launcher, intake, and lift.
 * This updated version incorporates AprilTag-based localization to dynamically
 * calculate the required launch velocity based on the robot's distance to a
 * fixed Alliance Goal location, with the goal ID selected by the driver.
 *
 * <p>This class combines several functionalities into a single, comprehensive
 * control program, demonstrating how to handle a complex robot. It includes:
 * <ul>
 * <li>Alliance selection for setting the correct target.</li>
 * <li>Field-centric mecanum drive using an IMU and AprilTag pose estimation.</li>
 * <li>A PID-controlled launcher for consistent velocity.</li>
 * <li>A quadratic regression model to determine launch velocity.</li>
 * <li>An intake mechanism.</li>
 * <li>A lift mechanism for endgame.</li>
 * <li>Comprehensive telemetry for debugging.</li>
 * </ul>
 * </p>
 *
 * @author Gemini
 * @version 1.4
 * @since 2024-05-20
 *
 * @see LinearOpMode
 */
@TeleOp(name = "Preview Event TeleOp", group = "Match")
//@Disabled
public class TeleOpPreviewEvent extends LinearOpMode {

    // --- State Variables and Constants ---
    private ElapsedTime runtime = new ElapsedTime();
    private boolean isSpinningUp = false;
    private boolean aButtonPreviouslyPressed = false;

    // Control Constants
    private static final double INTAKE_POWER = 1.0;
    private static final double LIFT_POWER = 1.0;
    private static final double TRIGGER_FIRED_POSITION = 0.5;
    private static final double TRIGGER_REST_POSITION = 0.7;

    // PID Constants (for launcher control)
    private static final double PID_kP = 0.001;
    private static final double PID_kI = 0.0;
    private static final double PID_kD = 0.0;
    private static final double PID_kF = 0.4;

    // --- QUADRATIC REGRESSION COEFFICIENTS ---
    // These coefficients are pre-calculated using regression analysis.
    // NOTE: You MUST tune these values for your specific robot and field setup.
    private static final double COEFF_A = 0.000302;
    private static final double COEFF_B = 14.75;
    private static final double COEFF_C = 950.0;

    // --- FIELD COORDINATES (in inches) and AprilTag IDs ---
    // The AprilTag IDs are for the Alliance GOALs.
    private static final int RED_ALLIANCE_GOAL_ID = 24;
    private static final double RED_ALLIANCE_GOAL_X = 100.0;
    private static final double RED_ALLIANCE_GOAL_Y = 200.0;

    private static final int BLUE_ALLIANCE_GOAL_ID = 20;
    private static final double BLUE_ALLIANCE_GOAL_X = 300.0;
    private static final double BLUE_ALLIANCE_GOAL_Y = 400.0;

    private static final double MIN_VELOCITY_PERCENTAGE_FOR_FIRE = 0.95;

    // Variables to hold the selected alliance goal coordinates and ID
    private double ALLIANCE_GOAL_X;
    private double ALLIANCE_GOAL_Y;
    private int selectedAprilTagId;

    // --- HARDWARE DECLARATIONS ---
    // Drive Motors
    private DcMotorEx frontLeft = null;
    private DcMotorEx frontRight = null;
    private DcMotorEx backLeft = null;
    private DcMotorEx backRight = null;

    // Mechanism Motors
    private DcMotor intake = null;
    private DcMotorEx launcherMotor = null;
    private DcMotorEx liftMotor = null;

    // Servos
    private Servo launchTrigger = null;

    // Sensors
    private IMU imu = null;

    // Vision
    private WebcamName webcam = null;
    private AprilTagProcessor aprilTag = null;
    private VisionPortal visionPortal = null;

    // --- ROBOT POSE ESTIMATION ---
    // These variables will be updated continuously by the AprilTag processor
    private double robotX = 0.0;
    private double robotY = 0.0;
    private double robotHeading = 0.0; // In radians

    // Custom PID controller class
    private PIDFController launcherPID;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initializing...");
        telemetry.update();

        // --- HARDWARE MAPPING ---
        // Drive Motors
        frontLeft = hardwareMap.get(DcMotorEx.class, "front_left");
        frontRight = hardwareMap.get(DcMotorEx.class, "front_right");
        backLeft = hardwareMap.get(DcMotorEx.class, "back_left");
        backRight = hardwareMap.get(DcMotorEx.class, "back_right");

        // Mechanism Motors & Servos
        intake = hardwareMap.get(DcMotor.class, "intake_motor");
        launcherMotor = hardwareMap.get(DcMotorEx.class, "launcher_motor");
        liftMotor = hardwareMap.get(DcMotorEx.class, "lift_motor");
        launchTrigger = hardwareMap.get(Servo.class, "launch_trigger");

        // Sensors
        imu = hardwareMap.get(IMU.class, "imu");
        webcam = hardwareMap.get(WebcamName.class, "Webcam1");

        // --- MOTOR DIRECTION AND BEHAVIOR ---
        // Drivetrain
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Mechanisms
        launcherMotor.setDirection(DcMotor.Direction.FORWARD);
        liftMotor.setDirection(DcMotor.Direction.FORWARD);
        intake.setDirection(DcMotor.Direction.FORWARD);
        launcherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        launcherMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // --- SENSOR INITIALIZATION ---
        // IMU Initialization
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));
        imu.resetYaw();

        // Vision Initialization
        initAprilTag();

        // PID Controller Initialization
        launcherPID = new PIDFController(PID_kP, PID_kI, PID_kD, PID_kF);

        // Set initial servo position
        launchTrigger.setPosition(TRIGGER_REST_POSITION);

        // --- ALLIANCE SELECTION LOOP ---
        telemetry.addData("Status", "Waiting for Alliance Selection...");
        telemetry.addData("Controls", "Press A for Blue Alliance, B for Red Alliance");
        telemetry.update();

        boolean allianceSelected = false;

        // This loop will wait for the driver to select the alliance
        while (!isStarted() && !isStopRequested() && !allianceSelected) {
            // Check for gamepad input to select the alliance
            if (gamepad1.a) {
                ALLIANCE_GOAL_X = BLUE_ALLIANCE_GOAL_X;
                ALLIANCE_GOAL_Y = BLUE_ALLIANCE_GOAL_Y;
                selectedAprilTagId = BLUE_ALLIANCE_GOAL_ID;
                allianceSelected = true;
                telemetry.addData("Alliance Selected", "Blue Alliance");
            } else if (gamepad1.b) {
                ALLIANCE_GOAL_X = RED_ALLIANCE_GOAL_X;
                ALLIANCE_GOAL_Y = RED_ALLIANCE_GOAL_Y;
                selectedAprilTagId = RED_ALLIANCE_GOAL_ID;
                allianceSelected = true;
                telemetry.addData("Alliance Selected", "Red Alliance");
            }
            telemetry.update();
        }

        telemetry.addData("Status", "Initialized. Ready to run!");
        telemetry.update();

        waitForStart();
        runtime.reset();
        launcherPID.reset();

        // Main OpMode Loop
        while (opModeIsActive()) {

            // --- LOCALIZATION: UPDATE ROBOT POSE FROM APRILTAGS ---
            updateRobotPoseFromAprilTag();

            // --- DRIVETRAIN: FIELD-CENTRIC MECANUM DRIVE ---
            double y = -gamepad1.left_stick_y; // Reverse stick for forward motion
            double x = gamepad1.left_stick_x;
            double rot = gamepad1.right_stick_x;

            // Rotate the movement vectors by the inverse of the robot's heading to achieve
            // field-centric control. We now use the robotHeading updated from AprilTags.
            double rotX = x * Math.cos(-robotHeading) - y * Math.sin(-robotHeading);
            double rotY = x * Math.sin(-robotHeading) + y * Math.cos(-robotHeading);

            // Normalize powers to a value between -1 and 1
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rot), 1);
            frontLeft.setPower((rotY + rotX + rot) / denominator);
            backLeft.setPower((rotY - rotX + rot) / denominator);
            frontRight.setPower((rotY - rotX - rot) / denominator);
            backRight.setPower((rotY + rotX - rot) / denominator);

            // --- INTAKE CONTROL ---
            if (gamepad1.right_trigger > 0.1) {
                // Ingesting (pulling items in)
                intake.setPower(INTAKE_POWER);
            } else if (gamepad1.left_trigger > 0.1) {
                // Ejecting (pushing items out)
                intake.setPower(-INTAKE_POWER);
            } else {
                intake.setPower(0);
            }

            // --- LAUNCHER CONTROL (PID) ---
            // Toggle launcher state with the 'A' button
            if (gamepad1.a && !aButtonPreviouslyPressed) {
                isSpinningUp = !isSpinningUp;
            }
            aButtonPreviouslyPressed = gamepad1.a;

            if (isSpinningUp) {
                // Calculate the distance to the Alliance Goal
                double distanceToGoal = Math.sqrt(Math.pow(ALLIANCE_GOAL_X - robotX, 2) + Math.pow(ALLIANCE_GOAL_Y - robotY, 2));
                // Use the quadratic function to get the required velocity
                double requiredVelocity = getLaunchVelocity(distanceToGoal);

                // Get current velocity from the encoder on the launcher motor
                double currentVelocity = launcherMotor.getVelocity();
                // Calculate the power required using the PID controller
                double motorPower = launcherPID.calculate(requiredVelocity, currentVelocity);
                // Set power to the launcher motor.
                launcherMotor.setPower(motorPower);

                // Fire the launcher using the 'B' button, but only if the launcher is at speed.
                if (gamepad1.b && (currentVelocity >= requiredVelocity * MIN_VELOCITY_PERCENTAGE_FOR_FIRE)) {
                    launchTrigger.setPosition(TRIGGER_FIRED_POSITION);
                } else {
                    launchTrigger.setPosition(TRIGGER_REST_POSITION);
                }
            } else {
                // If the launcher is off, turn motors off and reset trigger
                launcherMotor.setPower(0);
                launchTrigger.setPosition(TRIGGER_REST_POSITION);
            }


            // --- LIFT CONTROL ---
            if (gamepad1.dpad_up) {
                liftMotor.setPower(LIFT_POWER);
            } else if (gamepad1.dpad_down) {
                liftMotor.setPower(-LIFT_POWER);
            } else {
                liftMotor.setPower(0);
            }

            // --- TELEMETRY ---
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("--- Drivetrain ---", "");
            telemetry.addData("Robot Heading (IMU)", "%.2f degrees", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
            telemetry.addData("Robot Pose (AprilTag)", "(X=%.2f, Y=%.2f, Heading=%.2f)", robotX, robotY, Math.toDegrees(robotHeading));
            telemetry.addData("--- Launcher ---", "");
            telemetry.addData("Launcher Status", isSpinningUp ? "ON" : "OFF");
            telemetry.addData("Targeting AprilTag ID", selectedAprilTagId);
            if (isSpinningUp) {
                double distanceToGoal = Math.sqrt(Math.pow(ALLIANCE_GOAL_X - robotX, 2) + Math.pow(ALLIANCE_GOAL_Y - robotY, 2));
                double requiredVelocity = getLaunchVelocity(distanceToGoal);
                telemetry.addData("Target Velocity", "%.2f", requiredVelocity);
                telemetry.addData("Actual Velocity", "%.2f", launcherMotor.getVelocity());
                telemetry.addData("Launcher Power", "%.2f", launcherMotor.getPower());
                telemetry.addData("Distance to Goal", "%.2f inches", distanceToGoal);
            }
            telemetry.update();
        }

        // Close vision portal on OpMode end
        visionPortal.close();
    }

    /**
     * Initializes the AprilTag vision processor and vision portal.
     * This method is adapted from the FlyByLaunch example code.
     */
    private void initAprilTag() {
        // Set up the AprilTag processor
        // NOTE: The camera's position and orientation on the robot are hardcoded here.
        // You MUST tune these values for your specific robot setup.
        Position cameraPosition = new Position(DistanceUnit.INCH, 0, 0, 0, 0); // Position on robot
        YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES, 0, -90, 0, 0); // Orientation relative to robot
        aprilTag = new AprilTagProcessor.Builder()
                .setCameraPose(cameraPosition, cameraOrientation)
                .build();

        // Create the vision portal
        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(webcam);
        builder.addProcessor(aprilTag);
        visionPortal = builder.build();
    }

    /**
     * Updates the robot's pose (X, Y, Heading) based on AprilTag detections.
     * This version filters detections to only use the AprilTag ID of the selected Alliance Goal.
     * If no tags are detected, it falls back to the IMU for heading.
     */
    private void updateRobotPoseFromAprilTag() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        AprilTagDetection targetDetection = null;

        // Iterate through all detections to find the one matching our selected Alliance GOAL ID
        for (AprilTagDetection detection : currentDetections) {
            if (detection.id == selectedAprilTagId) {
                targetDetection = detection;
                break;
            }
        }

        if (targetDetection != null) {
            // Use the target tag's pose to update our robot's pose
            if (targetDetection.metadata != null) {
                robotX = targetDetection.ftcPose.x;
                robotY = targetDetection.ftcPose.y;
                robotHeading = targetDetection.ftcPose.yaw; // AprilTag yaw is in radians
            }
        } else {
            // If the target AprilTag is not detected, fall back to the IMU for heading.
            robotHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        }
    }

    /**
     * Calculates the required launch velocity based on distance using
     * a pre-calibrated quadratic regression model.
     * This method is adapted from the FlyByLaunch example code.
     *
     * @param distance The distance from the robot to the target in inches.
     * @return The required launcher motor velocity in ticks per second.
     */
    private double getLaunchVelocity(double distance) {
        // We use the coefficients derived from the DataAnalysis utility.
        return (COEFF_A * distance * distance) + (COEFF_B * distance) + COEFF_C;
    }

    /**
     * The FTC SDK does not include a built-in PID controller class.
     * This custom class provides a basic implementation for velocity control.
     */
    private static class PIDFController {
        private final double kP, kI, kD, kF;
        private double integralSum = 0;
        private double lastError = 0;
        private ElapsedTime timer = new ElapsedTime();

        public PIDFController(double kP, double kI, double kD, double kF) {
            this.kP = kP;
            this.kI = kI;
            this.kD = kD;
            this.kF = kF;
        }

        public double calculate(double target, double measuredValue) {
            double error = target - measuredValue;
            double proportional = error * kP;
            integralSum += error * timer.seconds();
            double derivative = (error - lastError) / timer.seconds();
            lastError = error;
            timer.reset();

            return proportional + (integralSum * kI) + (derivative * kD) + kF;
        }

        public void reset() {
            integralSum = 0;
            lastError = 0;
            timer.reset();
        }
    }
}
