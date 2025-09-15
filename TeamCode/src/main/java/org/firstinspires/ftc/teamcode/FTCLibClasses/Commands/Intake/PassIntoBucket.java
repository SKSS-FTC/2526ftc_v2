package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Intake;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.util.Timing;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Intake.SpinIntakeSubsystem;
import org.firstinspires.ftc.teamcode.RobotConfig;

import java.util.concurrent.TimeUnit;


public class PassIntoBucket extends CommandBase {

    private SpinIntakeSubsystem intake;
    private Timing.Timer timer;

    public PassIntoBucket(SpinIntakeSubsystem intake){
        this.intake = intake;
        timer = new Timing.Timer(RobotConfig.IntakeConstants.passThroughTimeMs, TimeUnit.MILLISECONDS);
    }

    public PassIntoBucket(SpinIntakeSubsystem intake, double millis){
        this.intake = intake;
        timer = new Timing.Timer((int)millis, TimeUnit.MILLISECONDS);
    }

    @Override
    public void initialize(){
        timer.start();
    }

    @Override
    public void execute(){
        intake.spinWheelsDown();
    }

    @Override
    public void end(boolean b){
        intake.stopIntakeWheels();
    }

    @Override
    public boolean isFinished(){
        return timer.done();
    }

    public PassIntoBucket copy(){
        return new PassIntoBucket(intake);
    }

    public PassIntoBucket copy(double millis){
        return new PassIntoBucket(intake,millis);
    }

}
