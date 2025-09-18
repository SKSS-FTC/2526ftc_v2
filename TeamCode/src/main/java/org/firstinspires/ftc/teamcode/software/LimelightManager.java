package org.firstinspires.ftc.teamcode.software;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.configuration.Settings;

/**
 * Incoming Yap Session:
 * Limelight returns Tx and Ty values, which return angles for where a detected
 * object is,
 * and trig is required to get pixel or distance values.
 * IMPORTANT: Tx and Ty are zero when no desired object is detected.
 */
public class LimelightManager {
	public final Limelight3A limelight;
	public final MatchSettings matchSettings;
	LLResult currentResult;
	Pipeline currentPipeline = Pipeline.APRILTAG; // to detect the obelisk april tag
	
	public LimelightManager(Limelight3A limelight, MatchSettings matchSettings) {
		this.limelight = limelight;
		this.matchSettings = matchSettings;
	}
	
	/**
	 * Initializes limelight with polling rate of 100 Hz
	 */
	public void init() {
		setCurrentPipeline(currentPipeline);
		limelight.start();
		limelight.setPollRateHz(100);
	}
	
	/**
	 * Updates the data and checks if there is a desired object detected
	 *
	 * @return if an artifact is detected
	 */
	public boolean detectArtifact(MatchSettings.ArtifactColor color) {
		setCurrentPipeline(getPipelineFromColor(color));
		currentResult = limelight.getLatestResult();
		return currentResult.getTx() != 0 && currentResult.getTy() != 0
				&& (Math.abs(currentResult.getTx()) < Settings.Limelight.limelightWindowSize);
	}
	
	public final Pipeline getPipelineFromColor(MatchSettings.ArtifactColor color) {
		if (color == MatchSettings.ArtifactColor.GREEN) {
			return Pipeline.GREEN;
		} else {
			return Pipeline.PURPLE;
		}
	}
	
	public MatchSettings.Motif detectMotif() {
		setCurrentPipeline(Pipeline.APRILTAG);
		// TODO match fiducial IDs
		switch (limelight.getLatestResult().getFiducialResults().get(0).getFiducialId()) {
			case 21:
				return MatchSettings.Motif.GPP;
			case 22:
				return MatchSettings.Motif.PGP;
			case 23:
				return MatchSettings.Motif.PPG;
			default:
				return MatchSettings.Motif.UNKNOWN;
		}
	}
	
	public LLResult detectGoal() {
		setCurrentPipeline(Pipeline.APRILTAG);
		return limelight.getLatestResult();
	}
	
	/**
	 * Switches the current pipeline to a new pipeline
	 *
	 * @param newPipeline The new pipeline to switch to:
	 *                    APRILTAG (2), GREEN (3), PURPLE (4), RED_GOAL (5), BLUE_GOAL (6)
	 */
	public void setCurrentPipeline(Pipeline newPipeline) {
		currentPipeline = newPipeline;
		limelight.pipelineSwitch(currentPipeline.ordinal() + 1);
	}
	
	public enum Pipeline {
		APRILTAG, // pipe 2
		GREEN, // pipe 3
		PURPLE, // pipe 4
		UNKNOWN
	}
}
