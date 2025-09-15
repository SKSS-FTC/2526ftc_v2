package org.firstinspires.ftc.teamcode.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @noinspection unchecked, we know what the blackboard is storing
 */
public class MatchSettings {
	private static final String CLASSIFIER_STATE_KEY = "classifierState";
	private static final String MOTIF_KEY = "motif";
	private static final String ALLIANCE_COLOR_KEY = "allianceColor";
	private final HashMap<String, Object> blackboard;
	
	public MatchSettings(HashMap<String, Object> blackboard) {
		this.blackboard = blackboard;
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
	
	public AllianceColor getAllianceColor() {
		String color = (String) blackboard.get(ALLIANCE_COLOR_KEY);
		return Objects.equals(color, "red") ? AllianceColor.RED : AllianceColor.BLUE;
	}
	
	public void setAllianceColor(AllianceColor color) {
		if (color != null) {
			blackboard.put(ALLIANCE_COLOR_KEY, color.name().toLowerCase());
		}
	}
	
	public Motif getMotif() {
		String motif = (String) blackboard.get(MOTIF_KEY);
		if (motif != null) {
			return Motif.valueOf(motif.toUpperCase());
		} else {
			return Motif.UNKNOWN;
		}
	}
	
	public void setMotif(Motif motif) {
		if (motif != null) {
			blackboard.put(MOTIF_KEY, motif.name().toLowerCase());
		}
	}
	
	// Get current state
	public List<ArtifactColor> getClassifierState() {
		return (List<ArtifactColor>) blackboard.getOrDefault(CLASSIFIER_STATE_KEY, new ArrayList<ArtifactColor>());
	}
	
	// Set state
	public void setClassifierState(List<ArtifactColor> state) {
		List<ArtifactColor> copy = new ArrayList<>(state.size() > 9 ? state.subList(0, 9) : state);
		blackboard.put(CLASSIFIER_STATE_KEY, copy);
	}
	
	// Append artifact
	public void addArtifact(ArtifactColor artifact) {
		List<ArtifactColor> state = getClassifierState();
		if (state.size() >= 9) return;
		state.add(artifact);
		blackboard.put(CLASSIFIER_STATE_KEY, state);
	}
	
	public void emptyClassifier() {
		blackboard.put(CLASSIFIER_STATE_KEY, new ArrayList<ArtifactColor>());
	}
	
	public void incrementClassifier() {
		List<ArtifactColor> state = getClassifierState();
		if (state.size() >= 9) return;
		state.add(ArtifactColor.UNKNOWN);
		blackboard.put(CLASSIFIER_STATE_KEY, state);
	}
	
	public ArtifactColor nextArtifactNeeded() {
		Motif motif = getMotif();
		if (motif == null || motif == Motif.UNKNOWN) return ArtifactColor.UNKNOWN;
		
		List<ArtifactColor> state = getClassifierState();
		ArtifactColor[] motifColors = motifToArtifactColors(motif);
		if (motifColors == null || motifColors.length == 0) return ArtifactColor.UNKNOWN;
		
		int index = state.size() % motifColors.length;
		return motifColors[index];
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
