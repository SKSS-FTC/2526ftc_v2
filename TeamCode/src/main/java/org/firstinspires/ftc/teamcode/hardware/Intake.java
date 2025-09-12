package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.configuration.Settings;
import org.firstinspires.ftc.teamcode.hardware.submechanisms.ColorSensor;

public class Intake {
    public final DcMotor intakeMotor;

    public final ColorSensor colorSensor;

    public Intake(DcMotor intakeMotor, RevColorSensorV3 intakeColorSensor) {
        this.intakeMotor = intakeMotor;
        colorSensor = new ColorSensor(intakeColorSensor);
    }

    public void in() {
        intakeMotor.setPower(Settings.Hardware.Intake.SPEED);
    }

    public void out() {
        intakeMotor.setPower(-Settings.Hardware.Intake.SPEED);
    }

    public void releaseExtras() {
        // TODO complex logic
    }

    public void releasePurple() {
        // TODO complex logic
    }

    public void releaseGreen() {
        // TODO complex logic
    }

    public void stop() {
        intakeMotor.setPower(0);
    }

    public void init() {
        colorSensor.init();
    }

    public void reset() {
        colorSensor.reset();
    }
}