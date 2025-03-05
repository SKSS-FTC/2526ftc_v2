package org.firstinspires.ftc.teamcode;

import com.pedropathing.localization.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.input.ControllerProfile;
import org.firstinspires.ftc.teamcode.input.ControllerProfileManager;
import org.firstinspires.ftc.teamcode.input.MainController;
import org.firstinspires.ftc.teamcode.input.SubController;
import org.firstinspires.ftc.teamcode.mechanisms.MechanismManager;
import org.firstinspires.ftc.teamcode.mechanisms.submechanisms.LimelightManager;
import org.firstinspires.ftc.teamcode.mechanisms.submechanisms.Shoulder;
import org.firstinspires.ftc.teamcode.mechanisms.submechanisms.ViperSlide;
import org.firstinspires.ftc.teamcode.mechanisms.submechanisms.Wrist;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main TeleOp class for driver-controlled period.
 * Handles controller profile selection and robot operation during matches.
 *
 * @noinspection HardcodedLineSeparator, CodeBlock2Expr
 */
@TeleOp(name = "MainOp", group = ".Competition Modes")
public class MainOp extends LinearOpMode {

    private MechanismManager mechanisms;
    private MainController mainController;
    private SubController subController;
    private Drivetrain drivetrain;
    private GoBildaPinpointDriver manualPinpoint;
    private boolean chassisDisabled = false;
    private double flip = 1.0;

    private boolean prevGamepadTriangle;
    private LimelightManager.LimelightPipeline pipeline = LimelightManager.LimelightPipeline.BLUE;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private double storedTx;

    // Store previous states for edge detection
    private boolean prevTouchpad = false;
    private boolean prevRightStickButton = false;
    private boolean prevLeftStickButton = false;
    private final boolean prevGuideButton = false;

    // Controller profile management
    private ControllerProfileManager profileManager;
    private boolean prevShareButton = false;
    private boolean prevOptionsButton = false;

    // Control settings
    private final boolean incrementalVertical = false;
    private final boolean incrementalHorizontal = false;

    /**
     * Main execution flow:
     * 1. Displays controller profile selection menu
     * 2. Initializes robot with selected profiles
     * 3. Runs main control loop for driver operation
     * 4. Handles shutdown when OpMode ends
     */
    @Override
    public void runOpMode() {
        // Initialize controller selection
        setupControllerSelection();

        // Initialize controller profile manager
        profileManager = new ControllerProfileManager();

        // Initialize robot systems
        mechanisms = new MechanismManager(hardwareMap);
        mainController = new MainController(gamepad1);
        subController = new SubController(gamepad2);
        drivetrain = new Drivetrain(hardwareMap);

        // Wait for start
        waitForStart();

        // Initialize mechanisms
        mechanisms.init();
        manualPinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        mechanisms.intake.limelight.setCurrentPipeline(pipeline);

        // Apply initial controller profiles
        applyControllerProfiles();

        // Main loop
        while (opModeIsActive()) {
            // Update controller states
            mainController.update(gamepad1);
            subController.update(gamepad2);

            // Update pinpoint localization
            manualPinpoint.update();

            // Check for profile switching
            checkProfileSwitching();

            // Process controller inputs
            processControllerInputs();

            // Check for automation conditions
            checkAutomationConditions();

            // Check for assistance conditions
            checkAssistanceConditions();

            // Update telemetry
            updateTelemetry();
        }

        // Stop all executor tasks
        scheduler.shutdownNow();
    }

    /**
     * Setup the initial controller selection and limelight pipeline
     */
    private void setupControllerSelection() {
        telemetry.addLine("=== Controller Setup ===");
        telemetry.addLine("Press Y/triangle to toggle limelight pipeline color");
        telemetry.addLine("Press A/cross to continue");

        boolean setupComplete = false;

        while (!isStarted() && !isStopRequested() && !setupComplete) {
            // Handle pipeline selection
            if (gamepad1.triangle || gamepad1.y) {
                if (!prevGamepadTriangle) {
                    pipeline = pipeline == LimelightManager.LimelightPipeline.BLUE
                            ? LimelightManager.LimelightPipeline.RED
                            : LimelightManager.LimelightPipeline.BLUE;
                    gamepad1.rumble(50);
                    gamepad2.rumble(50);
                }
                prevGamepadTriangle = true;
            } else {
                prevGamepadTriangle = false;
            }

            // Set game controller colors based on pipeline
            if (pipeline == LimelightManager.LimelightPipeline.BLUE) {
                gamepad1.setLedColor(0, 0, 255, 1000);
                gamepad2.setLedColor(0, 0, 255, 1000);
                telemetry.addLine("Current Pipeline: BLUE");
            } else {
                gamepad1.setLedColor(255, 0, 0, 1000);
                gamepad2.setLedColor(255, 0, 0, 1000);
                telemetry.addLine("Current Pipeline: RED");
            }

            // Check for completion
            if (gamepad1.cross || gamepad1.a) {
                setupComplete = true;
                gamepad1.rumble(200);
                gamepad2.rumble(200);
            }

            telemetry.update();
            sleep(50); // Small delay to prevent CPU hogging
        }
    }

    /**
     * Apply the currently active controller profiles
     */
    private void applyControllerProfiles() {
        mainController.setMapping(profileManager.getActiveMainProfile().getMapping());
        subController.setMapping(profileManager.getActiveSubProfile().getMapping());
    }

    /**
     * Check for controller profile switching
     */
    private void checkProfileSwitching() {
        // Main controller profile switching - using "share" button
        boolean sharePressed = gamepad1.back;
        if (sharePressed && !prevShareButton) {
            // Cycle to the next main profile
            ControllerProfile profile = profileManager.cycleMainProfile();
            mainController.setMapping(profile.getMapping());

            // Give feedback
            gamepad1.rumble(200);
            telemetry.addData("Main Controller Profile", profile.getName());
        }
        prevShareButton = sharePressed;

        // Sub controller profile switching - using "options" button
        boolean optionsPressed = gamepad2.start;
        if (optionsPressed && !prevOptionsButton) {
            // Cycle to the next sub profile
            ControllerProfile profile = profileManager.cycleSubProfile();
            subController.setMapping(profile.getMapping());

            // Give feedback
            gamepad2.rumble(200);
            telemetry.addData("Sub Controller Profile", profile.getName());
        }
        prevOptionsButton = optionsPressed;
    }

    /**
     * Process all inputs from both controllers
     */
    private void processControllerInputs() {
        // === MAIN CONTROLLER INPUTS ===

        // Chassis movement
        if (!chassisDisabled) {
            double forward = mainController.getValue("moveForward");
            double strafe = mainController.getValue("moveSideways");
            double rotate = mainController.getValue("rotate");

            drivetrain.mecanumDrive(forward, strafe, rotate, flip);
        }

        // Flip movement
        if (mainController.isPressed("guide")) {
            flip *= -1;
            gamepad1.rumble(100);
        }

        // Vertical slide controls - incremental mode
        if (incrementalVertical) {
            if (mainController.isActive("extendVertical")) {
                mechanisms.outtake.verticalSlide.increment();
            }

            if (mainController.isActive("retractVertical")) {
                mechanisms.outtake.verticalSlide.decrement();
            }
        } else { // Direct mode
            if (mainController.isPressed("extendVertical")) {
                mechanisms.outtake.verticalSlide.extend();
            }

            if (mainController.isPressed("retractVertical")) {
                mechanisms.outtake.verticalSlide.retract();
            }
        }

        // Vertical slide position presets
        if (mainController.isPressed("triangle")) {
            mechanisms.outtake.verticalSlide.setPosition(ViperSlide.VerticalPosition.HIGH_BASKET);
        }

        if (mainController.isPressed("cross")) {
            mechanisms.outtake.verticalSlide.setPosition(ViperSlide.VerticalPosition.HIGH_RUNG);
        }

        if (mainController.isPressed("square")) {
            mechanisms.outtake.verticalSlide.setPosition(ViperSlide.VerticalPosition.PREP_HIGH_RUNG);
        }

        if (mainController.isPressed("circle")) {
            mechanisms.outtake.verticalSlide.setPosition(ViperSlide.VerticalPosition.TRANSFER);
        }

        // Driver assistance toggle
        boolean touchpad = gamepad1.touchpad;
        if (touchpad != prevTouchpad) {
            chassisDisabled = touchpad;
            prevTouchpad = touchpad;
        }

        // === SUB CONTROLLER INPUTS ===

        // Intake claw controls
        if (subController.isPressed("leftTrigger")) {
            mechanisms.intake.intakeClaw.close();
            scheduleTask(() -> mechanisms.intake.wrist.setPosition(Wrist.Position.VERTICAL), 200);
            mechanisms.outtake.outtakeClaw.open();
        }

        if (subController.isActive("rightTrigger")) {
            mechanisms.intake.intakeClaw.open();
        }

        // Wrist controls
        if (subController.isPressed("moveLeft")) {
            if (mechanisms.intake.horizontalSlide.currentPosition.getValue() > 30 &&
                    mechanisms.intake.intakeClaw.opened) {
                mechanisms.intake.wrist.setPosition(Wrist.Position.READY);
            } else {
                mechanisms.intake.wrist.setPosition(Wrist.Position.VERTICAL);
            }
        }

        if (subController.isActive("moveRight")) {
            mechanisms.intake.wrist.setPosition(Wrist.Position.HORIZONTAL);
        }

        // Horizontal slide controls
        if (incrementalHorizontal) {
            if (subController.isActive("extendHorizontal")) {
                mechanisms.intake.horizontalSlide.increment();
            }

            if (subController.isActive("retractHorizontal")) {
                mechanisms.intake.horizontalSlide.decrement();
            }
        } else {
            if (subController.isPressed("extendHorizontal")) {
                mechanisms.intake.horizontalSlide.extend();
            }

            if (subController.isPressed("retractHorizontal")) {
                mechanisms.intake.horizontalSlide.retract();
                if (mechanisms.intake.horizontalSlide.currentPosition == ViperSlide.HorizontalPosition.COLLAPSED) {
                    mechanisms.outtake.outtakeClaw.open();
                    mechanisms.intake.wrist.setPosition(Wrist.Position.VERTICAL);
                }
            }
        }

        // Rotator control
        double rotatorValue = subController.getValue("rotator");
        if (Math.abs(rotatorValue) > 0.05) {
            double normalizedValue = (rotatorValue + 1) / 2; // Convert from -1..1 to 0..1
            mechanisms.intake.rotator.setPosition(normalizedValue);
        }

        // Outtake claw control
        boolean rightStickButton = subController.isPressed("rightStickButton");
        if (rightStickButton && !prevRightStickButton) {
            if (mechanisms.outtake.outtakeClaw.opened) {
                mechanisms.outtake.outtakeClaw.close();
                scheduleTask(() -> mechanisms.intake.intakeClaw.open(), 400);
            } else {
                mechanisms.outtake.outtakeClaw.open();
            }
            prevRightStickButton = true;
        } else if (!rightStickButton) {
            prevRightStickButton = false;
        }

        // Shoulder control
        boolean leftStickButton = subController.isPressed("leftStickButton");
        if (leftStickButton && !prevLeftStickButton) {
            mechanisms.outtake.shoulder.cyclePosition();
            prevLeftStickButton = true;
        } else if (!leftStickButton) {
            prevLeftStickButton = false;
        }

        // Linear actuator controls
        if (subController.isActive("triangle")) {
            mechanisms.linearActuator.extend();
        }

        if (subController.isActive("cross")) {
            mechanisms.linearActuator.retract();
        }
    }

    /**
     * Checks for conditions that trigger automated actions
     */
    private void checkAutomationConditions() {
        // Skip if automation is disabled
        if (!Settings.Teleop.automationEnabled) {
            return;
        }

        // Automatically transfer when everything is collapsed
        boolean horizontalCollapsed = mechanisms.intake.horizontalSlide.currentPosition
                .getValue() <= ViperSlide.HorizontalPosition.COLLAPSED.getValue() + 10;

        boolean verticalCollapsed = mechanisms.outtake.verticalSlide.verticalMotorRight
                .getCurrentPosition() <= ViperSlide.VerticalPosition.TRANSFER.getValue() + 10;

        boolean intakeHasPixel = !mechanisms.intake.intakeClaw.opened;
        boolean outtakeClawClosed = mechanisms.outtake.outtakeClaw.clawServo.getPosition() > 0.8;
        boolean shoulderInBackPosition = mechanisms.outtake.shoulder.position() == Shoulder.Position.PLACE_BACKWARD;

        if (horizontalCollapsed && verticalCollapsed && intakeHasPixel &&
                outtakeClawClosed && shoulderInBackPosition) {
            mechanisms.intake.intakeClaw.open();
            scheduleTask(() -> mechanisms.outtake.moveShoulderToBack(), 200);
        }
    }

    /**
     * Checks for conditions that trigger driver assistance
     */
    private void checkAssistanceConditions() {
        boolean specimenDetected = mechanisms.intake.limelight.specimenDetected();
        boolean headingAligned = Math.abs(manualPinpoint.getHeading()) < 0.3;

        if (specimenDetected && headingAligned) {
            if (gamepad1.touchpad) {
                gamepad1.setLedColor(0, 0, 255, 1000);
                drivetrain.interpolateToOffset(
                        mechanisms.intake.limelight.limelight.getLatestResult().getTx(),
                        Settings.Assistance.approachSpeed,
                        wrappedHeading());
                storedTx = mechanisms.intake.limelight.limelight.getLatestResult().getTx();
                chassisDisabled = true;
            } else {
                gamepad1.rumble(50);
                gamepad1.setLedColor(0, 255, 0, 1000);
                chassisDisabled = false;
            }
        } else if (chassisDisabled && gamepad1.touchpad && storedTx != 0) {
            gamepad1.setLedColor(255, 0, 255, 1000);
            drivetrain.interpolateToOffset(
                    mechanisms.intake.limelight.limelight.getLatestResult().getTx(),
                    0.35,
                    wrappedHeading());
        } else {
            storedTx = 0;
            if (gamepad1.touchpad) {
                gamepad1.setLedColor(0, 255, 255, 1000);
                drivetrain.interpolateToOffset(0, 0, wrappedHeading());
                chassisDisabled = true;
            } else {
                gamepad1.setLedColor(255, 0, 0, 1000);
                chassisDisabled = false;
            }
        }
    }

    /**
     * Updates telemetry with current system state
     */
    private void updateTelemetry() {
        // Add controller profile information with instructions
        telemetry.addLine("=== Controller Profiles ===");
        telemetry.addData("Main Profile", profileManager.getActiveMainProfile().getName() + " (Press BACK to change)");
        telemetry.addData("Sub Profile", profileManager.getActiveSubProfile().getName() + " (Press START to change)");

        // Add available profiles
        telemetry.addLine("Available Main Profiles:");
        List<ControllerProfile> mainProfiles = profileManager.getMainProfiles();
        for (int i = 0; i < mainProfiles.size(); i++) {
            telemetry.addData(" " + (i + 1), mainProfiles.get(i).getName());
        }

        telemetry.addLine("Available Sub Profiles:");
        List<ControllerProfile> subProfiles = profileManager.getSubProfiles();
        for (int i = 0; i < subProfiles.size(); i++) {
            telemetry.addData(" " + (i + 1), subProfiles.get(i).getName());
        }

        telemetry.addLine("=== System Status ===");
        // Original telemetry
        telemetry.addData("limelight tx", mechanisms.intake.limelight.limelight.getLatestResult().getTx());
        telemetry.addData("limelight ty", mechanisms.intake.limelight.limelight.getLatestResult().getTy());
        telemetry.addData("limelight detects specimen?", mechanisms.intake.limelight.specimenDetected());
        telemetry.addData("heading", wrappedHeading());
        telemetry.update();
    }

    /**
     * Normalizes heading to range -π to π
     */
    private double wrappedHeading() {
        return (manualPinpoint.getHeading() + Math.PI) % (2 * Math.PI) - Math.PI;
    }

    /**
     * Schedules a task to run after a delay
     */
    private void scheduleTask(Runnable task, long delayMillis) {
        scheduler.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
    }
}