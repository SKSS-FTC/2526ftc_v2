package org.firstinspires.ftc.teamcode;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;


@Autonomous(name = "roadrunnerTest")
public class roadrunnerTest extends LinearOpMode{
    public void runOpMode () {
        Pose2d initialPose = new Pose2d(0, 0, Math.toRadians(0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        TrajectoryActionBuilder traj1 = drive.actionBuilder(initialPose)
                .splineToLinearHeading(new Pose2d(60, 0, Math.toRadians(0)), Math.toRadians(90))
                .turn(Math.PI/2);
        TrajectoryActionBuilder traj2 = drive.actionBuilder(new Pose2d(60, 0, Math.toRadians(90)))
                .splineToLinearHeading(new Pose2d(60, 60, Math.toRadians(90)), Math.toRadians(180))
                .turn(Math.PI/2);
        TrajectoryActionBuilder traj3 = drive.actionBuilder(new Pose2d(60, 60, Math.toRadians(180)))
                .splineToLinearHeading(new Pose2d(0, 60, Math.toRadians(180)), Math.toRadians(270))
                .turn(Math.PI/2);
        TrajectoryActionBuilder traj4 = drive.actionBuilder(new Pose2d(0, 60, Math.toRadians(270)))
                .splineToLinearHeading(new Pose2d(0, 0, Math.toRadians(270)), Math.toRadians(0))
                .turn(Math.PI/2);
        if (isStopRequested()) return;
        waitForStart();
        int i = 0;
        Actions.runBlocking(
                new SequentialAction(
                        traj1.build(),
                        traj2.build(),
                        traj3.build(),
                        traj4.build()
                )
        );
        Pose2d pose = drive.localizer.getPose();
        String posePrint = pose.toString();
        telemetry.addData("Position ", posePrint);
        telemetry.update();
    }
}
