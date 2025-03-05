package org.firstinspires.ftc.teamcode.input;

/**
 * Example of a custom main controller profile
 * Create more profiles like this for different drivers or competition
 * situations
 */
public class CustomMainProfile extends MainMapping {
    /**
     * Constructor initializes the custom mapping
     */
    public CustomMainProfile() {
        super();
        // Clear all existing mappings and set our custom ones
        clearMappings();
        initializeCustomMappings();
    }

    /**
     * Sets up the custom mappings for this profile
     */
    private void initializeCustomMappings() {
        // === Driver Forward Controls ===
        // This example swaps the stick controls (right stick for movement, left for
        // turning)
        mapEvent("moveForward", GamepadInput.RIGHT_STICK_Y);
        mapEvent("moveSideways", GamepadInput.RIGHT_STICK_X);
        mapEvent("rotate", GamepadInput.LEFT_STICK_X);

        // === Other Controls ===
        mapEvent("guide", GamepadInput.GUIDE);

        // === Vertical Slide Controls ===
        // In this profile, we're using opposite bumpers for extending/retracting
        mapEvent("extendVertical", GamepadInput.RIGHT_BUMPER);
        mapEvent("retractVertical", GamepadInput.LEFT_BUMPER);

        // === Face Buttons for Presets ===
        mapEvent("triangle", GamepadInput.TRIANGLE);
        mapEvent("cross", GamepadInput.CROSS);
        mapEvent("square", GamepadInput.SQUARE);
        mapEvent("circle", GamepadInput.CIRCLE);

        // === Other Controls ===
        // Add any other controls specific to this profile
    }
}