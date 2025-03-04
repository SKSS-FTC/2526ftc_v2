package org.firstinspires.ftc.teamcode.input;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Central manager for handling gamepad input and dispatching events
 */
public class InputManager {
    public final CombinedControllerProfile profile;
    private final Gamepad mainGamepad;
    private final Gamepad subGamepad;
    // Track previous button states
    private final Map<GamepadInput, Boolean> prevMainButtonStates = new HashMap<>();
    private final Map<GamepadInput, Boolean> prevSubButtonStates = new HashMap<>();

    // Event handlers
    private final Map<String, Consumer<InputEvent>> eventHandlers = new HashMap<>();

    /**
     * Creates a new InputManager with the given gamepads and profile
     */
    public InputManager(Gamepad main, Gamepad sub, CombinedControllerProfile profile) {
        this.mainGamepad = main;
        this.subGamepad = sub;
        this.profile = profile;

        // Initialize previous button states
        for (GamepadInput input : GamepadInput.values()) {
            if (!input.isAxis()) {
                prevMainButtonStates.put(input, false);
                prevSubButtonStates.put(input, false);
            }
        }
    }

    /**
     * Registers a handler for a specific input event
     */
    public void on(String eventName, Consumer<InputEvent> handler) {
        eventHandlers.put(eventName, handler);
    }

    /**
     * Updates input state and triggers events
     * Should be called once per loop iteration
     */
    public void update() {
        // Process main gamepad
        processGamepad(mainGamepad, profile.mainGamepadSettings, prevMainButtonStates, "main");

        // Process sub gamepad
        processGamepad(subGamepad, profile.subGamepadSettings, prevSubButtonStates, "sub");

        // Process movement inputs
        processMovementInputs();
    }

    /**
     * Process all inputs for a gamepad
     */
    private void processGamepad(Gamepad gamepad, GamepadSettings settings,
                                Map<GamepadInput, Boolean> prevStates, String prefix) {
        for (GamepadInput input : GamepadInput.values()) {
            if (input.isAxis()) {
                // Process axis
                double value = getAxisValue(gamepad, input);
                if (Math.abs(value) > settings.stickDeadzone) {
                    triggerEvent(prefix + "." + input.name(),
                            new InputEvent(input, value, gamepad, settings));
                }
            } else {
                // Process button
                boolean currentState = getButtonState(gamepad, input);
                boolean prevState = Boolean.TRUE.equals(prevStates.getOrDefault(input, false));

                if (currentState && !prevState) {
                    triggerEvent(prefix + "." + input.name() + ".pressed",
                            new InputEvent(input, 1.0, gamepad, settings));
                } else if (currentState) {
                    triggerEvent(prefix + "." + input.name() + ".held",
                            new InputEvent(input, 1.0, gamepad, settings));
                } else if (prevState) {
                    triggerEvent(prefix + "." + input.name() + ".released",
                            new InputEvent(input, 0.0, gamepad, settings));
                }

                prevStates.put(input, currentState);
            }
        }
    }

    /**
     * Process movement-specific inputs
     */
    private void processMovementInputs() {
        GamepadSettings settings = profile.mainGamepadSettings;

        // Get movement values
        double leftStickY = applyDeadzone(
                getAxisValue(mainGamepad, GamepadInput.LEFT_STICK_Y),
                settings.stickDeadzone);
        double leftStickX = applyDeadzone(
                getAxisValue(mainGamepad, GamepadInput.LEFT_STICK_X),
                settings.stickDeadzone);
        double rightStickX = applyDeadzone(
                getAxisValue(mainGamepad, GamepadInput.RIGHT_STICK_X),
                settings.stickDeadzone);

        // Apply sensitivities and inversion
        leftStickY *= settings.invertYAxis ? -1 : 1;
        leftStickX *= settings.invertXAxis ? -1 : 1;

        // Calculate movement values
        double forward = -leftStickY * settings.leftStickSensitivity;
        double strafe = leftStickX * settings.leftStickSensitivity;
        double rotation = rightStickX * settings.rightStickSensitivity;

        // Add dpad movement
        if (getButtonState(mainGamepad, GamepadInput.DPAD_UP)) {
            forward += settings.dpadMovementSpeed;
        }
        if (getButtonState(mainGamepad, GamepadInput.DPAD_DOWN)) {
            forward -= settings.dpadMovementSpeed;
        }
        if (getButtonState(mainGamepad, GamepadInput.DPAD_RIGHT)) {
            strafe += settings.dpadMovementSpeed;
        }
        if (getButtonState(mainGamepad, GamepadInput.DPAD_LEFT)) {
            strafe -= settings.dpadMovementSpeed;
        }

        // Handle rotation from buttons if configured
        if (!settings.useRightStickRotation) {
            if (getButtonState(mainGamepad, settings.buttonMapping.rotateRight)) {
                rotation += settings.bumperRotationSpeed;
            }
            if (getButtonState(mainGamepad, settings.buttonMapping.rotateLeft)) {
                rotation -= settings.bumperRotationSpeed;
            }
        }

        // Apply boost/brake
        double boost = settings.applyBoostCurve(
                getAxisValue(mainGamepad, settings.buttonMapping.boost));
        double brake = settings.applyBoostCurve(
                getAxisValue(mainGamepad, settings.buttonMapping.brake));

        double powerMultiplier = 1 + (boost * 2) - brake;

        // Trigger movement event with calculated values
        MovementInputEvent movementEvent = new MovementInputEvent(
                forward * powerMultiplier,
                strafe * powerMultiplier,
                rotation * powerMultiplier,
                mainGamepad,
                settings
        );

        triggerEvent("movement", movementEvent);
    }

    /**
     * Triggers an event with the given name and data
     */
    private void triggerEvent(String eventName, InputEvent event) {
        Consumer<InputEvent> handler = eventHandlers.get(eventName);
        if (handler != null) {
            handler.accept(event);
        }
    }

    /**
     * Applies deadzone to an axis value
     */
    private double applyDeadzone(double value, double deadzone) {
        return Math.abs(value) > deadzone ? value : 0;
    }

    /**
     * Gets the state of a button from a gamepad
     */
    private boolean getButtonState(Gamepad gamepad, GamepadInput input) {
        switch (input) {
            case A:
                return gamepad.a;
            case B:
                return gamepad.b;
            case X:
                return gamepad.x;
            case Y:
                return gamepad.y;
            case DPAD_UP:
                return gamepad.dpad_up;
            case DPAD_DOWN:
                return gamepad.dpad_down;
            case DPAD_LEFT:
                return gamepad.dpad_left;
            case DPAD_RIGHT:
                return gamepad.dpad_right;
            case LEFT_BUMPER:
                return gamepad.left_bumper;
            case RIGHT_BUMPER:
                return gamepad.right_bumper;
            case START:
                return gamepad.start;
            case BACK:
                return gamepad.back;
            case LEFT_STICK_BUTTON:
                return gamepad.left_stick_button;
            case RIGHT_STICK_BUTTON:
                return gamepad.right_stick_button;
            case GUIDE:
                return gamepad.guide;
            case OPTIONS:
                return gamepad.options;
            case LEFT_TRIGGER:
                return gamepad.left_trigger > profile.mainGamepadSettings.triggerThreshold;
            case RIGHT_TRIGGER:
                return gamepad.right_trigger > profile.mainGamepadSettings.triggerThreshold;
            default:
                return false;
        }
    }

    /**
     * Gets the value of an axis from a gamepad
     */
    private double getAxisValue(Gamepad gamepad, GamepadInput axis) {
        switch (axis) {
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
            default:
                return 0;
        }
    }
} 