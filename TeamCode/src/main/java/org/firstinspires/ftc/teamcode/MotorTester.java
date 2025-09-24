package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.EK10582.EKLinear;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;
//@Disabled
@TeleOp(name="Motor Tester")
public class MotorTester extends LinearOpMode {
    @Override
    public void runOpMode() {

        waitForStart();

        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, "leftFront");

        while(opModeIsActive()) {

            motor.setPower(gamepad1.left_trigger - gamepad1.right_trigger);

            telemetry.addData("motor speed: ", gamepad1.right_trigger-gamepad1.left_trigger);
//            telemetry.addData("position: ", motor.getCurrentPosition());
            telemetry.update();
        }
    }
}