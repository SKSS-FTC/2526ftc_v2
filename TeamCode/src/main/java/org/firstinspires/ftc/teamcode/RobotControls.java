package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class RobotControls {
    private final LinearOpMode linearOpMode;

    public RobotControls(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
    }

    boolean intakeArtifact;
    float shootArtifact;
    boolean resetYaw;
    float leftStickX;
    float leftStickY;
    float rightStickX;
    boolean fastMode;
    boolean slowMode;
    boolean strafeToClassifier;
    public void updateControls() {
        shootArtifact = linearOpMode.gamepad2.right_trigger;
        intakeArtifact = linearOpMode.gamepad2.a;
        resetYaw = linearOpMode.gamepad1.start;
        leftStickX = -linearOpMode.gamepad1.left_stick_x;
        leftStickY = linearOpMode.gamepad1.left_stick_y;
        rightStickX = -linearOpMode.gamepad1.right_stick_x;
        fastMode = linearOpMode.gamepad1.left_bumper;
        slowMode = linearOpMode.gamepad1.right_bumper;
        strafeToClassifier = linearOpMode.gamepad1.back;
    }
}
