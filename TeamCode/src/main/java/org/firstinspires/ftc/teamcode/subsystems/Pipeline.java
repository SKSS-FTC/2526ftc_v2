package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.Limelight3A;

public class Pipeline {
    private Limelight3A limelight;
    private String color;

    public  Pipeline(Limelight3A limelight){
        this.limelight = limelight;

        limelight.setPollRateHz(100);
        limelight.start();
    }

    private void getNewColor(){
    }

    public boolean isGreen(){return false;}

    public void pipelineStop(){limelight.stop();}

    public void pipelineStart(){limelight.start();}

    public void update (){

    }

}
