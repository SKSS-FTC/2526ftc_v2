package org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.RobotConfig;

public class TutorialDriveSubsystem extends SubsystemBase {

    private MecanumDrive drive;
    private GamepadEx gamepad;
    private IMU imu;

    public TutorialDriveSubsystem(HardwareMap hMap, GamepadEx gamepad, IMU imu){
        drive = new MecanumDrive(
                new MotorEx(hMap, RobotConfig.DriveConstants.frontLeftWheelName),
                new MotorEx(hMap,RobotConfig.DriveConstants.frontRightWheelName),
                new MotorEx(hMap,RobotConfig.DriveConstants.backLeftWheelName),
                new MotorEx(hMap,RobotConfig.DriveConstants.backRightWheelName)
        );

        this.gamepad = gamepad;
        this.imu = imu;
    }

    @Override
    public void periodic(){
        drive.driveFieldCentric(
                gamepad.getLeftX(),
                -gamepad.getLeftY(),
                gamepad.getRightX(),
                imu.getRobotYawPitchRollAngles().getYaw()
        );
    }
}








