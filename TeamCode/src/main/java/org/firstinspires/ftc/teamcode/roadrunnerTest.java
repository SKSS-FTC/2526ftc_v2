
package org.firstinspires.ftc.teamcode;
import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "roadrunnerTest")
public class roadrunnerTest extends LinearOpMode{
    public void runOpMode () {
        Pose2d initialPose = new Pose2d(11.8, 61.7, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        TrajectoryActionBuilder traj1 = drive.actionBuilder(initialPose)
            .lineToY(-12)
            .waitSeconds(2)
            .lineToY(24);
        TrajectoryActionBuilder traj2 = drive.actionBuilder(initialPose)
            .lineToY(10)
            .setTangent(Math.toRadians(0))
            .lineToX(10);
        
        if (isStopRequested()) return;

        waitForStart();

        Actions.runBlocking(
            new SequentialAction(
                traj1.build(),
                traj2.build()
            )
        );

    }
}
