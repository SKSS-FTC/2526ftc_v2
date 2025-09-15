package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Outtake;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.ArmSubsystem;

public class ArmDownCommand extends CommandBase {

    private ArmSubsystem armSubsystem;

    public ArmDownCommand(ArmSubsystem arm){
        armSubsystem =arm;
        addRequirements(arm);
    }

    @Override
    public void execute(){
        armSubsystem.goDown();
    }

    @Override
    public boolean isFinished(){
        return armSubsystem.isDown();
    }

    @Override
    public void end(boolean b){
        armSubsystem.stop();
    }

    public ArmDownCommand copy(){
        return new ArmDownCommand(armSubsystem);
    }
}
