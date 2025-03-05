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
        // Claw controls
        mapEvent("intakeIn", GamepadInput.LEFT_TRIGGER);
        mapEvent("intakeOut", GamepadInput.RIGHT_TRIGGER);
        mapEvent("intakeStop", GamepadInput.OPTIONS);
        mapEvent("clawIn", GamepadInput.OPTIONS);
        mapEvent("clawOut", GamepadInput.START);
        mapEvent("clawToggle", GamepadInput.RIGHT_STICK_BUTTON);
        mapEvent("rotator", GamepadInput.RIGHT_STICK_X);

        // Wrist controls
        mapEvent("wristUp", GamepadInput.DPAD_LEFT);
        mapEvent("wristDown", GamepadInput.DPAD_RIGHT);

        // Shoulder controls
        mapEvent("shoulderUp", GamepadInput.LEFT_STICK_BUTTON);
        mapEvent("shoulderDown", GamepadInput.RIGHT_STICK_BUTTON);

        // Linear actuator controls
        mapEvent("linearActuatorExtend", GamepadInput.CIRCLE);
        mapEvent("linearActuatorRetract", GamepadInput.SQUARE);

        // Ascend extensor controls
        mapEvent("ascendExtensorExtend", GamepadInput.DPAD_RIGHT);
        mapEvent("ascendExtensorRetract", GamepadInput.DPAD_LEFT);
        mapEvent("ascendExtensorGround", GamepadInput.DPAD_DOWN);
        mapEvent("ascendExtensorCeiling", GamepadInput.DPAD_UP);
    }
}