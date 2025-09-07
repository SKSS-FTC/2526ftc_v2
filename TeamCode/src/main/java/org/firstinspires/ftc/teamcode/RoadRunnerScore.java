package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "SlowScoreBasket")
public class RoadRunnerScore extends LinearOpMode {
    public void runOpMode() {
        Movement move = new Movement();
        move.initialize(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d())
                .strafeLeft(28)
                .build();
        Trajectory traj2 = drive.trajectoryBuilder(new Pose2d())
                .forward(-35)
                .build();
        Trajectory traj3 = drive.trajectoryBuilder(new Pose2d())
                .forward(1)
                .build();
        Trajectory traj4 = drive.trajectoryBuilder(new Pose2d())
                .forward(18)
                .build();
        Trajectory traj5 = drive.trajectoryBuilder(new Pose2d())
                .strafeLeft(49)
                .build();
        Trajectory traj6 = drive.trajectoryBuilder(new Pose2d())
                .forward(-21)
                .build();
        waitForStart();
        drive.followTrajectory(traj1);
        drive.turn(Math.toRadians(43));
        drive.followTrajectory(traj2);
        move.armMove(-2050, 100, telemetry);
        move.linearSlide(-3040, 100, telemetry);
        move.pivot(.3);
        sleep(1000);
        move.claw(false);
        sleep(2000);
        move.claw(true);
        move.pivot(1);
        sleep(1000);
        //drive.followTrajectory(traj3);
        move.linearSlide(0, 100, telemetry);
        //move.armMove(0, 100, telemetry);
        //move.stopSlide();
        drive.turn(Math.toRadians(-45));
        drive.followTrajectory(traj4);
        drive.followTrajectory(traj5);
        drive.turn(Math.toRadians(150));
        drive.followTrajectory(traj6);
        sleep(1000);
        move.pivot(0);
        move.armMove(-2050, 100, telemetry);
    }
}
