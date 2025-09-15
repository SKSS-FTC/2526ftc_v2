package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Outtake;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.ArmSubsystem;

public class ArmLowDunkCommand extends CommandBase {

    private ArmSubsystem armSubsystem;

    public ArmLowDunkCommand(ArmSubsystem armSubsystem){
        this.armSubsystem = armSubsystem;
        addRequirements(armSubsystem);
    }

    @Override
    public void execute(){
        armSubsystem.dunkLowPos();
    }

    @Override
    public void end(boolean b){
        armSubsystem.stop();
    }

    @Override
    public boolean isFinished(){
        return armSubsystem.hasLowDunked();
    }

    public ArmLowDunkCommand copy(){
        return new ArmLowDunkCommand(armSubsystem);
    }
}
