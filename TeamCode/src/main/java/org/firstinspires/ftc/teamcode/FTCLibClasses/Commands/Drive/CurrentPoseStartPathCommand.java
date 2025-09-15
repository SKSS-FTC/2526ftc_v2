package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Drive;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.FollowerSubsystem;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;

public class CurrentPoseStartPathCommand extends CommandBase {

    PedroPathAutoCommand pedroPathAutoCommand;
    FollowerSubsystem followerSubsystem;
    double endHeading;
    Point point1;
    Point point2;


    public CurrentPoseStartPathCommand(FollowerSubsystem followerSubsystem,double heading, Point point){
        this.followerSubsystem= followerSubsystem;
        this.point1 = point;
        point2=point;
        endHeading = heading;
        addRequirements(followerSubsystem);
    }

    @Override
    public void initialize(){
        boolean addParameterPoint = false;
        Pose pose = followerSubsystem.getFollower().getPose();
        if (pose.getY()<-48.5&&pose.getX()<0){
            addParameterPoint = true;
        }
        if (addParameterPoint){
            pedroPathAutoCommand = new PedroPathAutoCommand(
                    followerSubsystem,
                    new Path.PathBuilder(
                            new BezierCurve(
                                    new Point(pose),
                                    new Point(new Pose(27.9,-46.4)),
                                    point1
                            )
                    )
                            .setLinearHeadingInterpolation(pose.getHeading(), endHeading)
                            .build()
            );
        } else {
            pedroPathAutoCommand = new PedroPathAutoCommand(
                    followerSubsystem,
                    new Path.PathBuilder(
                            new BezierCurve(
                                    new Point(pose),
                                    point1
                            )
                    )
                            .setLinearHeadingInterpolation(pose.getHeading(), endHeading)
                            .build()
            );
        }

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
