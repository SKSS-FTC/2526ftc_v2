package org.firstinspires.ftc.teamcode.input;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Represents an input event from a gamepad
 */
public class InputEvent {
    public final GamepadInput input;
    public final double value;
    public final Gamepad gamepad;
    public final GamepadSettings settings;

    public InputEvent(GamepadInput input, double value, Gamepad gamepad, GamepadSettings settings) {
        this.input = input;
        this.value = value;
        this.gamepad = gamepad;
        this.settings = settings;
    }
} 