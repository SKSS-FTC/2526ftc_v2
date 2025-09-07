package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "Parking")
public class AutoPark extends LinearOpMode {
    public void runOpMode() {
        Movement move = new Movement();
        move.initialize(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d())
                .forward(55)
                .build();

        waitForStart();
        drive.followTrajectory(traj1);
        move.armMove(-2050, 100, telemetry);
    }
}
