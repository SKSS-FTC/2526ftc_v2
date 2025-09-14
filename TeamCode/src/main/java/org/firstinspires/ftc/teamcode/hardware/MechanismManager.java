package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.configuration.Settings;
import org.firstinspires.ftc.teamcode.software.AlignmentEngine;
import org.firstinspires.ftc.teamcode.software.Drivetrain;
import org.firstinspires.ftc.teamcode.software.LimelightManager;
import org.firstinspires.ftc.teamcode.software.TrajectoryEngine;

public class MechanismManager {
    public Intake intake;
    public Sorter sorter;

    public GoBildaPinpointDriver pinpoint;
    public Launcher launcher;
    public LimelightManager limelightManager;

    public AlignmentEngine alignmentEngine;
    public TrajectoryEngine trajectoryEngine;
    public Drivetrain drivetrain;

    public MechanismManager(HardwareMap hardwareMap, MatchSettings matchSettings) {
        drivetrain = new Drivetrain(hardwareMap);

        ColorSensor colorSensor = new ColorSensor(hardwareMap.get(RevColorSensorV3.class, Settings.Hardware.IDs.COLOR_SENSOR));
        DcMotor intakeMotor = hardwareMap.get(DcMotor.class, Settings.Hardware.IDs.INTAKE_MOTOR);
        Servo[] intakeServoArray = new Servo[4];
        for (int i = 0; i < 4; i++) {
            intakeServoArray[i] = hardwareMap.get(Servo.class, Settings.Hardware.IDs.INTAKE_SERVO_ARRAY[i]);
        }

        intake = new Intake(intakeMotor, intakeServoArray, colorSensor);

        limelightManager = new LimelightManager(hardwareMap.get(Limelight3A.class, Settings.Hardware.IDs.LIMELIGHT), matchSettings);
        GoBildaPinpointDriver pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, Settings.Hardware.IDs.PINPOINT);
        trajectoryEngine = new TrajectoryEngine(limelightManager, pinpoint, matchSettings);
        alignmentEngine = new AlignmentEngine(matchSettings, drivetrain, limelightManager, pinpoint);

        Servo sorterServo = hardwareMap.get(Servo.class, Settings.Hardware.IDs.SORTER_SERVO);
        Servo turretTransferServo = hardwareMap.get(Servo.class, Settings.Hardware.IDs.TURRET_TRANSFER_SERVO);
        ColorSensor sorterColorSensor = new ColorSensor(hardwareMap.get(RevColorSensorV3.class, Settings.Hardware.IDs.SORTER_COLOR_SENSOR));
        sorter = new Sorter(sorterServo, turretTransferServo, sorterColorSensor, matchSettings);

        DcMotor turretLauncherRight = hardwareMap.get(DcMotor.class, Settings.Hardware.IDs.TURRET_LAUNCHER_RIGHT);
        DcMotor turretLauncherLeft = hardwareMap.get(DcMotor.class, Settings.Hardware.IDs.TURRET_LAUNCHER_LEFT);
        Servo horizontalServo = hardwareMap.get(Servo.class, Settings.Hardware.IDs.TURRET_HORIZONTAL_SERVO);
        Servo verticalServo = hardwareMap.get(Servo.class, Settings.Hardware.IDs.TURRET_VERTICAL_SERVO);
        launcher = new Launcher(sorter, turretLauncherRight, turretLauncherLeft, horizontalServo, verticalServo, trajectoryEngine);
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
