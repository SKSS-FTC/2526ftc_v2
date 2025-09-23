package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.CompOpModes.CompTeleOps.CompTeleOpTemplate;
import org.firstinspires.ftc.teamcode.CompOpModes.RobotOpMode;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Drive.PedroPathAutoCommand;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.FollowerSubsystem;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;

@TeleOp
public class LimelightLocalizationTest extends CompTeleOpTemplate {

    PedroPathAutoCommand drive;
    boolean isrunning = false;
    @Override
    public void runInLoop(){

        telemetry.addData("DeadWheel Pose",robot.followerSubsystem.getFollower().getPose());
        Pose3D pose3D = robot.limelightSubsystem.getAprilTagPose(1);
        telemetry.addData("Limelight Pose",pose3D.toString());
        Follower follower = robot.followerSubsystem.getFollower();
//        follower.setPose(new Pose(pose3D.getPosition().x*39.37,pose3D.getPosition().y*39.37,pose3D.getOrientation().getYaw(AngleUnit.DEGREES)));
//        Pose curPose = follower.getPose();
//        if (!isrunning) {
//            Position pos = pose3D.getPosition();
//            if (!(pos.x == 0 &&pos.y==0)) {
//                double yaw = Math.toDegrees(Math.atan((1.5 - pose3D.getPosition().x) / (1.43 - pose3D.getPosition().y)));
//                Path path = new Path.PathBuilder(
//                        new BezierLine(
//                                new Point(follower.getPose()),
//                                new Point(new Pose(curPose.getX(), curPose.getY(), yaw))
//                        )
//                ).build();
//                drive = new PedroPathAutoCommand(robot.followerSubsystem, path);
//                drive.schedule();
//                isrunning = true;
//            }
//        }
//        if (drive!=null){
//           isrunning = !drive.isFinished();
//        }
    }

    @Override
    public void setAllianceColor(){
        allianceColor=AllianceColor.RED;
    }
}
