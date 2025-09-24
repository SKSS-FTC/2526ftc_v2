package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake extends Subsystem{

    public boolean activeIntake;

    public void init(boolean isAuton){
        activeIntake=false;

    }
    public void update(boolean isAuton){
        if(activeIntake) {
            Robot.getInstance().intakeMotor.setPower(0.8);
        }
        else{
            Robot.getInstance().intakeMotor.setPower(0);
        }

    }

    public void stop(){

    }
    public void printToTelemetry(Telemetry telemetry){
        telemetry.addData("Intake State", activeIntake);

    }


}
