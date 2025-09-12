package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.configuration.Settings;
import org.firstinspires.ftc.teamcode.hardware.MechanismManager;
import org.firstinspires.ftc.teamcode.hardware.Turret;
import org.firstinspires.ftc.teamcode.hardware.submechanisms.LimelightManager;
import org.firstinspires.ftc.teamcode.software.AlignmentEngine;
import org.firstinspires.ftc.teamcode.software.Drivetrain;
import org.firstinspires.ftc.teamcode.software.TrajectoryEngine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main TeleOp class for driver-controlled period.
 * Handles controller profile selection and robot operation during matches.
 *
 * @noinspection ClassWithoutConstructor
 */
@TeleOp(name = "MainOp", group = ".Competition Modes")
public class MainOp extends LinearOpMode {

    private MechanismManager mechanisms;
    private LimelightManager limelightManager;
    private Controller mainController;
    private Controller subController;
    private Drivetrain drivetrain;
    private GoBildaPinpointDriver manualPinpoint;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private AlignmentEngine alignmentEngine;
    private TrajectoryEngine trajectoryEngine;
    public MatchSettings matchSettings;
    public Turret turret;

    /**
     * Main execution flow:
     * 1. Displays controller profile selection menu
     * 2. Initializes robot with selected profiles
     * 3. Runs main control loop for driver operation
     * 4. Handles shutdown when OpMode ends
     */
    @Override
    public final void runOpMode() {
        // Pull stored settings from auto
        matchSettings = new MatchSettings(blackboard);

        // Initialize robot systems
        mechanisms = new MechanismManager(hardwareMap);
        manualPinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        mainController = new Controller(gamepad1, manualPinpoint, matchSettings);
        subController = new Controller(gamepad2, manualPinpoint, matchSettings);
        drivetrain = new Drivetrain(hardwareMap);
        limelightManager = new LimelightManager(hardwareMap.get(Limelight3A.class, "limelight"));
        alignmentEngine = new AlignmentEngine(mainController, matchSettings, drivetrain, mechanisms, limelightManager, manualPinpoint);
        trajectoryEngine = new TrajectoryEngine(limelightManager, manualPinpoint, matchSettings);
        turret = new Turret();
        // Wait for start
        waitForStart();

        // Initialize mechanisms
        mechanisms.init();

        // Main loop
        while (opModeIsActive()) {
            manualPinpoint.update();

            processControllerInputs();

            runAutomations();

            updateTelemetry();

            mainController.saveLastState();
            subController.saveLastState();
            if (matchSettings.nextArtifactNeeded() == MatchSettings.ArtifactColor.GREEN) {
                subController.setLedColor(0, 255, 0, 100);
            } else if (matchSettings.nextArtifactNeeded() == MatchSettings.ArtifactColor.PURPLE) {
                subController.setLedColor(255, 0, 255, 100);
            } else {
                subController.setLedColor(0, 0, 0, 0);
            }
        }

        // Stop all executor tasks at the end
        scheduler.shutdownNow();
    }

    /**
     * Process controller inputs
     */
    private void processControllerInputs() {

        // MAIN CONTROLLER INPUTS
        // Get drivetrain controls
        double drive = mainController.getProcessedDrive();
        double strafe = mainController.getProcessedStrafe();
        double rotate = mainController.getProcessedRotation();

        // Apply the values to drivetrain
        drivetrain.mecanumDrive(drive, strafe, rotate);

        // Go to predetermined positions, overriding the mecanum drive if a button is pressed
        Controller.Action[] gotoActions = {
                Controller.Action.GOTO_CLOSE_SHOOT, Controller.Action.GOTO_FAR_SHOOT,
                Controller.Action.GOTO_HUMAN_PLAYER, Controller.Action.GOTO_SECRET_TUNNEL
        };
        for (Controller.Action action : gotoActions) {
            if (mainController.wasJustPressed(action)) {
                drivetrain.goTo(Drivetrain.Position.valueOf(action.name().substring("GOTO_".length())));
                break; // only one goto action can be triggered at a time
            }
        }

        //if (mainController.wasJustPressed(Controller.Action.PARK_EXTEND)) {
        // we arent implementing this until like state bro
        //}

        // Intake controls using Controller's isPressed
        if (subController.wasJustPressed(Controller.Action.AIM)) {
            alignmentEngine.run();
        }

        if (subController.wasJustPressed(Controller.Action.LAUNCH)) {
            turret.launch();
        }

        if (subController.getProcessedValue(Controller.Action.INTAKE) > 0) {
            mechanisms.intake.in();
        } else {
            mechanisms.intake.stop();
        }

        if (subController.getProcessedValue(Controller.Action.RELEASE_EXTRAS) > 0) {
            mechanisms.intake.releaseExtras();
        }
        if (subController.getProcessedValue(Controller.Action.RELEASE_PURPLE) > 0) {
            mechanisms.intake.releasePurple();
        }
        if (subController.getProcessedValue(Controller.Action.RELEASE_GREEN) > 0) {
            mechanisms.intake.releaseGreen();
        }

        if (subController.getProcessedValue(Controller.Action.EMPTY_CLASSIFIER_STATE) > 0) {
            matchSettings.emptyClassifier();
        }
        if (subController.getProcessedValue(Controller.Action.INCREMENT_CLASSIFIER_STATE) > 0) {
            matchSettings.incrementClassifier();
        }
    }

    /**
     * Check automation conditions
     */
    private void runAutomations() {
        // Skip if automation is disabled
        if (!Settings.Teleop.automationEnabled) {
        }

        // TODO update for new automations

        // boolean shoulderInBackPosition = mechanisms.outtake.shoulder.position() == Shoulder.Position.PLACE_BACKWARD;

//        if (horizontalCollapsed && verticalCollapsed && intakeHasPixel &&
//                outtakeClawClosed && shoulderInBackPosition) {
//            mechanisms.intake.intakeClaw.open();
//            scheduleTask(() -> mechanisms.outtake.moveShoulderToBack(), 200);
//        }
    }

    /**
     * Update telemetry with current status
     */
    private void updateTelemetry() {
        // Add controller profile information
        telemetry.addLine("Ready to go!");
        telemetry.update();
    }

    /**
     * Schedule a task to run after a delay
     */
    private void scheduleTask(Runnable task, long delayMillis) {
        scheduler.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
    }
}