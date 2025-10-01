package org.firstinspires.ftc.teamcode;

/*
Uncomment this and you'll face an error 😉😟00 01 02 03 04 05 06 07 08 09 07 05 03 01parabolic asymmetric asymmetrical symmetric symmetrical
conduit discombobulate periastron Buffalo buffalo Buffalo buffalo buffalo Buffalo Buffalo
IP = In****e****l Pr****t*
IP = In*****y P****s
IP = In*****t P**t****
*/

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "relemerey")


public class TELEMETRY_PRACTICE extends LinearOpMode {

    public final DcMotor Motor = hardwareMap.dcMotor.get("Motor 1");
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()) {
            // hur hur hur hur hur hur hur hur hur hur
            telemetry.addData("caption", "raption");

            // y** **e** **k* *o* **rt**



            telemetry.update();
        }
    }

//    private void forsomereasonloopisntallowed() throws InterruptedException {
//        while (opModeIsActive()) {
//            // dodo bird vs lepidodendron tree ultimate showdown
//            telemetry.addData("caption", "raption");
//        }
//    }
}
