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
    public Limelight3A limelight;
    LLResult currentResult;
    Pipeline currentPipeline = Pipeline.OBELISK; // to detect the obelisk april tag

    public LimelightManager(Limelight3A limelight) {
        this.limelight = limelight;
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

    public final Pipeline getPipelineFromAlliance(MatchSettings.AllianceColor color) {
        if (color == MatchSettings.AllianceColor.RED) {
            return Pipeline.RED_GOAL;
        } else {
            return Pipeline.BLUE_GOAL;
        }
    }

    public MatchSettings.Motif detectMotif() {
        setCurrentPipeline(Pipeline.OBELISK);
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

    public LLResult detectGoal(MatchSettings matchSettings) {
        setCurrentPipeline(getPipelineFromAlliance(matchSettings.getAllianceColor()));
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
        OBELISK, // pipe 2
        GREEN, // pipe 3
        PURPLE, // pipe 4
        RED_GOAL, // pipe 5
        BLUE_GOAL, // pipe 6
        UNKNOWN
    }
}
