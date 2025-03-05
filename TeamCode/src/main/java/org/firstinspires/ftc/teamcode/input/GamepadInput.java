package org.firstinspires.ftc.teamcode.input;

/**
 * Represents all possible gamepad inputs (buttons and axes)
 */
public enum GamepadInput {
    // Buttons
    TRIANGLE, CIRCLE, SQUARE, CROSS,
    DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT,
    LEFT_BUMPER, RIGHT_BUMPER,
    START, BACK, GUIDE, OPTIONS,
    LEFT_STICK_BUTTON, RIGHT_STICK_BUTTON,
    TOUCHPAD,

    // Axes
    LEFT_TRIGGER, RIGHT_TRIGGER,
    LEFT_STICK_X, LEFT_STICK_Y,
    RIGHT_STICK_X, RIGHT_STICK_Y;

    /**
     * Determines if this input is an axis (vs. a button)
     */
    public boolean isAxis() {
        return this == LEFT_TRIGGER || this == RIGHT_TRIGGER ||
                this == LEFT_STICK_X || this == LEFT_STICK_Y ||
                this == RIGHT_STICK_X || this == RIGHT_STICK_Y;
    }
}