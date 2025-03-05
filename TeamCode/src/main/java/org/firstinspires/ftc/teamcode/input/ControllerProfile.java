package org.firstinspires.ftc.teamcode.input;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a controller profile with input mappings and settings
 */
public class ControllerProfile {
    public final String name;
    public final Map<String, String> inputMappings; // Maps input IDs to event names
    public final Map<String, Object> settings; // Controller-specific settings

    public ControllerProfile(String name) {
        this.name = name;
        this.inputMappings = new HashMap<>();
        this.settings = new HashMap<>();
    }

    /**
     * Maps an input to an event
     *
     * @param inputId   The input identifier (e.g., "DPAD_UP", "LEFT_STICK_Y")
     * @param eventName The event to trigger
     * @return This profile for chaining
     */
    public ControllerProfile mapInput(String inputId, String eventName) {
        inputMappings.put(inputId, eventName);
        return this;
    }

    /**
     * Adds a setting to the profile
     *
     * @param key   Setting name
     * @param value Setting value
     * @return This profile for chaining
     */
    public ControllerProfile addSetting(String key, Object value) {
        settings.put(key, value);
        return this;
    }

    /**
     * Gets a setting value with a default fallback
     */
    @SuppressWarnings("unchecked")
    public <T> T getSetting(String key, T defaultValue) {
        return settings.containsKey(key) ? (T) settings.get(key) : defaultValue;
    }
}