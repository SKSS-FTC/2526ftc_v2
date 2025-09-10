package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.configuration.Settings;
import org.firstinspires.ftc.teamcode.hardware.MechanismManager;
import org.firstinspires.ftc.teamcode.hardware.submechanisms.LimelightManager;
import org.firstinspires.ftc.teamcode.hardware.submechanisms.Shoulder;
import org.firstinspires.ftc.teamcode.hardware.submechanisms.ViperSlide;
import org.firstinspires.ftc.teamcode.hardware.submechanisms.Wrist;
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
        mainController = new Controller(gamepad1);
        subController = new Controller(gamepad2);
        drivetrain = new Drivetrain(hardwareMap);
        limelightManager = new LimelightManager(hardwareMap.get(Limelight3A.class, "limelight"));
        manualPinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        alignmentEngine = new AlignmentEngine(mainController, matchSettings, drivetrain, mechanisms, limelightManager, manualPinpoint);
        trajectoryEngine = new TrajectoryEngine(limelightManager, manualPinpoint, matchSettings);
        // Wait for start
        waitForStart();

        // Initialize mechanisms
        mechanisms.init();

        // Main loop
        while (opModeIsActive()) {
            manualPinpoint.update();

            processControllerInputs();

            checkAutomationConditions();
            checkAssistanceConditions();

            updateTelemetry();

            mainController.saveLastState();
            subController.saveLastState();
        }

        // Stop all executor tasks at the end
        scheduler.shutdownNow();
    }

    /**
     * Process controller inputs
     */
    private void processControllerInputs() {
        // Get drivetrain controls
        double drive = mainController.getProcessedValue(Controller.Action.MOVE_Y);
        double strafe = mainController.getProcessedValue(Controller.Action.MOVE_X);
        double rotate = mainController.getProcessedValue(Controller.Action.ROTATE);

        // Apply the values to drivetrain
        drivetrain.mecanumDrive(drive, strafe, rotate);

        // Vertical slide controls - incremental mode
        if (Settings.Controls.incrementalVertical) {
            if (mainController.getProcessedValue(Controller.Action.EXTEND_VERTICAL) > 0.0) {
                mechanisms.outtake.verticalSlide.increment();
            }
            if (mainController.getProcessedValue(Controller.Action.RETRACT_VERTICAL) > 0.0) {
                mechanisms.outtake.verticalSlide.decrement();
            }
        } else { // Direct mode
            if (mainController.wasJustPressed(Controller.Action.EXTEND_VERTICAL)) {
                mechanisms.outtake.verticalSlide.extend();
            }
            if (mainController.wasJustPressed(Controller.Action.RETRACT_VERTICAL)) {
                mechanisms.outtake.verticalSlide.retract();
            }

        }

        // Intake controls using Controller's isPressed
        if (subController.wasJustPressed(Controller.Action.CLOSE_CLAW)) {
            mechanisms.intake.intakeClaw.close();
            scheduleTask(() -> mechanisms.intake.wrist.setPosition(Wrist.Position.VERTICAL), 200);
            mechanisms.outtake.outtakeClaw.open();
        }

        if (subController.wasJustPressed(Controller.Action.OPEN_CLAW)) {
            mechanisms.intake.intakeClaw.open();
            if (mechanisms.intake.horizontalSlide.currentPosition.getValue() > 30 &&
                    mechanisms.intake.intakeClaw.opened) {
                mechanisms.intake.wrist.setPosition(Wrist.Position.READY);
            } else {
                mechanisms.intake.wrist.setPosition(Wrist.Position.VERTICAL);
            }
        }

        if (subController.getProcessedValue(Controller.Action.WRIST_HORIZONTAL) > 0) {
            mechanisms.intake.wrist.setPosition(Wrist.Position.HORIZONTAL);
        }

        // Horizontal slide controls
        if (Settings.Controls.incrementalHorizontal) {
            if (subController.getProcessedValue(Controller.Action.EXTEND_HORIZONTAL) > 0) {
                mechanisms.intake.horizontalSlide.increment();
            }

            if (subController.getProcessedValue(Controller.Action.RETRACT_HORIZONTAL) > 0) {
                mechanisms.intake.horizontalSlide.decrement();
            }
        } else {
            if (subController.wasJustPressed(Controller.Action.EXTEND_HORIZONTAL)) {
                mechanisms.intake.horizontalSlide.extend();
            }

            if (subController.wasJustPressed(Controller.Action.RETRACT_VERTICAL)) {
                mechanisms.intake.horizontalSlide.retract();
                if (mechanisms.intake.horizontalSlide.currentPosition == ViperSlide.HorizontalPosition.COLLAPSED) {
                    mechanisms.outtake.outtakeClaw.open();
                    mechanisms.intake.wrist.setPosition(Wrist.Position.VERTICAL);
                }
            }
        }

        // Rotator control
        double rotatorValue = subController.getProcessedValue(Controller.Action.ROTATOR);
        if (Math.abs(rotatorValue) > 0.05) {
            double normalizedValue = (rotatorValue + 1) / 2; // Convert from -1..1 to 0..1
            mechanisms.intake.rotator.setPosition(normalizedValue);
        }

        if (subController.getProcessedValue(Controller.Action.HANG_EXTEND) > 0) {
            mechanisms.linearActuator.extend();
        }

        if (subController.getProcessedValue(Controller.Action.HANG_EXTEND) > 0) {
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
        alignmentEngine.check();
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