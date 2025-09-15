package org.firstinspires.ftc.teamcode.TestOpModes;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test.TutorialDriveSubsystem;

@Disabled
@TeleOp
public class DriveTest extends OpMode {

    private TutorialDriveSubsystem drive;
    private GamepadEx drivePad;

    @Override
    public void init(){

        drivePad = new GamepadEx(gamepad1);
        IMU imu = hardwareMap.get(IMU.class,"imu");
        drive = new TutorialDriveSubsystem(hardwareMap,drivePad,imu);
    }

    @Override
    public void loop(){
        drivePad.readButtons();
        CommandScheduler.getInstance().run();
    }
}
