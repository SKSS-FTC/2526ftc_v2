package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@TeleOp(name = "Pivot")
public class PivotTest extends LinearOpMode {
    public void runOpMode() {
        Movement move = new Movement();
        move.initialize(hardwareMap);
        waitForStart();
        while(opModeIsActive()) {
            if(gamepad1.a) {
                waitForStart();
                move.pivot(0.082);
                sleep(1000);
                move.armMove(-2050, 100, telemetry);
                move.linearSlide(-1405, 100, telemetry);
                move.claw(true);
            }
            if(gamepad1.b) {
                move.linearSlide(-1785, 100, telemetry);
                move.pivot(.7);
                move.claw(false);
                sleep(1000);
                move.linearSlide(0, 100, telemetry);
            }
        }
    }
}
