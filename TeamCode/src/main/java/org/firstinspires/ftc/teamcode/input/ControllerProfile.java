package org.firstinspires.ftc.teamcode.input;

/**
 * Represents a named controller profile with a specific mapping
 */
public class ControllerProfile {
    private final String name;
    private final ControllerMapping mapping;

    /**
     * Creates a new controller profile
     *
     * @param name    The name of the profile
     * @param mapping The mapping for this profile
     */
    public ControllerProfile(String name, ControllerMapping mapping) {
        this.name = name;
        this.mapping = mapping;
    }

    /**
     * Gets the name of this profile
     *
     * @return The profile name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the mapping for this profile
     *
     * @return The controller mapping
     */
    public ControllerMapping getMapping() {
        return mapping;
    }
}