package org.firstinspires.ftc.teamcode.input;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages controller profiles for both main and sub controllers
 */
public class ControllerProfileManager {
    private final List<ControllerProfile> mainProfiles = new ArrayList<>();
    private final List<ControllerProfile> subProfiles = new ArrayList<>();

    private int activeMainProfileIndex = 0;
    private int activeSubProfileIndex = 0;

    /**
     * Creates a new profile manager with default profiles
     */
    public ControllerProfileManager() {
        // Add default main profile
        mainProfiles.add(new ControllerProfile("Default", new MainMapping()));

        // Add default sub profile
        subProfiles.add(new ControllerProfile("Default", new SubMapping()));

        // Initialize with default profiles
        initializeDefaultProfiles();
    }

    /**
     * Initializes the default set of profiles
     * Override this method to add your custom profiles
     */
    protected void initializeDefaultProfiles() {
        // Add our custom profiles
        mainProfiles.add(new ControllerProfile("Custom", new CustomMainProfile()));
        subProfiles.add(new ControllerProfile("Custom", new CustomSubProfile()));

        // Add a second main profile with alternative mappings (the original example)
        MainMapping alternativeMain = new MainMapping();
        // Clear default mappings and set custom ones
        alternativeMain.clearMappings();

        // Example of an alternative mapping where controls are different
        alternativeMain.mapEvent("moveForward", GamepadInput.RIGHT_STICK_Y);
        alternativeMain.mapEvent("moveSideways", GamepadInput.RIGHT_STICK_X);
        alternativeMain.mapEvent("rotate", GamepadInput.LEFT_STICK_X);

        // Add other mappings as needed
        alternativeMain.mapEvent("guide", GamepadInput.GUIDE);
        alternativeMain.mapEvent("extendVertical", GamepadInput.LEFT_BUMPER);
        alternativeMain.mapEvent("retractVertical", GamepadInput.RIGHT_BUMPER);
        alternativeMain.mapEvent("triangle", GamepadInput.TRIANGLE);
        alternativeMain.mapEvent("cross", GamepadInput.CROSS);
        alternativeMain.mapEvent("square", GamepadInput.SQUARE);
        alternativeMain.mapEvent("circle", GamepadInput.CIRCLE);

        mainProfiles.add(new ControllerProfile("Alternative", alternativeMain));

        // Add a second sub profile with alternative mappings
        SubMapping alternativeSub = new SubMapping();
        alternativeSub.clearMappings();

        // Example of alternative sub controller mapping
        alternativeSub.mapEvent("intakeIn", GamepadInput.RIGHT_TRIGGER);
        alternativeSub.mapEvent("intakeOut", GamepadInput.LEFT_TRIGGER);
        alternativeSub.mapEvent("moveLeft", GamepadInput.DPAD_LEFT);
        alternativeSub.mapEvent("moveRight", GamepadInput.DPAD_RIGHT);

        subProfiles.add(new ControllerProfile("Alternative", alternativeSub));
    }

    /**
     * Adds a main controller profile
     *
     * @param profile The profile to add
     */
    public void addMainProfile(ControllerProfile profile) {
        mainProfiles.add(profile);
    }

    /**
     * Adds a sub controller profile
     *
     * @param profile The profile to add
     */
    public void addSubProfile(ControllerProfile profile) {
        subProfiles.add(profile);
    }

    /**
     * Gets the current active main controller profile
     *
     * @return The active main profile
     */
    public ControllerProfile getActiveMainProfile() {
        return mainProfiles.get(activeMainProfileIndex);
    }

    /**
     * Gets the current active sub controller profile
     *
     * @return The active sub profile
     */
    public ControllerProfile getActiveSubProfile() {
        return subProfiles.get(activeSubProfileIndex);
    }

    /**
     * Cycles to the next main profile
     *
     * @return The new active profile
     */
    public ControllerProfile cycleMainProfile() {
        activeMainProfileIndex = (activeMainProfileIndex + 1) % mainProfiles.size();
        return getActiveMainProfile();
    }

    /**
     * Cycles to the next sub profile
     *
     * @return The new active profile
     */
    public ControllerProfile cycleSubProfile() {
        activeSubProfileIndex = (activeSubProfileIndex + 1) % subProfiles.size();
        return getActiveSubProfile();
    }

    /**
     * Gets all available main profiles
     *
     * @return List of main profiles
     */
    public List<ControllerProfile> getMainProfiles() {
        return new ArrayList<>(mainProfiles);
    }

    /**
     * Gets all available sub profiles
     *
     * @return List of sub profiles
     */
    public List<ControllerProfile> getSubProfiles() {
        return new ArrayList<>(subProfiles);
    }

    /**
     * Sets the active main profile by index
     *
     * @param index The index of the profile to set active
     * @return The newly active profile
     */
    public ControllerProfile setMainProfileIndex(int index) {
        if (index >= 0 && index < mainProfiles.size()) {
            activeMainProfileIndex = index;
        }
        return getActiveMainProfile();
    }

    /**
     * Sets the active sub profile by index
     *
     * @param index The index of the profile to set active
     * @return The newly active profile
     */
    public ControllerProfile setSubProfileIndex(int index) {
        if (index >= 0 && index < subProfiles.size()) {
            activeSubProfileIndex = index;
        }
        return getActiveSubProfile();
    }
}