package org.firstinspires.ftc.teamcode.pedroPathing.localization.localizers;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Encoder;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Localizer;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;

public class TestTwoWheelOdometry  {
    private Encoder forwardEncoder;
    private Pose forwardEncoderPose;
    private double forwardTicksToInches = RobotConfig.DriveConstants.forwardMultiplier;

    private Encoder strafeEncoder;
    private Pose strafeEncoderPose;
    private double lateralTicksToInches = RobotConfig.DriveConstants.lateralMultiplier;


    private double forwardEncoderRadius;
    private double strafeEncoderRadius;

    //ALL ANGLES ARE IN RADIANS
    private IMU imu;
    private double lastHeading = 0;

    private Pose curPose;




    public TestTwoWheelOdometry(HardwareMap hMap){
        forwardEncoder = new Encoder(hMap.get(DcMotorEx.class, RobotConfig.DriveConstants.frontLeftWheelName));
        strafeEncoder = new Encoder(hMap.get(DcMotorEx.class, RobotConfig.DriveConstants.backLeftWheelName));

        imu = hMap.get(IMU.class,"imu");

        forwardEncoderPose = new Pose(-.25, 6.89, 0);
        strafeEncoderPose = new Pose(-3.25, 2.5, Math.toRadians(90));

        forwardEncoderRadius = Math.abs(forwardEncoderPose.getY());
        strafeEncoderRadius = Math.abs(strafeEncoderPose.getX());


        forwardEncoder.reset();
        strafeEncoder.reset();

        curPose = new Pose(0,0,0);
    }



    public void update(){
        double deltaHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) - lastHeading;

        double sinDeltaHeading = Math.sin(deltaHeading);

        forwardEncoder.update();
        strafeEncoder.update();

        //First, convert the rotation into how far the forward/left encoders should have went forwards/sideways
        //We assume that the bot only rotated
        double assumedDeltaForwardEncoder = sinDeltaHeading*forwardEncoderRadius*Math.atan(forwardEncoderPose.getX()/forwardEncoderPose.getY());
        double assumedDeltaStrafeEncoder = sinDeltaHeading*strafeEncoderRadius*Math.atan(strafeEncoderPose.getX()/strafeEncoderPose.getY());

        //Next, compare to actual deltas to see if the bot also moved translationally
        //These differences will be what the bot moved translationally


        //Remember, X is forward, and Y is left
        double deltaX = forwardEncoder.getDeltaPosition()-assumedDeltaForwardEncoder;
        double deltaY = strafeEncoder.getDeltaPosition()-assumedDeltaStrafeEncoder;

        deltaX*=forwardTicksToInches;
        deltaY*=lateralTicksToInches;

        curPose.setX(curPose.getX()+deltaX);
        curPose.setY(curPose.getY()+deltaY);

        curPose.setHeading(curPose.getHeading()+deltaHeading);
    }

    public Pose getPose(){
        return curPose;
    }



}
