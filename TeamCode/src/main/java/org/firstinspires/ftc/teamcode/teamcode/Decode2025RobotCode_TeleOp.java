package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Decode2025RobotCode_TeleOp", group = "Robot")
@Disabled
public class Decode2025RobotCode_TeleOp extends OpMode {

    public DcMotor frontLeftDrive = null;
    public DcMotor frontRightDrive  = null;
    public DcMotor rearLeftDrive = null;
    public DcMotor rearRightDrive  = null;

    private IMU imu;

    @Override
    public void init() {
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeft_motor");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRight_motor");
        rearLeftDrive = hardwareMap.get(DcMotor.class, "rearLeft_motor");
        rearRightDrive = hardwareMap.get(DcMotor.class, "rearRight_motor");

        frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rearLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot RevOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);

        imu.initialize(new IMU.Parameters(RevOrientation));
    }

    public void drive(double forward, double strafe, double rotate) {
        double frontLeftDrive = forward + strafe + rotate;
        double rearLeftDrive = forward - strafe + rotate;
        double frontRightDrive = forward - strafe - rotate;
        double rearRightDrive = forward + strafe - rotate;

        double maxPower = 1.0;
        double maxSpeed = 1.0;

        maxPower = Math.max(maxPower, Math.abs(frontLeftDrive));
        maxPower = Math.max(maxPower, Math.abs(rearLeftDrive));
        maxPower = Math.max(maxPower, Math.abs(frontRightDrive));
        maxPower = Math.max(maxPower, Math.abs(rearRightDrive));

        this.frontLeftDrive.setPower(maxSpeed * (frontLeftDrive/ maxPower));
        this.rearRightDrive.setPower(maxSpeed * (rearRightDrive/ maxPower));
        this.rearLeftDrive.setPower(maxSpeed * (rearLeftDrive/ maxPower));
        this.frontRightDrive.setPower(maxSpeed * (frontRightDrive/ maxPower));

    }



    @Override
    public void loop() {

    }
}

