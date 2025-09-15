package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Test;


import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test.MotorTutorial;

public class MotorCommandTutorial extends CommandBase {
    private MotorTutorial motor;
    private GamepadEx gamepad;

    public MotorCommandTutorial(MotorTutorial motor, GamepadEx gamepad){
        this.motor = motor;
        this.gamepad = gamepad;
    }

    @Override
    public void initialize(){
        motor.stopMotor();
    }

    @Override
    public void execute(){
        motor.runMotor();
    }

    @Override
    public void end(boolean b){
        motor.stopMotor();
    }

    @Override
    public boolean isFinished(){
        return !gamepad.getButton(GamepadKeys.Button.A);
    }
}
