package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Intake;

import androidx.annotation.RequiresPermission;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.util.Timing;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Intake.VerticalIntakeSubsystem;

import java.util.concurrent.TimeUnit;

public class ReadyVerticalNormal extends CommandBase {
    private VerticalIntakeSubsystem verticalIntakeSubsystem;
    private Timing.Timer timer;

    public ReadyVerticalNormal(VerticalIntakeSubsystem verticalIntakeSubsystem){
        this.verticalIntakeSubsystem =verticalIntakeSubsystem;
    }

    @Override
    public void initialize(){
        if (verticalIntakeSubsystem.getServoPos()>.2){
            timer = new Timing.Timer(400, TimeUnit.MILLISECONDS);
        } else {
            timer = new Timing.Timer(10,TimeUnit.MILLISECONDS);
        }
        verticalIntakeSubsystem.moveIntakeToOutOfSubReady();
    }

    @Override
    public boolean isFinished(){
        return timer.done();
    }

    public ReadyVerticalNormal copy(){
        return new ReadyVerticalNormal(verticalIntakeSubsystem);
    }

}
