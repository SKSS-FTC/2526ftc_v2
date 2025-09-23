package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.CameraControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;

@TeleOp
public class WebcamTest extends OpMode {

    AprilTagProcessor aprilTagProcessor;
    VisionPortal visionPortal;
    @Override
    public void init(){
        aprilTagProcessor=  new AprilTagProcessor.Builder()

                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class,"Webcam 1"))
                .setCameraResolution(new Size(640,480))
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .addProcessor(aprilTagProcessor)
                .build();


        telemetry.addLine("Ready to go");
        telemetry.update();
    }


    @Override
    public void loop(){
        ArrayList<AprilTagDetection> detections = aprilTagProcessor.getFreshDetections();
        double tx=-1000;
        boolean isNull= true;
        if (detections!= null) {
            isNull=false;
            for (AprilTagDetection detection : detections) {

                if (detection.metadata != null) {  // This check for non-null Metadata is not needed for reading only ID code.
                    if (detection.id == 20) {
                        tx = detection.ftcPose.bearing;
                    }

                    // Now take action based on this tag's ID code, or store info for later action.

                }
            }
        }
        telemetry.addData("Tx",tx);
        telemetry.addData("Sees",isNull);
        telemetry.update();
    }
}
