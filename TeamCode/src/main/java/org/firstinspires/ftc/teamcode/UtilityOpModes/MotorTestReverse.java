package org.firstinspires.ftc.teamcode.UtilityOpModes;

import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.RobotConfig;

@TeleOp
public class MotorTestReverse extends OpMode {

    private DcMotorEx motor;

    @Override
    public void init(){
        motor = hardwareMap.get(DcMotorEx.class, RobotConfig.DriveConstants.frontLeftWheelName);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop(){
        motor.setPower(gamepad1.right_stick_y);
    }
}
