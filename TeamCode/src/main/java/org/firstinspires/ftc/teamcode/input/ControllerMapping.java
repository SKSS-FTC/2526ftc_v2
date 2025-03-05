package org.firstinspires.ftc.teamcode.input;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Base class for controller mappings
 */
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
        // Implemented in SubMapping and MainMapping
    }

    /**
     * Maps an event to a specific input
     *
     * @param eventName The name of the event
     * @param input     The input to map to
     * @param override  Whether to wipe other existing mappings
     * @return This mapping for chaining
     */
    public ControllerMapping mapEvent(String eventName, GamepadInput input, boolean override) {
        if (override) {
            eventToInputMap.remove(eventName);
        }
        eventToInputMap.put(eventName, input);
        return this;
    }

    public ControllerMapping mapEvent(String eventName, GamepadInput input) {
        return mapEvent(eventName, input, false);
    }


    /**
     * Gets the input mapped to an event
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
     * @return Set of event names
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
}