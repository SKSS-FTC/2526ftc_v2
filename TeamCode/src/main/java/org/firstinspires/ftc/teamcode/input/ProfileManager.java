package org.firstinspires.ftc.teamcode.input;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages available controller profiles
 */
public class ProfileManager {
    private static final List<CombinedControllerProfile> mainProfiles = new ArrayList<>();
    private static final List<CombinedControllerProfile> subProfiles = new ArrayList<>();

    static {
        // Initialize with default profiles
        initializeDefaultProfiles();
    }

    /**
     * Sets up the default controller profiles
     */
    private static void initializeDefaultProfiles() {
        // Agney profile
        GamepadSettings agneyMain = new GamepadSettings() {
            @Override
            public double applyBoostCurve(double input) {
                return BoostCurves.exponential(input);
            }
        };
        agneyMain.dpadMovementSpeed = 0.6;
        agneyMain.bumperRotationSpeed = 0.8;
        agneyMain.incrementalVertical = true;

        GamepadSettings agneySub = new GamepadSettings();
        agneySub.triggerThreshold = 0.1;

        mainProfiles.add(new CombinedControllerProfile("Agney", agneyMain, agneySub));

        // Conner profile
        GamepadSettings connerMain = new GamepadSettings() {
            @Override
            public double applyBoostCurve(double input) {
                return BoostCurves.quadratic(input);
            }
        };
        connerMain.dpadMovementSpeed = 0.6;
        connerMain.bumperRotationSpeed = 0.9;

        GamepadSettings connerSub = new GamepadSettings();
        connerSub.triggerThreshold = 0.15;

        mainProfiles.add(new CombinedControllerProfile("Conner", connerMain, connerSub));

        // Ben profile
        GamepadSettings benMain = new GamepadSettings() {
            @Override
            public double applyBoostCurve(double input) {
                return BoostCurves.smooth(input);
            }
        };
        benMain.dpadMovementSpeed = 0.8;
        benMain.bumperRotationSpeed = 0.7;

        GamepadSettings benSub = new GamepadSettings();
        benSub.triggerThreshold = 0.2;

        subProfiles.add(new CombinedControllerProfile("Ben", benMain, benSub));
    }

    /**
     * Gets all available main controller profiles
     */
    public static List<CombinedControllerProfile> getMainProfiles() {
        return new ArrayList<>(mainProfiles);
    }

    /**
     * Gets all available sub controller profiles
     */
    public static List<CombinedControllerProfile> getSubProfiles() {
        return new ArrayList<>(subProfiles);
    }

    /**
     * Gets a main profile by name
     */
    public static CombinedControllerProfile getMainProfile(String name) {
        return mainProfiles.stream()
                .filter(p -> p.name.equals(name))
                .findFirst()
                .orElse(mainProfiles.get(0));
    }

    /**
     * Gets a sub profile by name
     */
    public static CombinedControllerProfile getSubProfile(String name) {
        return subProfiles.stream()
                .filter(p -> p.name.equals(name))
                .findFirst()
                .orElse(subProfiles.get(0));
    }
}