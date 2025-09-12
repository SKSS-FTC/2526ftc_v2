package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.hardware.submechanisms.ColorSensor;

public class Intake {
    public final DcMotor intakeMotor;

    public final ColorSensor colorSensor;

    public Intake(DcMotor intakeMotor, RevColorSensorV3 intakeColorSensor) {
        this.intakeMotor = intakeMotor;
        colorSensor = new ColorSensor(intakeColorSensor);
    }

    public void init() {
        colorSensor.init();
    }

    public void reset() {
        colorSensor.reset();
    }
}