package org.firstinspires.ftc.teamcode.hardware;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevColorSensorV3;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;

// TODO tune color thresholds and confidence
@Config
public class ColorSensor {
    RevColorSensorV3 colorSensor;
    double[] rgbValues = {0,0,0};

    // Target "center" colors for confidence-based matching
    // These should be tuned to approximate measured values
    public static double[] greenTarget = {0, 200, 0};
    public static double[] purpleTarget = {200, 0, 200};

    // Acceptable distance threshold (lower is stricter, higher is looser)
    public static double CONFIDENCE_THRESHOLD = 100.0;

    public ColorSensor(RevColorSensorV3 colorSensorV3) {
        this.colorSensor = colorSensorV3;
    }

    /**
     * Initializes Color Sensor and enables LED
     */
    public final void init() {
        colorSensor.initialize();
        colorSensor.enableLed(true);
    }

    /**
     * Updates the data and checks if there is a desired object detected
     * @return detected artifact color
     */
    public MatchSettings.ArtifactColor getArtifactColor() {
        rgbValues[0] = colorSensor.red();
        rgbValues[1] = colorSensor.green();
        rgbValues[2] = colorSensor.blue();

        double greenConfidence = computeDistance(rgbValues, greenTarget);
        double purpleConfidence = computeDistance(rgbValues, purpleTarget);

        if (greenConfidence < purpleConfidence && greenConfidence < CONFIDENCE_THRESHOLD) {
            return MatchSettings.ArtifactColor.GREEN;
        } else if (purpleConfidence < greenConfidence && purpleConfidence < CONFIDENCE_THRESHOLD) {
            return MatchSettings.ArtifactColor.PURPLE;
        } else {
            return MatchSettings.ArtifactColor.UNKNOWN;
        }
    }

    /**
     * Euclidean distance between measured RGB and target RGB
     */
    private double computeDistance(double[] measured, double[] target) {
        double dr = measured[0] - target[0];
        double dg = measured[1] - target[1];
        double db = measured[2] - target[2];
        return Math.sqrt(dr * dr + dg * dg + db * db);
    }
}