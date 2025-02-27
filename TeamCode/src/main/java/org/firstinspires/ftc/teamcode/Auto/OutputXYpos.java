package org.firstinspires.ftc.teamcode.Auto;

import static org.firstinspires.ftc.teamcode.ODO.GoBildaPinpointDriver.EncoderDirection.FORWARD;
import static org.firstinspires.ftc.teamcode.ODO.GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Mekanism.Mekanism;
import org.firstinspires.ftc.teamcode.ODO.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.Swerve.wpilib.geometry.Rotation2d;

public class OutputXYpos extends LinearOpMode {
    private GoBildaPinpointDriver odometry;
    private Mekanism mek;

    @Override
    public void runOpMode() throws InterruptedException {
        odometry = hardwareMap.get(GoBildaPinpointDriver.class, "odo");
        odometry.recalibrateIMU();
        odometry.resetPosAndIMU();
        odometry.setOffsets(110, 30);
        odometry.setEncoderResolution(goBILDA_4_BAR_POD);
        odometry.setEncoderDirections(FORWARD, FORWARD);
        odometry.resetHeading(Rotation2d.fromDegrees(120));

        waitForStart();
        while (opModeIsActive()) {
            odometry.update();
            telemetry.addData("Heading:", odometry.getHeading());
            telemetry.addData("  X pos:", odometry.getPosX());
            telemetry.addData("  Y pos:", odometry.getPosY());
            telemetry.update();
        }// While opmode active
    }// run Op Mode
}// OutputXY end