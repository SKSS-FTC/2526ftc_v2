package org.firstinspires.ftc.teamcode.input;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.HashMap;
import java.util.Map;

/**
 * Base controller class that extends Gamepad and provides mapping functionality
 */
public class Controller extends Gamepad {
    protected ControllerMapping mapping;
    protected ControllerSettings settings;
    protected Map<String, Boolean> previousButtonStates = new HashMap<>();

    /**
     * Creates a new controller with the specified gamepad and mapping
     *
     * @param gamepad The gamepad to wrap
     * @param mapping The mapping to use
     */
    public Controller(Gamepad gamepad, ControllerMapping mapping) {
        // Copy all gamepad values from the provided gamepad
        this.copy(gamepad);
        this.mapping = mapping;
        this.settings = new ControllerSettings();
        updatePreviousButtonStates();
    }

    /**
     * Creates a new controller with the specified gamepad, mapping, and settings
     *
     * @param gamepad  The gamepad to wrap
     * @param mapping  The mapping to use
     * @param settings The settings to use
     */
    public Controller(Gamepad gamepad, ControllerMapping mapping, ControllerSettings settings) {
        // Copy all gamepad values from the provided gamepad
        this.copy(gamepad);
        this.mapping = mapping;
        this.settings = settings;
        updatePreviousButtonStates();
    }

    /**
     * Updates the controller with the latest gamepad state
     *
     * @param gamepad The gamepad to copy state from
     */
    public void update(Gamepad gamepad) {
        this.copy(gamepad);
        updatePreviousButtonStates();
    }

    /**
     * Updates the previous button states for edge detection
     */
    protected void updatePreviousButtonStates() {
        for (String eventName : mapping.getEventNames()) {
            GamepadInput input = mapping.getInputForEvent(eventName);
            if (input != null && isButtonInput(input)) {
                previousButtonStates.put(eventName, isActive(eventName));
            }
        }
    }

    /**
     * Checks if an input is a button (not an axis)
     *
     * @param input The input to check
     * @return True if the input is a button, false otherwise
     */
    protected boolean isButtonInput(GamepadInput input) {
        switch (input) {
            case LEFT_TRIGGER:
            case RIGHT_TRIGGER:
            case LEFT_STICK_X:
            case LEFT_STICK_Y:
            case RIGHT_STICK_X:
            case RIGHT_STICK_Y:
                return false;
            default:
                return true;
        }
    }

    /**
     * Checks if the event is currently active
     *
     * @param eventName The name of the event to check
     * @return True if the event is active, false otherwise
     */
    public boolean isActive(String eventName) {
        GamepadInput input = mapping.getInputForEvent(eventName);
        if (input == null) {
            return false;
        }

        double value = getInputValue(input);
        return value > 0.1; // Small threshold for buttons and axes
    }

    /**
     * Checks if the event was just pressed this frame
     *
     * @param eventName The name of the event to check
     * @return True if the event was just pressed, false otherwise
     */
    public boolean isPressed(String eventName) {
        boolean previousState = previousButtonStates.getOrDefault(eventName, false);
        boolean currentState = isActive(eventName);
        return currentState && !previousState;
    }

    /**
     * Checks if the event was just released this frame
     *
     * @param eventName The name of the event to check
     * @return True if the event was just released, false otherwise
     */
    public boolean isReleased(String eventName) {
        boolean previousState = previousButtonStates.getOrDefault(eventName, false);
        boolean currentState = isActive(eventName);
        return !currentState && previousState;
    }

    /**
     * Gets the value of an event
     *
     * @param eventName The name of the event to get the value of
     * @return The value of the event, or 0.0 if not mapped or inactive
     */
    public double getValue(String eventName) {
        GamepadInput input = mapping.getInputForEvent(eventName);
        if (input == null) {
            return 0.0;
        }

        double rawValue = getInputValue(input);

        // Apply settings to process the input
        if (input == GamepadInput.LEFT_STICK_X) {
            return settings.processStickInput(rawValue, "leftStick", "X");
        } else if (input == GamepadInput.LEFT_STICK_Y) {
            return settings.processStickInput(rawValue, "leftStick", "Y");
        } else if (input == GamepadInput.RIGHT_STICK_X) {
            return settings.processStickInput(rawValue, "rightStick", "X");
        } else if (input == GamepadInput.RIGHT_STICK_Y) {
            return settings.processStickInput(rawValue, "rightStick", "Y");
        } else if (input == GamepadInput.LEFT_TRIGGER || input == GamepadInput.RIGHT_TRIGGER) {
            // Process trigger input with deadzone and sensitivity
            String triggerName = (input == GamepadInput.LEFT_TRIGGER) ? "leftTrigger" : "rightTrigger";
            double deadzone = settings.getDoubleSetting("triggerDeadzone", 0.05);

            if (rawValue < deadzone) {
                return 0.0;
            }

            double sensitivity = settings.getDoubleSetting("triggerSensitivity", 1.0);
            rawValue *= sensitivity;

            String curveType = settings.getStringSetting("triggerCurve", "linear");
            return settings.applyResponseCurve(rawValue, curveType);
        }

        return rawValue;
    }

    /**
     * Gets the raw value of an input
     *
     * @param input The input to get the value of
     * @return The value of the input
     */
    protected double getInputValue(GamepadInput input) {
        switch (input) {
            // Buttons
            case TRIANGLE:
                return triangle ? 1.0 : 0.0;
            case CIRCLE:
                return circle ? 1.0 : 0.0;
            case CROSS:
                return cross ? 1.0 : 0.0;
            case SQUARE:
                return square ? 1.0 : 0.0;
            case DPAD_UP:
                return dpad_up ? 1.0 : 0.0;
            case DPAD_DOWN:
                return dpad_down ? 1.0 : 0.0;
            case DPAD_LEFT:
                return dpad_left ? 1.0 : 0.0;
            case DPAD_RIGHT:
                return dpad_right ? 1.0 : 0.0;
            case LEFT_BUMPER:
                return left_bumper ? 1.0 : 0.0;
            case RIGHT_BUMPER:
                return right_bumper ? 1.0 : 0.0;
            case START:
                return start ? 1.0 : 0.0;
            case BACK:
                return back ? 1.0 : 0.0;
            case GUIDE:
                return guide ? 1.0 : 0.0;
            case OPTIONS:
                return options ? 1.0 : 0.0;
            case LEFT_STICK_BUTTON:
                return left_stick_button ? 1.0 : 0.0;
            case RIGHT_STICK_BUTTON:
                return right_stick_button ? 1.0 : 0.0;
            case TOUCHPAD:
                return touchpad ? 1.0 : 0.0;

            // Axes
            case LEFT_TRIGGER:
                return left_trigger;
            case RIGHT_TRIGGER:
                return right_trigger;
            case LEFT_STICK_X:
                return left_stick_x;
            case LEFT_STICK_Y:
                return left_stick_y;
            case RIGHT_STICK_X:
                return right_stick_x;
            case RIGHT_STICK_Y:
                return right_stick_y;

            default:
                return 0.0;
        }
    }

    /**
     * Gets the current mapping
     *
     * @return The current mapping
     */
    public ControllerMapping getMapping() {
        return mapping;
    }

    /**
     * Sets a new mapping
     *
     * @param mapping The new mapping to use
     */
    public void setMapping(ControllerMapping mapping) {
        this.mapping = mapping;
        // Reset previous button states
        previousButtonStates.clear();
        updatePreviousButtonStates();
    }

    /**
     * Gets the current settings
     *
     * @return The current settings
     */
    public ControllerSettings getSettings() {
        return settings;
    }

    /**
     * Sets new settings
     *
     * @param settings The new settings to use
     */
    public void setSettings(ControllerSettings settings) {
        this.settings = settings;
    }
}