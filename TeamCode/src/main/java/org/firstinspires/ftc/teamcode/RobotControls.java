package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class RobotControls {
    private final LinearOpMode linearOpMode;

    public RobotControls(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
    }

    boolean shootArtifact;

    public void updateControls() {
        shootArtifact = linearOpMode.gamepad2.a;
    }
}
