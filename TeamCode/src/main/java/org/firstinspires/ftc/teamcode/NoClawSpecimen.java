package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "SpecimenNoClaw")
// lift up to rung
// forward to rung
// lift up
// release
public class NoClawSpecimen extends LinearOpMode {
    public void runOpMode() {
        Movement move = new Movement();
        move.initialize(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d())
                .forward(-26)
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
                .forward(3)
                .build();
        Trajectory traj6 = drive.trajectoryBuilder(new Pose2d())
                .strafeLeft(30)
                .build();
        Trajectory trajFix = drive.trajectoryBuilder(new Pose2d())
                .forward(2)
                .build();
        Trajectory trajFix2 = drive.trajectoryBuilder(new Pose2d())
                .forward(-32)
                .build();
        Trajectory traj7 = drive.trajectoryBuilder(new Pose2d())
                .strafeLeft(7)
                .build();
        Trajectory traj8 = drive.trajectoryBuilder(new Pose2d())
                .forward(43)
                .build();
        Trajectory traj9 = drive.trajectoryBuilder(new Pose2d())
                .forward(-43)
                .build();
        Trajectory traj10 = drive.trajectoryBuilder(new Pose2d())
                .strafeLeft(10)
                .build();
        Trajectory traj11 = drive.trajectoryBuilder(new Pose2d())
                .strafeLeft(8)
                .build();
        waitForStart();
        drive.followTrajectory(traj1);
        drive.followTrajectory(traj2);
        move.armMove(-2050, 100, telemetry);
        move.linearSlide(-1405, 100, telemetry);
        //sleep(30000);
        drive.followTrajectory(traj3);
        sleep(1500);
        move.linearSlide(-1785, 100, telemetry);
        sleep(500);
        drive.followTrajectory(trajFix);
        sleep(1000);
        //sleep(2000);
        //move.pivot(0.5);
        sleep(1000);
        drive.followTrajectory(traj4);
        move.linearSlide(0, 100, telemetry);
        drive.followTrajectory(traj5);
        drive.followTrajectory(traj6);
        drive.followTrajectory(trajFix2);
        drive.followTrajectory(traj7);

        // hhhhhhhhhhhhhhhhhhhhhhhh
        drive.followTrajectory(traj8);
        drive.followTrajectory(traj9);
        drive.followTrajectory(traj10);
        drive.followTrajectory(traj8);
        drive.followTrajectory(traj9);
        drive.followTrajectory(traj11);
        drive.followTrajectory(traj8);
        //move.armMove(0, 100, telemetry);

        //move.pivot(.5);
//        sleep(1000);
//        //move.pivot(0);
//        sleep(3000);
//        move.claw(false);
//        sleep(2000);
//        move.linearSlide(-1800, 100, telemetry);
//        move.claw(true);
//        move.pivot(.5);
//        sleep(5000);
        //move.linearSlide(0, 1000, telemetry);
        //drive.followTrajectory(traj3);
        //move.linearSlide(0, 100, telemetry);
        //move.claw(true);
        //move.pivot(1);

    }
}
