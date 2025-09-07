package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Movement;

@Autonomous(name = "auto-direction-test")
public class AutoDirectionTest extends LinearOpMode{
    public void runOpMode() {
        Movement move = new Movement();
        move.initialize(hardwareMap);


        // Wait for the DS start button to be touched.
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch Play to start OpMode");
        telemetry.update();
        waitForStart();


        if (opModeIsActive()) {
            //move.moveForward(-500, 25, telemetry); - forward
            //move.strafe(500, 25, telemetry); - left
            //move.turn(500, 25); - left
            //move.linearSlide(-500, 25, telemetry); - extends
            move.armMove(-1800, 40, telemetry); // up
            move.linearSlide(-1800, 25, telemetry); // extends
        }
    }
}