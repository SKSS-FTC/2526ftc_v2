package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Intake;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Intake.ExtendMotorSubsystem;


public class RetractIntake extends CommandBase {
    private final ExtendMotorSubsystem intake;

    public RetractIntake(ExtendMotorSubsystem intake){
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void initialize(){
        intake.retractMotorFully();
    }

    @Override
    public void execute(){
       intake.retractMotorFully();
    }

    @Override
    public void end(boolean b){
        intake.stopExtend();
    }

    @Override
    public boolean isFinished(){
        return intake.extendFinished();
    }

    public RetractIntake copy(){
        return new RetractIntake(intake);
    }
}
