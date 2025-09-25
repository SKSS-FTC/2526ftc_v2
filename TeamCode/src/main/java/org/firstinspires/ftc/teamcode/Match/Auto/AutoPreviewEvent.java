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
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

@Autonomous(name="Preview Event Auto", group="Match", preselectTeleOp="TeleOpPreviewEvent")
//@Disabled
public class AutoPreviewEvent extends LinearOpMode {

    // --- VISION CONSTANTS ---
    private static final boolean USE_WEBCAM = true;
    private static final int[] OBELISK_TAG_IDS = {21, 22, 23};
    // AprilTag IDs for the Red and Blue Alliance goals
    private static final int RED_GOAL_TAG_ID = 24;
    private static final int BLUE_GOAL_TAG_ID = 20;
    private static final double LAUNCH_DISTANCE_INCHES = 6.0;

    // --- WAYPOINT CONSTANTS ---
    // Waypoints for Red Alliance starting positions and post-obelisk positions
    private final Map<Position, Waypoint> redStartWaypoints = new HashMap<Position, Waypoint>() {{
        put(Position.POS1, new Waypoint(12, 12, 0));
        put(Position.POS2, new Waypoint(12, 36, 0));
        put(Position.POS3, new Waypoint(12, 60, 0));
    }};
    private final Map<Position, Waypoint> redGoalWaypoints = new HashMap<Position, Waypoint>() {{
        put(Position.POS1, new Waypoint(12, 12, 90));
        put(Position.POS2, new Waypoint(12, 36, 90));
        put(Position.POS3, new Waypoint(12, 60, 90));
    }};

    // Waypoints for Blue Alliance starting positions and post-obelisk positions
    private final Map<Position, Waypoint> blueStartWaypoints = new HashMap<Position, Waypoint>() {{
        put(Position.POS1, new Waypoint(-12, 12, 180));
        put(Position.POS2, new Waypoint(-12, 36, 180));
        put(Position.POS3, new Waypoint(-12, 60, 180));
    }};
    private final Map<Position, Waypoint> blueGoalWaypoints = new HashMap<Position, Waypoint>() {{
        put(Position.POS1, new Waypoint(-12, 12, 270));
        put(Position.POS2, new Waypoint(-12, 36, 270));
        put(Position.POS3, new Waypoint(-12, 60, 270));
    }};
    private static final Waypoint LEAVE_WAYPOINT = new Waypoint(0, 0, 0); // Placeholder values

    // --- CAMERA OFFSETS ---
    private static final double X_CAMERA_OFFSET_INCHES = 0.0;
    private static final double Y_CAMERA_OFFSET_INCHES = 0.0;

    // --- CONTROL CONSTANTS ---
    // Proportional constant for position correction. Higher values mean more aggressive correction.
    static final double POSITION_KP = 0.1;
    // Proportional constant for heading correction. Higher values mean more aggressive turning.
    static final double HEADING_KP = 0.05;
    // The amount of heading error (in degrees) that is tolerated before initiating a turn-in-place.
    static final double HEADING_TURN_TOLERANCE_DEG = 5.0;
    static final double POSITION_TOLERANCE_IN = 1.0;
    static final double HEADING_TOLERANCE_DEG = 2.0;
    static final double TIMEOUT_S = 10.0;
    static final int ARTIFACTS_TO_LAUNCH = 3;
    static final long LAUNCH_DELAY_MS = 1000;

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
        SCAN_OBELISK,
        POSITION_FOR_GOAL,
        SCAN_GOAL,
        DRIVE_TO_GOAL,
        LAUNCH,
        WAIT_FOR_LAUNCH,
        LEAVE,
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
    private RobotState currentState = RobotState.SCAN_OBELISK;
    private Alliance alliance = Alliance.RED;
    private Position position = Position.POS1;

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
        // --- Advise on IMU orientation on robot ---
        telemetry.addData("IMU", "Logo UP");
        telemetry.addData("IMU", "USB FORWARD");
        telemetry.update();

        // --- Vision Initialization ---
        initAprilTag();

        telemetry.addData("Status", "Ready for Selections");
        telemetry.update();

        // --- Driver Hub Pre-Match Selection ---
        // Pause and wait for Alliance selection
        boolean allianceSelected = false;
        while (!isStarted() && !isStopRequested() && !allianceSelected) {
            telemetry.addData("Status", "SELECT ALLIANCE");
            telemetry.addData("Alliance", "Press X for Red, B for Blue");
            telemetry.update();

            if (gamepad1.x) {
                alliance = Alliance.RED;
                allianceSelected = true;
            } else if (gamepad1.b) {
                alliance = Alliance.BLUE;
                allianceSelected = true;
            }
        }

        // Pause and wait for Position selection
        boolean positionSelected = false;
        while (!isStarted() && !isStopRequested() && !positionSelected) {
            telemetry.addData("Status", "SELECT STARTING POSITION");
            telemetry.addData("Current Alliance", alliance.toString());
            telemetry.addData("Position", "D-pad Left (POS1), Up (POS2), Right (POS3)");
            telemetry.update();

            if (gamepad1.dpad_left) {
                position = Position.POS1;
                positionSelected = true;
            } else if (gamepad1.dpad_up) {
                position = Position.POS2;
                positionSelected = true;
            } else if (gamepad1.dpad_right) {
                position = Position.POS3;
                positionSelected = true;
            }
        }

        // Display final selections for user confirmation
        while (!isStarted() && !isStopRequested()) {
            telemetry.addData("Status", "Selections Confirmed. Press INIT");
            telemetry.addData("Alliance", alliance.toString());
            telemetry.addData("Position", position.toString());
            telemetry.update();
        }

        // Set the correct GOAL tag based on the selected alliance
        goalTagId = (alliance == Alliance.RED) ? RED_GOAL_TAG_ID : BLUE_GOAL_TAG_ID;

        waitForStart();

        // Stop vision portal to save CPU resources
        visionPortal.stopStreaming();

        // --- Autonomous Execution ---
        if (opModeIsActive()) {
            runAutonomousRoutine();
        }

        // Final cleanup
        stopRobot();
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
     * Main Autonomous Routine with FSM
     */
    private void runAutonomousRoutine() {
        while (opModeIsActive() && currentState != RobotState.COMPLETE) {
            telemetry.addData("Current State", currentState.toString());
            telemetry.update();

            switch (currentState) {
                case SCAN_OBELISK:
                    telemetry.addData("Status", "Scanning for Obelisk");
                    // Obelisk tags are 21, 22, 23
                    AprilTagDetection obeliskDetection = getFirstDetectedTag(OBELISK_TAG_IDS);
                    if (obeliskDetection != null) {
                        telemetry.addData("Obelisk Found", "ID: %d", obeliskDetection.id);
                        driveToAprilTag(obeliskDetection, LAUNCH_DISTANCE_INCHES); // Drive to obelisk
                        currentState = RobotState.POSITION_FOR_GOAL;
                    }
                    break;

                case POSITION_FOR_GOAL:
                    telemetry.addData("Status", "Positioning for Goal");
                    Waypoint goalWaypoint = (alliance == Alliance.RED) ? redGoalWaypoints.get(position) : blueGoalWaypoints.get(position);
                    driveToWaypoint(goalWaypoint);
                    currentState = RobotState.SCAN_GOAL;
                    break;

                case SCAN_GOAL:
                    telemetry.addData("Status", "Scanning for Goal");
                    AprilTagDetection goalDetection = getFirstDetectedTag(goalTagId);
                    if (goalDetection != null) {
                        telemetry.addData("Goal Found", "ID: %d", goalDetection.id);
                        telemetry.addData("Range", "%.2f", goalDetection.ftcPose.range);
                        telemetry.addData("Bearing", "%.2f", goalDetection.ftcPose.bearing);
                        telemetry.addData("Elevation", "%.2f", goalDetection.ftcPose.elevation);
                        telemetry.update();
                        currentState = RobotState.DRIVE_TO_GOAL;
                    }
                    break;

                case DRIVE_TO_GOAL:
                    telemetry.addData("Status", "Driving to Launch Position");
                    AprilTagDetection latestGoalDetection = getFirstDetectedTag(goalTagId);
                    if (latestGoalDetection != null) {
                        driveToAprilTag(latestGoalDetection, LAUNCH_DISTANCE_INCHES);
                        currentState = RobotState.LAUNCH;
                    }
                    break;

                case LAUNCH:
                    telemetry.addData("Status", "Launching Artifacts");
                    for (int i = 0; i < ARTIFACTS_TO_LAUNCH; i++) {
                        launch();
                        telemetry.addData("Launch", "%d of %d", i + 1, ARTIFACTS_TO_LAUNCH);
                        telemetry.update();
                        sleep(LAUNCH_DELAY_MS);
                    }
                    currentState = RobotState.WAIT_FOR_LAUNCH;
                    break;

                case WAIT_FOR_LAUNCH:
                    telemetry.addData("Status", "Waiting for Launch Sequence to Complete");
                    sleep(1000);
                    currentState = RobotState.LEAVE;
                    break;

                case LEAVE:
                    telemetry.addData("Status", "Leaving Launch Zone");
                    driveToWaypoint(LEAVE_WAYPOINT); // Drive to a pre-defined safe location
                    currentState = RobotState.COMPLETE;
                    break;

                case COMPLETE:
                    telemetry.addData("Status", "Autonomous Routine Complete");
                    telemetry.update();
                    stopRobot();
                    break;
            }
        }
    }

    /**
     * Finds the first detected AprilTag with a specific ID.
     * @param tagIds The IDs of the tags to search for.
     * @return The first AprilTagDetection found, or null if not found.
     */
    private AprilTagDetection getFirstDetectedTag(int[] tagIds) {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            for (int tagId : tagIds) {
                if (detection.id == tagId) {
                    return detection;
                }
            }
        }
        return null;
    }

    /**
     * Finds the first detected AprilTag with a specific ID.
     * @param tagId The ID of the tag to search for.
     * @return The first AprilTagDetection found, or null if not found.
     */
    private AprilTagDetection getFirstDetectedTag(int tagId) {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
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
     * The target waypoint is created by reducing the range to the tag by LAUNCH_DISTANCE.
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
                // Assume a simpler world where the corrected pose is our current pose
                double currentX = correctedPose.x;
                double currentY = correctedPose.y;
                double currentHeading = correctedPose.heading;

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
    }

    /**
     * Drives the robot to a specific waypoint using a proportional control loop.
     * This function now prioritizes turning to face the target before driving.
     * @param target The target waypoint (x, y, heading) to navigate to.
     */
    private void driveToWaypoint(Waypoint target) {
        double xError = target.x - target.x; // Use a better pose estimation later
        double yError = target.y - target.y; // Use a better pose estimation later
        double headingError = getHeadingError(target.heading);

        while (opModeIsActive() && (Math.abs(xError) > POSITION_TOLERANCE_IN || Math.abs(yError) > POSITION_TOLERANCE_IN || Math.abs(headingError) > HEADING_TOLERANCE_DEG)) {
            xError = target.x - target.x; // Re-evaluate pose here
            yError = target.y - target.y; // Re-evaluate pose here
            headingError = getHeadingError(target.heading);

            while (headingError > 180) headingError -= 360;
            while (headingError < -180) headingError += 360;

            double axialPower;
            double lateralPower;
            double yawPower;

            if (Math.abs(headingError) > HEADING_TURN_TOLERANCE_DEG) {
                axialPower = 0;
                lateralPower = 0;
                yawPower = -headingError * HEADING_KP;
            } else {
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

            telemetry.addData("Current State", currentState.toString());
            telemetry.addData("Target Waypoint", "X=%.1f, Y=%.1f, Heading=%.1f", target.x, target.y, target.heading);
            telemetry.addData("Error", "X=%.1f, Y=%.1f, Heading=%.1f", xError, yError, headingError);
            telemetry.update();
        }
        stopRobot();
    }

    /**
     * Gets the heading from the IMU in degrees.
     */
    private double getImuHeading() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
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
     * Helper method to simulate launching an artifact.
     */
    private void launch() {
        // Placeholder for launching logic
        // This is where you would activate your launcher motor or servo
    }

    /**
     * Stops all drive motors.
     */
    private void stopRobot() {
        motorLeftFront.setPower(0);
        motorLeftBack.setPower(0);
        motorRightFront.setPower(0);
        motorRightBack.setPower(0);
    }

    // A simple class to represent a Waypoint in the autonomous path
    private static class Waypoint {
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
