package org.firstinspires.ftc.teamcode.software;

import com.pedropathing.localization.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResult;

import org.firstinspires.ftc.teamcode.Controller;
import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.configuration.Settings;
import org.firstinspires.ftc.teamcode.hardware.MechanismManager;
import org.firstinspires.ftc.teamcode.hardware.submechanisms.LimelightManager;

public class AlignmentEngine {
    private final Controller controller;
    private final Drivetrain drivetrain;
    private final MechanismManager mechanisms;
    private final GoBildaPinpointDriver pinpoint;
    private final MatchSettings matchSettings;
    private final LimelightManager limelightManager;
    private double storedTx;

    public AlignmentEngine(Controller controller, MatchSettings matchSettings, Drivetrain drivetrain, MechanismManager mechanisms, LimelightManager limelightManager, GoBildaPinpointDriver pinpoint) {
        this.controller = controller;
        this.drivetrain = drivetrain;
        this.mechanisms = mechanisms;
        this.pinpoint = pinpoint;
        this.matchSettings = matchSettings;
        this.limelightManager = limelightManager;
        this.storedTx = 0;
    }

    public void check() {
        LLResult llResult = limelightManager.detectGoal(matchSettings);
        boolean artifactDetected = llResult.isValid();
        // TODO everything past this is old
        boolean headingAligned = Math.abs(wrappedHeading()) < 10; // TODO make this a setting

        if (artifactDetected && headingAligned) {
            if (Settings.Assistance.use_deadeye) {
                drivetrain.state = Drivetrain.State.DEADEYE_ENABLED;
                double Tx = limelightManager.limelight.getLatestResult().getTx();
                controller.setLedColor(0, 0, 255, 1000);
                drivetrain.interpolateToOffset(
                        Tx,
                        Settings.Assistance.approachSpeed,
                        wrappedHeading());
                storedTx = Tx;
            } else {
                controller.setLedColor(0, 255, 0, 1000);
                controller.rumble(50);
                drivetrain.state = Drivetrain.State.DEFAULT;
            }
        } else if ((drivetrain.state == Drivetrain.State.DEADEYE_ENABLED) && Settings.Assistance.use_deadeye && storedTx != 0) {
            controller.setLedColor(255, 0, 255, 1000);
            drivetrain.interpolateToOffset(
                    limelightManager.limelight.getLatestResult().getTx(),
                    Settings.Assistance.approachSpeed,
                    wrappedHeading());
        } else {
            storedTx = 0;
            if (Settings.Assistance.use_deadeye) {
                controller.setLedColor(0, 255, 255, 1000);
                drivetrain.interpolateToOffset(0, 0, wrappedHeading());
                drivetrain.state = Drivetrain.State.DEADEYE_ENABLED;
            } else {
                controller.setLedColor(255, 0, 0, 1000);
                drivetrain.state = Drivetrain.State.DEFAULT;
            }
        }
    }

    private double wrappedHeading() {
        return (pinpoint.getHeading() + Math.PI) % (2 * Math.PI) - Math.PI;
    }
}
