package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Outtake;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.LiftSubsystem;

public class PullHangDownCommand extends CommandBase {
    LiftSubsystem liftSubsystem;
    public PullHangDownCommand(LiftSubsystem liftSubsystem){
        this.liftSubsystem=liftSubsystem;
    }
    @Override
    public void initialize(){
        liftSubsystem.pullHangDown();
    }
    public void execute(){
        liftSubsystem.pullHangDown();
    }
    @Override
    public boolean isFinished(){
        return liftSubsystem.isDown();
    }
    public PullHangDownCommand copy(){
        return new PullHangDownCommand(liftSubsystem);
    }
}
