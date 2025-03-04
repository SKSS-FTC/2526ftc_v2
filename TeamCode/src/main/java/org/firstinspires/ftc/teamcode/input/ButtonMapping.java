package org.firstinspires.ftc.teamcode.input;

import com.acmerobotics.dashboard.config.Config;

/**
 * Defines button mappings for robot controls
 *
 * @noinspection CanBeFinal
 */
@Config
public class ButtonMapping {
    // Movement controls
    public GamepadInput moveForward = GamepadInput.LEFT_STICK_Y;
    public GamepadInput moveSideways = GamepadInput.LEFT_STICK_X;
    public GamepadInput rotate = GamepadInput.RIGHT_STICK_X;
    public GamepadInput flipMovement = GamepadInput.GUIDE;

    // Directional movement controls
    public GamepadInput moveUp = GamepadInput.DPAD_UP;
    public GamepadInput moveDown = GamepadInput.DPAD_DOWN;
    public GamepadInput moveLeft = GamepadInput.DPAD_LEFT;
    public GamepadInput moveRight = GamepadInput.DPAD_RIGHT;
    public GamepadInput rotateRight = GamepadInput.X;
    public GamepadInput rotateLeft = GamepadInput.A;

    // Boost/brake controls
    public GamepadInput boost = GamepadInput.RIGHT_TRIGGER;
    public GamepadInput brake = GamepadInput.LEFT_TRIGGER;

    // Extensor controls
    public GamepadInput extendHorizontal = GamepadInput.B;
    public GamepadInput retractHorizontal = GamepadInput.X;
    public GamepadInput extendVerticalToChamber = GamepadInput.A;
    public GamepadInput extendVerticalToChamberPrep = GamepadInput.X;
    public GamepadInput extendVerticalToBasket = GamepadInput.Y;
    public GamepadInput retractVerticalToTransfer = GamepadInput.B;
    public GamepadInput extendVertical = GamepadInput.RIGHT_BUMPER;
    public GamepadInput retractVertical = GamepadInput.LEFT_BUMPER;

    // Claw controls
    public GamepadInput intakeIn = GamepadInput.LEFT_TRIGGER;
    public GamepadInput intakeOut = GamepadInput.RIGHT_TRIGGER;
    public GamepadInput intakeStop = GamepadInput.OPTIONS;
    public GamepadInput clawIn = GamepadInput.OPTIONS;
    public GamepadInput clawOut = GamepadInput.START;
    public GamepadInput clawToggle = GamepadInput.RIGHT_STICK_BUTTON;
    public GamepadInput rotator = GamepadInput.RIGHT_STICK_X;

    // Wrist controls
    public GamepadInput wristUp = GamepadInput.DPAD_LEFT;
    public GamepadInput wristDown = GamepadInput.DPAD_RIGHT;

    // Shoulder controls
    public GamepadInput shoulderUp = GamepadInput.LEFT_STICK_BUTTON;
    public GamepadInput shoulderDown = GamepadInput.RIGHT_STICK_BUTTON;

    // Linear actuator controls
    public GamepadInput linearActuatorExtend = GamepadInput.Y;
    public GamepadInput linearActuatorRetract = GamepadInput.A;

    // Ascend extensor controls
    public GamepadInput ascendExtensorExtend = GamepadInput.DPAD_RIGHT;
    public GamepadInput ascendExtensorRetract = GamepadInput.DPAD_LEFT;
    public GamepadInput ascendExtensorGround = GamepadInput.DPAD_DOWN;
    public GamepadInput ascendExtensorCeiling = GamepadInput.DPAD_UP;
} 