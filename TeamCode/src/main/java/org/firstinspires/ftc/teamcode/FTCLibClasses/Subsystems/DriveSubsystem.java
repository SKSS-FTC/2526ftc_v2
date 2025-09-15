package org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.RobotConfig;

import java.util.function.DoubleSupplier;

public class DriveSubsystem extends SubsystemBase {

    private  MecanumDrive drive;
    private  DoubleSupplier forward;
    private  DoubleSupplier strafe;
    private  DoubleSupplier rotate;
    private  ToggleButtonReader toggleFieldToBotCentric;
    private  DoubleSupplier heading;

    private double startHeading;

    private IMU imu;
    private int lockCount=0;
    private double timer=0;
    MotorEx leftFront;
    MotorEx rightFront;
    MotorEx backLeft;
    MotorEx backRight;


    private String telemetry = "";


    private GamepadEx gamepadEx;

    public DriveSubsystem(HardwareMap hMap, GamepadEx gamepad, IMU imu){
        leftFront = new MotorEx(hMap,RobotConfig.DriveConstants.frontLeftWheelName);
        rightFront = new MotorEx(hMap,RobotConfig.DriveConstants.frontRightWheelName);
        backLeft = new MotorEx(hMap,RobotConfig.DriveConstants.backLeftWheelName);
        backRight = new MotorEx(hMap,RobotConfig.DriveConstants.backRightWheelName);

        leftFront.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);



//        backLeft.setInverted(true);
//        backRight.setInverted(true);

        imu.initialize(RobotConfig.DriveConstants.compIMUOrientation);

        this.drive = new MecanumDrive(false,leftFront, rightFront,backLeft,backRight);
        forward = gamepad::getLeftY;
        strafe = gamepad::getLeftX;
        rotate = gamepad::getRightX;
        toggleFieldToBotCentric = new ToggleButtonReader(gamepad, GamepadKeys.Button.RIGHT_BUMPER);
        startHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        heading = () -> imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)-startHeading;
        this.imu = imu;

        this.gamepadEx = gamepad;


    }

    public void setBot(Bot bot){
        if (bot == Bot.COMP){
            imu.initialize(RobotConfig.DriveConstants.compIMUOrientation);
        } else {
            imu.initialize(RobotConfig.DriveConstants.practiceIMUOrientation);
        }
    }

    public String getTelemetry(){
        String tele = "In FTC Lib Drive"+telemetry;
        telemetry = "";
        return tele;
    }


    public void driverControlDrive(){

        double heading = gamepadEx.getRightX();
        leftFront.setInverted(true);
        rightFront.setInverted(false);

//        if (Math.abs(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)%(Math.PI/4))<=Math.toRadians(5)){
//            heading = 0;
//            lockCount++;
//        }
//
//        if (lockCount>10){
//            heading = gamepadEx.getRightX();
//            lockCount = 0;
//        }

        drive.driveRobotCentric(
                strafe.getAsDouble(),
                forward.getAsDouble(),
                rotate.getAsDouble(),
                //heading,
                true
        );

        if (gamepadEx.gamepad.left_bumper&&gamepadEx.gamepad.right_bumper){
            startHeading = this.heading.getAsDouble();
            telemetry = telemetry + "\nForward Set";
        }

    }




//
//    public void doTeleOp(){
//        if (curCommand!=null){
//            curCommand.cancel();
//            curCommand = null;
//            isCurCommandScheduled =false;
//        }
//        isAuto = false;
//    }



}





