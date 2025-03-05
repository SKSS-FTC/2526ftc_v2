package org.firstinspires.ftc.teamcode.input;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores settings for controller profiles, such as sensitivity, deadzones,
 * and input processing configurations.
 */
public class ControllerSettings {
    // Maps setting names to double values
    private final Map<String, Double> doubleSettings = new HashMap<>();

    // Maps setting names to string values
    private final Map<String, String> stringSettings = new HashMap<>();

    // Maps setting names to boolean values
    private final Map<String, Boolean> booleanSettings = new HashMap<>();

    /**
     * Creates a new controller settings object with default values
     */
    public ControllerSettings() {
        initializeDefaultSettings();
    }

    /**
     * Initialize default settings
     * Override this to customize default settings
     */
    protected void initializeDefaultSettings() {
        // Double settings
        setDoubleSetting("leftStickDeadzone", 0.05);
        setDoubleSetting("rightStickDeadzone", 0.05);
        setDoubleSetting("triggerDeadzone", 0.05);
        setDoubleSetting("stickSensitivity", 1.0);
        setDoubleSetting("triggerSensitivity", 1.0);

        // String settings
        setStringSetting("leftStickCurve", "linear"); // linear, quadratic, cubic, etc.
        setStringSetting("rightStickCurve", "linear");
        setStringSetting("triggerCurve", "linear");

        // Boolean settings
        setBooleanSetting("invertLeftY", false);
        setBooleanSetting("invertRightY", false);
        setBooleanSetting("invertLeftX", false);
        setBooleanSetting("invertRightX", false);
    }

    /**
     * Set a double setting
     *
     * @param name  The setting name
     * @param value The setting value
     * @return This instance for chaining
     */
    public ControllerSettings setDoubleSetting(String name, double value) {
        doubleSettings.put(name, value);
        return this;
    }

    /**
     * Get a double setting
     *
     * @param name         The setting name
     * @param defaultValue The default value to return if setting does not exist
     * @return The setting value, or defaultValue if not found
     */
    public double getDoubleSetting(String name, double defaultValue) {
        return doubleSettings.getOrDefault(name, defaultValue);
    }

    /**
     * Get a double setting with a default of 0.0
     *
     * @param name The setting name
     * @return The setting value, or 0.0 if not found
     */
    public double getDoubleSetting(String name) {
        return getDoubleSetting(name, 0.0);
    }

    /**
     * Set a string setting
     *
     * @param name  The setting name
     * @param value The setting value
     * @return This instance for chaining
     */
    public ControllerSettings setStringSetting(String name, String value) {
        stringSettings.put(name, value);
        return this;
    }

    /**
     * Get a string setting
     *
     * @param name         The setting name
     * @param defaultValue The default value to return if setting does not exist
     * @return The setting value, or defaultValue if not found
     */
    public String getStringSetting(String name, String defaultValue) {
        return stringSettings.getOrDefault(name, defaultValue);
    }

    /**
     * Get a string setting with a default of empty string
     *
     * @param name The setting name
     * @return The setting value, or empty string if not found
     */
    public String getStringSetting(String name) {
        return getStringSetting(name, "");
    }

    /**
     * Set a boolean setting
     *
     * @param name  The setting name
     * @param value The setting value
     * @return This instance for chaining
     */
    public ControllerSettings setBooleanSetting(String name, boolean value) {
        booleanSettings.put(name, value);
        return this;
    }

    /**
     * Get a boolean setting
     *
     * @param name         The setting name
     * @param defaultValue The default value to return if setting does not exist
     * @return The setting value, or defaultValue if not found
     */
    public boolean getBooleanSetting(String name, boolean defaultValue) {
        return booleanSettings.getOrDefault(name, defaultValue);
    }

    /**
     * Get a boolean setting with a default of false
     *
     * @param name The setting name
     * @return The setting value, or false if not found
     */
    public boolean getBooleanSetting(String name) {
        return getBooleanSetting(name, false);
    }

    /**
     * Apply a response curve to an input value based on the curve type
     *
     * @param input     The input value (typically from -1.0 to 1.0)
     * @param curveType The curve type (linear, quadratic, cubic, etc.)
     * @return The processed input value
     */
    public double applyResponseCurve(double input, String curveType) {
        switch (curveType) {
            case "quadratic":
                return BoostCurves.quadratic(input);
            case "cubic":
                return BoostCurves.cubic(input);
            case "squareRoot":
                return BoostCurves.squareRoot(input);
            case "smooth":
                return BoostCurves.smooth(input);
            case "exponential":
                return BoostCurves.exponential(input);
            case "linear":
            default:
                return BoostCurves.linear(input);
        }
    }

    /**
     * Process a raw stick input value applying deadzone, sensitivity and curve
     * settings
     *
     * @param input     The raw input value
     * @param stickName The stick name (leftStick, rightStick)
     * @param axis      The axis (X or Y)
     * @return The processed input value
     */
    public double processStickInput(double input, String stickName, String axis) {
        // Apply deadzone
        String deadzoneKey = stickName + "Deadzone";
        double deadzone = getDoubleSetting(deadzoneKey, 0.05);

        if (Math.abs(input) < deadzone) {
            return 0.0;
        }

        // Apply inversion if needed
        String invertKey = "invert" + stickName.substring(0, 1).toUpperCase() +
                stickName.substring(1) + axis;
        if (getBooleanSetting(invertKey, false)) {
            input = -input;
        }

        // Apply sensitivity
        String sensitivityKey = stickName + "Sensitivity";
        double sensitivity = getDoubleSetting(sensitivityKey, 1.0);
        input *= sensitivity;

        // Apply response curve
        String curveKey = stickName + "Curve";
        String curveType = getStringSetting(curveKey, "linear");

        return applyResponseCurve(input, curveType);
    }
}