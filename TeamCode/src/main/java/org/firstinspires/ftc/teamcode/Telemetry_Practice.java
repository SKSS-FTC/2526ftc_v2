package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Telemetry Practice")
public class Telemetry_Practice extends LinearOpMode {


private final DcMotor Motor = hardwareMap.dcMotor.get("Motor 1");


private double counter = 0.1;


@Override
public void runOpMode() throws InterruptedException {

    Motor.setDirection(DcMotorSimple.Direction.FORWARD);


    waitForStart();
    if (isStopRequested()) return;
    mainTeleOpLoop();
}


private void mainTeleOpLoop() throws InterruptedException {
    while (opModeIsActive()) {
        if (counter <=1) {
            Motor.setPower(counter);
        }
        telemetry.addData("Status", "Running");
        telemetry.addData("Hello World!", "Game");
        telemetry.update();
        counter += 0.1;

        }

    }
}
