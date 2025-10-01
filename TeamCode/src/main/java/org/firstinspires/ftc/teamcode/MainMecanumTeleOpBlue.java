package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Main Mecanum TeleOp Blue")
public class MainMecanumTeleOpBlue extends LinearOpMode {

    private ArtifactHandlingSystem artifactHandlingSystem;

    private RobotControls robotControls;
    private DriveTrain driveTrain;

    @Override
    public void runOpMode() throws InterruptedException {
        artifactHandlingSystem = new ArtifactHandlingSystem(this);
        robotControls = new RobotControls(this);
        driveTrain = new DriveTrain(this);

        configureMotorModes();

        waitForStart();
        if (isStopRequested()) return;
        mainTeleOpLoop();
    }

    private void mainTeleOpLoop() throws InterruptedException {
        while (opModeIsActive()) {
            robotControls.updateControls();

            driveTrain.adjustTurnSpeed();
            driveTrain.setMotorPowers();
            driveTrain.resetYaw();
            artifactHandlingSystem.shootArtifact(robotControls.shootArtifact);
            artifactHandlingSystem.intakeArtifact(robotControls.intakeArtifact);

            displayTelemetry();
        }
    }

    private void configureMotorModes() {
        artifactHandlingSystem.configureMotorModes();
        driveTrain.configureMotorModes();
    }

    private void displayTelemetry() {
        driveTrain.displayTelemetry();
        artifactHandlingSystem.displayTelemetry();

        telemetry.update();
    }
}
