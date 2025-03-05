package org.firstinspires.ftc.teamcode.input;

/**
 * Example of a custom sub controller profile
 * Create more profiles like this for different operators or competition
 * situations
 */
public class CustomSubProfile extends SubMapping {
    /**
     * Constructor initializes the custom mapping
     */
    public CustomSubProfile() {
        super();
        // Clear all existing mappings and set our custom ones
        clearMappings();
        initializeCustomMappings();
    }

    /**
     * Sets up the custom mappings for this profile
     */
    private void initializeCustomMappings() {
        // === Intake Controls ===
        // Swapped triggers for intake operations
        mapEvent("intakeIn", GamepadInput.RIGHT_TRIGGER);
        mapEvent("intakeOut", GamepadInput.LEFT_TRIGGER);

        // === Wrist Controls ===
        // Using DPAD up/down for wrist instead of left/right
        mapEvent("wristUp", GamepadInput.DPAD_UP);
        mapEvent("wristDown", GamepadInput.DPAD_DOWN);

        // === Directional Movement Controls ===
        mapEvent("moveUp", GamepadInput.DPAD_UP);
        mapEvent("moveDown", GamepadInput.DPAD_DOWN);
        mapEvent("moveLeft", GamepadInput.DPAD_LEFT);
        mapEvent("moveRight", GamepadInput.DPAD_RIGHT);

        // === Face Buttons ===
        mapEvent("horizontalExtend", GamepadInput.TRIANGLE);
        mapEvent("horizontalRetract", GamepadInput.CROSS);

        // === Other Controls ===
        // Add other controls for your specific setup
    }
}