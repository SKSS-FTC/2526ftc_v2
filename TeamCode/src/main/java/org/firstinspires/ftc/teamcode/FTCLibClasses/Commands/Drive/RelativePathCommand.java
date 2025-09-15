package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Drive;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.FollowerSubsystem;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;

public class RelativePathCommand extends CommandBase {

    private FollowerSubsystem followerSubsystem;
    private PedroPathAutoCommand pedroPathAutoCommand=null;

    private Point relativeTarget;

    private double heading;

    public RelativePathCommand(FollowerSubsystem followerSubsystem, Point relativeTarget, double heading){
        this.followerSubsystem =followerSubsystem;
        addRequirements(followerSubsystem);
        this.relativeTarget = relativeTarget;
        this.heading = heading;

    }

    @Override
    public void initialize(){

        Pose curPose = followerSubsystem.getFollower().getPose();
        Pose nextPose = curPose.copy();
        nextPose.add(new Pose(relativeTarget.getX(),relativeTarget.getY()));
        Path pedroPath = new Path.PathBuilder(
                new BezierLine(
                        new Point(curPose),
                        new Point(nextPose)
                        )

                )
                .setConstantHeadingInterpolation(heading)
                .build();

        pedroPathAutoCommand = new PedroPathAutoCommand(followerSubsystem,pedroPath);
        pedroPathAutoCommand.initialize();
    }
    @Override
    public void execute(){
        pedroPathAutoCommand.execute();
    }

    @Override
    public void end(boolean b){
        pedroPathAutoCommand.end(b);
    }

    @Override
    public boolean isFinished(){
        return pedroPathAutoCommand.isFinished();
    }
}
