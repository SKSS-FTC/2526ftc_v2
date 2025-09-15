package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Intake;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Intake.ExtendMotorSubsystem;

public class ExtendIntake extends CommandBase {

    private ExtendMotorSubsystem intake;

    public ExtendIntake(ExtendMotorSubsystem intake){
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void execute(){
        intake.extendMotorOutFully();

    }
    @Override
    public void end(boolean b){
        intake.stopExtend();
    }

    @Override
    public boolean isFinished(){
        return intake.extendFinished();
    }

    public ExtendIntake copy(){
        return new ExtendIntake(intake);
    }
}
