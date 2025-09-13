package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.configuration.Settings;
import org.firstinspires.ftc.teamcode.hardware.submechanisms.ColorSensor;

public class Intake {
    public final DcMotor intakeMotor;

    public final ColorSensor colorSensor;


    public MatchSettings.ArtifactColor expectation;

    public Intake(DcMotor intakeMotor, RevColorSensorV3 intakeColorSensor) {
        this.intakeMotor = intakeMotor;
        colorSensor = new ColorSensor(intakeColorSensor);
        this.expectation = MatchSettings.ArtifactColor.UNKNOWN;
    }

    public void in() {
        intakeMotor.setPower(Settings.Hardware.Intake.SPEED);
    }

    public void out() {
        intakeMotor.setPower(-Settings.Hardware.Intake.SPEED);
    }

    public void stop() {
        intakeMotor.setPower(0);
    }

    public void init() {
        colorSensor.init();
    }

    public void update() {
        MatchSettings.ArtifactColor artifact = colorSensor.getArtifactColor();
        if (artifact == expectation || expectation == MatchSettings.ArtifactColor.UNKNOWN) {
            // if there is no expectation or it matches, let it through and reset expectation
            this.expectation = MatchSettings.ArtifactColor.UNKNOWN;
        } else {
            // the artifact is not the one we want, so spit it out
            out();
        }
    }

    public void setNextExpectedBall(MatchSettings.ArtifactColor expectation) {
        this.expectation = expectation;
    }

    public void reset() {
        colorSensor.reset();
    }
}