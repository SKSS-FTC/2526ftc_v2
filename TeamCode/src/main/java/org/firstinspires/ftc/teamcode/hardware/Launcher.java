package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.software.TrajectoryEngine;

public class Launcher {
    // TODO turret
    private final TrajectoryEngine trajectoryEngine;

    public Servo horizontalServo;
    public Servo verticalServo;
    public DcMotor turretLauncherRight;
    public DcMotor turretLauncherLeft;
    private MatchSettings.ArtifactColor chamberedColor = null;
    public Launcher(TrajectoryEngine trajectoryEngine) {
        this.trajectoryEngine = trajectoryEngine;
    }

    public void launch() {
        // if (trajectoryEngine.okayToLaunch()) {
        // TODO launch
        // }
    }

    public void load(MatchSettings.ArtifactColor color) {
        this.chamberedColor = color;
    }

}
