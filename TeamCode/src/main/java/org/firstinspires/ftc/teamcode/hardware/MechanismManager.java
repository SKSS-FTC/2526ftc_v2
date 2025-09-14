package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.configuration.Settings;
import org.firstinspires.ftc.teamcode.software.LimelightManager;
import org.firstinspires.ftc.teamcode.software.TrajectoryEngine;

public class MechanismManager {
    public Intake intake;
    public Sorter sorter;

    public MechanismManager(HardwareMap hardwareMap, MatchSettings matchSettings) {
        ColorSensor colorSensor = new ColorSensor(hardwareMap.get(RevColorSensorV3.class, Settings.Hardware.IDs.COLOR_SENSOR));
        DcMotor intakeMotor = hardwareMap.get(DcMotor.class, Settings.Hardware.IDs.INTAKE_MOTOR);
        Servo[] intakeServoArray = new Servo[4];
        for (int i = 0; i < 4; i++) {
            intakeServoArray[i] = hardwareMap.get(Servo.class, Settings.Hardware.IDs.INTAKE_SERVO_ARRAY[i]);
        }

        intake = new Intake(intakeMotor, intakeServoArray, colorSensor);

        LimelightManager limelightManager = new LimelightManager(hardwareMap.get(Limelight3A.class, Settings.Hardware.IDs.LIMELIGHT));
        GoBildaPinpointDriver pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, Settings.Hardware.IDs.PINPOINT);
        TrajectoryEngine trajectoryEngine = new TrajectoryEngine(limelightManager, pinpoint, matchSettings);
        Launcher launcher = new Launcher(trajectoryEngine);
        Servo sorterServo = hardwareMap.get(Servo.class, Settings.Hardware.IDs.SORTER_SERVO);
        Servo turretTransferServo = hardwareMap.get(Servo.class, Settings.Hardware.IDs.TURRET_TRANSFER_SERVO);
        ColorSensor sorterColorSensor = new ColorSensor(hardwareMap.get(RevColorSensorV3.class, Settings.Hardware.IDs.SORTER_COLOR_SENSOR));
        sorter = new Sorter(launcher, sorterServo, turretTransferServo, sorterColorSensor);
    }

    public void init() {
        if (Settings.Deploy.INTAKE) {
            intake.init();
            sorter.init();
        }
    }

    public void update() {
        if (Settings.Deploy.INTAKE) {
            intake.update();
            sorter.update();
        }
    }
}
