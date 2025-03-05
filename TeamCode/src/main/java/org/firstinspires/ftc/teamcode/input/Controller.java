package org.firstinspires.ftc.teamcode.input;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.HashMap;
import java.util.Map;

/**
 * Base controller class that extends Gamepad and provides mapping functionality
 */
public class Controller extends Gamepad {
    protected ControllerMapping mapping;
    protected Map<String, Boolean> previousButtonStates = new HashMap<>();

    /**
     * Creates a new Controller with the given mapping
     *
     * @param gamepad The gamepad to wrap
     * @param mapping The mapping to use
     */
    public Controller(Gamepad gamepad, ControllerMapping mapping) {
        // Copy all gamepad values from the provided gamepad
        this.copy(gamepad);
        this.mapping = mapping;

        // Initialize previous button states
        for (String eventName : mapping.getEventNames()) {
            previousButtonStates.put(eventName, false);
        }
    }

    /**
     * Updates the controller state from the provided gamepad
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
            previousButtonStates.put(eventName, isActive(eventName));
        }
    }

    /**
     * Checks if a specific event is currently active
     *
     * @param eventName The name of the event to check
     * @return True if the event is active, false otherwise
     */
    public boolean isActive(String eventName) {
        GamepadInput input = mapping.getInputForEvent(eventName);
        if (input == null) {
            return false;
        }

        return getInputValue(input) > 0.5;
    }

    /**
     * Checks if a specific event was just pressed (rising edge)
     *
     * @param eventName The name of the event to check
     * @return True if the event was just pressed, false otherwise
     */
    public boolean isPressed(String eventName) {
        return isActive(eventName) && Boolean.FALSE.equals(previousButtonStates.getOrDefault(eventName, false));
    }

    /**
     * Checks if a specific event was just released (falling edge)
     *
     * @param eventName The name of the event to check
     * @return True if the event was just released, false otherwise
     */
    public boolean isReleased(String eventName) {
        return !isActive(eventName) && Boolean.TRUE.equals(previousButtonStates.getOrDefault(eventName, false));
    }

    /**
     * Gets the value of a specific event (useful for analog inputs)
     *
     * @param eventName The name of the event to check
     * @return The value of the event (0-1 for buttons, -1 to 1 for axes)
     */
    public double getValue(String eventName) {
        GamepadInput input = mapping.getInputForEvent(eventName);
        if (input == null) {
            return 0.0;
        }

        return getInputValue(input);
    }

    /**
     * Gets the value of a specific input
     *
     * @param input The input to check
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
        for (String eventName : mapping.getEventNames()) {
            previousButtonStates.put(eventName, false);
        }
    }
}