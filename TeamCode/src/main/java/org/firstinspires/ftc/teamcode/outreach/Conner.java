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
@TeleOp(name = "Outreach", group = "Outreach")
public class Conner extends OpMode {
	public DcMotor motor;
	
	
	@Override
	public final void init() {
		motor = hardwareMap.get(DcMotor.class, "frontLeft");
	}
	
	
	public final void loop() {
		motor.setPower(gamepad1.right_trigger - gamepad1.left_trigger);
	}
}