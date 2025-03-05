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

    // Add back these important variables
    private LimelightManager.LimelightPipeline pipeline = LimelightManager.LimelightPipeline.BLUE;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private double storedTx;

    // Controller profile management
    private ControllerProfileManager profileManager;

    /**
     * Main execution flow:
     * 1. Displays controller profile selection menu
     * 2. Initializes robot with selected profiles
     * 3. Runs main control loop for driver operation
     * 4. Handles shutdown when OpMode ends
     */
    @Override
    public void runOpMode() {
        // Initialize controller profile manager first
        profileManager = new ControllerProfileManager();

        // Initialize robot systems
        mechanisms = new MechanismManager(hardwareMap);
        mainController = new MainController(gamepad1);
        subController = new SubController(gamepad2);
        drivetrain = new Drivetrain(hardwareMap);

        // Initialize controller selection and profiles before waiting for start
        // This will also handle pipeline selection
        configureMatchSettings();

        // Wait for start
        waitForStart();

        // Initialize mechanisms
        mechanisms.init();
        manualPinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        mechanisms.intake.limelight.setCurrentPipeline(pipeline);

        // Main loop
        while (opModeIsActive()) {
            // Update controller states
            mainController.update(mainController);
            subController.update(subController);

            // Update pinpoint localization
            manualPinpoint.update();

            // Process inputs
            processControllerInputs();

            // Check automation and assistance conditions
            checkAutomationConditions();
            checkAssistanceConditions();

            // Update telemetry
            updateTelemetry();
        }

        // Stop all executor tasks at the end
        scheduler.shutdownNow();
    }

    /**
     * Sets up controller selection and profile selection before the OpMode starts
     */
    private void configureMatchSettings() {
        telemetry.addLine("=== Match Setup ===");
        telemetry.addLine("Press triangle to toggle limelight pipeline color");
        telemetry.addLine("Press square to select profile");
        telemetry.addLine("Press A/cross to continue");

        boolean setupComplete = false;
        boolean pipelineButtonState = false;
        boolean mainProfileButtonState = false;
        boolean subProfileButtonState = false;

        while (!isStarted() && !isStopRequested() && !setupComplete) {
            // Handle pipeline selection
            if (mainController.triangle) {
                if (!pipelineButtonState) {
                    pipeline = pipeline == LimelightManager.LimelightPipeline.BLUE
                            ? LimelightManager.LimelightPipeline.RED
                            : LimelightManager.LimelightPipeline.BLUE;
                    mainController.rumble(50);
                    subController.rumble(50);
                    pipelineButtonState = true;
                }
            } else {
                pipelineButtonState = false;
            }

            // Handle main profile selection
            if (mainController.square) {
                if (!mainProfileButtonState) {
                    ControllerProfile profile = profileManager.cycleMainProfile();
                    mainController.setMapping(profile.getMapping());
                    mainController.rumble(50);
                    mainProfileButtonState = true;
                }
            } else {
                mainProfileButtonState = false;
            }

            // Handle sub profile selection
            if (subController.square) {
                if (!subProfileButtonState) {
                    ControllerProfile profile = profileManager.cycleSubProfile();
                    subController.setMapping(profile.getMapping());
                    subController.rumble(50);
                    subProfileButtonState = true;
                }
            } else {
                subProfileButtonState = false;
            }

            // Set game controller colors based on pipeline
            if (pipeline == LimelightManager.LimelightPipeline.BLUE) {
                mainController.setLedColor(0, 0, 255, 1000);
                subController.setLedColor(0, 0, 255, 1000);
                telemetry.addLine("Current Pipeline: BLUE");
            } else {
                mainController.setLedColor(255, 0, 0, 1000);
                subController.setLedColor(255, 0, 0, 1000);
                telemetry.addLine("Current Pipeline: RED");
            }

            // Display selected profiles
            telemetry.addData("Main Profile", profileManager.getActiveMainProfile().getName());
            telemetry.addData("Sub Profile", profileManager.getActiveSubProfile().getName());

            // Check for completion
            if (mainController.cross) {
                setupComplete = true;
                mainController.rumble(200);
                subController.rumble(200);
            }

            telemetry.update();
        }
    }

    /**
     * Process controller inputs
     */
    private void processControllerInputs() {
        // Get drivetrain controls
        double drive = -mainController.getValue("moveForward") * flip;
        double strafe = mainController.getValue("moveSideways") * flip;
        double rotate = mainController.getValue("rotate");

        // Apply the values to drivetrain
        if (!chassisDisabled) {
            drivetrain.mecanumDrive(drive, strafe, rotate);
        }

        // Flip controls - using Controller's edge detection
        if (mainController.isPressed("flip")) {
            flip *= -1;
            mainController.rumble(100);
        }

        // Vertical slide controls - incremental mode
        if (mainController.getSettings().getBooleanSetting("incrementalVertical")) {
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

        // Intake controls using Controller's isPressed
        if (subController.isPressed("closeClaw")) {
            mechanisms.intake.intakeClaw.close();
            scheduleTask(() -> mechanisms.intake.wrist.setPosition(Wrist.Position.VERTICAL), 200);
            mechanisms.outtake.outtakeClaw.open();
        }

        if (subController.isPressed("openClaw")) {
            mechanisms.intake.intakeClaw.open();
            if (mechanisms.intake.horizontalSlide.currentPosition.getValue() > 30 &&
                    mechanisms.intake.intakeClaw.opened) {
                mechanisms.intake.wrist.setPosition(Wrist.Position.READY);
            } else {
                mechanisms.intake.wrist.setPosition(Wrist.Position.VERTICAL);
            }
        }

        if (subController.isActive("wristHorizontal")) {
            mechanisms.intake.wrist.setPosition(Wrist.Position.HORIZONTAL);
        }

        // Horizontal slide controls
        if (mainController.getSettings().getBooleanSetting("incrementalHorizontal")) {
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

        if (subController.isActive("hangExtend")) {
            mechanisms.linearActuator.extend();
        }

        if (subController.isActive("hangRetract")) {
            mechanisms.linearActuator.retract();
        }
    }

    /**
     * Check automation conditions
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
     * Check for assistance conditions
     */
    private void checkAssistanceConditions() {
        boolean specimenDetected = mechanisms.intake.limelight.specimenDetected();
        boolean headingAligned = Math.abs(wrappedHeading()) < 10;

        if (specimenDetected && headingAligned) {
            if (mainController.isActive("deadeye")) {
                mainController.setLedColor(0, 0, 255, 1000);
                drivetrain.interpolateToOffset(
                        mechanisms.intake.limelight.limelight.getLatestResult().getTx(),
                        Settings.Assistance.approachSpeed,
                        wrappedHeading());
                storedTx = mechanisms.intake.limelight.limelight.getLatestResult().getTx();
                chassisDisabled = true;
            } else {
                mainController.rumble(50);
                mainController.setLedColor(0, 255, 0, 1000);
                chassisDisabled = false;
            }
        } else if (chassisDisabled && mainController.isActive("touchpad") && storedTx != 0) {
            mainController.setLedColor(255, 0, 255, 1000);
            drivetrain.interpolateToOffset(
                    mechanisms.intake.limelight.limelight.getLatestResult().getTx(),
                    0.35,
                    wrappedHeading());
        } else {
            storedTx = 0;
            if (mainController.isActive("touchpad")) {
                mainController.setLedColor(0, 255, 255, 1000);
                drivetrain.interpolateToOffset(0, 0, wrappedHeading());
                chassisDisabled = true;
            } else {
                mainController.setLedColor(255, 0, 0, 1000);
                chassisDisabled = false;
            }
        }
    }

    /**
     * Update telemetry with current status
     */
    private void updateTelemetry() {
        // Add controller profile information
        telemetry.addLine("=== Controller Profiles ===");
        telemetry.addData("Main Profile", profileManager.getActiveMainProfile().getName());
        telemetry.addData("Sub Profile", profileManager.getActiveSubProfile().getName());

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
     * Schedule a task to run after a delay
     */
    private void scheduleTask(Runnable task, long delayMillis) {
        scheduler.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
    }
}