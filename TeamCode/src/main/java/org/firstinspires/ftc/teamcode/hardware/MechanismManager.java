package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.configuration.Settings;

public class MechanismManager {
    public Intake intake;

    public MechanismManager(HardwareMap hardwareMap) {
        if (Settings.Deploy.INTAKE) {
            Limelight3A limelight3A = hardwareMap.get(Limelight3A.class, Settings.Hardware.IDs.LIMELIGHT);
            RevColorSensorV3 colorSensor = hardwareMap.get(RevColorSensorV3.class, Settings.Hardware.IDs.COLOR_SENSOR);
            DcMotor intakeMotor = hardwareMap.get(DcMotor.class, Settings.Hardware.IDs.INTAKE_MOTOR);
            intake = new Intake(intakeMotor, colorSensor);
        }
    }

    public void init() {
        if (Settings.Deploy.INTAKE) {
            intake.init();
        }
    }

    public void reset() {
        if (Settings.Deploy.INTAKE) {
            intake.reset();
        }
    }
}
