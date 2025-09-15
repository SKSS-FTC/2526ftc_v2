package org.firstinspires.ftc.teamcode.configuration;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.ftcontrol.panels.json.Point;

import org.firstinspires.ftc.teamcode.Controller;

import java.util.EnumMap;

/**
 * The Settings class houses all of the constants and configuration for the robot.
 * Any magic numbers we may need to change later should be stored here.
 *
 * @noinspection ClassWithoutConstructor
 */
@Config
public class Settings {
	@Config
	public static class Controls {
		// Using EnumMap for better performance and type safety with Enum keys
		// The keys are the physical controller inputs, the values are the game actions.
		public static final EnumMap<Controller.Action, Controller.Control> actionControlMap =
				new EnumMap<>(Controller.Action.class); // Initialize with the Control enum class
		
		static {
			// Main Controller Mapping
			actionControlMap.put(Controller.Action.MOVE_Y, Controller.Control.LEFT_STICK_Y);
			actionControlMap.put(Controller.Action.MOVE_X, Controller.Control.LEFT_STICK_X);
			actionControlMap.put(Controller.Action.ROTATE_AXIS, Controller.Control.RIGHT_STICK_X);
			actionControlMap.put(Controller.Action.ROTATE_LEFT, Controller.Control.LEFT_BUMPER);
			actionControlMap.put(Controller.Action.ROTATE_RIGHT, Controller.Control.RIGHT_BUMPER);
			actionControlMap.put(Controller.Action.ABS_NORTH, Controller.Control.DPAD_UP);
			actionControlMap.put(Controller.Action.ABS_EAST, Controller.Control.DPAD_RIGHT);
			actionControlMap.put(Controller.Action.ABS_WEST, Controller.Control.DPAD_LEFT);
			actionControlMap.put(Controller.Action.ABS_SOUTH, Controller.Control.DPAD_DOWN);
			actionControlMap.put(Controller.Action.GOTO_CLOSE_SHOOT, Controller.Control.CIRCLE);
			actionControlMap.put(Controller.Action.GOTO_FAR_SHOOT, Controller.Control.CROSS);
			actionControlMap.put(Controller.Action.GOTO_HUMAN_PLAYER, Controller.Control.SQUARE);
			actionControlMap.put(Controller.Action.GOTO_SECRET_TUNNEL, Controller.Control.TRIANGLE);
			actionControlMap.put(Controller.Action.PARK_EXTEND, Controller.Control.START);
			
			// Secondary Controller Mapping
			actionControlMap.put(Controller.Action.AIM, Controller.Control.LEFT_TRIGGER);
			actionControlMap.put(Controller.Action.LAUNCH, Controller.Control.RIGHT_TRIGGER);
			actionControlMap.put(Controller.Action.LAUNCHER_STEEPNESS_AXIS, Controller.Control.RIGHT_STICK_Y);
			actionControlMap.put(Controller.Action.LAUNCHER_ROTATION_AXIS, Controller.Control.RIGHT_STICK_X);
			actionControlMap.put(Controller.Action.INTAKE, Controller.Control.SQUARE);
			actionControlMap.put(Controller.Action.RELEASE_EXTRAS, Controller.Control.CIRCLE);
			actionControlMap.put(Controller.Action.RELEASE_PURPLE, Controller.Control.TRIANGLE);
			actionControlMap.put(Controller.Action.RELEASE_GREEN, Controller.Control.CROSS);
			actionControlMap.put(Controller.Action.INCREMENT_CLASSIFIER_STATE, Controller.Control.DPAD_UP);
			actionControlMap.put(Controller.Action.EMPTY_CLASSIFIER_STATE, Controller.Control.DPAD_DOWN);
			
			// Everything else is "UNSET"
			for (Controller.Action action : Controller.Action.values()) {
				actionControlMap.putIfAbsent(action, Controller.Control.UNKNOWN); // Ensures all controls have a mapping
			}
		}
	}
	
	// Movement settings
	@Config
	public static class Teleop {
		// Multiplier applied to strafe movements to compensate for mechanical differences
		public static final double strafe_power_coefficient = 1.2;
	}
	
	// Deploy flags control what parts of the robot are on
	@Config
	public static class Deploy {
		// Core Mechanisms
		public static final boolean INTAKE = true;
		public static final boolean SORTER = true;
		public static final boolean AUTOMATIONS = true;
	}
	
	/**
	 * @noinspection InnerClassTooDeeplyNested
	 */
	// Hardware settings
	@Config
	public static class Hardware {
		@Config
		public static class Intake {
			public static final double SPEED = 0.5;
		}
		
		@Config
		public static final class Sorter {
			// Default calibrated servo positions for slots at intake
			public static final double[] SLOT_INTAKE_POSITIONS = {0.10, 0.43, 0.77}; // TODO TUNE
			
			// Offset from intake to exit alignment
			public static final double EXIT_OFFSET = 0.25; // TODO TUNE
			
			public static final long EJECT_EXIT_TIME_MS = 200; // ms needed from servo receiving open to the ball fully leaving the sorter
			
			public static final double TRANSFER_SERVO_CLOSED_POSITION = 1.0; // TODO TUNE
			public static final double TRANSFER_SERVO_OPEN_POSITION = 0.0; // TODO TUNE
			
			public static final double TOLERANCE = 5.0 / 360.0; // how close a slot must be to the exit in order to launch
		}
		
		@Config
		public static class IDs {
			
			// Drive motors
			public static final String FRONT_LEFT_MOTOR = "frontLeft";
			public static final String FRONT_RIGHT_MOTOR = "frontRight";
			public static final String PINPOINT = "pinpoint";
			public static final String REAR_LEFT_MOTOR = "rearLeft";
			public static final String REAR_RIGHT_MOTOR = "rearRight";
			
			public static final String INTAKE_MOTOR = "intakeMotor";
			public static final String[] INTAKE_SERVO_ARRAY = {"intakeServoLowerLeft", "intakeServoLowerRight", "intakeServoUpperLeft", "intakeServoUpperRight"};
			public static final String LAUNCHER_TRANSFER_SERVO = "launcherTransferServo";
			public static final String SORTER_COLOR_SENSOR = "sorterColorSensor";
			
			public static final String LIMELIGHT = "limelight";
			public static final String COLOR_SENSOR = "colorSensor";
			public static final String SORTER_SERVO = "sorterServo";
			public static final String LAUNCHER_HORIZONTAL_SERVO = "launcherHorizontalServo";
			public static final String LAUNCHER_VERTICAL_SERVO = "launcherVerticalServo";
			
			public static final String LAUNCHER_LAUNCHER_RIGHT = "launcherLauncherRight";
			public static final String LAUNCHER_LAUNCHER_LEFT = "launcherLauncherLeft";
		}
	}
	
	@Config
	public static class Aiming {
		public static final double gravity = 9.81;
		// todo tune
		public static double muzzleSpeed;
		public static double muzzleHeight;
		public static double goalHeight;
		public static double maxYawError = 1;      // rad
		public static double maxPitchError = 1;    // rad
		public static double goalTolerance;    // cm (vertical tolerance window)
	}
	
	@Config
	public static class Limelight {
		/**
		 * Limelight horizontal window size (degrees)
		 */
		public static final double limelightWindowSize = 40;
	}
	
	@Config
	public static class Launcher {
		public static final double BELT_SYNC_KP = 0.05; // how fast the belts move to match each other
		public static final double MIN_PITCH = 0; // degrees from horizontal when vert servo is fully down
		public static final double MAX_PITCH = 30; // degrees above horiz when vert servo is maxed
		public static final double MIN_YAW = -20; // degrees left when horiz servo is min
		public static final double MAX_YAW = 20; // degrees right when horiz servo is maxed
		/**
		 * Launcher motor speed (0..1)
		 */
		public static final double BELT_MOTOR_SPEED = 1;
		public static final long BELT_SPINUP_TIME_MS = 500; // TODO tune
	}
	
	@Config
	public static class Alignment {
		// Translational control
		/**
		 * Max drive/strafe speed when far from target (0..1)
		 */
		public static final double maxTranslationalSpeed = 0.5;
		
		/**
		 * Distance (inches) outside of which translational speed hits max
		 */
		public static final double fullSpeedDistance = 30.0;
		
		/**
		 * Distance (inches) inside which translational speed tapers to near zero
		 */
		public static final double stopDistance = 1.0;
		
		// Rotational control
		/**
		 * Max rotation speed (0..1)
		 */
		public static final double maxRotationSpeed = 0.5;
		
		/**
		 * Heading error (radians) at which rotation is full speed
		 */
		public static final double fullSpeedHeadingError = Math.toRadians(90);
		
		/**
		 * Deadband: don't rotate if error below this (radians)
		 */
		public static final double headingDeadband = Math.toRadians(2.5);
	}
	
	public static class Positions {
		public static final double RED_GOAL_CENTER_X = 50; // TODO tune
		public static final double RED_GOAL_CENTER_Y = 50; // TODO tune
		
		public static final double BLUE_GOAL_CENTER_X = 50; // TODO tune
		public static final double BLUE_GOAL_CENTER_Y = 50; // TODO tune
		
		public static final Point FAR_LAUNCH_ZONE_FRONT_CORNER = new Point(0, 0); // TODO tune
		public static final Point FAR_LAUNCH_ZONE_LEFT_CORNER = new Point(0, 0); // TODO tune
		public static final Point FAR_LAUNCH_ZONE_RIGHT_CORNER = new Point(0, 0); // TODO tune
		
		public static final Point CLOSE_LAUNCH_ZONE_FRONT_CORNER = new Point(0, 0); // TODO tune
		public static final Point CLOSE_LAUNCH_ZONE_LEFT_CORNER = new Point(0, 0); // TODO tune
		public static final Point CLOSE_LAUNCH_ZONE_RIGHT_CORNER = new Point(0, 0); // TODO tune
	}
}
