package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class DriveTrainBacking extends LinearOpMode {
    private DriveTrain driveTrain;

    @Override
    public void runOpMode() throws InterruptedException {
        driveTrain = new DriveTrain(this);
        driveTrain.configureMotorModes();

        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()) {
            driveTrain.setMovePower(-0.5);

        }

    }
}
