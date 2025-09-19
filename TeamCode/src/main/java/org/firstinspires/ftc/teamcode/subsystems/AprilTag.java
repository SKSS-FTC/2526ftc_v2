package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class AprilTag {
    private double id;
    private double bearing;
    private double elevation;
    private double range;
    private final VisionPortal portal;
    private final AprilTagProcessor processor;

    public AprilTag(HardwareMap hardwareMap) {
        AprilTagLibrary library = AprilTagGameDatabase.getCurrentGameTagLibrary();
        processor = new AprilTagProcessor.Builder()
                .setTagLibrary(library)
                .build();
        portal = VisionPortal.easyCreateWithDefaults(hardwareMap.get(WebcamName.class, "webcam"), processor);
    }

    public void toggle(boolean bool) {
        portal.setProcessorEnabled(processor, bool);
    }

    public boolean scan() {
        boolean foundTag = false;
        List<AprilTagDetection> detectionList = processor.getDetections();

        for (AprilTagDetection detection : detectionList) {
            foundTag = true;

            id = detection.id;

            range = detection.ftcPose.range; // distance to tag center
            bearing = detection.ftcPose.bearing; // the angle (left/right) the camera must turn to directly point at the tag center
            elevation = detection.ftcPose.elevation; // the angle (up/down) the camera must turn to directly point at the tag center
        }
        return foundTag;
    }

    public double getId(){
        return id;
    }
    public double getElevation(){
        return elevation;
    }
    public double getRange(){
        return range;
    }
    public double getBearing(){
        return bearing;
    }
}
