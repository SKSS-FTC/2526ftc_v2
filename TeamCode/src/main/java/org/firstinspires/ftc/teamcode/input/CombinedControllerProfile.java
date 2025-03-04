package org.firstinspires.ftc.teamcode.input;

import com.acmerobotics.dashboard.config.Config;

/**
 * Defines a controller profile with customized settings
 */
@Config
public class CombinedControllerProfile {
    public final String name;
    public final GamepadSettings mainGamepadSettings;
    public final GamepadSettings subGamepadSettings;

    public CombinedControllerProfile(String name, GamepadSettings main, GamepadSettings sub) {
        this.name = name;
        this.mainGamepadSettings = main;
        this.subGamepadSettings = sub;
    }
} 