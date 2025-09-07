package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "trajectory-test")
public class TrajectoryTest extends LinearOpMode {
    public void runOpMode() {
        Movement move = new Movement();
        move.initialize(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d())
                .forward(20)
                .build();
        Trajectory traj2 = drive.trajectoryBuilder(new Pose2d())
                .forward(40)
                .build();
        waitForStart();
        drive.followTrajectory(traj2);
        drive.turn(Math.toRadians(-90));
        drive.followTrajectory(traj1);
        drive.turn(Math.toRadians(-90));
        drive.followTrajectory(traj2);
        move.armMove(-1990, 100, telemetry);
        move.linearSlide(-2925, 100, telemetry);
        move.pivot(0);
        sleep(500);
        move.claw(false);
        sleep(2000);
        move.claw(true);
        move.pivot(1);
        move.linearSlide(0, 100, telemetry);
        move.armMove(0, 100, telemetry);
    }
}
