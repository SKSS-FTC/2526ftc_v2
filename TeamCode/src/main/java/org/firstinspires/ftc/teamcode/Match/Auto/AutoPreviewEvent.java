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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

/**
 * AutoPreviewEvent is a flexible autonomous OpMode utilizing an FSM for complex
 * navigation and a combined AprilTag/IMU system for reliable localization.
 *
 * FIX: The runOpMode structure has been updated with a try-finally block
 * to safely close the VisionPortal and prevent NullPointerExceptions during OpMode termination.
 */
@Autonomous(name="Preview Event Auto", group="Match", preselectTeleOp="TeleOpPreviewEvent")
//@Disabled
public class AutoPreviewEvent extends LinearOpMode {

    // --- VISION CONSTANTS ---
    private static final boolean USE_WEBCAM = true;
    private static final int[] OBELISK_TAG_IDS = {21, 22, 23};
    // AprilTag IDs for the Red and Blue Alliance goals
    private static final int RED_GOAL_TAG_ID = 24;
    private static final int BLUE_GOAL_TAG_ID = 20;
    private static final double LAUNCH_DISTANCE_INCHES = 30.0;

    // --- WAYPOINT CONSTANTS (Field Coordinates in Inches) ---
    // Note: These are example field coordinates.
    private final Map<Position, Waypoint> redStartWaypoints = new HashMap<Position, Waypoint>() {{
        put(Position.POS1, new Waypoint(12, 12, 0));
        put(Position.POS2, new Waypoint(36, 36, 45));
        put(Position.POS3, new Waypoint(30, 30, 35));
    }};
    private final Map<Position, Waypoint> redGoalWaypoints = new HashMap<Position, Waypoint>() {{
        put(Position.POS1, new Waypoint(36, 36, 45));
        put(Position.POS2, new Waypoint(36, 36, 45));
        put(Position.POS3, new Waypoint(36, 36, 35));
    }};

    private final Map<Position, Waypoint> blueStartWaypoints = new HashMap<Position, Waypoint>() {{
        put(Position.POS1, new Waypoint(-12, 12, 0));
        put(Position.POS2, new Waypoint(-36, 36, 45));
        put(Position.POS3, new Waypoint(-30, 60, 35));
    }};
    private final Map<Position, Waypoint> blueGoalWaypoints = new HashMap<Position, Waypoint>() {{
        put(Position.POS1, new Waypoint(-36, 36, 45));
        put(Position.POS2, new Waypoint(-36, 36, 45));
        put(Position.POS3, new Waypoint(-36, 36, 45));
    }};
    private static final Waypoint LEAVE_WAYPOINT = new Waypoint(0, 0, 0); // Placeholder values

    // --- CAMERA OFFSETS (MUST BE TUNED) ---
    private static final double X_CAMERA_OFFSET_INCHES = 0.0; // Horizontal offset from robot center
    private static final double Y_CAMERA_OFFSET_INCHES = 0.0; // Forward/Backward offset from robot center

    // --- CONTROL CONSTANTS ---
    static final double POSITION_KP = 0.1;
    static final double HEADING_KP = 0.05;
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
        COMPLETE // Must be the final state
    }

    // --- FLEXIBLE FSM VARIABLES ---
    private List<RobotState> autoSequence;
    private int currentStepIndex = 0;
    private boolean debugMode = false;
    private boolean matchModeActive = false;

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

        // --- Vision Initialization ---
        initAprilTag();

        telemetry.addData("Status", "Ready for Selections");
        telemetry.update();

        // --- Driver Hub Pre-Match Selection Loop (Combined) ---
        boolean allianceSelected = false;
        boolean positionSelected = false;

        while (!isStarted() && !isStopRequested()) {
            // 1. Alliance Selection (X/B)
            if (!allianceSelected) {
                telemetry.addData("Status", "SELECT Alliance color from gamepad:)");
                telemetry.addData("Select", "for Red: X □, for Blue: B ○)");
                if (gamepad1.x) { alliance = Alliance.RED; allianceSelected = true; }
                if (gamepad1.b) { alliance = Alliance.BLUE; allianceSelected = true; }
            }

            // 2. Position Selection (D-pad)
            else if (!positionSelected) {
                telemetry.addData("Current Alliance", alliance.toString());
                telemetry.addData("Position", "SELECT START position with D-Pad:");
                telemetry.addData("Position", "Left =  1,  Up = 2, Right = 3");
                if (gamepad1.dpad_left) { position = Position.POS1; positionSelected = true; }
                if (gamepad1.dpad_up) { position = Position.POS2; positionSelected = true; }
                if (gamepad1.dpad_right) { position = Position.POS3; positionSelected = true; }
            }

            // 3. Mode Selection (Safety Interlock)
            else {
                telemetry.addData("Alliance/Position", alliance.toString() + " | " + position.toString());
                telemetry.addData("Mode", "Partial Auto: Y △");
                telemetry.addData("Mode", "Full Auto: RB - right bumper");
                // Safety Interlock: Press RB to force Match Mode (full sequence)
                if (gamepad1.right_bumper) {
                    matchModeActive = true;
                    debugMode = false; // Match Mode overrides Debug Mode
                }

                // Debug Toggle: Press Y to toggle debug mode (only if Match Mode is NOT active)
                if (gamepad1.y && !matchModeActive) {
                    // Simple debounce for the toggle
                    long buttonPressTime = System.currentTimeMillis();
                    while (gamepad1.y && opModeInInit()) {
                        sleep(10);
                    }
                    if (System.currentTimeMillis() - buttonPressTime > 50) {
                        debugMode = !debugMode;
                    }
                }

                // Display Current Configuration
                if (matchModeActive) {
                    telemetry.addData(">> MODE <<", ">> MATCH MODE ACTIVE (FULL) <<");
                } else {
                    telemetry.addData(">> MODE <<", "DEBUG Mode Toggled (Y): " + (debugMode ? "SKIP LAUNCH" : "FULL SEQUENCE"));
                    telemetry.addData("WARNING", "Press RB to LOCK Match Mode");
                }
            }

            telemetry.update();
            sleep(50);
        }

        // --- Apply Final Mode Selection and Set Sequence ---
        if (matchModeActive || !debugMode) {
            autoSequence = Arrays.asList(
                    RobotState.SCAN_OBELISK,
                    RobotState.POSITION_FOR_GOAL,
                    RobotState.SCAN_GOAL,
                    RobotState.DRIVE_TO_GOAL,
                    RobotState.LAUNCH,
                    RobotState.WAIT_FOR_LAUNCH,
                    RobotState.LEAVE,
                    RobotState.COMPLETE
            );
        } else {
            autoSequence = Arrays.asList(
                    RobotState.LAUNCH,
                    RobotState.WAIT_FOR_LAUNCH,
                    RobotState.LEAVE,
                    RobotState.COMPLETE
            );
        }

        // Display final selections for user confirmation
        while (!isStarted() && !isStopRequested()) {
            telemetry.addData("Status", "Selections Confirmed. Awaiting Start.");
            telemetry.addData("Alliance", alliance.toString());
            telemetry.addData("Position", position.toString());
            telemetry.addData("Sequence Length", autoSequence.size());
            telemetry.addData("Start State", autoSequence.get(0).toString());
            telemetry.addData("Final Mode", matchModeActive ? "MATCH LOCK" : (debugMode ? "DEBUG (Short)" : "FULL (Default)"));
            telemetry.update();
        }

        // Set the correct GOAL tag based on the selected alliance
        goalTagId = (alliance == Alliance.RED) ? RED_GOAL_TAG_ID : BLUE_GOAL_TAG_ID;

        // --- CRITICAL FIX: Use try-finally for robust cleanup ---
        try {
            waitForStart();

            // Stop streaming only when starting autonomous (optional, can be moved to finally block)
            if (visionPortal != null) {
                visionPortal.stopStreaming();
            }

            // --- Autonomous Execution ---
            if (opModeIsActive()) {
                runAutonomousRoutine();
            }

        } catch (Exception e) {
            // Catch any unexpected runtime errors during the autonomous period
            telemetry.addData("CRITICAL Autonomous OpMode ERROR", e.getMessage());
            telemetry.update();
            sleep(3000); // Display error before final cleanup
        } finally {
            // Final cleanup is GUARANTEED to run, even if exceptions occur.
            stopRobot();

            // Use close() instead of stopStreaming() for definitive cleanup.
            // Null check prevents the initial NullPointerException if initialization failed.
            if (visionPortal != null) {
                visionPortal.close();
            }
        }
    }

    /**
     * Estimates the robot's current pose (X, Y, Heading) relative to the field origin.
     * Uses the Alliance Goal Tag for position and IMU for heading fallback.
     * @return A Waypoint object representing the robot's current pose.
     */
    private Waypoint getRobotPose() {
        // Fallback to IMU for heading, set initial position to (0,0) if no tag is found
        double currentX = 0.0;
        double currentY = 0.0;
        double currentHeading = getImuHeading(); // Always use IMU for raw heading

        // Look for the Alliance Goal Tag (the most important for localization)
        AprilTagDetection detection = getFirstDetectedTag(goalTagId);

        if (detection != null) {
            // Use the existing function to correct the pose based on the detection
            Waypoint correctedPose = getRobotPoseFromAprilTagDetection(detection);
            currentX = correctedPose.x;
            currentY = correctedPose.y;
            // Heading remains IMU based for stability, but we use the tag's reliable X/Y position

            telemetry.addData("Pose Source", "AprilTag %d (X/Y)", detection.id);
        } else {
            telemetry.addData("Pose Source", "IMU (X/Y Unknown)");
        }

        return new Waypoint(currentX, currentY, currentHeading);
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
     * Main Autonomous Routine with FSM (Uses the flexible state list)
     */
    private void runAutonomousRoutine() {
        while (opModeIsActive() && currentStepIndex < autoSequence.size()) {

            RobotState currentState = autoSequence.get(currentStepIndex);

            telemetry.addData("FSM Step", String.format(Locale.US, "%d/%d", currentStepIndex + 1, autoSequence.size()));
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
                    } else {
                        telemetry.addData("Warning", "Obelisk Not Found. Proceeding...");
                    }
                    currentStepIndex++; // Move to next state (regardless of success, to avoid getting stuck)
                    break;

                case POSITION_FOR_GOAL:
                    telemetry.addData("Status", "Positioning for Goal");
                    Waypoint goalWaypoint = (alliance == Alliance.RED) ? redGoalWaypoints.get(position) : blueGoalWaypoints.get(position);
                    driveToWaypoint(goalWaypoint);
                    currentStepIndex++;
                    break;

                case SCAN_GOAL:
                    telemetry.addData("Status", "Scanning for Goal");
                    AprilTagDetection goalDetection = getFirstDetectedTag(goalTagId);
                    if (goalDetection != null) {
                        telemetry.addData("Goal Found", "ID: %d", goalDetection.id);
                        telemetry.update();
                        // If goal is found, we can proceed to the next step
                        currentStepIndex++;
                    } else {
                        telemetry.addData("Warning", "Goal Tag Not Found. Retrying...");
                        sleep(500); // Wait a bit before retrying scan
                    }
                    break;

                case DRIVE_TO_GOAL:
                    telemetry.addData("Status", "Driving to Launch Position");
                    AprilTagDetection latestGoalDetection = getFirstDetectedTag(goalTagId);
                    if (latestGoalDetection != null) {
                        driveToAprilTag(latestGoalDetection, LAUNCH_DISTANCE_INCHES);
                    } else {
                        // If tag is lost, try driving to the last known fixed goal waypoint instead
                        telemetry.addData("Warning", "Goal Tag Lost. Driving to fixed position.");
                        Waypoint fixedGoalWaypoint = (alliance == Alliance.RED) ? redGoalWaypoints.get(position) : blueGoalWaypoints.get(position);
                        driveToWaypoint(fixedGoalWaypoint);
                    }
                    currentStepIndex++;
                    break;

                case LAUNCH:
                    telemetry.addData("Status", "Launching Artifacts");
                    for (int i = 0; i < ARTIFACTS_TO_LAUNCH; i++) {
                        launch();
                        telemetry.addData("Launch", "%d of %d", i + 1, ARTIFACTS_TO_LAUNCH);
                        telemetry.update();
                        sleep(LAUNCH_DELAY_MS);
                    }
                    currentStepIndex++;
                    break;

                case WAIT_FOR_LAUNCH:
                    telemetry.addData("Status", "Waiting for Launch Sequence to Complete");
                    sleep(1000);
                    currentStepIndex++;
                    break;

                case LEAVE:
                    telemetry.addData("Status", "Leaving Launch Zone");
                    driveToWaypoint(LEAVE_WAYPOINT);
                    currentStepIndex++;
                    break;

                case COMPLETE:
                    telemetry.addData("Status", "Autonomous Routine Complete");
                    telemetry.update();
                    currentStepIndex++;
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
     * @return A new Waypoint object with the corrected robot position and heading.
     */
    private Waypoint getRobotPoseFromAprilTagDetection(AprilTagDetection detection) {
        // These values are the robot's pose relative to the field origin, as estimated by the SDK.
        double tagX = detection.ftcPose.x;
        double tagY = detection.ftcPose.y;
        double tagHeading = detection.ftcPose.yaw; // SDK provides yaw in degrees

        double headingRad = Math.toRadians(tagHeading);

        // Apply camera offset correction
        double cameraXInRobotFrame = Y_CAMERA_OFFSET_INCHES * Math.cos(headingRad) - X_CAMERA_OFFSET_INCHES * Math.sin(headingRad);
        double cameraYInRobotFrame = Y_CAMERA_OFFSET_INCHES * Math.sin(headingRad) + X_CAMERA_OFFSET_INCHES * Math.cos(headingRad);

        double robotX = tagX - cameraXInRobotFrame;
        double robotY = tagY - cameraYInRobotFrame;
        double robotHeading = tagHeading;

        return new Waypoint(robotX, robotY, robotHeading);
    }

    /**
     * Drives the robot to a waypoint relative to a detected AprilTag.
     * The target waypoint is created by calculating a fixed field coordinate in front of the tag.
     * @param targetTag The AprilTag to navigate towards.
     * @param travelDelta The distance in inches to stop before the tag's location (along the line of sight).
     */
    private void driveToAprilTag(AprilTagDetection targetTag, double travelDelta) {
        telemetry.addData("Driving to Tag", targetTag.id);
        telemetry.update();

        double startTime = getRuntime();
        while (opModeIsActive() && getRuntime() - startTime < TIMEOUT_S) {
            AprilTagDetection latestDetection = getFirstDetectedTag(targetTag.id);

            if (latestDetection != null) {
                // Calculate the target position relative to the tag's pose
                // We want to drive *towards* the tag, stopping at travelDelta range
                double remainingDistance = latestDetection.ftcPose.range - travelDelta;
                double bearingRad = Math.toRadians(latestDetection.ftcPose.bearing);

                Waypoint currentPose = getRobotPose();

                double targetX = currentPose.x + remainingDistance * Math.cos(bearingRad + Math.toRadians(currentPose.heading));
                double targetY = currentPose.y + remainingDistance * Math.sin(bearingRad + Math.toRadians(currentPose.heading));
                double targetHeading = latestDetection.ftcPose.yaw;

                Waypoint targetWaypoint = new Waypoint(targetX, targetY, targetHeading);
                driveToWaypoint(targetWaypoint);

                // Check for completion using the detection range (simpler)
                if (latestDetection.ftcPose.range <= travelDelta + POSITION_TOLERANCE_IN && Math.abs(getHeadingError(targetHeading)) < HEADING_TOLERANCE_DEG) {
                    break;
                }
            } else {
                telemetry.addData("Warning", "Lost sight of target tag %d", targetTag.id);
                telemetry.update();
                // We break here if the tag is lost, moving to the next state,
                // as tag-relative driving is impossible without the tag.
                break;
            }
        }
        stopRobot();
    }

    /**
     * Drives the robot to a specific waypoint using a proportional control loop.
     * Uses the continuous pose estimation from {@link #getRobotPose()}.
     * @param target The target waypoint (x, y, heading) to navigate to.
     */
    private void driveToWaypoint(Waypoint target) {
        double startTime = getRuntime();

        while (opModeIsActive() && getRuntime() - startTime < TIMEOUT_S) {
            // 1. Get current pose
            Waypoint currentPose = getRobotPose();
            double currentX = currentPose.x;
            double currentY = currentPose.y;
            double currentHeading = currentPose.heading;

            // 2. Calculate errors
            double xError = target.x - currentX;
            double yError = target.y - currentY;
            double headingError = getHeadingError(target.heading);

            // 3. Check for completion
            if (Math.abs(xError) < POSITION_TOLERANCE_IN && Math.abs(yError) < POSITION_TOLERANCE_IN && Math.abs(headingError) < HEADING_TOLERANCE_DEG) {
                break; // Target reached
            }

            // 4. Proportional Drive Logic
            while (headingError > 180) headingError -= 360;
            while (headingError < -180) headingError += 360;

            double axialPower;
            double lateralPower;
            double yawPower;

            // Strategy: Turn in place if heading error is too large, otherwise drive and correct heading simultaneously.
            if (Math.abs(headingError) > HEADING_TURN_TOLERANCE_DEG) {
                // Large error, only turn in place
                axialPower = 0;
                lateralPower = 0;
            } else {
                // Calculate power based on position error
                axialPower = yError * POSITION_KP;
                lateralPower = xError * POSITION_KP;
            }
            // Add proportional yaw correction
            yawPower = -headingError * HEADING_KP;


            // 5. Normalize powers to prevent over-powering
            double max = Math.max(Math.abs(axialPower + lateralPower + yawPower),
                    Math.abs(axialPower - lateralPower - yawPower));
            max = Math.max(max, Math.abs(axialPower - lateralPower + yawPower));
            max = Math.max(max, Math.abs(axialPower + lateralPower - yawPower));

            if (max > 1.0) {
                axialPower /= max;
                lateralPower /= max;
                yawPower /= max;
            }

            // 6. Apply motor power (Mecanum Field-Centric)
            motorLeftFront.setPower(axialPower + lateralPower + yawPower);
            motorRightFront.setPower(axialPower - lateralPower - yawPower);
            motorLeftBack.setPower(axialPower - lateralPower + yawPower);
            motorRightBack.setPower(axialPower + lateralPower - yawPower);

            // 7. Telemetry Update
            telemetry.addData("Waypoint Status", "Driving");
            telemetry.addData("Target Waypoint", "X=%.1f, Y=%.1f, H=%.1f", target.x, target.y, target.heading);
            telemetry.addData("Current Pose", "X=%.1f, Y=%.1f, H=%.1f", currentX, currentY, currentHeading);
            telemetry.addData("Error", "X=%.1f, Y=%.1f, H=%.1f", xError, yError, headingError);
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
        // Placeholder for launching logic (e.g., set launcher motor to velocity, flip trigger servo)
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
