package org.firstinspires.ftc.teamcode.configuration;

import java.util.HashMap;
import java.util.Objects;

public class MatchSettings {
    private final HashMap blackboard;

    public MatchSettings(HashMap blackboard) {
        this.blackboard = blackboard;
    }

    public AllianceColor getAllianceColor() {
        String color = (String) blackboard.get("allianceColor");
        return Objects.equals(color, "red") ? AllianceColor.RED : AllianceColor.BLUE;
    }

    public void setAllianceColor(AllianceColor color) {
        if (color != null) {
            blackboard.put("allianceColor", color.name().toLowerCase());
        }
    }

    public enum AllianceColor {
        RED,
        BLUE
    }
}
