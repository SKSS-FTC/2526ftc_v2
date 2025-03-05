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
}