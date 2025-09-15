package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Test;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test.ServoTest;

public class ServoCommandTest extends CommandBase {
    private ServoTest servo;
    private double randNum;

    public ServoCommandTest(ServoTest sub){
        servo = sub;
        addRequirements(servo);
    }

    @Override
    public void initialize(){
        //servo.setToPos(0);
        randNum = Math.random();
    }

    @Override
    public void execute(){
        servo.setToPos(randNum);
    }

    @Override
    public void end(boolean hi){
        //servo.setToPos(.5);
    }
    @Override
    public boolean isFinished(){
        return servo.getPos() == randNum;
    }
}
