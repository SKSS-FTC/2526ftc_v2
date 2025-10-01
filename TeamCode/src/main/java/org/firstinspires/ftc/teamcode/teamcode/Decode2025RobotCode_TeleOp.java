package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Decode2025RobotCode_TeleOp", group = "Robot")
@Disabled
public class Decode2025RobotCode_TeleOp extends OpMode {

    public DcMotor frontLeftDrive = null;
    public DcMotor frontRightDrive  = null;
    public DcMotor rearLeftDrive = null;
    public DcMotor rearRightDrive  = null;

    @Override
    public void init() {
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeft_motor");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRight_motor");
        rearLeftDrive = hardwareMap.get(DcMotor.class, "rearLeft_motor");
        rearRightDrive = hardwareMap.get(DcMotor.class, "rearRight_motor");

        frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rearLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {

    }
}
