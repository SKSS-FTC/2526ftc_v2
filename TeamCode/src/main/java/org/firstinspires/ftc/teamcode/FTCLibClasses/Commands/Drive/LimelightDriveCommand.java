package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Drive;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Limelight.SampleFinder;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.FollowerSubsystem;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.LimelightSubsystem;
import org.firstinspires.ftc.teamcode.NoSampleFoundException;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;

import java.util.concurrent.TimeUnit;

public class LimelightDriveCommand extends CommandBase {

    private Path path;
    private Pose pose;
    private PedroPathAutoCommand pedroPathAutoCommand;

    private FollowerSubsystem followerSubsystem;
    private Telemetry telemetry;

    private SampleFinder sampleFinder;
    private double beforeInitMaxSpeed = 1;

    public LimelightDriveCommand(SampleFinder sampleFinder, FollowerSubsystem followerSubsystem, Telemetry telemetry){
        this.sampleFinder = sampleFinder;
        this.followerSubsystem = followerSubsystem;
        addRequirements(followerSubsystem);
        this.telemetry = telemetry;
    }

    @Override
    public void initialize(){

        beforeInitMaxSpeed = followerSubsystem.getFollower().getMaxPower();
        followerSubsystem.getFollower().setMaxPower(1);
        pose = sampleFinder.getPose();
//        if (Math.abs(pose.getX()-5)<3){
//            pose.setX(pose.getX()+3);
//        }



        telemetry.addData("pose X",pose.getX());
        Pose endPoint;
        double curPoseHeading = followerSubsystem.getFollower().getPose().getHeading();
        double extendDistance = 19;

        double approachHeading;
        if ((curPoseHeading<=Math.PI/4&&curPoseHeading>=0)||(curPoseHeading<=2*Math.PI&&curPoseHeading>7*Math.PI/4)){
            endPoint = new Pose(pose.getX()-extendDistance,pose.getY());
            approachHeading = 0;
        } else if (curPoseHeading<=Math.PI*3/4&& curPoseHeading>Math.PI/4){
            endPoint = new Pose(pose.getX(),pose.getY()-extendDistance);
            approachHeading =  Math.PI/2;

        } else if (curPoseHeading<=Math.PI*5/4&& curPoseHeading>3*Math.PI/4) {
            endPoint = new Pose(pose.getX()+extendDistance,pose.getY());
            approachHeading = Math.PI;

        } else {
            endPoint = new Pose(pose.getX(),pose.getY()+extendDistance);
            approachHeading = 3*Math.PI/2;

        }
        telemetry.addData("approach heading",approachHeading);
        path = new Path.PathBuilder(
                new BezierLine(
                        new Point(followerSubsystem.getFollower().getPose()),
                        new Point(endPoint)
                )
        )
                .setConstantHeadingInterpolation(approachHeading)
                .setPathEndTimeoutConstraint(750)
                .build();
        path.setZeroPowerAccelerationMultiplier(.5);
        pedroPathAutoCommand = new PedroPathAutoCommand(followerSubsystem,path);
        pedroPathAutoCommand.initialize();
    }

    @Override
    public void execute(){

        telemetry.addData("pose X", pose.getX());
        pedroPathAutoCommand.execute();
        telemetry.addData("pose", pose);

    }

    @Override
    public void end (boolean b){
        pedroPathAutoCommand.end(b);
        pose = null;
        followerSubsystem.getFollower().setMaxPower(beforeInitMaxSpeed);
    }

    @Override
    public boolean isFinished(){

        return pedroPathAutoCommand.isFinished();
    }

    public SampleFinder getSampleFinder(){
        return sampleFinder;
    }

    public LimelightDriveCommand copy(){
        return new LimelightDriveCommand(sampleFinder,followerSubsystem,telemetry);
    }
}
