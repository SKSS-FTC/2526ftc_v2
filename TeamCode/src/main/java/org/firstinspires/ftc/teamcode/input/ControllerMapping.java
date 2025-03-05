package org.firstinspires.ftc.teamcode.input;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Base class for controller mappings
 */
@Config
public class ControllerMapping {
    // Maps event names to inputs
    protected final Map<String, GamepadInput> eventToInputMap = new HashMap<>();

    /**
     * Creates a new empty mapping
     */
    public ControllerMapping() {
        initializeDefaultMappings();
    }

    /**
     * Initializes default mappings - override in subclasses
     */
    protected void initializeDefaultMappings() {
        // Base implementation has no mappings
    }

    /**
     * Maps an event to a specific input
     *
     * @param eventName The name of the event
     * @param input     The input to map to
     * @return This mapping for chaining
     */
    public ControllerMapping mapEvent(String eventName, GamepadInput input) {
        eventToInputMap.put(eventName, input);
        return this;
    }

    /**
     * Gets the input mapped to a specific event
     *
     * @param eventName The name of the event
     * @return The input mapped to the event, or null if not mapped
     */
    public GamepadInput getInputForEvent(String eventName) {
        return eventToInputMap.get(eventName);
    }

    /**
     * Gets all event names in this mapping
     *
     * @return A set of all event names
     */
    public Set<String> getEventNames() {
        return eventToInputMap.keySet();
    }

    /**
     * Clears all mappings
     */
    public void clearMappings() {
        eventToInputMap.clear();
    }

    /**
     * Resets to default mappings
     */
    public void resetToDefaults() {
        clearMappings();
        initializeDefaultMappings();
    }
}