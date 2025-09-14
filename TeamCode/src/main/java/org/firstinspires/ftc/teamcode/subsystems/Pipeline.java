package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

public class Pipeline {
    public Limelight3A limelight;
    private int cacheColor = 0;

    public Pipeline(Limelight3A limelight){
        this.limelight = limelight;

        limelight.pipelineSwitch(0);
        limelight.setPollRateHz(100);
    }

    private int getNewColor(){
        LLResult result = limelight.getLatestResult();
        int color = 0;

        if(result != null) {
            double[] pythonOutputs = result.getPythonOutput(); // This is llpython
            if (pythonOutputs != null && pythonOutputs.length >= 5) {
                color = (int) pythonOutputs[4];
            }
        }

        return color;
    }

    public void getColor (){
        cacheColor = getNewColor();
    }

    public boolean isGreen(){return cacheColor == 1;} //color 1 is green in the sanscript

    public boolean isPurple(){return cacheColor == 2;} //color 2 is purple in the sanscript

    public void stop(){limelight.stop();}

    public void start(){limelight.start();}


}
