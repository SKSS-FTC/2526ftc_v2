package org.firstinspires.ftc.teamcode.outreach;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Main TeleOp class for driver-controlled period.
 * Handles controller profile selection and robot operation during matches.
 *
 * @noinspection ClassWithoutConstructor
 */
@TeleOp(name = "SimpleDrivetrain", group = "Outreach")
public class SimpleDrivetrain extends OpMode {
	public DcMotor frontLeftMotor;
	public DcMotor frontRightMotor;
	public DcMotor backLeftMotor;
	public DcMotor backRightMotor;
	
	
	@Override
	public final void init() {
		frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
		frontRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
		backLeftMotor = hardwareMap.get(DcMotor.class, "rearLeft");
		backRightMotor = hardwareMap.get(DcMotor.class, "rearRight");
		
		frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
		backRightMotor.setDirection(DcMotor.Direction.REVERSE);
		frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
	}
	
	
	public final void loop() {
		double drivePower = -gamepad1.left_stick_y;
		double strafePower = gamepad1.left_stick_x;
		double rotatePower = gamepad1.right_stick_x;
		
		if (gamepad1.right_bumper) {
			rotatePower = -0.1;
		}
		if (gamepad1.left_bumper) {
			rotatePower = 0.1;
		}
		if (gamepad1.dpad_up) {
			drivePower = 0.2;
		}
		if (gamepad1.dpad_down) {
			drivePower = -0.2;
		}
		if (gamepad1.dpad_left) {
			strafePower = -0.2;
		}
		if (gamepad1.dpad_right) {
			strafePower = 0.2;
		}
		
		mecanumDrive(strafePower, drivePower, rotatePower);
	}
	
	public void mecanumDrive(double strafePower, double drivePower, double rotatePower) {
		double frontLeftPower = drivePower + strafePower + rotatePower;
		double frontRightPower = drivePower - strafePower - rotatePower;
		double backLeftPower = drivePower - strafePower + rotatePower;
		double backRightPower = drivePower + strafePower - rotatePower;
		
		frontLeftMotor.setPower(frontLeftPower);
		frontRightMotor.setPower(frontRightPower);
		backLeftMotor.setPower(backLeftPower);
		backRightMotor.setPower(backRightPower);
	}
}