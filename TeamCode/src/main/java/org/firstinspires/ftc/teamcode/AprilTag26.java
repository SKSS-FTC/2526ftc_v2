package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;

@TeleOp
public class AprilTag26 extends OpMode {
    AprilTagProcessor processor;
    VisionPortal vPortal;
    Servo servo;
    int width = 1920;
    @Override
    public void init(){
        servo=hardwareMap.get(Servo.class,"servo");
        processor =  AprilTagProcessor.easyCreateWithDefaults();
        vPortal=new VisionPortal.Builder()
                .setCamera(hardwareMap.get(CameraName.class,"Webcam 1"))
                .addProcessor(processor)
                .setCameraResolution(new Size(width,1080))
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .enableLiveView(true)
                .setAutoStopLiveView(true)
                .build();
        telemetry.addLine("Finished Init");
        telemetry.update();
        servo.setPosition(.6);
    }

    @Override
    public void loop(){
        double k =.0000000012;
        ArrayList<AprilTagDetection> detections = processor.getDetections();
        boolean isdetected = false;
        double heading =0;
        for (AprilTagDetection detection: detections) {
            if (true||detection.metadata!=null) {
                if (detection.id == 23) {
                    telemetry.addData("X",detection.center.x);
                    heading = width/2-detection.center.x;
                    heading = heading*Math.abs(heading);
                    telemetry.addData("bearing", heading);
                    isdetected = true;
                }
            }else{
                telemetry.addLine("Detection is null");
                telemetry.addData("Id",detection.center.x);
            }

        }
        servo.setPosition(servo.getPosition() + heading * k);
        telemetry.addData("Servo Pos",servo.getPosition());
        if (!isdetected){

            telemetry.addLine("No tag detected");

        }
        if (detections!= null){
            telemetry.addData("Num Detections",detections.size());
        }
        telemetry.update();


    }

}
