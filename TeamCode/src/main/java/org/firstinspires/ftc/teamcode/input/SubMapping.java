package org.firstinspires.ftc.teamcode.input;

import com.acmerobotics.dashboard.config.Config;

/**
 * Default mapping for the sub controller
 */
@Config
public class SubMapping extends ControllerMapping {
    /**
     * Initializes default mappings for the sub controller
     */
    @Override
    protected void initializeDefaultMappings() {
        // Intake controls
        mapEvent("intakeIn", GamepadInput.LEFT_TRIGGER);
        mapEvent("intakeOut", GamepadInput.RIGHT_TRIGGER);
        mapEvent("closeClaw", GamepadInput.RIGHT_BUMPER);
        mapEvent("openClaw", GamepadInput.LEFT_BUMPER);

        // Wrist controls
        mapEvent("wristHorizontal", GamepadInput.DPAD_LEFT);
        mapEvent("wristVertical", GamepadInput.DPAD_RIGHT);

        // Horizontal slide controls
        mapEvent("extendHorizontal", GamepadInput.DPAD_UP);
        mapEvent("retractHorizontal", GamepadInput.DPAD_DOWN);
        mapEvent("horizontalSlide", GamepadInput.RIGHT_STICK_Y);

        // Face buttons
        mapEvent("triangle", GamepadInput.TRIANGLE);
        mapEvent("circle", GamepadInput.CIRCLE);
        mapEvent("cross", GamepadInput.CROSS);
        mapEvent("square", GamepadInput.SQUARE);

        // Advanced controls
        mapEvent("rightStickButton", GamepadInput.RIGHT_STICK_BUTTON);
        mapEvent("leftStickButton", GamepadInput.LEFT_STICK_BUTTON);
    }
}