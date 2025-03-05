package org.firstinspires.ftc.teamcode.input;

/**
 * Represents a named controller profile with a specific mapping and settings
 */
public class ControllerProfile {
    private final String name;
    private final ControllerMapping mapping;
    private final ControllerSettings settings;

    /**
     * Creates a new controller profile
     *
     * @param name    The name of the profile
     * @param mapping The mapping for this profile
     */
    public ControllerProfile(String name, ControllerMapping mapping) {
        this(name, mapping, new ControllerSettings());
    }

    /**
     * Creates a new controller profile with custom settings
     *
     * @param name     The name of the profile
     * @param mapping  The mapping for this profile
     * @param settings The settings for this profile
     */
    public ControllerProfile(String name, ControllerMapping mapping, ControllerSettings settings) {
        this.name = name;
        this.mapping = mapping;
        this.settings = settings;
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

    /**
     * Gets the settings for this profile
     *
     * @return The controller settings
     */
    public ControllerSettings getSettings() {
        return settings;
    }
}