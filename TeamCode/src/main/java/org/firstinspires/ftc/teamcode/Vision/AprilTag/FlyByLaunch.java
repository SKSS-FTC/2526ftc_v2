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
 *  Initialize hardware including Vision Processor under runOpMode()
 *  Add a LAUNCH_VELOCITY_MAP
 *  Create a velocity calculation method getLaunchVelocityFromDistance()
 *  Update state machine: (DRIVE_TO_PRELAUNCH, SPIN_UP_LAUNCHER, FIRE_ARTIFACT)
 *  Refine telemetry for requiredVelocity (setpoint) and launcherVelocity (measured value)
 * @see https://ftc-resources.firstinspires.org/ftc/game/manual
 *
 */
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

// Vision-related imports
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

import java.util.List;


public class FlyByLaunch extends LinearOpMode {

    // --- ROBOT HARDWARE DECLARATIONS ---
    private DcMotorEx frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
    private DcMotorEx launcherMotor;
    private Servo launchTrigger;

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
    private double robotHeading = 0.0;

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

        // --- PID Controller Initialization ---
        launcherPID = new PIDFController(PID_kP, PID_kI, PID_kD, PID_kF);

        // --- APRILTAG INITIALIZATION ---
        initAprilTag();

        RobotState currentState = RobotState.DRIVE_TO_PRELAUNCH;

        telemetry.addData("Status", "Initialized with PID and AprilTag");
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
                    // Use the PID controller to set the launcher motor power
                    double launcherVelocity = launcherMotor.getVelocity();
                    double motorPower = launcherPID.calculate(LAUNCHER_SETPOINT_VELOCITY, launcherVelocity);
                    launcherMotor.setPower(motorPower);

                    // Continue driving towards the final target
                    double angleToTarget = Math.atan2(TARGET_Y - robotY, TARGET_X - robotX);
                    mecanumDrive(Math.cos(angleToTarget), Math.sin(angleToTarget), 0);

                    // Check if we are at the target and the launcher is ready
                    double distanceToTarget = Math.sqrt(Math.pow(TARGET_X - robotX, 2) + Math.pow(TARGET_Y - robotY, 2));
                    if (distanceToTarget < 5.0 && launcherVelocity > (LAUNCHER_SETPOINT_VELOCITY * 0.95)) {
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
            telemetry.addData("Launcher Velocity", launcherMotor.getVelocity());
            telemetry.update();
        }
        visionPortal.close();
    }

    private void mecanumDrive(double x, double y, double rot) {
        double frontLeftPower = x + y + rot;
        double frontRightPower = -x + y - rot;
        double backLeftPower = -x + y + rot;
        double backRightPower = x + y - rot;

        double maxPower = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));

        if (maxPower > 1.0) {
            frontLeftPower /= maxPower;
            frontRightPower /= maxPower;
            backLeftPower /= maxPower;
            backRightPower /= maxPower;
        }

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
            AprilTagDetection detection = currentDetections.get(0); // Take the first detected tag

            // This assumes a single tag is detected and the robot's pose is calculated relative to it
            // For a more advanced setup, you would use multiple tags and a pose fusion algorithm

            if (detection.metadata != null) {
                // The detection.robotPose is the pose of the robot *relative to the tag*.
                // This means a positive X value is right of the tag, Y is forward from the tag.
                robotX = detection.ftcPose.x;
                robotY = detection.ftcPose.y;
                robotHeading = detection.ftcPose.yaw;

                // Add telemetry for debugging
                telemetry.addData("AprilTag ID", detection.id);
            }
        }
    }
}
