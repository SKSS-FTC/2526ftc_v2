package org.firstinspires.ftc.teamcode.input;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Specialized event for movement inputs
 */
public class MovementInputEvent extends InputEvent {
    public final double forward;
    public final double strafe;
    public final double rotation;

    public MovementInputEvent(double forward, double strafe, double rotation,
                              Gamepad gamepad, GamepadSettings settings) {
        super(null, 0, gamepad, settings);
        this.forward = forward;
        this.strafe = strafe;
        this.rotation = rotation;
    }
}