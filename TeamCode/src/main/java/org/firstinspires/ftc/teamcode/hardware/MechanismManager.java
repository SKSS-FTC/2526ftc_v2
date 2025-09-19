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
	public final Intake intake;
	public final Spindex spindex;
	public final Launcher launcher;
	public final LimelightManager limelightManager;
	public final AlignmentEngine alignmentEngine;
	public final TrajectoryEngine trajectoryEngine;
	public final Drivetrain drivetrain;
	public GoBildaPinpointDriver pinpoint;
	
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
		
		Servo spindexServo = hardwareMap.get(Servo.class, Settings.Hardware.IDs.SPINDEX_SERVO);
		Servo launcherTransferServo = hardwareMap.get(Servo.class, Settings.Hardware.IDs.LAUNCHER_TRANSFER_SERVO);
		ColorSensor spindexColorSensor = new ColorSensor(hardwareMap.get(RevColorSensorV3.class, Settings.Hardware.IDs.SPINDEX_COLOR_SENSOR));
		Servo intakeTransferServo = hardwareMap.get(Servo.class, Settings.Hardware.IDs.INTAKE_TRANSFER_SERVO);
		spindex = new Spindex(spindexServo, launcherTransferServo, intakeTransferServo, spindexColorSensor, matchSettings);
		
		DcMotor launcherLauncherRight = hardwareMap.get(DcMotor.class, Settings.Hardware.IDs.LAUNCHER_LAUNCHER_RIGHT);
		DcMotor launcherLauncherLeft = hardwareMap.get(DcMotor.class, Settings.Hardware.IDs.LAUNCHER_LAUNCHER_LEFT);
		Servo horizontalServo = hardwareMap.get(Servo.class, Settings.Hardware.IDs.LAUNCHER_HORIZONTAL_SERVO);
		Servo verticalServo = hardwareMap.get(Servo.class, Settings.Hardware.IDs.LAUNCHER_VERTICAL_SERVO);
		launcher = new Launcher(spindex, launcherLauncherRight, launcherLauncherLeft, horizontalServo, verticalServo, trajectoryEngine);
	}
	
	public void init() {
		intake.init();
		spindex.init();
		launcher.init();
	}
	
	public void update() {
		intake.update();
		spindex.update();
		launcher.update();
	}
}
