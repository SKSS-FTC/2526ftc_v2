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

package org.firstinspires.ftc.teamcode.Match.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Autonomous(name="Preview Event", group="Match", preselectTeleOp="PreviewEventTeleOp")
//@Disabled
public class AutoPreviewEvent extends LinearOpMode {

    // --- VISION CONSTANTS ---
    private static final boolean USE_WEBCAM = true;
    private static final int OBELISK_TAG_ID = 1;
    // AprilTag IDs for the Red and Blue Alliance goals
    private static final int RED_GOAL_TAG_ID = 24;
    private static final int BLUE_GOAL_TAG_ID = 20;
    private static final double TRAVEL_DELTA_INCHES = 6.0;

    // --- CAMERA OFFSETS ---
    private static final double X_CAMERA_OFFSET_INCHES = 0.0;
    private static final double Y_CAMERA_OFFSET_INCHES = 0.0;

    // --- ENCODER CONSTANTS ---
    static final double COUNTS_PER_MOTOR_REV = 537.7;
    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double DRIVE_GEAR_REDUCTION = 1.0;
    static final double TICKS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math.PI);
    static final double ROBOT_TRACK_WIDTH_IN = 14.0;
    static final double DRIVE_SPEED = 0.6;
    static final double TIMEOUT_S = 10.0;

    // --- CONTROL CONSTANTS ---
    // Proportional constant for position correction. Higher values mean more aggressive correction.
    // Be careful with high values, as they can cause oscillations.
    static final double POSITION_KP = 0.1;
    // Proportional constant for heading correction. Higher values mean more aggressive turning.
    static final double HEADING_KP = 0.05;
    // The amount of heading error (in degrees) that is tolerated before initiating a turn-in-place.
    static final double HEADING_TURN_TOLERANCE_DEG = 5.0;
    static final double POSITION_TOLERANCE_IN = 1.0;
    static final double HEADING_TOLERANCE_DEG = 2.0;

    // Enumerations for Alliance and Position
    enum Alliance {
        RED,
        BLUE
    }

    enum Position {
        POS1,
        POS2,
        POS3
    }

    // Enumeration for the Finite State Machine (FSM)
    enum RobotState {
        FIND_OBELISK,
        DRIVE_TO_OBELISK,
        FIND_GOAL,
        DRIVE_TO_GOAL,
        LAUNCH,
        WAIT_FOR_LAUNCH,
        COMPLETE
    }

    // Hardware for Mecanum Drivetrain
    private DcMotorEx motorLeftFront = null;
    private DcMotorEx motorLeftBack = null;
    private DcMotorEx motorRightFront = null;
    private DcMotorEx motorRightBack = null;
    private IMU imu = null;

    // AprilTag and Vision variables
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;
    private int goalTagId;

    // State Machine variables
    private RobotState currentState = RobotState.FIND_OBELISK;

    // Robot pose variables (updated by AprilTag localization)
    private double currentX = 0;
    private double currentY = 0;
    private double currentHeading = 0;

    @Override
    public void runOpMode() {
        // --- Hardware Initialization ---
        motorLeftFront = hardwareMap.get(DcMotorEx.class, "motorLeftFront");
        motorLeftBack = hardwareMap.get(DcMotorEx.class, "motorLeftBack");
        motorRightFront = hardwareMap.get(DcMotorEx.class, "motorRightFront");
        motorRightBack = hardwareMap.get(DcMotorEx.class, "motorRightBack");
        imu = hardwareMap.get(IMU.class, "imu");

        motorLeftFront.setDirection(DcMotorEx.Direction.REVERSE);
        motorLeftBack.setDirection(DcMotorEx.Direction.REVERSE);
        motorRightFront.setDirection(DcMotorEx.Direction.FORWARD);
        motorRightBack.setDirection(DcMotorEx.Direction.FORWARD);

        motorLeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        )));
        imu.resetYaw();

        // --- Vision Initialization ---
        initAprilTag();

        telemetry.addData("Status", "Ready for Selection");
        telemetry.update();

        // --- Driver Hub Pre-Match Selection ---
        Alliance alliance = Alliance.RED; // Default to Red Alliance
        while (!isStarted() && !isStopRequested()) {
            // Alliance Selection
            if (gamepad1.b) {
                alliance = Alliance.BLUE;
            } else if (gamepad1.x) {
                alliance = Alliance.RED;
            }
            telemetry.addData("Alliance", "Press B for Blue, X for Red");
            telemetry.addData("Current Selection", "Alliance: %s", alliance.toString());
            telemetry.update();
        }

        // Set the correct GOAL tag based on the selected alliance
        if (alliance == Alliance.RED) {
            goalTagId = RED_GOAL_TAG_ID;
        } else {
            goalTagId = BLUE_GOAL_TAG_ID;
        }

        waitForStart();

        // --- Autonomous Execution ---
        if (opModeIsActive()) {
            telemetry.addData("Status", "Executing AprilTag Routine");
            telemetry.addData("Target Goal", "ID: %d", goalTagId);
            telemetry.update();
            runAutonomousRoutine();
            sleep(1000);
        }
    }

    // --- Main Autonomous Routine with FSM ---
    private void runAutonomousRoutine() {
        while (opModeIsActive() && currentState != RobotState.COMPLETE) {
            telemetry.addData("Current State", currentState.toString());
            telemetry.update();

            switch (currentState) {
                case FIND_OBELISK:
                    // Find and drive to the Obelisk AprilTag.
                    AprilTagDetection obeliskDetection = getFirstDetectedTag(OBELISK_TAG_ID);
                    if (obeliskDetection != null) {
                        driveToAprilTag(obeliskDetection, TRAVEL_DELTA_INCHES);
                        currentState = RobotState.FIND_GOAL;
                    }
                    break;

                case FIND_GOAL:
                    // Find and drive to the Goal AprilTag based on the alliance.
                    AprilTagDetection goalDetection = getFirstDetectedTag(goalTagId);
                    if (goalDetection != null) {
                        driveToAprilTag(goalDetection, TRAVEL_DELTA_INCHES);
                        currentState = RobotState.LAUNCH;
                    }
                    break;

                case LAUNCH:
                    // Perform the launch action at the current waypoint location.
                    launch(1.0);
                    currentState = RobotState.WAIT_FOR_LAUNCH;
                    break;

                case WAIT_FOR_LAUNCH:
                    sleep(2000);
                    currentState = RobotState.COMPLETE;
                    break;

                case COMPLETE:
                    break;
            }
        }
    }

    /**
     * Initializes the AprilTag processor and VisionPortal.
     */
    private void initAprilTag() {
        aprilTag = new AprilTagProcessor.Builder()
                .setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary()) // Use the default library for game tags
                .setOutputUnits(org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        builder.addProcessor(aprilTag);
        visionPortal = builder.build();
    }

    /**
     * Finds the first detected AprilTag with a specific ID.
     * @param tagId The ID of the tag to search for.
     * @return The first AprilTagDetection found, or null if not found.
     */
    private AprilTagDetection getFirstDetectedTag(int tagId) {
        visionPortal.stopStreaming();
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        visionPortal.resumeStreaming();

        for (AprilTagDetection detection : currentDetections) {
            if (detection.id == tagId) {
                return detection;
            }
        }
        return null;
    }

    /**
     * Corrects the AprilTag-detected robot pose to account for the camera's
     * mounting offset from the robot's center.
     * @param detection The raw AprilTag detection.
     * @return A new Pose2d object with the corrected robot position and heading.
     */
    private Waypoint getRobotPoseFromAprilTagDetection(AprilTagDetection detection) {
        double tagX = detection.ftcPose.x;
        double tagY = detection.ftcPose.y;
        double tagHeading = detection.ftcPose.yaw;

        double headingRad = Math.toRadians(tagHeading);

        double cameraXInRobotFrame = Y_CAMERA_OFFSET_INCHES * Math.cos(headingRad) - X_CAMERA_OFFSET_INCHES * Math.sin(headingRad);
        double cameraYInRobotFrame = Y_CAMERA_OFFSET_INCHES * Math.sin(headingRad) + X_CAMERA_OFFSET_INCHES * Math.cos(headingRad);

        double robotX = tagX - cameraXInRobotFrame;
        double robotY = tagY - cameraYInRobotFrame;
        double robotHeading = tagHeading;

        return new Waypoint(robotX, robotY, robotHeading);
    }

    /**
     * Drives the robot to a waypoint relative to a detected AprilTag.
     * The target waypoint is created by reducing the range to the tag by TRAVEL_DELTA.
     * @param targetTag The AprilTag to navigate towards.
     * @param travelDelta The distance in inches to stop before the tag's location.
     */
    private void driveToAprilTag(AprilTagDetection targetTag, double travelDelta) {

        telemetry.addData("Driving to Tag", targetTag.id);
        telemetry.update();

        double startTime = getRuntime();
        Waypoint targetWaypoint = null;

        while (opModeIsActive() && getRuntime() - startTime < TIMEOUT_S) {

            AprilTagDetection latestDetection = getFirstDetectedTag(targetTag.id);

            if (latestDetection != null) {
                Waypoint correctedPose = getRobotPoseFromAprilTagDetection(latestDetection);
                currentX = correctedPose.x;
                currentY = correctedPose.y;
                currentHeading = correctedPose.heading;

                double targetRange = latestDetection.ftcPose.range - travelDelta;
                double targetBearingRad = Math.toRadians(latestDetection.ftcPose.bearing);

                double targetX = currentX + targetRange * Math.cos(targetBearingRad);
                double targetY = currentY + targetRange * Math.sin(targetBearingRad);
                double targetHeading = latestDetection.ftcPose.yaw;

                targetWaypoint = new Waypoint(targetX, targetY, targetHeading);

                driveToWaypoint(targetWaypoint);

                if (Math.abs(targetX - currentX) < POSITION_TOLERANCE_IN &&
                        Math.abs(targetY - currentY) < POSITION_TOLERANCE_IN &&
                        Math.abs(getHeadingError(targetHeading)) < HEADING_TOLERANCE_DEG) {
                    break;
                }

            } else {
                telemetry.addData("Warning", "Lost sight of target tag %d", targetTag.id);
                telemetry.update();
                if (targetWaypoint != null) {
                    driveToWaypoint(targetWaypoint);
                }
            }
        }

        motorLeftFront.setPower(0);
        motorLeftBack.setPower(0);
        motorRightFront.setPower(0);
        motorRightBack.setPower(0);
    }

    /**
     * Drives the robot to a specific waypoint using a proportional control loop.
     * This function now prioritizes turning to face the target before driving.
     * @param target The target waypoint (x, y, heading) to navigate to.
     */
    private void driveToWaypoint(Waypoint target) {
        double xError = target.x - currentX;
        double yError = target.y - currentY;
        double headingError = getHeadingError(target.heading);

        while (headingError > 180) headingError -= 360;
        while (headingError < -180) headingError += 360;

        double axialPower;
        double lateralPower;
        double yawPower;

        // Check if the robot's heading is within the acceptable range for driving.
        if (Math.abs(headingError) > HEADING_TURN_TOLERANCE_DEG) {
            // Turn in place
            axialPower = 0;
            lateralPower = 0;
            yawPower = -headingError * HEADING_KP;
        } else {
            // Drive and correct heading simultaneously
            axialPower = yError * POSITION_KP;
            lateralPower = xError * POSITION_KP;
            yawPower = -headingError * HEADING_KP;
        }

        double max = Math.max(Math.abs(axialPower + lateralPower + yawPower),
                Math.abs(axialPower - lateralPower - yawPower));
        max = Math.max(max, Math.abs(axialPower - lateralPower + yawPower));
        max = Math.max(max, Math.abs(axialPower + lateralPower - yawPower));

        if (max > 1.0) {
            axialPower /= max;
            lateralPower /= max;
            yawPower /= max;
        }

        motorLeftFront.setPower(axialPower + lateralPower + yawPower);
        motorRightFront.setPower(axialPower - lateralPower - yawPower);
        motorLeftBack.setPower(axialPower - lateralPower + yawPower);
        motorRightBack.setPower(axialPower + lateralPower - yawPower);

        // --- NEW: Detailed Telemetry Output ---
        telemetry.addData("Current State", currentState.toString());
        telemetry.addData("Current Location", "X=%.1f, Y=%.1f, Heading=%.1f", currentX, currentY, currentHeading);
        telemetry.addData("Target Waypoint", "X=%.1f, Y=%.1f, Heading=%.1f", target.x, target.y, target.heading);
        telemetry.addData("Error", "X=%.1f, Y=%.1f, Heading=%.1f", xError, yError, headingError);
        telemetry.addData("Motor Powers", "Axial=%.2f, Lateral=%.2f, Yaw=%.2f", axialPower, lateralPower, yawPower);
        telemetry.update();
    }

    /**
     * Gets the heading from the IMU in degrees.
     */
    private double getImuHeading() {
        Orientation angles = imu.getRobotOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angles.firstAngle;
    }

    /**
     * Calculates the shortest angle difference between two headings.
     */
    private double getHeadingError(double targetHeading) {
        double currentImuHeading = getImuHeading();
        double error = targetHeading - currentImuHeading;
        while (error > 180) {
            error -= 360;
        }
        while (error < -180) {
            error += 360;
        }
        return error;
    }

    /**
     * Helper method to start the launching mechanism.
     * Replace the placeholder code with your actual launcher control logic.
     */
    private void launch(double power) {
        // Example: Set the launcher motor power
        // launcherMotor.setPower(1.0);
        sleep(1500); // Placeholder for launch duration
    }

    // A simple class to represent a Waypoint in the autonomous path
    private class Waypoint {
        public double x;
        public double y;
        public double heading; // in degrees

        public Waypoint(double x, double y, double heading) {
            this.x = x;
            this.y = y;
            this.heading = heading;
        }
    }
}
