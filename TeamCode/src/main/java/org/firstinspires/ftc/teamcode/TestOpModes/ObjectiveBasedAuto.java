package org.firstinspires.ftc.teamcode.TestOpModes;

import org.firstinspires.ftc.teamcode.AllianceColor;
import org.firstinspires.ftc.teamcode.CompOpModes.RobotOpMode;
import org.firstinspires.ftc.teamcode.RobotConfig;

public class ObjectiveBasedAuto extends RobotOpMode {

    private boolean hasSample = false;
    private double timeLeft=230;

    @Override
    public void createLogic(){

    }
    @Override
    public void runInLoop(){

    }

    private void setTimeLeft(){
        double timeElapsed = (System.currentTimeMillis()- RobotConfig.GlobalConstants.startTime)/1000;
        double timeLeft= 230-timeElapsed;
    }
    @Override
    public void setAllianceColor(){
        allianceColor = AllianceColor.RED;
    }
}
