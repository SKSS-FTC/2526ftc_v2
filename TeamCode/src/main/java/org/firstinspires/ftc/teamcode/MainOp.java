package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.hardware.MechanismManager;
import org.firstinspires.ftc.teamcode.software.Drivetrain;

/**
 * Main TeleOp class for driver-controlled period.
 * Handles controller profile selection and robot operation during matches.
 *
 * @noinspection ClassWithoutConstructor
 */
@TeleOp(name = "MainOp", group = ".Competition Modes")
public class MainOp extends OpMode {
	public MatchSettings matchSettings;
	private TelemetryManager logging;
	private MechanismManager mechanisms;
	private Controller mainController;
	private Controller subController;
	private Follower follower;
	
	@Override
	public final void init() {
		// Pull stored settings from auto
		matchSettings = new MatchSettings(blackboard);
		
		// Initialize robot systems
		mechanisms = new MechanismManager(hardwareMap, matchSettings);
		mainController = new Controller(gamepad1, mechanisms.follower, matchSettings);
		subController = new Controller(gamepad2, mechanisms.follower, matchSettings);
		logging = PanelsTelemetry.INSTANCE.getTelemetry();
	}
	
	public final void start() {
		mechanisms.init();
	}
	
	public final void loop() {
		mechanisms.update();
		
		processControllerInputs();
		logging.update();
		
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
	
	public final void stop() {
		mechanisms.stop();
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
		
		if (mainController.wasJustPressed(Controller.Action.CANCEL_ASSISTED_DRIVING)) {
			mechanisms.drivetrain.switchToManual();
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
			mechanisms.spindex.prepareForIntake();
			mechanisms.intake.in();
		} else {
			mechanisms.intake.stop();
		}

        /*
        if (subController.getProcessedValue(Controller.Action.RELEASE_EXTRAS) > 0) {
            mechanisms.spindex.loadExtra();
        }
        if (subController.getProcessedValue(Controller.Action.RELEASE_PURPLE) > 0) {
            mechanisms.spindex.loadPurple();
        }
        if (subController.getProcessedValue(Controller.Action.RELEASE_GREEN) > 0) {
            mechanisms.spindex.loadGreen();
        }
         */
		
		if (subController.getProcessedValue(Controller.Action.EMPTY_CLASSIFIER_STATE) > 0) {
			matchSettings.emptyClassifier();
		}
		if (subController.getProcessedValue(Controller.Action.INCREMENT_CLASSIFIER_STATE) > 0) {
			matchSettings.incrementClassifier();
		}
	}
}