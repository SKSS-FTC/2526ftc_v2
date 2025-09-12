package org.firstinspires.ftc.teamcode.configuration;

import com.acmerobotics.dashboard.config.Config;

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
    /* The configuration for the Controllers. **/
    @Config
    public static class Controls {
        public static boolean incrementalVertical = false;
        public static boolean incrementalHorizontal = false;
        /**
         * @noinspection PublicStaticCollectionField (i dont car)
         */
        // Using EnumMap for better performance and type safety with Enum keys
        // The keys are the physical controller inputs, the values are the game actions.
        public static EnumMap<Controller.Action, Controller.Control> actionControlMap =
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
            actionControlMap.put(Controller.Action.TURRET_STEEPNESS_AXIS, Controller.Control.RIGHT_STICK_Y);
            actionControlMap.put(Controller.Action.TURRET_ROTATION_AXIS, Controller.Control.RIGHT_STICK_X);
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
        public static double strafe_power_coefficient = 1.2;

        // Enable or disable all automatic functions
        public static boolean automationEnabled = true;

    }

    // Deploy flags control what parts of the tob
    @Config
    public static class Deploy {
        // Core Mechanisms
        public static final boolean INTAKE = true;
        public static final boolean OUTTAKE = true;
        public static final boolean LINEAR_ACTUATOR = false;
    }

    @Config
    public static class Assistance {
        public static boolean use_deadeye = true;
        public static double inverseLateralMultiplier = 50; // move at full power at 30 inches laterally away, going down to 0.0333333333 at 1 inch away
        public static double minimumRotationCorrectionThreshold = Math.PI / 70; // Don't correct heading within 0.1570796327
        public static double approachSpeed = 0.5; // if within an inch it's good enough
        public static double limelightWindowSize = 40; // degrees
        public static boolean ECHOLOCATE_ENABLED = false;
    }

    /** @noinspection InnerClassTooDeeplyNested*/
    // Hardware settings
    @Config
    public static class Hardware {
        // Servo positions
        @Config
        public static class Servo {
            @Config
            public static class OuttakeClaw {
                /**
                 * Values for open and closed positions on the outtake claw
                 */
                public static double OPEN = 0;
                public static double CLOSED = 1;
            }

            @Config
            public static class IntakeClaw {
                /**
                 * Values for open and closed positions on the outtake claw
                 */
                public static double OPEN = 0.5;
                public static double CLOSED = 0.8;
            }

            @Config
            public static class Wrist {
                public static double HORIZONTAL_POSITION = .53;
                public static double VERTICAL_POSITION = 0.88;
                public static double READY_POSITION = .65;
            }

            @Config
            public static class Rotator {
                public static double LEFT_LIMIT = 0.1;
                public static double RIGHT_LIMIT = 1;
                public static double CENTER = (LEFT_LIMIT + RIGHT_LIMIT) / 2;
            }

            @Config
            public static class Shoulder {
                public static double PLACE_FORWARD_POSITION = 0;
                public static double PLACE_BACKWARD_POSITION = 0.65;

            }
        }

        @Config
        public static class IDs {

            // Drive motors
            public static final String FRONT_LEFT_MOTOR = "frontLeft";
            public static final String FRONT_RIGHT_MOTOR = "frontRight";
            public static final String REAR_LEFT_MOTOR = "rearLeft";
            public static final String REAR_RIGHT_MOTOR = "rearRight";

            public static final String INTAKE_MOTOR = "intakeMotor";
            public static final String LIMELIGHT = "limelight";
            public static final String COLOR_SENSOR = "colorSensor";
        }

        @Config
        public static class Intake {
            public static double SPEED = -1;
        }
    }
}
