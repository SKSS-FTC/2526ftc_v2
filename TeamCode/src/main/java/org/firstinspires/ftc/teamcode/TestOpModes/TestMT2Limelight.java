package org.firstinspires.ftc.teamcode.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Vision.LimelightTest;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;


@TeleOp
public class TestMT2Limelight extends OpMode {


    LimelightTest limelightTest;


    public void init(){
        limelightTest = new LimelightTest(hardwareMap);

    }
    public void loop(){
        limelightTest.run();
        Pose pose = limelightTest.getCurPose();
        telemetry.addData("Pose X (reg coords)",pose.getX());
        telemetry.addData("Pose Y (reg coords)",pose.getY());
        telemetry.addData("Pose Heading (reg coords)",pose.getX());
        telemetry.update();
    }
}
