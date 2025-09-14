package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.hardware.MechanismManager;
import org.firstinspires.ftc.teamcode.software.Drivetrain;

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
    private Controller mainController;
    private Controller subController;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public MatchSettings matchSettings;

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
        mechanisms = new MechanismManager(hardwareMap, matchSettings);
        mainController = new Controller(gamepad1, mechanisms.pinpoint, matchSettings);
        subController = new Controller(gamepad2, mechanisms.pinpoint, matchSettings);
        // Wait for start
        waitForStart();

        // Initialize mechanisms
        mechanisms.init();

        // Main loop
        while (opModeIsActive()) {
            mechanisms.update();

            processControllerInputs();

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
        mechanisms.drivetrain.mecanumDrive(drive, strafe, rotate);

        // Go to predetermined positions, overriding the mecanum drive if a button is pressed
        Controller.Action[] gotoActions = {
                Controller.Action.GOTO_CLOSE_SHOOT, Controller.Action.GOTO_FAR_SHOOT,
                Controller.Action.GOTO_HUMAN_PLAYER, Controller.Action.GOTO_SECRET_TUNNEL
        };
        for (Controller.Action action : gotoActions) {
            if (mainController.wasJustPressed(action)) {
                mechanisms.drivetrain.goTo(Drivetrain.Position.valueOf(action.name().substring("GOTO_".length())));
                break; // only one goto action can be triggered at a time
            }
        }

        //if (mainController.wasJustPressed(Controller.Action.PARK_EXTEND)) {
        // we arent implementing this until like state bro
        //}

        // Intake controls using Controller's isPressed
        if (subController.getProcessedValue(Controller.Action.AIM) > 0.2) {
            mechanisms.alignmentEngine.run();
            mechanisms.launcher.ready();
        } else {
            mechanisms.launcher.stop();
        }

        if (subController.wasJustPressed(Controller.Action.LAUNCH)) {
            mechanisms.launcher.launch();
        }

        if (subController.getProcessedValue(Controller.Action.INTAKE) > 0) {
            mechanisms.intake.in();
        } else {
            mechanisms.intake.stop();
        }

        /*
        if (subController.getProcessedValue(Controller.Action.RELEASE_EXTRAS) > 0) {
            mechanisms.sorter.loadExtra();
        }
        if (subController.getProcessedValue(Controller.Action.RELEASE_PURPLE) > 0) {
            mechanisms.sorter.loadPurple();
        }
        if (subController.getProcessedValue(Controller.Action.RELEASE_GREEN) > 0) {
            mechanisms.sorter.loadGreen();
        }
         */

        if (subController.getProcessedValue(Controller.Action.EMPTY_CLASSIFIER_STATE) > 0) {
            matchSettings.emptyClassifier();
        }
        if (subController.getProcessedValue(Controller.Action.INCREMENT_CLASSIFIER_STATE) > 0) {
            matchSettings.incrementClassifier();
        }
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