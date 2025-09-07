package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "Specimen Park")
// lift up to rung
// forward to rung
// lift up
// release
public class SpecimenPark extends LinearOpMode {
    public void runOpMode() {
        Movement move = new Movement();
        move.initialize(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d())
                .forward(-21)
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
                .forward(22)
                .build();
//        Trajectory traj6 = drive.trajectoryBuilder(new Pose2d())
//                .strafeLeft(85)
//                .build();
        Trajectory trajFix = drive.trajectoryBuilder(new Pose2d())
                .forward(2)
                .build();
        Trajectory traj6 = drive.trajectoryBuilder(new Pose2d())
                .forward(65)
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
        drive.followTrajectory(trajFix);
        sleep(400);

        move.claw(false);
        sleep(400);
        drive.followTrajectory(traj4);
        move.pivot(0);
        move.claw(true);
        move.linearSlide(0, 100, telemetry);
        drive.followTrajectory(traj5);

        drive.turn(Math.toRadians(80));
        drive.followTrajectory(traj6);

    }
}
