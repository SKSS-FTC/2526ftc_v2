package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "Specimen")
// lift up to rung
// forward to rung
// lift up
// release
public class SpecimenHang extends LinearOpMode {
    public void runOpMode() {
        Movement move = new Movement();
        move.initialize(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d())
                .forward(-21.5)
                .build();
        Trajectory traj2 = drive.trajectoryBuilder(new Pose2d())
                .strafeRight(5)
                .build();
        Trajectory traj3 = drive.trajectoryBuilder(new Pose2d())
                .forward(-3.25)
                .build();
        Trajectory traj4 = drive.trajectoryBuilder(new Pose2d())
                .forward(2.1)
                .build();
        Trajectory traj5 = drive.trajectoryBuilder(new Pose2d())
                .forward(18.5)
                .build();
//        Trajectory traj6 = drive.trajectoryBuilder(new Pose2d())
//                .strafeLeft(85)
//                .build();
        Trajectory trajFix = drive.trajectoryBuilder(new Pose2d())
                .forward(2)
                .build();
        Trajectory traj6 = drive.trajectoryBuilder(new Pose2d())
                .forward(12.5)
                .build();
        Trajectory traj7 = drive.trajectoryBuilder(new Pose2d())
                .forward(-7.5)
                .build();
        Trajectory traj8 = drive.trajectoryBuilder(new Pose2d())
                .forward(-18)
                .build();
        Trajectory traj9 = drive.trajectoryBuilder(new Pose2d())
                .forward(-3)
                .build();
        waitForStart();
        drive.followTrajectory(traj1);
        drive.followTrajectory(traj2);
        move.armMove(-2050, 100, telemetry);
        move.linearSlide(-1100, 100, telemetry);
        drive.followTrajectory(traj3);
        move.claw(true);
        sleep(400);
        move.linearSlide(-1485, 100, telemetry);
        move.pivot(.5);
        sleep(900);
        //drive.followTrajectory(trajFix);
        //sleep(400);

        move.claw(false);
        sleep(400);
        drive.followTrajectory(traj4);
        move.pivot(0);
        move.claw(true);
        move.linearSlide(0, 100, telemetry);
        drive.followTrajectory(traj5);

        drive.turn(Math.toRadians(80));
        drive.followTrajectory(traj6);
        move.armMove(0, 100, telemetry);
        move.pivot(1);
        move.claw(false );
        sleep(200);
        move.linearSlide(-3000, 100, telemetry);
        move.claw(true);
        sleep(300);
        move.pivot(0);
        sleep(300);
        move.linearSlide(0, 100, telemetry);
        move.armMove(-2050, 100, telemetry);
        drive.followTrajectory(traj7);
        drive.turn(Math.toRadians(-76));
        drive.followTrajectory(traj8);
        move.linearSlide(-1100, 100, telemetry);
        drive.followTrajectory(traj9);
        move.linearSlide(-1485, 100, telemetry);
        move.pivot(.5);
        sleep(400);
        drive.followTrajectory(trajFix);
        sleep(700);
        move.claw(false);
        sleep(1000);
    }
}
