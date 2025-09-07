package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Movement;

@Autonomous(name = "score")
public class ScoreAuto extends LinearOpMode{
    public void runOpMode() {
        Movement move = new Movement();
        move.initialize(hardwareMap);


        // Wait for the DS start button to be touched.
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch Play to start OpMode");
        telemetry.update();
        waitForStart();


        if (opModeIsActive()) {
            //move.strafe(-500, 50);
            //move.moveForward(-800, 25, telemetry);
            move.strafe(-400, 50, telemetry);
            move.moveForward(-1200, 50, telemetry);
            move.turn(-750, 50);
            move.moveForward(-1050, 100, telemetry);
            move.turn(-3000, 70);
            move.moveForward(-2800, 80, telemetry);
            move.armMove(-1990, 100, telemetry);
            move.linearSlide(-2925, 100, telemetry);
            //move.armMove(-1, 50, telemetry);
            move.pivot(0);
            sleep(500);
            move.claw(false);
            sleep(2000);
            move.claw(true);
            move.pivot(1);
            move.linearSlide(0, 100, telemetry);
            //move.left(-400, 25);
            //move.claw(false);
            //sleep(1000);
            //move.claw(true);
            //move.linearSlide(0, 60, telemetry);
            //move.armMove(0, 10, telemetry, -.1);
        }
    }
}