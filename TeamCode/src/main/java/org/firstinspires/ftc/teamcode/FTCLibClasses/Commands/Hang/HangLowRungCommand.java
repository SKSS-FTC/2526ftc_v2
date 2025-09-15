package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Hang;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Hang.HangSubsystem;

public class HangLowRungCommand extends CommandBase {
    HangSubsystem hangLift;

    public HangLowRungCommand(HangSubsystem hangLift){
        this.hangLift = hangLift;
        addRequirements(hangLift);


    }

    @Override
    public void execute(){
        hangLift.goToMaxHeight();
    }
    @Override
    public void end(boolean b){
        hangLift.stop();
    }
    @Override
    public boolean isFinished(){
       return hangLift.isThere();
    }
    public HangLowRungCommand copy() {return new HangLowRungCommand(hangLift);}
}
