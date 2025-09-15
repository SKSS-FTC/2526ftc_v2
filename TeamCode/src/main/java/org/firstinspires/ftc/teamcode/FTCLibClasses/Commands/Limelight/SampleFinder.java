package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Limelight;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.LimelightSubsystem;
import org.firstinspires.ftc.teamcode.NoSampleFoundException;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;

public class SampleFinder extends CommandBase {

    private Pose pose;
    private LimelightSubsystem limelightSubsystem;
    private Telemetry telemetry;

    public SampleFinder(LimelightSubsystem limelightSubsystem, Telemetry telemetry){
        this.limelightSubsystem=limelightSubsystem;
        this.telemetry = telemetry;
    }


    @Override
    public void initialize(){
        limelightSubsystem.captureSnapshot();
        pose =null;
    }

    @Override
    public void execute(){
        try {
            pose=limelightSubsystem.processResults();
        } catch (NoSampleFoundException e){

        }
    }
    @Override
    public boolean isFinished(){
        return pose!=null;
    }

    public Pose getPose(){return pose;}

}
