package org.nknsd.teamcode.programs.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name = "Speed Test")
public class MotorSpeedTester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor dcMotor = hardwareMap.dcMotor.get("spin");

        waitForStart();

        dcMotor.setPower(1);
        while (opModeIsActive()) {
            int x = dcMotor.getCurrentPosition(); // encoder value

            //sleep(1000);

            //int y = x - dcMotor.getCurrentPosition();

            telemetry.addData("Ticks", x);
            telemetry.update();
        }
    }
}
