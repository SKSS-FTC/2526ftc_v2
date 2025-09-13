package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.configuration.Settings;

public class MechanismManager {
    public Intake intake;
    public Sorter sorter;

    public MechanismManager(HardwareMap hardwareMap) {
        if (Settings.Deploy.INTAKE) {
            Limelight3A limelight3A = hardwareMap.get(Limelight3A.class, Settings.Hardware.IDs.LIMELIGHT);
            RevColorSensorV3 colorSensor = hardwareMap.get(RevColorSensorV3.class, Settings.Hardware.IDs.COLOR_SENSOR);
            DcMotor intakeMotor = hardwareMap.get(DcMotor.class, Settings.Hardware.IDs.INTAKE_MOTOR);
            intake = new Intake(intakeMotor, colorSensor);

        }
        if (Settings.Deploy.SORTER) {
            Servo sorterServo = hardwareMap.get(Servo.class, Settings.Hardware.IDs.SORTER_SERVO);
            sorter = new Sorter(sorterServo);
        }
    }

    public void init() {
        if (Settings.Deploy.INTAKE) {
            intake.init();
            sorter.init();
        }
    }

    public void reset() {
        if (Settings.Deploy.INTAKE) {
            intake.reset();
            sorter.reset();
        }
    }
}
