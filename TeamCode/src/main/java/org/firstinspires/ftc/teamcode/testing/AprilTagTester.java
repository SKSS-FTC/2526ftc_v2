package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.AprilTag;

@TeleOp(name = "AprilTagTester", group = "TeleOp")
public class AprilTagTester extends LinearOpMode{
    AprilTag aprilTag = new AprilTag(hardwareMap);

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        while (opModeIsActive()) {
            boolean foundTag = aprilTag.scan();
            if (foundTag) {
                telemetry.addData("Id: ",aprilTag.getId());
                telemetry.addData("Elevation: ",aprilTag.getElevation());
                telemetry.addData("Range: ",aprilTag.getRange());
                telemetry.addData("Bearing: ",aprilTag.getBearing());
                telemetry.update();
            }
        }
    }
}