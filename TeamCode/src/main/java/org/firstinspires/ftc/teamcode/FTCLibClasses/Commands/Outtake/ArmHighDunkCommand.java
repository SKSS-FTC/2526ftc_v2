package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Outtake;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.util.Timing;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.ArmSubsystem;
import org.firstinspires.ftc.teamcode.RobotConfig;

import java.util.concurrent.TimeUnit;

public class ArmHighDunkCommand extends CommandBase {

    private ArmSubsystem armSubsystem;
    private Timing.Timer timer;

    public ArmHighDunkCommand(ArmSubsystem armSubsystem){
        this.armSubsystem = armSubsystem;
        addRequirements(armSubsystem);
        timer = new Timing.Timer(RobotConfig.OuttakeConstants.highDunkLingerTimeMs, TimeUnit.MILLISECONDS);
    }
    @Override
    public void initialize(){
        timer.start();
        timer.pause();
    }
    @Override
    public void execute(){

        armSubsystem.dunkHighPos();
    }

    @Override
    public void end(boolean b){
        armSubsystem.stop();
    }

    @Override
    public boolean isFinished(){
        return armSubsystem.hasDunked();
    }

    public ArmHighDunkCommand copy(){
        return new ArmHighDunkCommand(armSubsystem);
    }
}
