package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Outtake;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.ArmSubsystem;

public class ArmHangPosCommand extends CommandBase {
    private ArmSubsystem armSubsystem;

    public ArmHangPosCommand(ArmSubsystem arm){
        armSubsystem =arm;
        addRequirements(arm);
    }

    @Override
    public void execute(){
        armSubsystem.goToHangPos();
    }

    @Override
    public boolean isFinished(){
        return armSubsystem.isAtHangPos();
    }

    @Override
    public void end(boolean b){
        armSubsystem.stop();
    }

    public ArmHangPosCommand copy(){
        return new ArmHangPosCommand(armSubsystem);
    }
}
