package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Hang;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Hang.HangSubsystem;

public class HangPullDownCommand extends CommandBase {
    HangSubsystem hangLift;

    public HangPullDownCommand(HangSubsystem hangLift){
        this.hangLift = hangLift;
        addRequirements(hangLift);


    }

    @Override
    public void execute(){
        hangLift.goDown();
    }
    @Override
    public void end(boolean b){
        hangLift.stop();
    }
    @Override
    public boolean isFinished(){
        return hangLift.isThereDown();
    }
    public HangPullDownCommand copy() {return new HangPullDownCommand(hangLift);}
}
