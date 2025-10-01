package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
public class RobotControls {
    private final LinearOpMode linearOpMode;

    public RobotControls(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
    }

    boolean shootArtifact;
    boolean intakeArtifact;


    public void updateControls() {
        shootArtifact = linearOpMode.gamepad2.a;
        intakeArtifact = linearOpMode.gamepad2.b;
    }
}
