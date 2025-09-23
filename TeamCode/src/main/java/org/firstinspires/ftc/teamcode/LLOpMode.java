package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import java.util.List;

public class LLOpMode {
    Limelight3A limelight;
    private LinearOpMode opmode = null;
    public void init(LinearOpMode opMode) {
        HardwareMap hwMap;
        hwMap = opMode.hardwareMap;

        limelight = hwMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.start();
        opMode.telemetry.addLine("Color siers");
        limelight.pipelineSwitch(7);
    }

    public List fiducialResult;

//    LLresult result = limelight.getLatestResult();
//
//    List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
//    for (LLResultTypes.FiducialResult fiducial : fiducials) {
//        int id = fiducial.getFiducialId();
//        opmode.telemetry.addData("Fiducial " + id);
//    }
}
