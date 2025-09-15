package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Test;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test.MotorTest;
public class MotorCommandTest extends CommandBase {
    MotorTest motor;
    int numTimes=0;
    public MotorCommandTest(MotorTest sub){
        this.motor = sub;
        addRequirements(motor);
    }

    @Override
    public void initialize(){
        motor.reset();
        motor.setToValue(0);

    }
    @Override
    public void execute(){

        motor.setToValue(2000*(numTimes+1));
    }

    @Override
    public void end(boolean bool){
        motor.stop();

    }

    @Override
    public boolean isFinished(){

        return Math.abs(motor.getValue()-2000) < 50;
    }

}
