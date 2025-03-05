package org.firstinspires.ftc.teamcode.input;

/**
 * Enum representing all possible gamepad inputs
 * Supports both PlayStation and Xbox style controllers
 */
public enum GamepadInput {
    // Buttons
    TRIANGLE, // Y on Xbox
    CIRCLE, // B on Xbox
    CROSS, // A on Xbox
    SQUARE, // X on Xbox

    DPAD_UP,
    DPAD_DOWN,
    DPAD_LEFT,
    DPAD_RIGHT,

    LEFT_BUMPER,
    RIGHT_BUMPER,

    START,
    BACK,
    GUIDE, // Xbox button or PS button
    OPTIONS,

    LEFT_STICK_BUTTON,
    RIGHT_STICK_BUTTON,

    TOUCHPAD, // PS4/PS5 touchpad

    // Axes
    LEFT_TRIGGER,
    RIGHT_TRIGGER,
    LEFT_STICK_X,
    LEFT_STICK_Y,
    RIGHT_STICK_X,
    RIGHT_STICK_Y
}