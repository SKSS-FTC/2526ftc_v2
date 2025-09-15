package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.CompOpModes.CompTeleOps.CompTeleOpTemplate;
import org.firstinspires.ftc.teamcode.CompOpModes.RobotOpMode;

@TeleOp
public class LimelightLocalizationTest extends CompTeleOpTemplate {
    @Override
    public void runInLoop(){
        telemetry.addData("DeadWheel Pose",robot.followerSubsystem.getFollower().getPose());
        telemetry.addData("Limelight Pose",robot.limelightSubsystem.getAprilTagPose());

    }

    @Override
    public void setAllianceColor(){
        allianceColor=AllianceColor.RED;
    }
}
