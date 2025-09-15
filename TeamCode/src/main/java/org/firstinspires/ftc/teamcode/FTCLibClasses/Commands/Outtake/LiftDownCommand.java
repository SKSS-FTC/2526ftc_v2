package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Outtake;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.LiftSubsystem;

public class LiftDownCommand extends CommandBase {

    private LiftSubsystem lift;

    public LiftDownCommand(LiftSubsystem lift){
        this.lift = lift;
        addRequirements(lift);
    }

    @Override
    public void execute(){
        lift.goDown();
    }

    @Override
    public boolean isFinished(){
        return lift.isDown();
    }

    @Override
    public void end(boolean b){
        lift.stop();
    }

    public LiftDownCommand copy(){
        return new LiftDownCommand(lift);
    }
}
