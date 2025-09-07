package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "Specimen2, claw no")
// lift up to rung
// forward to rung
// lift up
// release
public class TwoSpecimenNoClaw extends LinearOpMode {
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
                .forward(26)
                .build();
        Trajectory trajFix = drive.trajectoryBuilder(new Pose2d())
                .forward(2)
                .build();
        Trajectory traj6 = drive.trajectoryBuilder(new Pose2d())
                .forward(10)
                .build();
        Trajectory traj7 = drive.trajectoryBuilder(new Pose2d())
                .forward(-10)
                .build();
        waitForStart();
        drive.followTrajectory(traj1);
        drive.followTrajectory(traj2);
        move.armMove(-2050, 100, telemetry);
        move.linearSlide(-1405, 100, telemetry);
        //sleep(30000);
        drive.followTrajectory(traj3);
        move.claw(true);
        sleep(1500);
        move.linearSlide(-1785, 100, telemetry);
        move.pivot(.7);
        sleep(500);
        drive.followTrajectory(trajFix);
        sleep(1000);

        move.claw(false);
        //sleep(2000);
        //move.pivot(0.5);
        sleep(1000);
        drive.followTrajectory(traj4);
        move.claw(true);
        move.linearSlide(0, 100, telemetry);
        drive.followTrajectory(traj5);
        drive.turn(Math.toRadians(87));
        drive.followTrajectory(traj6);
        move.armMove(0, 100, telemetry);
        move.linearSlide(-1500, 100, telemetry);
        sleep(1000);
        move.linearSlide(0, 100, telemetry);
        move.armMove(-2050, 100, telemetry);
        drive.followTrajectory(traj7);
        drive.turn(Math.toRadians(87));
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
