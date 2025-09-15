package org.firstinspires.ftc.teamcode.UtilityOpModes;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Vision.AprilTagReader;
import org.firstinspires.ftc.teamcode.Vision.NoAprilTagFoundException;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import static java.lang.Math.PI;

@TeleOp(name = "April Tag Reader Test",group = "Utility")
public class AprilTagReaderTest extends OpMode {
    private AprilTagReader aprilTagReader;
    private Follower follower;
    private Pose curPose;


    @Override
    public void init(){
        AprilTagProcessor aprilTagProcessor= new AprilTagProcessor.Builder()
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
//                .setLensIntrinsics()
                .setLensIntrinsics(
                        RobotConfig.CameraConstants.fx,
                        RobotConfig.CameraConstants.fy,
                        RobotConfig.CameraConstants.cx,
                        RobotConfig.CameraConstants.cy
                )
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();

        VisionPortal visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTagProcessor)
                .setCameraResolution(new Size(640,480))
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .setAutoStopLiveView(true)
                .build();
        aprilTagReader = new AprilTagReader(aprilTagProcessor,telemetry);

        follower = new Follower(hardwareMap);

        follower.setPose(new Pose(0,0,3*PI/2));
    }


    @Override
    public void loop(){
        try {
            curPose = aprilTagReader.readTag();
            telemetry.addLine("=================April Tag Coords====================");
            telemetry.addData("Robot X (Reg Coords)",curPose.getX());
            telemetry.addData("Robot Y (Reg Coords)",curPose.getY());
            telemetry.addData("Robot Heading",curPose.getHeading());

        } catch (NoAprilTagFoundException e) {
            telemetry.addLine("No Tag Detected");
        }
        telemetry.addLine("=================Pedro Path Coords====================");
        telemetry.addData("Robot X (Pedro Coords)",follower.getPose().getX());
        telemetry.addData("Robot Y (Pedro Coords)",follower.getPose().getY());
        telemetry.addData("Robot Heading",follower.getPose().getHeading());



        telemetry.update();
    }
}
