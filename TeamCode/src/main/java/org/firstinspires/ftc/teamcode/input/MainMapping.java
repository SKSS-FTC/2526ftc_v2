package org.firstinspires.ftc.teamcode.input;

import com.acmerobotics.dashboard.config.Config;

/**
 * Default mapping for the main controller
 */
@Config
public class MainMapping extends ControllerMapping {
    /**
     * Initializes default mappings for the main controller
     */
    @Override
    protected void initializeDefaultMappings() {
        // Movement controls
        mapEvent("moveForward", GamepadInput.LEFT_STICK_Y);
        mapEvent("moveSideways", GamepadInput.LEFT_STICK_X);
        mapEvent("rotate", GamepadInput.RIGHT_STICK_X);
        mapEvent("flipMovement", GamepadInput.GUIDE);

        // Directional movement controls
        mapEvent("moveUp", GamepadInput.DPAD_UP);
        mapEvent("moveDown", GamepadInput.DPAD_DOWN);
        mapEvent("moveLeft", GamepadInput.DPAD_LEFT);
        mapEvent("moveRight", GamepadInput.DPAD_RIGHT);
        mapEvent("rotateRight", GamepadInput.SQUARE);
        mapEvent("rotateLeft", GamepadInput.TRIANGLE);

        // Boost/brake controls
        mapEvent("boost", GamepadInput.RIGHT_TRIGGER);
        mapEvent("brake", GamepadInput.LEFT_TRIGGER);

        // Extensor controls
        mapEvent("extendHorizontal", GamepadInput.CIRCLE);
        mapEvent("retractHorizontal", GamepadInput.SQUARE);
        mapEvent("extendVertical", GamepadInput.RIGHT_BUMPER);
        mapEvent("retractVertical", GamepadInput.LEFT_BUMPER);

        // Add touchpad for driver assistance
        mapEvent("touchpad", GamepadInput.TOUCHPAD);

        // Common face buttons
        mapEvent("triangle", GamepadInput.TRIANGLE);
        mapEvent("circle", GamepadInput.CIRCLE);
        mapEvent("cross", GamepadInput.CROSS);
        mapEvent("square", GamepadInput.SQUARE);

        // Add guide button for special functions
        mapEvent("guide", GamepadInput.GUIDE);
    }
}