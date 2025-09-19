package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

public class Pipeline {
    private final Limelight3A limelight;
    private int cacheColor = 0;

    public Pipeline(Limelight3A limelight) {
        this.limelight = limelight;
        limelight.pipelineSwitch(0);
        limelight.setPollRateHz(100);
    }

    public void update() {
        LLResult result = limelight.getLatestResult();
        if (result != null) {
            double[] pythonOutputs = result.getPythonOutput();
            if (pythonOutputs != null && pythonOutputs.length >= 5) {
                cacheColor = (int) pythonOutputs[4];
                return;
            }
        }
        cacheColor = 0; // nothing detected
    }

    public String getColorName() {
        switch (cacheColor) {
            case 1: return "GREEN";
            case 2: return "PURPLE";
            default: return "NONE";
        }
    }

    public boolean isGreen() { return cacheColor == 1; }
    public boolean isPurple() { return cacheColor == 2; }
    public boolean hasDetection() { return cacheColor != 0; }

    public void start() { limelight.start(); }
    public void stop() { limelight.stop(); }
}
