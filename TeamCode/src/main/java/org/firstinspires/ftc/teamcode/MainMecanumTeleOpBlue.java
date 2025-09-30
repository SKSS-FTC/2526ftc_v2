package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Main Mecanum TeleOp Blue")
public class MainMecanumTeleOpBlue extends LinearOpMode {

    private ArtifactHandlingSystem artifactHandlingSystem;

    private RobotControls controls;

    @Override
    public void runOpMode() throws InterruptedException {
        artifactHandlingSystem = new ArtifactHandlingSystem(this);
        controls = new RobotControls(this);

        configureMotorModes();

        waitForStart();
        if (isStopRequested()) return;
        mainTeleOpLoop();
    }

    private void mainTeleOpLoop() throws InterruptedException {
        while (opModeIsActive()) {
            controls.updateControls();

            artifactHandlingSystem.shootArtifact(controls.shootArtifact);

            displayTelemetry();
        }
    }

    private void configureMotorModes() {
        artifactHandlingSystem.configureMotorModes();
    }

    private void displayTelemetry() {
        artifactHandlingSystem.displayTelemetry();

        telemetry.update();
    }
}
