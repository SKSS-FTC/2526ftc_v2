package org.nknsd.teamcode.components.handlers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNComponent;

import java.util.concurrent.TimeUnit;

public class ShaiHuludHandler implements NKNComponent {
    private DcMotor extensionMotor;
    private Servo wristServo;
    private Servo spikeServo;

    private ShaiHuludPosition[] positions = new ShaiHuludPosition[6];

    private ShaiStates state = ShaiStates.TUCK;

    long stateStartTime;

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        extensionMotor = hardwareMap.dcMotor.get("motorShaiHulud");
        wristServo = hardwareMap.servo.get("servoShaiHuludWrist");
        spikeServo = hardwareMap.servo.get("servoShaiHuludSpike");


        extensionMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extensionMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        extensionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extensionMotor.setTargetPosition(0);


        positions[0] = new ShaiHuludPosition(0, 0.7, 0.8); // tuck
        positions[1] = new ShaiHuludPosition(2450, 0.7, 0.8); // extend
        positions[2] = new ShaiHuludPosition(2450,0.28,0.8); // rotate down
        positions[3] = new ShaiHuludPosition(2450,0.28,0.2); // spike grab
        positions[4] = new ShaiHuludPosition(0, 0.7, 0.2); // retract
        positions[5] = new ShaiHuludPosition(0,0.7,0.8); // eject

        setPositions(positions[0]);

        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {
    state = ShaiStates.TUCK;
    state = ShaiStates.BEGINEXTEND;
    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "Shai Hulud Handler";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        switch (state) {
            case TUCK:
                setPositions(positions[0]);
                break;
            case BEGINEXTEND:
                setPositions(positions[1]);
                state = ShaiStates.WAITINGFOREXTEND;
                break;
            case WAITINGFOREXTEND:
                if (!extensionMotor.isBusy()){ state = ShaiStates.ROTATEDOWN; }
                break;
            case ROTATEDOWN:
                stateStartTime = runtime.time(TimeUnit.MILLISECONDS);
                setPositions(positions[2]);
                state = ShaiStates.WAITFORROTATE;
                break;
            case WAITFORROTATE:
                if (runtime.time(TimeUnit.MILLISECONDS) - stateStartTime >= 500) { state = ShaiStates.SPIKEGRAB; }
                break;
            case SPIKEGRAB:
                stateStartTime = runtime.time(TimeUnit.MILLISECONDS);
                setPositions(positions[3]);
                state = ShaiStates.WAITFORGRAB;
                break;
            case WAITFORGRAB:
                if (runtime.time(TimeUnit.MILLISECONDS) - stateStartTime >= 200) { state = ShaiStates.BEGINRETRACT; }
                break;
            case BEGINRETRACT:
                stateStartTime = runtime.time(TimeUnit.MILLISECONDS);
                setPositions(positions[4]);
                state = ShaiStates.WAITFORRETRACT;
                break;
            case WAITFORRETRACT:
                if (extensionMotor.getCurrentPosition() < 300){ state = ShaiStates.EJECT;; }
                break;
            case EJECT:
                stateStartTime = runtime.time(TimeUnit.MILLISECONDS);
                setPositions(positions[5]);
                state = ShaiStates.WAITFOREJECT;
                break;
            case WAITFOREJECT:
                if (runtime.time(TimeUnit.MILLISECONDS) - stateStartTime >= 300) { state = ShaiStates.BEGINEXTEND; }
                break;
        }
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {

    }

    private enum ShaiStates {
        TUCK(),
        BEGINEXTEND(),
        WAITINGFOREXTEND(),
        ROTATEDOWN(),
        WAITFORROTATE(),
        SPIKEGRAB(),
        WAITFORGRAB(),
        BEGINRETRACT(),
        WAITFORRETRACT(),
        EJECT(),
        WAITFOREJECT();

    }

    private void setPositions(ShaiHuludPosition shaiHuludPosition) {
        wristServo.setPosition(shaiHuludPosition.wristPos);
        spikeServo.setPosition(shaiHuludPosition.spikePos);
        if (extensionMotor.getTargetPosition()!=shaiHuludPosition.motorPos) {
            extensionMotor.setTargetPosition(shaiHuludPosition.motorPos);
            extensionMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            extensionMotor.setPower(1);
        }
    }

    public static class ShaiHuludPosition {
        public final int motorPos;
        public final double wristPos;
        public final double spikePos;

        public ShaiHuludPosition(int motorPos, double wristPos, double spikePos) {
            this.motorPos = motorPos;
            this.wristPos = wristPos;
            this.spikePos = spikePos;
        }
    }
}
