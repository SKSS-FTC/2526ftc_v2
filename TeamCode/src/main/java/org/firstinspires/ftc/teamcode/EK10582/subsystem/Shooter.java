package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.EK10582.EKLinear;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.SubsystemConstants.ShooterStates;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter extends Subsystem {

    private double motorSpeed = 0;
    public boolean activeShoot;

    public SubsystemConstants.ShooterStates currentState = SubsystemConstants.ShooterStates.IDLE;

    @Override
    public void init(boolean isAuton){
        activeShoot=false;
        currentState = SubsystemConstants.ShooterStates.IDLE;
    }
    @Override
    public void update(boolean isAuton){
        if(activeShoot) {
            Robot.getInstance().rightShooter.setPower(1);
            Robot.getInstance().leftShooter.setPower(-1);
        }
        else{
            Robot.getInstance().rightShooter.setPower(0);
            Robot.getInstance().leftShooter.setPower(0);
        }




    }
    @Override
    public void stop(){

    }
    @Override
    public void printToTelemetry(Telemetry telemetry){
        telemetry.addData("Shooter State", activeShoot);

    }

}
