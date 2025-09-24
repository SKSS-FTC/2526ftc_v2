package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.robotcore.external.navigation.*;
import org.firstinspires.ftc.teamcode.Chassis;

/**
 * Example OpMode. Demonstrates use of gyro, color sensor, encoders, and telemetry.
 *
 */
@TeleOp(name = ".XendyOpModeTesting", group = "MecanumBot")
public class Teleop extends LinearOpMode {

    public void runOpMode(){
        Chassis chassis = new Chassis(hardwareMap);
        waitForStart();
        while (opModeIsActive()){
            double px = gamepad1.left_stick_x;
            double py = gamepad1.left_stick_y;
            double pa = gamepad1.right_stick_x;
            chassis.update(telemetry);
            chassis.moveFieldRelative(px, py, pa);
//
//            if(gamepad1.y) {
//                chassis.frontLeft.setPower(0.5);
//            } else {
//                chassis.frontLeft.setPower(0);
//            }
//            if(gamepad1.b) {
//                chassis.frontRight.setPower(0.5);
//            } else {
//                chassis.frontRight.setPower(0);
//            }
//            if(gamepad1.x) {
//                chassis.backRight.setPower(0.5);
//            } else {
//                chassis.backRight.setPower(0);
//            }
//            if(gamepad1.a) {
//                chassis.backLeft.setPower(0.5);
//            } else {
//                chassis.backLeft.setPower(0);
//            }

            telemetry.update();
        }
    }
}
