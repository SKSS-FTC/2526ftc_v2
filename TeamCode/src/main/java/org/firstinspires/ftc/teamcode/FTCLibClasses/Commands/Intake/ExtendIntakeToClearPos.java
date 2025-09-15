package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Intake;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Intake.ExtendMotorSubsystem;

public class ExtendIntakeToClearPos extends CommandBase {

    private ExtendMotorSubsystem extendIntake;

    public ExtendIntakeToClearPos(ExtendMotorSubsystem extendIntake){
        this.extendIntake = extendIntake;
        addRequirements(extendIntake);
    }

    @Override
    public void initialize(){
        extendIntake.extendToClearPos();
    }

    @Override
    public void execute(){
        extendIntake.extendToClearPos();
    }

    @Override
    public boolean isFinished(){
        return extendIntake.extendFinished();
    }

    @Override
    public void end(boolean b){
        extendIntake.stopExtend();
    }

    public ExtendIntakeToClearPos copy(){
        return new ExtendIntakeToClearPos(extendIntake);
    }
}
