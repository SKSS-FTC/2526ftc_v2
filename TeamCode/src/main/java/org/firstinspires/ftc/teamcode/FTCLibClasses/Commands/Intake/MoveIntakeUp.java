package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Intake;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.util.Timing;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Intake.VerticalIntakeSubsystem;

import java.util.concurrent.TimeUnit;

public class MoveIntakeUp extends CommandBase {

    private VerticalIntakeSubsystem intake;
    private boolean isRunning=false;
    private Timing.Timer timer;

    public MoveIntakeUp(VerticalIntakeSubsystem intake){
        this.intake = intake;
        addRequirements(intake);

        timer = new Timing.Timer(400, TimeUnit.MILLISECONDS);
    }

    public String getTelemetry(){
        if (isRunning){
            return "Move Intake Up is running";
        }
        return "Move Intake Up is not running";
    }

    @Override
    public void initialize(){
        if (intake.getServoPos()>.2){
            timer = new Timing.Timer(20,TimeUnit.MILLISECONDS);
        } else {
            timer = new Timing.Timer(350,TimeUnit.MILLISECONDS);
        }
        intake.moveIntakeUp();
        isRunning = true;
        timer.start();
    }

    @Override
    public boolean isFinished(){
        isRunning = !timer.done();

        return timer.done();
    }

    public MoveIntakeUp copy(){
        return new MoveIntakeUp(intake);
    }
}
