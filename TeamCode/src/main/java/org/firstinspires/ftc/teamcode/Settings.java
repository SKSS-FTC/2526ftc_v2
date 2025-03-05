package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

import java.lang.reflect.Field;

/**
 * @noinspection CanBeFinal, ClassWithoutConstructor
 */
@Config
public class Settings {

    // Movement settings
    @Config
    public static class Teleop {
        // Multiplier applied to strafe movements to compensate for mechanical differences
        public static double strafe_power_coefficient = 1.2;

        // Begin the robot controls flipped (-1) or regular (1)
        public static double initial_flip = 1;

        // Enable or disable automatic functions
        public static boolean automationEnabled = true;

    }

    // Deploy flags
    @Config
    public static class Deploy {
        // Core Mechanisms
        public static final boolean INTAKE = true;
        public static final boolean OUTTAKE = true;
        public static final boolean LINEAR_ACTUATOR = false;

        public enum AutonomousMode {
            JUST_PARK, JUST_PLACE, CHAMBER, BASKET
        }
    }

    public static String getDisabledFlags() {
        StringBuilder enabledFlags = new StringBuilder();

        Field[] fields = Deploy.class.getFields();

        for (Field field : fields) {
            try {
                if (!field.getBoolean(null)) {
                    enabledFlags.append(field.getName()).append(", ");
                }
            } catch (IllegalAccessException ignored) {
            }
        }

        return enabledFlags.toString();
    }

    @Config
    public static class Calibration {
        /**
         * Multiplier applied to strafe movements to compensate for mechanical
         * differences
         */
        public static double headingTolerance = 0.02;
        //        public static Vector2d spacialTolerance = new Vector2d(0.5, 0.5);
        public static double CALIBRATION_APPROXIMATION_COEFFICIENT = 0;
    }

    @Config
    public static class Assistance {
        public static double inverseLateralMultiplier = 50; // move at full power at 30 inches laterally away, going down to 0.0333333333 at 1 inch away
        public static double minimumRotationCorrectionThreshold = Math.PI / 70; // Don't correct heading within 0.1570796327
        public static double approachSpeed = 0.5; // if within an inch it's good enough
        public static double limelightWindowSize = 40; // degrees
    }

    // Hardware settings
    @Config
    public static class Hardware {
        /**
         * Encoder counts per full motor revolution
         */
        public static final double COUNTS_PER_REVOLUTION = 10323.84; // ish? may need to recalculate later
        /**
         * Diameter of the odometry wheels in inches
         */
        public static final double WHEEL_DIAMETER_INCHES = 3.5;

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
                // TODO: TUNE WHEN NEW SERVO GOES IN
                public static double TRANSFER_POSITION = 0.1;
                public static double PLACE_FORWARD_POSITION = 0;
                public static double PLACE_BACKWARD_POSITION = 0.65;

            }
        }

        @Config
        public static class IDs {
            public static final String IMU = "imu";
            public static final String LED = "led";

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
            public static final String PINPOINT = "pinpoint";
            public static final String SLIDE_VERTICAL_TOUCH_SENSOR = "verticalSlideSensor";
            public static final String LIMELIGHT = "limelight";
            public static final String COLOR_SENSOR = "colorSensor";
        }

        @Config
        public static class VerticalSlide {
            // Positions in encoder ticks

            public static int TRANSFER = 0;
            public static int LOW_RUNG = 80;
            public static int LOW_BASKET = 550;
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
            // TODO: TUNE
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

            public static double SPEED = 0.5;
        }

        @Config
        public static class Intake {
            public static double SPEED = -1;
        }
    }

    // Autonomous settings
    @Config
    public static class Autonomous {
        public static boolean ECHOLOCATE_ENABLED = false;

        @Config
        public static class Movement {
            public static int ENCODERS_NEEDED_TO_CORRECT_ODOMETRY = 3;
        }

        @Config
        public static class Timing {
            /**
             * Pause duration after claw operations (milliseconds)
             */
            public static long CLAW_PAUSE = 500;
            public static long WRIST_PAUSE = 1000;
            public static long EXTENSOR_PAUSE = 2500;
        }
    }
}
