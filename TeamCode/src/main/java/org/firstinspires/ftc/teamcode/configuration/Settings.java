package org.firstinspires.ftc.teamcode.configuration;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.Controller;

import java.util.EnumMap;

/**
 * The Settings class houses all constants and configurations for the robot.
 * This centralized approach makes tuning and adjustments more efficient.
 * The class is organized into logical static inner classes for clarity.
 */
@Configurable
public class Settings {
	
	/**
	 * Contains all settings for the autonomous period.
	 * P_ prefixes denote Pose objects (for coordinates).
	 * H_ prefixes denote Heading values in radians.
	 */
	@Configurable
	public static class Autonomous {
		// The robot's starting position and orientation on the field.
		public static final Pose START_POSE = new Pose(65.533, 12.244, Math.toRadians(115));
		
		// Path Waypoints (Poses are used for x, y coordinates)
		public static final Pose P_START = new Pose(65.533, 12.244);
		public static final Pose P_WAYPOINT_1 = new Pose(35.526, 28.455);
		public static final Pose P_PRELOAD_1_PICKUP = new Pose(18.453, 28.628);
		public static final Pose P_SCORE_1_APPROACH = new Pose(64.671, 44.493);
		public static final Pose P_SCORE_1 = new Pose(63.808, 69.499);
		public static final Pose P_WAYPOINT_2 = new Pose(38.802, 54.668);
		public static final Pose P_PRELOAD_2_PICKUP = new Pose(18.970, 54.496);
		public static final Pose P_SCORE_2 = new Pose(52.081, 80.364);
		public static final Pose P_PRELOAD_3_PICKUP = new Pose(19.143, 80.019);
		public static final Pose P_SCORE_3 = new Pose(40.354, 92.091);
		
		// Path Headings (in radians for the pathing library)
		public static final double H_START = Math.toRadians(115);
		public static final double H_PRELOAD_1 = Math.toRadians(180);
		public static final double H_SCORE_1 = Math.toRadians(130);
		public static final double H_PRELOAD_2 = Math.toRadians(180);
		public static final double H_SCORE_2 = Math.toRadians(135);
		public static final double H_PRELOAD_3 = Math.toRadians(180);
		public static final double H_SCORE_3 = Math.toRadians(126);
	}
	
	/**
	 * Maps controller inputs to robot actions for TeleOp.
	 */
	@Configurable
	public static class Controls {
		public static final EnumMap<Controller.Action, Controller.Control> actionControlMap =
				new EnumMap<>(Controller.Action.class);
		
		static {
			// Main Controller (Driver)
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
			actionControlMap.put(Controller.Action.CANCEL_ASSISTED_DRIVING, Controller.Control.LEFT_STICK_BUTTON);
			actionControlMap.put(Controller.Action.PARK_EXTEND, Controller.Control.START);
			
			// Secondary Controller (Operator)
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
			
			for (Controller.Action action : Controller.Action.values()) {
				actionControlMap.putIfAbsent(action, Controller.Control.UNKNOWN);
			}
		}
	}
	
	/**
	 * Settings related to the robot's drivetrain and movement.
	 */
	@Configurable
	public static class Drive {
		// Multiplier applied to strafe movements to compensate for mechanical differences
		public static final double STRAFE_POWER_COEFFICIENT = 1.2;
	}
	
	/**
	 * Parameters for assisted driving and alignment behaviors.
	 */
	@Configurable
	public static class Alignment {
		// Translational control
		public static final double MAX_TRANSLATIONAL_SPEED = 0.5; // Max drive/strafe speed when far from target (0..1)
		public static final double FULL_SPEED_DISTANCE = 30.0;   // Distance (inches) outside of which translational speed hits max
		public static final double STOP_DISTANCE = 1.0;          // Distance (inches) inside which translational speed tapers to near zero
		
		// Rotational control
		public static final double MAX_ROTATION_SPEED = 0.5; // Max rotation speed (0..1)
		public static final double FULL_SPEED_HEADING_ERROR = Math.toRadians(90); // Heading error (radians) at which rotation is full speed
		public static final double HEADING_DEADBAND = Math.toRadians(2.5); // Deadband: don't rotate if error below this (radians)
	}
	
	/**
	 * Hardware device name mapping.
	 */
	@Configurable
	public static class HardwareIDs {
		// Drive motors
		public static final String FRONT_LEFT_MOTOR = "frontLeft";
		public static final String FRONT_RIGHT_MOTOR = "frontRight";
		public static final String REAR_LEFT_MOTOR = "rearLeft";
		public static final String REAR_RIGHT_MOTOR = "rearRight";
		public static final String PINPOINT = "pinpoint"; // Assuming this is a drive motor or odometry pod
		
		// Subsystem motors and servos
		public static final String INTAKE_MOTOR = "intakeMotor";
		public static final String LAUNCHER_LAUNCHER_RIGHT = "launcherLauncherRight";
		public static final String LAUNCHER_LAUNCHER_LEFT = "launcherLauncherLeft";
		public static final String[] INTAKE_SERVO_ARRAY = {"intakeServoLowerLeft", "intakeServoLowerRight", "intakeServoUpperLeft", "intakeServoUpperRight"};
		public static final String LAUNCHER_TRANSFER_SERVO = "launcherTransferServo";
		public static final String INTAKE_TRANSFER_SERVO = "intakeTransferServo";
		public static final String SPINDEX_SERVO = "spindexServo";
		public static final String LAUNCHER_HORIZONTAL_SERVO = "launcherHorizontalServo";
		public static final String LAUNCHER_VERTICAL_SERVO = "launcherVerticalServo";
		
		// Sensors
		public static final String SPINDEX_COLOR_SENSOR = "spindexColorSensor";
		public static final String LIMELIGHT = "limelight";
		public static final String COLOR_SENSOR = "colorSensor";
	}
	
	/**
	 * Settings for the Intake mechanism.
	 */
	@Configurable
	public static class Intake {
		public static final double SPEED = 0.5;
	}
	
	/**
	 * Settings for the Spindex (indexer/sorter) mechanism.
	 */
	@Configurable
	public static class Spindex {
		public static final double[] SLOT_INTAKE_POSITIONS = {0.10, 0.43, 0.77}; // Calibrated servo positions for slots at intake
		public static final double EXIT_OFFSET = 0.25; // Offset from intake to exit alignment
		public static final double RAPID_FIRE_COOLDOWN_MS = 200;
		public static final long EJECT_EXIT_TIME_MS = 200; // ms for ball to fully leave the spindex after servo opens
		public static final double EXIT_SERVO_CLOSED_POSITION = 1.0;
		public static final double EXIT_SERVO_OPEN_POSITION = 0.0;
		public static final double INTAKE_SERVO_CLOSED_POSITION = 1.0;
		public static final double INTAKE_SERVO_OPEN_POSITION = 0.0;
		public static final double TOLERANCE = 5.0 / 360.0; // how close a slot must be to the exit to launch
	}
	
	/**
	 * Settings for the Launcher mechanism.
	 */
	@Configurable
	public static class Launcher {
		public static final double BELT_MOTOR_SPEED = 1.0; // Launcher motor speed (0..1)
		public static final long BELT_SPINUP_TIME_MS = 500;
		public static final double BELT_SYNC_KP = 0.05; // Proportional gain for synchronizing belt speeds
		public static final double MIN_PITCH = 0; // degrees from horizontal when vert servo is min
		public static final double MAX_PITCH = 30; // degrees from horizontal when vert servo is max
		public static final double MIN_YAW = -20; // degrees left when horizontal servo is min
		public static final double MAX_YAW = 20; // degrees right when horizontal servo is max
	}
	
	@Configurable
	public static class Vision {
		public static final double LL_WINDOW_SIZE_DEGREES = 40; // Horizontal window size
	}
	
	@Configurable
	public static class ColorSensor {
		public static final double[] GREEN_TARGET = {0, 200, 0};
		public static final double[] PURPLE_TARGET = {200, 0, 200};
		public static final double CONFIDENCE_THRESHOLD = 100.0; // Acceptable distance threshold
	}
	
	@Configurable
	public static class Aiming {
		public static final double GRAVITY = 9.81;
		public static double MUZZLE_SPEED; // m/s, TODO: tune
		public static double MUZZLE_HEIGHT; // inches, TODO: tune
		public static double GOAL_HEIGHT; // inches, TODO: tune
		public static double MAX_YAW_ERROR = 1;   // rad
		public static double MAX_PITCH_ERROR = 1; // rad
		public static double GOAL_TOLERANCE;  // inches (vertical tolerance window)
	}
	
	/**
	 * Key locations on the game field.
	 */
	@Configurable
	public static class Field {
		public static final double RED_GOAL_CENTER_X = 50; // TODO tune
		public static final double RED_GOAL_CENTER_Y = 50; // TODO tune
		
		public static final double BLUE_GOAL_CENTER_X = 50; // TODO tune
		public static final double BLUE_GOAL_CENTER_Y = 50; // TODO tune
		
		public static final Pose FAR_LAUNCH_ZONE_FRONT_CORNER = new Pose(0, 0); // TODO tune
		public static final Pose FAR_LAUNCH_ZONE_LEFT_CORNER = new Pose(0, 0); // TODO tune
		public static final Pose FAR_LAUNCH_ZONE_RIGHT_CORNER = new Pose(0, 0); // TODO tune
		
		public static final Pose CLOSE_LAUNCH_ZONE_FRONT_CORNER = new Pose(0, 0); // TODO tune
		public static final Pose CLOSE_LAUNCH_ZONE_LEFT_CORNER = new Pose(0, 0); // TODO tune
		public static final Pose CLOSE_LAUNCH_ZONE_RIGHT_CORNER = new Pose(0, 0); // TODO tune
		
		public static final Pose BLUE_ENDING_POSE = new Pose(0, 0, 0); // TODO TUNE
		public static final Pose RED_ENDING_POSE = new Pose(0, 0, 0); // TODO TUNE
	}
	
	/**
	 * Flags to enable or disable major robot subsystems.
	 * Useful for testing and debugging.
	 */
	@Configurable
	public static class Deploy {
		public static final boolean INTAKE = true;
		public static final boolean SPINDEX = true;
		public static final boolean AUTOMATIONS = true;
	}
}

