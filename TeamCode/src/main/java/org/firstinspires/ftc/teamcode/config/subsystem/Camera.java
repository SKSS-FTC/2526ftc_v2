package org.firstinspires.ftc.teamcode.config.subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class Camera {
    private boolean visionEnable;
    private AprilTagProcessor myAprilTagProcessor;
    private Position cameraPosition;
    private YawPitchRollAngles cameraOrientation;
    private VisionPortal myVisionPortal;
    private int ID;
    private double x,y,z,pitch,roll,yaw;



    public Camera(HardwareMap hardwareMap) {

        AprilTagProcessor.Builder myAprilTagProcessorBuilder;
        VisionPortal.Builder myVisionPortalBuilder;

        // First, create an AprilTagProcessor.Builder.
        myAprilTagProcessorBuilder = new AprilTagProcessor.Builder();
        myAprilTagProcessorBuilder.setCameraPose(cameraPosition, cameraOrientation);
        // Create an AprilTagProcessor by calling build.
        myAprilTagProcessor = myAprilTagProcessorBuilder.build();
        // Next, create a VisionPortal.Builder and set attributes related to the camera.
        myVisionPortalBuilder = new VisionPortal.Builder();
        myVisionPortalBuilder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        // Add myAprilTagProcessor to the VisionPortal.Builder.
        myVisionPortalBuilder.addProcessor(myAprilTagProcessor);
        // Create a VisionPortal by calling build.
        myVisionPortal = myVisionPortalBuilder.build();

        visionEnable = false;
        ID = 0;
    }

    public void disableCamera(){
        myVisionPortal.stopStreaming();
        visionEnable = false;
    }

    public void enableCamera(){
        myVisionPortal.resumeStreaming();
        visionEnable = true;
    }

    public boolean isCameraEnable(){
        return visionEnable;
    }

    public void update(){
        List<AprilTagDetection> myAprilTagDetections;
        AprilTagDetection myAprilTagDetection;

        // Get a list of AprilTag detections.
        myAprilTagDetections = myAprilTagProcessor.getDetections();
        // Iterate through list and call a function to display info for each recognized AprilTag.
        for (AprilTagDetection myAprilTagDetection_item : myAprilTagDetections) {
            myAprilTagDetection = myAprilTagDetection_item;
            // Display info about the detection.
            if (myAprilTagDetection.metadata != null) {
                ID = myAprilTagDetection.id;
                x = myAprilTagDetection.robotPose.getPosition().x;
                y = myAprilTagDetection.robotPose.getPosition().y;
                z = myAprilTagDetection.robotPose.getPosition().z;
                pitch = myAprilTagDetection.robotPose.getOrientation().getPitch();
                roll = myAprilTagDetection.robotPose.getOrientation().getRoll();
                yaw = myAprilTagDetection.robotPose.getOrientation().getYaw();

            } else {
                ID = 0;
                x = 0;
                y = 0;
                z = 0;
                pitch = 0;
                roll = 0;
                yaw = 0;
            }
        }
    }
    public int getID(){
        return ID;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getZ(){
        return z;
    }
    public double getYaw(){
        return yaw;
    }
    public double getPitch(){
        return pitch;
    }
    public double getRoll(){
        return roll;
    }
}
