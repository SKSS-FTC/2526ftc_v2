package org.firstinspires.ftc.teamcode.input;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages controller profiles for driver preferences
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
     * Initialize default profiles including alternate configurations
     * Override this to add custom profiles
     */
    protected void initializeDefaultProfiles() {
        // Create a custom main profile
        MainMapping customMain = new MainMapping();
        customMain.clearMappings();

        // Custom main mappings
        customMain.mapEvent("moveForward", GamepadInput.RIGHT_STICK_Y);
        customMain.mapEvent("moveSideways", GamepadInput.RIGHT_STICK_X);
        customMain.mapEvent("rotate", GamepadInput.LEFT_STICK_X);

        // Add other mappings
        customMain.mapEvent("guide", GamepadInput.GUIDE);
        customMain.mapEvent("extendVertical", GamepadInput.LEFT_BUMPER);
        customMain.mapEvent("retractVertical", GamepadInput.RIGHT_BUMPER);
        customMain.mapEvent("triangle", GamepadInput.TRIANGLE);
        customMain.mapEvent("cross", GamepadInput.CROSS);
        customMain.mapEvent("square", GamepadInput.SQUARE);
        customMain.mapEvent("circle", GamepadInput.CIRCLE);

        // Create settings with custom values
        ControllerSettings customMainSettings = new ControllerSettings();
        customMainSettings.setDoubleSetting("leftStickDeadzone", 0.08);
        customMainSettings.setDoubleSetting("rightStickDeadzone", 0.08);
        customMainSettings.setDoubleSetting("stickSensitivity", 1.2);
        customMainSettings.setStringSetting("leftStickCurve", "cubic");
        customMainSettings.setStringSetting("rightStickCurve", "quadratic");
        customMainSettings.setBooleanSetting("invertLeftY", true);

        mainProfiles.add(new ControllerProfile("Custom", customMain, customMainSettings));

        // Create a custom sub profile
        SubMapping customSub = new SubMapping();
        customSub.clearMappings();

        // Custom sub mappings
        customSub.mapEvent("intakeIn", GamepadInput.RIGHT_TRIGGER);
        customSub.mapEvent("intakeOut", GamepadInput.LEFT_TRIGGER);
        customSub.mapEvent("wristUp", GamepadInput.DPAD_UP);
        customSub.mapEvent("wristDown", GamepadInput.DPAD_DOWN);
        customSub.mapEvent("moveLeft", GamepadInput.DPAD_LEFT);
        customSub.mapEvent("moveRight", GamepadInput.DPAD_RIGHT);
        customSub.mapEvent("horizontalExtend", GamepadInput.TRIANGLE);
        customSub.mapEvent("horizontalRetract", GamepadInput.CROSS);

        // Create settings with custom values
        ControllerSettings customSubSettings = new ControllerSettings();
        customSubSettings.setDoubleSetting("triggerDeadzone", 0.1);
        customSubSettings.setDoubleSetting("triggerSensitivity", 1.5);
        customSubSettings.setStringSetting("triggerCurve", "exponential");

        subProfiles.add(new ControllerProfile("Custom", customSub, customSubSettings));

        // Add a second main profile with alternative mappings and settings
        MainMapping alternativeMain = new MainMapping();
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

        // Create settings with smooth response curves
        ControllerSettings smoothSettings = new ControllerSettings();
        smoothSettings.setStringSetting("leftStickCurve", "smooth");
        smoothSettings.setStringSetting("rightStickCurve", "smooth");
        smoothSettings.setDoubleSetting("stickSensitivity", 0.8);

        mainProfiles.add(new ControllerProfile("Alternative", alternativeMain, smoothSettings));

        // Add a second sub profile with alternative mappings
        SubMapping alternativeSub = new SubMapping();
        alternativeSub.clearMappings();

        // Example of alternative sub controller mapping
        alternativeSub.mapEvent("intakeIn", GamepadInput.RIGHT_TRIGGER);
        alternativeSub.mapEvent("intakeOut", GamepadInput.LEFT_TRIGGER);
        alternativeSub.mapEvent("moveLeft", GamepadInput.DPAD_LEFT);
        alternativeSub.mapEvent("moveRight", GamepadInput.DPAD_RIGHT);

        // Create square root response curve settings for more precision at lower inputs
        ControllerSettings precisionSettings = new ControllerSettings();
        precisionSettings.setStringSetting("triggerCurve", "squareRoot");
        precisionSettings.setDoubleSetting("triggerSensitivity", 1.2);

        subProfiles.add(new ControllerProfile("Alternative", alternativeSub, precisionSettings));

        // Add a high sensitivity profile for advanced users
        MainMapping expertMain = new MainMapping();

        ControllerSettings expertSettings = new ControllerSettings();
        expertSettings.setDoubleSetting("leftStickDeadzone", 0.03);
        expertSettings.setDoubleSetting("rightStickDeadzone", 0.03);
        expertSettings.setDoubleSetting("stickSensitivity", 1.5);
        expertSettings.setStringSetting("leftStickCurve", "exponential");
        expertSettings.setStringSetting("rightStickCurve", "exponential");

        mainProfiles.add(new ControllerProfile("Expert", expertMain, expertSettings));
    }

    /**
     * Adds a main profile to the manager
     *
     * @param profile The profile to add
     */
    public void addMainProfile(ControllerProfile profile) {
        mainProfiles.add(profile);
    }

    /**
     * Adds a sub profile to the manager
     *
     * @param profile The profile to add
     */
    public void addSubProfile(ControllerProfile profile) {
        subProfiles.add(profile);
    }

    /**
     * Gets the active main profile
     *
     * @return The active main profile
     */
    public ControllerProfile getActiveMainProfile() {
        return mainProfiles.get(activeMainProfileIndex);
    }

    /**
     * Gets the active sub profile
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
     * Gets a list of all main profiles
     *
     * @return List of main profiles
     */
    public List<ControllerProfile> getMainProfiles() {
        return new ArrayList<>(mainProfiles);
    }

    /**
     * Gets a list of all sub profiles
     *
     * @return List of sub profiles
     */
    public List<ControllerProfile> getSubProfiles() {
        return new ArrayList<>(subProfiles);
    }

    /**
     * Sets the main profile by index
     *
     * @param index The index of the profile to set
     * @return The newly active profile
     */
    public ControllerProfile setMainProfileIndex(int index) {
        if (index >= 0 && index < mainProfiles.size()) {
            activeMainProfileIndex = index;
        }
        return getActiveMainProfile();
    }

    /**
     * Sets the sub profile by index
     *
     * @param index The index of the profile to set
     * @return The newly active profile
     */
    public ControllerProfile setSubProfileIndex(int index) {
        if (index >= 0 && index < subProfiles.size()) {
            activeSubProfileIndex = index;
        }
        return getActiveSubProfile();
    }
}