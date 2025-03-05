package org.firstinspires.ftc.teamcode.input;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Controller implementation for the sub gamepad
 */
public class SubController extends Controller {
    /**
     * Creates a new SubController with default mapping
     *
     * @param gamepad The gamepad to wrap
     */
    public SubController(Gamepad gamepad) {
        super(gamepad, new SubMapping());
    }

    /**
     * Creates a new SubController with custom mapping
     *
     * @param gamepad The gamepad to wrap
     * @param mapping The mapping to use
     */
    public SubController(Gamepad gamepad, SubMapping mapping) {
        super(gamepad, mapping);
    }

    /**
     * Gets the current mapping as a SubMapping
     *
     * @return The current mapping
     */
    @Override
    public SubMapping getMapping() {
        return (SubMapping) mapping;
    }

    // Convenience methods for common sub controller operations

    /**
     * Checks if claw toggle is pressed
     *
     * @return True if claw toggle is pressed
     */
    public boolean isClawTogglePressed() {
        return isPressed("clawToggle");
    }

    /**
     * Checks if intake in is active
     *
     * @return True if intake in is active
     */
    public boolean isIntakeInActive() {
        return isActive("intakeIn");
    }

    /**
     * Checks if intake out is active
     *
     * @return True if intake out is active
     */
    public boolean isIntakeOutActive() {
        return isActive("intakeOut");
    }

    /**
     * Gets the intake in value (0 to 1)
     *
     * @return The intake in value
     */
    public double getIntakeInValue() {
        return getValue("intakeIn");
    }

    /**
     * Gets the intake out value (0 to 1)
     *
     * @return The intake out value
     */
    public double getIntakeOutValue() {
        return getValue("intakeOut");
    }

    /**
     * Gets the rotator value (-1 to 1)
     *
     * @return The rotator value
     */
    public double getRotatorValue() {
        return getValue("rotator");
    }
}