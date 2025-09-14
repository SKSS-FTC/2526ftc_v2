package org.nknsd.teamcode.drivers;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.WheelHandler;
import org.nknsd.teamcode.components.sensors.IMUSensor;
import org.nknsd.teamcode.components.utility.GamePadHandler;
import org.nknsd.teamcode.controlSchemes.abstracts.WheelControlScheme;
import org.nknsd.teamcode.frameworks.NKNComponent;

// Adds events to gamepad to control the wheels
public class AdvancedWheelDriver implements NKNComponent {
    private final double speedMin;
    private final double speedMax;
    private final double speedStepAmount;
    private final GamePadHandler.GamepadSticks forwardStick;
    private final GamePadHandler.GamepadSticks strafeStick;
    private final GamePadHandler.GamepadSticks turnStick;
    private GamePadHandler gamePadHandler;
    private WheelHandler wheelHandler;
    private WheelControlScheme controlScheme;

    private double moveSpeedMultiplier;
    private boolean imuCorrection = false; // Disabled because new robot does NOT correct for any autonomous stuff

    Runnable speedUp = new Runnable() {
        @Override
        public void run() {
            if (!(moveSpeedMultiplier + speedStepAmount > speedMax)) {
                moveSpeedMultiplier = moveSpeedMultiplier + speedStepAmount;
            }
        }
    };

    Runnable speedDown = new Runnable() {
        @Override
        public void run() {
            if (!(moveSpeedMultiplier - speedStepAmount < speedMin)) {
                moveSpeedMultiplier = moveSpeedMultiplier - speedStepAmount;
            }
        }
    };

    Runnable disableAutonomousIMUCorrection = new Runnable() {
        @Override
        public void run() {
            imuCorrection = false;
        }
    };

    Runnable resetImu = new Runnable() {
        @Override
        public void run() {
            imuSensor.resetIMU();
            imuCorrection = false;
        }
    };

    private Gamepad gamepad;
    private IMUSensor imuSensor;

    public AdvancedWheelDriver(double speedMin, double speedMax, int speedSteps, GamePadHandler.GamepadSticks forwardStick, GamePadHandler.GamepadSticks strafeStick, GamePadHandler.GamepadSticks turnStick) {
        this.speedMin = speedMin;
        this.speedMax = speedMax;
        this.forwardStick = forwardStick;
        this.strafeStick = strafeStick;
        this.turnStick = turnStick;
        speedStepAmount = (speedMax - speedMin) / 5;
    }

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        moveSpeedMultiplier = 0;
        this.gamepad = gamepad1;

        gamePadHandler.addListener(controlScheme.initDisableAutoFix(), disableAutonomousIMUCorrection, "Disable Autonomous IMU Yaw Correction");
        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {
        gamePadHandler.addListener(controlScheme.gearDown(), speedDown, "Speed Down");
        gamePadHandler.addListener(controlScheme.gearUp(), speedUp, "Speed Up");
        gamePadHandler.addListener(controlScheme.resetAngle(), resetImu, "Reset Angle");

        gamePadHandler.removeListener("Disable Autonomous IMU Yaw Correction");
    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "AdvancedWheelDriver";
    }


    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        double yaw = imuSensor.getYaw();
        if (imuCorrection) {
            yaw += 90;
        }

        wheelHandler.absoluteVectorToMotion(strafeStick.getValue(gamepad) * moveSpeedMultiplier, forwardStick.getValue(gamepad) * moveSpeedMultiplier, turnStick.getValue(gamepad) * moveSpeedMultiplier, yaw);
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        telemetry.addData("Gear", (moveSpeedMultiplier - speedMin) / speedStepAmount);
        telemetry.addData("Raw Speed", moveSpeedMultiplier);
        telemetry.addData("Wheel Controls", controlScheme.getName());
    }

    public void link(GamePadHandler gamePadHandler, WheelHandler wheelHandler, IMUSensor imuSensor, WheelControlScheme controlScheme) {
        this.gamePadHandler = gamePadHandler;
        this.wheelHandler = wheelHandler;
        this.imuSensor = imuSensor;
        this.controlScheme = controlScheme;
    }
}
