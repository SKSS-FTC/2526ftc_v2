package org.firstinspires.ftc.teamcode.input;

import com.acmerobotics.dashboard.config.Config;

/**
 * Configurable settings for gamepad behavior
 *
 * @noinspection CanBeFinal
 */
@Config
public class GamepadSettings {
    /**
     * Sensitivity multiplier for left stick input
     */
    public double leftStickSensitivity = 1.0;

    /**
     * Speed for dpad-based absolute movement, from 0 to 1
     */
    public double dpadMovementSpeed = 0.3;

    /**
     * Threshold for trigger inputs
     */
    public double triggerThreshold = 0.1;

    /**
     * Deadzone for stick inputs to prevent drift
     */
    public double stickDeadzone = 0.05;

    /**
     * Sensitivity multiplier for right stick input
     */
    public double rightStickSensitivity = 0.7;

    /**
     * Bumper rotation speed
     */
    public double bumperRotationSpeed = 0.8;

    /**
     * Whether to invert Y axis controls
     */
    public boolean invertYAxis = false;

    /**
     * Whether to invert X axis controls
     */
    public boolean invertXAxis = false;

    /**
     * Whether to use right stick for rotation instead of bumpers
     */
    public boolean useRightStickRotation = true;

    /**
     * Whether to use incremental movement for horizontal slide
     */
    public boolean incrementalHorizontal = false;

    /**
     * Whether to use incremental movement for vertical slide
     */
    public boolean incrementalVertical = false;

    /**
     * The button mapping configuration for this gamepad
     */
    public ButtonMapping buttonMapping = new ButtonMapping();

    /**
     * Applies a boost curve to input values
     */
    public double applyBoostCurve(double input) {
        // Default implementation: linear
        return Math.max(0, Math.min(1, input));
    }
}