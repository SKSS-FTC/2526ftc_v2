package org.firstinspires.ftc.teamcode.software;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.configuration.Settings;
import org.firstinspires.ftc.teamcode.configuration.Settings.Alignment;

import java.util.HashMap;
import java.util.Map;

public class Drivetrain {
	
	public final DcMotor frontLeftMotor;
	public final DcMotor frontRightMotor;
	public final DcMotor rearLeftMotor;
	public final DcMotor rearRightMotor;
	public final Map<String, DcMotor> motors = new HashMap<>();
	public final State state = State.MANUAL;
	
	public Drivetrain(HardwareMap hardwareMap) {
		frontLeftMotor = hardwareMap.get(DcMotor.class, Settings.Hardware.IDs.FRONT_LEFT_MOTOR);
		frontRightMotor = hardwareMap.get(DcMotor.class, Settings.Hardware.IDs.FRONT_RIGHT_MOTOR);
		rearLeftMotor = hardwareMap.get(DcMotor.class, Settings.Hardware.IDs.REAR_LEFT_MOTOR);
		rearRightMotor = hardwareMap.get(DcMotor.class, Settings.Hardware.IDs.REAR_RIGHT_MOTOR);
		
		// IF A WHEEL IS GOING THE WRONG DIRECTION CHECK WIRING red/black
		frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
		frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
		rearLeftMotor.setDirection(DcMotor.Direction.FORWARD);
		rearRightMotor.setDirection(DcMotor.Direction.REVERSE);
		
		frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		rearLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		rearRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		
		motors.put(Settings.Hardware.IDs.FRONT_LEFT_MOTOR, frontLeftMotor);
		motors.put(Settings.Hardware.IDs.FRONT_RIGHT_MOTOR, frontRightMotor);
		motors.put(Settings.Hardware.IDs.REAR_LEFT_MOTOR, rearLeftMotor);
		motors.put(Settings.Hardware.IDs.REAR_RIGHT_MOTOR, rearRightMotor);
	}
	
	/**
	 * Implements mecanum drive calculations and motor control
	 *
	 * @param drivePower  Forward/backward power (-1.0 to 1.0)
	 * @param strafePower Left/right strafe power (-1.0 to 1.0)
	 * @param rotation    Rotational power (-1.0 to 1.0)
	 **/
	public void mecanumDrive(double drivePower, double strafePower, double rotation, boolean asDeadeye) {
		if (state != State.MANUAL) {
			return; // automation is handling driving, do not manually drive
		}
		// Adjust the values for strafing and rotation
		strafePower *= Settings.Teleop.strafe_power_coefficient;
		double frontLeft = (drivePower + strafePower) + rotation;
		double frontRight = (drivePower - strafePower) - rotation;
		double rearLeft = (drivePower - strafePower) + rotation;
		double rearRight = (drivePower + strafePower) - rotation;
		
		frontLeftMotor.setPower(frontLeft);
		frontRightMotor.setPower(frontRight);
		rearLeftMotor.setPower(rearLeft);
		rearRightMotor.setPower(rearRight);
	}
	
	// mecanum drive assumes you are not driving as deadeye
	
	public void interpolateToOffset(double offsetX, double offsetY, double offsetHeading) {
		// translational distance
		double dist = Math.hypot(offsetX, offsetY);
		
		// linear ramp for translational speed
		double translationalScale = Math.min(1.0,
				Math.max(0.0, (dist - Alignment.stopDistance) /
						(Alignment.fullSpeedDistance - Alignment.stopDistance)));
		
		double drivePower = -offsetY / dist * translationalScale * Alignment.maxTranslationalSpeed;
		double strafePower = -offsetX / dist * translationalScale * Alignment.maxTranslationalSpeed;
		
		// rotation
		double absErr = Math.abs(offsetHeading);
		double rotation = 0.0;
		if (absErr > Alignment.headingDeadband) {
			double rotScale = Math.min(1.0, absErr / Alignment.fullSpeedHeadingError);
			rotation = Math.copySign(rotScale * Alignment.maxRotationSpeed, offsetHeading);
		}
		
		mecanumDrive(drivePower, strafePower, rotation);
	}
	
	public void mecanumDrive(double drivePower, double strafePower, double rotation) {
		mecanumDrive(drivePower, strafePower, rotation, false);
	}
	
	/**
	 * @noinspection EmptyMethod for now
	 */
	public void goTo(Position position) {
		// TODO automatic positions
	}
	
	public enum Position {
		CLOSE_SHOOT,
		FAR_SHOOT,
		HUMAN_PLAYER,
		SECRET_TUNNEL,
	}
	
	public enum State {
		MANUAL,
		AIMING,
		GOTO,
	}
}
