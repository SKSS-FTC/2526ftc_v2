package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

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
        // Using EnumMap for better performance and type safety with Enum keys
        // The keys are the physical controller inputs, the values are the game actions.
        protected static EnumMap<Controller.Action, Controller.Control> actionControlMap =
                new EnumMap<>(Controller.Action.class); // Initialize with the Control enum class

        static {
            actionControlMap.put(Controller.Action.OPEN_CLAW, Controller.Control.RIGHT_TRIGGER);
            actionControlMap.put(Controller.Action.CLOSE_CLAW, Controller.Control.LEFT_TRIGGER);
            actionControlMap.put(Controller.Action.EXTEND_VERTICAL, Controller.Control.DPAD_UP);
            actionControlMap.put(Controller.Action.RETRACT_VERTICAL, Controller.Control.DPAD_DOWN);
            actionControlMap.put(Controller.Action.EXTEND_HORIZONTAL, Controller.Control.DPAD_LEFT);
            actionControlMap.put(Controller.Action.RETRACT_HORIZONTAL, Controller.Control.DPAD_RIGHT);
            actionControlMap.put(Controller.Action.MOVE_Y, Controller.Control.LEFT_STICK_Y);
            actionControlMap.put(Controller.Action.MOVE_X, Controller.Control.LEFT_STICK_X);
            actionControlMap.put(Controller.Action.ROTATE, Controller.Control.RIGHT_STICK_X);

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

            // Arm components
            public static final String SLIDE_VERTICAL_LEFT = "slideVerticalLeft";
            public static final String SLIDE_VERTICAL_RIGHT = "slideVerticalRight";
            public static final String SLIDE_HORIZONTAL = "slideHorizontal";
            public static final String LINEAR_ACTUATOR = "linearActuator";
            public static final String WRIST = "wrist";
            public static final String ROTATOR = "rotator";
            public static final String LEFT_SHOULDER = "shoulderLeft";
            public static final String RIGHT_SHOULDER = "shoulderRight";
            public static final String INTAKE_CLAW = "intakeClaw";
            public static final String OUTTAKE_CLAW = "outtakeClaw";
            public static final String SLIDE_VERTICAL_TOUCH_SENSOR = "verticalSlideSensor";
            public static final String LIMELIGHT = "limelight";
            public static final String COLOR_SENSOR = "colorSensor";
        }

        @Config
        public static class VerticalSlide {
            // Positions in encoder ticks

            public static int TRANSFER = 0;
            public static int HANG_RUNG_1 = 3350;

            public static int HIGH_RUNG_PREP_AUTO = 1890;
            public static int HIGH_RUNG = 2250;
            public static int HIGH_BASKET = 3350;

            // Motor power settings
            public static double MOVEMENT_POWER = 1;
            public static double IDLE_POWER = 0.2;

            public static double INCREMENTAL_MOVEMENT_POWER = 20;

            public static boolean ENABLE_LOWER_LIMIT = false;
        }

        @Config
        public static class HorizontalSlide {
            // Positions in encoder ticks
            public static int COLLAPSED = 0;
            public static int LEVEL_1 = 82;
            public static int EXPANDED = 250;

            // Motor power settings
            public static double MOVEMENT_POWER = 0.5;
            public static int INCREMENTAL_MOVEMENT_POWER = 15;
        }

        @Config
        public static class LinearActuator {
            // Positions in encoder ticks
            public static int MAX = 1000;
            public static int MIN = 0;
        }

        @Config
        public static class Intake {
            public static double SPEED = -1;
        }
    }
}
