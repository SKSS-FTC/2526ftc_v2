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

}