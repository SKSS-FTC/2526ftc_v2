package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp
public class PracticeBot extends OpMode {
    MecanumDrive mecanumDrive;
    @Override
    public void init(){

        Motor fl = new MotorEx(hardwareMap,"fl");
        Motor fr = new MotorEx(hardwareMap,"fr");
        Motor bl = new MotorEx(hardwareMap,"bl");
        Motor br = new MotorEx(hardwareMap,"br");

        mecanumDrive=new MecanumDrive(true,fl,fr,bl, br);
    }
    @Override
    public void loop(){
        mecanumDrive.driveRobotCentric(gamepad1.right_stick_x,-gamepad1.right_stick_y,-gamepad1.left_stick_x,true);
        telemetry.addData("LeftX: ",gamepad1.left_stick_x);
        telemetry.addData("RightX: ",gamepad1.right_stick_x);
        telemetry.update();
    }
}
