package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Launcher;
import org.firstinspires.ftc.teamcode.hardware.MechanismManager;
import org.firstinspires.ftc.teamcode.hardware.Spindex;
import org.firstinspires.ftc.teamcode.pedroPathing.Drawing;
import org.firstinspires.ftc.teamcode.software.AlignmentEngine;
import org.firstinspires.ftc.teamcode.software.Drivetrain;

import java.util.function.Consumer;

/**
 * This is our main TeleOp class for the driver-controlled period, which occurs after Auto.
 * Handles controller profile selection and robot operation during matches.
 */
@TeleOp(name = "MainOp", group = ".Competition Modes")
public class MainOp extends OpMode {
	public MatchSettings matchSettings;
	private TelemetryManager logging;
	private MechanismManager mechanisms;
	private Controller mainController;
	private Controller subController;
	
	/**
	 * This allows us to run commands only if the related mechanism works.
	 * For example I could run "if launcher exists, shoot it" using this.
	 */
	private static <T> void ifMechanismValid(T obj, Consumer<T> action) {
		if (obj != null) {
			try {
				action.accept(obj);
			} catch (Exception ignored) {
				// swallow failures
			}
		}
	}
	
	/**
	 * Runs when "init" is pressed on the Driver Station.
	 */
	@Override
	public final void init() {
		// Pull the stored match state and settings from when they were set during auto
		matchSettings = new MatchSettings(blackboard);
		
		// Initialize robot systems
		mechanisms = new MechanismManager(hardwareMap, matchSettings);
		mainController = new Controller(gamepad1, mechanisms.drivetrain.follower, matchSettings);
		subController = new Controller(gamepad2, mechanisms.drivetrain.follower, matchSettings);
		logging = PanelsTelemetry.INSTANCE.getTelemetry();
	}
	
	/**
	 * Runs after "init" and before "start" repeatedly.
	 */
	@Override
	public final void init_loop() {
		// draw the robot at its starting position
		Drawing.drawRobot(mechanisms.drivetrain.follower.getPose());
		Drawing.sendPacket();
	}
	
	/**
	 * Runs when "start" is pressed on the Driver Station.
	 */
	@Override
	public final void start() {
		ifMechanismValid(mechanisms, m -> m.init());
		mechanisms.drivetrain.follower.startTeleopDrive();
	}
	
	/**
	 * Runs repeatedly after "start" is pressed on the Driver Station, during the actual game.
	 */
	@Override
	public final void loop() {
		mechanisms.update();
		
		processControllerInputs();
		setControllerLEDs();
		
		// Log everything that happened
		Drawing.drawDebug(mechanisms.drivetrain.follower);
		telemetry.addData("Heading", mechanisms.drivetrain.follower.getHeading());
		telemetry.addData("X", mechanisms.drivetrain.follower.getPose().getX());
		telemetry.addData("Y", mechanisms.drivetrain.follower.getPose().getY());
		logging.update();
	}
	
	/**
	 * Runs when "stop" is pressed on the Driver Station.
	 * Cleanup and shutdown should occur instantaneously and be non-blocking.
	 */
	@Override
	public final void stop() {
		mechanisms.stop();
	}
	
	/**
	 * Process controller inputs
	 */
	private void processControllerInputs() {
		// Drivetrain
		double drive = mainController.getProcessedDrive();
		double strafe = mainController.getProcessedStrafe();
		double rotate = mainController.getProcessedRotation();
		ifMechanismValid(mechanisms.drivetrain, dt -> dt.mecanumDrive(drive, strafe, rotate));
		
		// Go-to actions
		Controller.Action[] gotoActions = {
				Controller.Action.GOTO_CLOSE_SHOOT,
				Controller.Action.GOTO_FAR_SHOOT,
				Controller.Action.GOTO_HUMAN_PLAYER,
				Controller.Action.GOTO_SECRET_TUNNEL
		};
		for (Controller.Action action : gotoActions) {
			if (mainController.wasJustPressed(action)) {
				ifMechanismValid(mechanisms.drivetrain, dt ->
						dt.goTo(Drivetrain.Position.valueOf(action.name().substring("GOTO_".length())))
				);
				break;
			}
		}
		
		if (mainController.wasJustPressed(Controller.Action.CANCEL_ASSISTED_DRIVING)) {
			ifMechanismValid(mechanisms.drivetrain, Drivetrain::switchToManual);
		}
		
		// Alignment & Launcher
		if (subController.getProcessedValue(Controller.Action.AIM) > 0.2) {
			ifMechanismValid(mechanisms.get(AlignmentEngine.class), AlignmentEngine::run);
			ifMechanismValid(mechanisms.get(Launcher.class), Launcher::ready);
		} else {
			ifMechanismValid(mechanisms.get(Launcher.class), Launcher::stop);
		}
		
		if (subController.wasJustPressed(Controller.Action.LAUNCH)) {
			if (mechanisms.alignmentEngine.isInLaunchZone(mechanisms.drivetrain.getPose())) {
				ifMechanismValid(mechanisms.get(Launcher.class), Launcher::launch);
			}
		}
		
		// Intake & Spindex
		if (subController.getProcessedValue(Controller.Action.INTAKE) > 0) {
			ifMechanismValid(mechanisms.get(Spindex.class), Spindex::prepareForIntake);
			ifMechanismValid(mechanisms.get(Intake.class), Intake::in);
		} else {
			ifMechanismValid(mechanisms.get(Intake.class), Intake::stop);
		}

	    /*
	    Optional extra loads (commented out)
	    if (subController.getProcessedValue(Controller.Action.RELEASE_EXTRAS) > 0)
	        ifMechanismValid(mechanisms.get(Spindex.class), Spindex::loadExtra);
	    if (subController.getProcessedValue(Controller.Action.RELEASE_PURPLE) > 0)
	        ifMechanismValid(mechanisms.get(Spindex.class), Spindex::loadPurple);
	    if (subController.getProcessedValue(Controller.Action.RELEASE_GREEN) > 0)
	        ifMechanismValid(mechanisms.get(Spindex.class), Spindex::loadGreen);
	    */
		
		// Classifier controls
		if (subController.getProcessedValue(Controller.Action.EMPTY_CLASSIFIER_STATE) > 0)
			matchSettings.emptyClassifier();
		
		if (subController.getProcessedValue(Controller.Action.INCREMENT_CLASSIFIER_STATE) > 0)
			matchSettings.incrementClassifier();
		
		mainController.saveLastState();
		subController.saveLastState();
	}
	
	/**
	 * Set the LEDs on the controller based on the match state.
	 */
	private void setControllerLEDs() {
		if (matchSettings.nextArtifactNeeded() == MatchSettings.ArtifactColor.GREEN) {
			subController.setLedColor(0, 255, 0, 100);
		} else if (matchSettings.nextArtifactNeeded() == MatchSettings.ArtifactColor.PURPLE) {
			subController.setLedColor(255, 0, 255, 100);
		} else {
			subController.setLedColor(0, 0, 0, 0);
		}
	}
}
