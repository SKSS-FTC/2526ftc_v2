package org.firstinspires.ftc.teamcode.input;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Controller implementation for the main gamepad
 */
public class MainController extends Controller {
    /**
     * Creates a new MainController with default mapping
     *
     * @param gamepad The gamepad to wrap
     */
    public MainController(Gamepad gamepad) {
        super(gamepad, new MainMapping());
    }

    /**
     * Creates a new MainController with custom mapping
     *
     * @param gamepad The gamepad to wrap
     * @param mapping The mapping to use
     */
    public MainController(Gamepad gamepad, MainMapping mapping) {
        super(gamepad, mapping);
    }

    /**
     * Gets the current mapping as a MainMapping
     *
     * @return The current mapping
     */
    @Override
    public MainMapping getMapping() {
        return (MainMapping) mapping;
    }

    // Convenience methods for common main controller operations

    /**
     * Gets the movement forward value (-1 to 1)
     *
     * @return The movement forward value
     */
    public double getMoveForward() {
        return -getValue("moveForward"); // Inverted because forward is negative on the Y axis
    }

    /**
     * Gets the movement sideways value (-1 to 1)
     *
     * @return The movement sideways value
     */
    public double getMoveSideways() {
        return getValue("moveSideways");
    }

    /**
     * Gets the rotation value (-1 to 1)
     *
     * @return The rotation value
     */
    public double getRotation() {
        return getValue("rotate");
    }

    /**
     * Checks if boost is active
     *
     * @return True if boost is active
     */
    public boolean isBoostActive() {
        return isActive("boost");
    }

    /**
     * Checks if brake is active
     *
     * @return True if brake is active
     */
    public boolean isBrakeActive() {
        return isActive("brake");
    }

    /**
     * Gets the boost value (0 to 1)
     *
     * @return The boost value
     */
    public double getBoostValue() {
        return getValue("boost");
    }
}