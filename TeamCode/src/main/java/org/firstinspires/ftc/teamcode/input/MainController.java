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
     * Creates a new MainController with the provided mapping
     *
     * @param gamepad The gamepad to wrap
     * @param mapping The mapping to use
     */
    public MainController(Gamepad gamepad, MainMapping mapping) {
        super(gamepad, mapping);
    }

    /**
     * Creates a new MainController with the provided mapping and settings
     *
     * @param gamepad  The gamepad to wrap
     * @param mapping  The mapping to use
     * @param settings The settings to use
     */
    public MainController(Gamepad gamepad, MainMapping mapping, ControllerSettings settings) {
        super(gamepad, mapping, settings);
    }

    /**
     * Gets the current mapping
     *
     * @return The current mapping
     */
    @Override
    public MainMapping getMapping() {
        return (MainMapping) mapping;
    }
}