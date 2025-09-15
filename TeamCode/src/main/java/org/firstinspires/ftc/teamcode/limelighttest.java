package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLFieldMap;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.Rev9AxisImuOrientationOnRobot;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.ArrayList;
import java.util.List;


public class limelighttest {
    private Limelight3A limelight3A;
    private LLFieldMap llFieldMap;
    private IMU imu;
    public limelighttest(HardwareMap hMap){
        limelight3A=hMap.get(Limelight3A.class,"limelight");
        List<Double> fid24Transform = new ArrayList<>();
        fid24Transform.add(-0.5877852522924729);
        fid24Transform.add(-0.8090169943749473);
        fid24Transform.add(0.);
        fid24Transform.add(1.4859);
        fid24Transform.add(0.8090169943749473);
        fid24Transform.add(-0.5877852522924729);
        fid24Transform.add(0.);
        fid24Transform.add(-1.4351);
        fid24Transform.add(0.);
        fid24Transform.add(0.);
        fid24Transform.add(1.);
        fid24Transform.add(0.7736);
        fid24Transform.add(0.);
        fid24Transform.add(0.);
        fid24Transform.add(0.);
        fid24Transform.add(1.);

        List<Double> fid20Transform = new ArrayList<>();
        fid20Transform.add(-0.5877852522924729);
        fid20Transform.add(0.8090169943749473);
        fid20Transform.add(0.);
        fid20Transform.add(1.4859);
        fid20Transform.add(-0.8090169943749473);
        fid20Transform.add(-0.5877852522924729);
        fid20Transform.add(0.);
        fid20Transform.add(1.4351);
        fid20Transform.add(0.);
        fid20Transform.add(0.);
        fid20Transform.add(1.);
        fid20Transform.add(0.7736);
        fid20Transform.add(0.);
        fid20Transform.add(0.);
        fid20Transform.add(0.);
        fid20Transform.add(1.);

        List<LLFieldMap.Fiducial> fiducials = new ArrayList<>();
        fiducials.add(new LLFieldMap.Fiducial(24,165.1,"36h11",fid24Transform,true));
        fiducials.add(new LLFieldMap.Fiducial(20,165.1,"36h11",fid20Transform,true));
        imu=hMap.get(IMU.class,"imu");
        IMU.Parameters imuParams = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )
        );
        imu.initialize(imuParams);
        llFieldMap=new LLFieldMap(fiducials,"ftc");
        limelight3A.uploadFieldmap(llFieldMap,5);
        limelight3A.start();
        limelight3A.pipelineSwitch(5);


    }
    public String doCamera(){
        limelight3A.updateRobotOrientation(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        LLResult result =limelight3A.getLatestResult();

        return result.getBotpose_MT2().toString();
    }
}
