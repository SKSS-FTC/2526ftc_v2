package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class RobotControls {
    private final LinearOpMode linearOpMode;

    public RobotControls(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
    }

    boolean intakeArtifact;
    float shootArtifact;
    boolean manuallySend;
    boolean celebrate;
    boolean increaseHoodAngle;
    boolean decreaseHoodAngle;
    public void updateControls() {
        shootArtifact = linearOpMode.gamepad2.right_trigger;
        intakeArtifact = linearOpMode.gamepad2.a;
        manuallySend = linearOpMode.gamepad2.b;
        celebrate = linearOpMode.gamepad2.back;
        increaseHoodAngle = linearOpMode.gamepad2.dpad_up;
        decreaseHoodAngle = linearOpMode.gamepad2.dpad_down;
    }
}
