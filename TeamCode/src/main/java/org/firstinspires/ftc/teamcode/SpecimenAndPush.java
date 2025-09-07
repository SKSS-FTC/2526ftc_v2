package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "Specimen Push")
// lift up to rung
// forward to rung
// lift up
// release
public class SpecimenAndPush extends LinearOpMode {
    public void runOpMode() {
        Movement move = new Movement();
        move.initialize(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d())
                .forward(43)
                .build();
        Trajectory traj2 = drive.trajectoryBuilder(new Pose2d())
                .forward(-43)
                .build();
        Trajectory traj3 = drive.trajectoryBuilder(new Pose2d())
                .strafeRight(10)
                .build();
        Trajectory traj4 = drive.trajectoryBuilder(new Pose2d())
                .strafeRight(8)
                .build();
        drive.followTrajectory(traj1);
        drive.followTrajectory(traj2);
        drive.followTrajectory(traj3);
        drive.followTrajectory(traj1);
        drive.followTrajectory(traj2);
        drive.followTrajectory(traj4);
        drive.followTrajectory(traj1);


    }
}
