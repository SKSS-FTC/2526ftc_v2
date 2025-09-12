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

    /**
     * Returns an array of ArtifactColor representing the sequence for the given
     * Motif.
     * The order of colors corresponds to the Motif's letter order.
     * <p>
     * Example:
     * Motif.PPG -> [PURPLE, PURPLE, GREEN]
     * Motif.PGP -> [PURPLE, GREEN, PURPLE]
     * Motif.GPP -> [GREEN, PURPLE, PURPLE]
     *
     * @param motif The motif to convert
     * @return An array of ArtifactColor in motif order
     */
    public static ArtifactColor[] motifToArtifactColors(Motif motif) {
        if (motif == null)
            return null;
        String motifStr = motif.name();
        ArtifactColor[] colors = new ArtifactColor[3];
        for (int i = 0; i < 3; i++) {
            char c = motifStr.charAt(i);
            switch (c) {
                case 'P':
                    colors[i] = ArtifactColor.PURPLE;
                    break;
                case 'G':
                    colors[i] = ArtifactColor.GREEN;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown motif character: " + c);
            }
        }
        return colors;
    }

    public Motif getMotif() {
        String motif = (String) blackboard.get("motif");
        return Motif.valueOf(motif.toUpperCase());
    }

    public void setMotif(Motif motif) {
        if (motif != null) {
            blackboard.put("motif", motif.name().toLowerCase());
        }
    }

    public enum AllianceColor {
        RED,
        BLUE,
        UNKNOWN
    }

    public enum ArtifactColor {
        GREEN,
        PURPLE,
        UNKNOWN
    }

    public enum Motif {
        PPG,
        PGP,
        GPP,
        UNKNOWN
    }
}
