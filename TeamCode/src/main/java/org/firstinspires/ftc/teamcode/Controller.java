package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.HashMap;

public class Controller extends Gamepad {
    private final Gamepad gamepad;
    private HashMap<Control, Double> previousControlState = new HashMap<>();

    // Define your Action enum
    public enum Action {
        MOVE_Y,
        MOVE_X,
        ROTATE,
        EXTEND_VERTICAL,
        RETRACT_VERTICAL,
        EXTEND_HORIZONTAL,
        RETRACT_HORIZONTAL,
        WRIST_HORIZONTAL,
        ROTATOR,
        HANG_RETRACT,
        HANG_EXTEND,
        OPEN_CLAW,
        CLOSE_CLAW,
        UNSET,
    }

    public Controller(Gamepad gamepad) {
        this.gamepad = gamepad;
        this.previousControlState = new HashMap<>();
    }

    public void saveLastState() {
        for (Control control : Control.values()) {
            previousControlState.put(control, getRawValue(control));
        }
    }

    public final boolean wasJustPressed(Control control) {
        return getRawValue(control) != 0.0 && previousControlState.getOrDefault(control, 0.0) == 0;
    }

    public final boolean wasJustPressed(Action action) {
        return wasJustPressed(getControlForAction(action));
    }

    public final Control getControlForAction(Action action) {
        return Settings.Controls.actionControlMap.getOrDefault(action, Control.UNKNOWN);
    }

    public final double getProcessedValue(Control control) {
        // add value modifiers here
        double val = getRawValue(control);

        switch (control) {
            case LEFT_STICK_X:
                val = -val;
                break;
            // add more here
        }
        return val;
    }

    public final double getProcessedValue(Action action) {
        return getProcessedValue(getControlForAction(action));
    }
    private final double getRawValue(Control control) {
        switch (control) {
            case LEFT_TRIGGER:
                return gamepad.left_trigger;
            case RIGHT_TRIGGER:
                return gamepad.right_trigger;
            case LEFT_STICK_X:
                return gamepad.left_stick_x;
            case LEFT_STICK_Y:
                return gamepad.left_stick_y;
            case RIGHT_STICK_X:
                return gamepad.right_stick_x;
            case RIGHT_STICK_Y:
                return gamepad.right_stick_y;
            case BACK:
                return gamepad.back ? 1 : 0;
            case START:
                return gamepad.start ? 1 : 0;
            case CIRCLE:
                return gamepad.circle ? 1 : 0;
            case CROSS:
                return gamepad.cross ? 1 : 0;
            case SQUARE:
                return gamepad.square ? 1 : 0;
            case TRIANGLE:
                return gamepad.triangle ? 1 : 0;
            case LEFT_BUMPER:
                return gamepad.left_bumper ? 1 : 0;
            case RIGHT_BUMPER:
                return gamepad.right_bumper ? 1 : 0;
            case DPAD_UP:
                return gamepad.dpad_up ? 1 : 0;
            case DPAD_DOWN:
                return gamepad.dpad_down ? 1 : 0;
            case DPAD_LEFT:
                return gamepad.dpad_left ? 1 : 0;
            case DPAD_RIGHT:
                return gamepad.dpad_right ? 1 : 0;
            case LEFT_STICK_BUTTON:
                return gamepad.left_stick_button ? 1 : 0;
            case RIGHT_STICK_BUTTON:
                return gamepad.right_stick_button ? 1 : 0;
            case GUIDE:
                return gamepad.guide ? 1 : 0;
            case OPTIONS:
                return gamepad.options ? 1 : 0;
            case TOUCHPAD:
                return gamepad.touchpad ? 1 : 0;
            case TOUCHPAD_X:
                return gamepad.touchpad_finger_1_x;
            case TOUCHPAD_Y:
                return gamepad.touchpad_finger_1_y;
            default:
                return 0;
        }
    }

    public enum Control {
        TRIANGLE, CIRCLE, CROSS, SQUARE,
        DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT,
        LEFT_BUMPER, RIGHT_BUMPER,
        START, BACK, GUIDE, OPTIONS,
        LEFT_STICK_BUTTON, RIGHT_STICK_BUTTON,
        TOUCHPAD, TOUCHPAD_X, TOUCHPAD_Y,

        LEFT_TRIGGER, RIGHT_TRIGGER,
        LEFT_STICK_X, LEFT_STICK_Y,
        RIGHT_STICK_X, RIGHT_STICK_Y,

        UNKNOWN
    }
}