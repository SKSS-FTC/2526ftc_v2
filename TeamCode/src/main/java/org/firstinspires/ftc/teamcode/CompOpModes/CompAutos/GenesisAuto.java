package org.firstinspires.ftc.teamcode.CompOpModes.CompAutos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.AllianceColor;
import org.firstinspires.ftc.teamcode.CompOpModes.RobotOpMode;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Drive.PedroPathAutoCommand;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;

@Autonomous
public class GenesisAuto extends RobotOpMode {
    @Override
    public void createLogic(){
        robot.followerSubsystem.getFollower().setPose(new Pose(0,0,0));
        Path path = new Path(
                new BezierLine(
                        new Point(new Pose(0,0,0)),
                        new Point(new Pose(0,-20,0))
                )
        );
        PedroPathAutoCommand park = new PedroPathAutoCommand(robot.followerSubsystem,path);
        park.schedule();
    }
    @Override
    public void setAllianceColor(){
        allianceColor= AllianceColor.BLUE;
    }
}
