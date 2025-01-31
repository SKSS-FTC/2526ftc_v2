package org.nknsd.teamcode.components.handlers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.State;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.sensors.hummelvision.LilyVisionHandler;
import org.nknsd.teamcode.components.utility.ColorPicker;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.helperClasses.PosPair;

import java.util.concurrent.TimeUnit;

public class ShaiHuludHandler implements NKNComponent {
    long stateStartTime;
    private DcMotor extensionMotor;
    private Servo wristServo;
    private Servo spikeServo;
    private ShaiHuludPosition[] positions = new ShaiHuludPosition[6];
    private ShaiStates state = ShaiStates.TUCK;
    private LilyVisionHandler visionHandler; private ColorPicker colorPicker; private WheelHandler wheelHandler;
    private final double ALIGN_MARGIN = 0.5;
    private final int PRIORITY = 1;

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        extensionMotor = hardwareMap.dcMotor.get("motorShaiHulud");
        wristServo = hardwareMap.servo.get("servoShaiHuludWrist");
        spikeServo = hardwareMap.servo.get("servoShaiHuludSpike");
        //extensionMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        positions[0] = new ShaiHuludPosition(-600, 0.7, 0.8); // tuck
        positions[1] = new ShaiHuludPosition(-2400, 0.6, 0.8); // extend
        positions[2] = new ShaiHuludPosition(-2400, 0.28, 0.8); // rotate down
        positions[3] = new ShaiHuludPosition(-2400, 0.28, 0.2); // spike grab
        positions[4] = new ShaiHuludPosition(-600, 0.7, 0.2); // retract
        positions[5] = new ShaiHuludPosition(-600, 0.7, 0.8); // eject

        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {
        extensionMotor.setPower(1);

        // extensionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extensionMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extensionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extensionMotor.setTargetPosition(0);

        setPositions(positions[0]);

        state = ShaiStates.TUCK;
    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        switch (state) {
            case TUCK:
                setPositions(positions[0]);
                break;

            case ALIGNTOSAMPLE:
                if (alignToSample()) {
                    state = ShaiStates.BEGINEXTEND;
                }
                break;

            case BEGINEXTEND:
                setPositions(positions[1]);
                state = ShaiStates.WAITINGFOREXTEND;
                break;

            case WAITINGFOREXTEND:
                if (!extensionMotor.isBusy()) {
                    state = ShaiStates.ROTATEDOWN;
                }
                break;

            case ROTATEDOWN:
                stateStartTime = runtime.time(TimeUnit.MILLISECONDS);
                setPositions(positions[2]);
                state = ShaiStates.WAITFORROTATE;
                break;

            case WAITFORROTATE:
                if (runtime.time(TimeUnit.MILLISECONDS) - stateStartTime >= 500) {
                    state = ShaiStates.SPIKEGRAB;
                }
                break;

            case SPIKEGRAB:
                stateStartTime = runtime.time(TimeUnit.MILLISECONDS);
                setPositions(positions[3]);
                state = ShaiStates.WAITFORGRAB;
                break;

            case WAITFORGRAB:
                if (runtime.time(TimeUnit.MILLISECONDS) - stateStartTime >= 200) {
                    state = ShaiStates.BEGINRETRACT;
                }
                break;

            case BEGINRETRACT:
                stateStartTime = runtime.time(TimeUnit.MILLISECONDS);
                setPositions(positions[4]);
                state = ShaiStates.WAITFORRETRACT;
                break;

            case WAITFORRETRACT:
                if (!extensionMotor.isBusy()) {
                    state = ShaiStates.EJECT;
                }
                break;

            case EJECT:
                stateStartTime = runtime.time(TimeUnit.MILLISECONDS);
                setPositions(positions[5]);
                state = ShaiStates.WAITFOREJECT;
                break;

            case WAITFOREJECT:
                if (runtime.time(TimeUnit.MILLISECONDS) - stateStartTime >= 300) {
                    state = ShaiStates.TUCK;
                }
                break;
        }
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        telemetry.addData("State", state.name());
        telemetry.addData("Motor Position", extensionMotor.getCurrentPosition());
    }

    private PosPair getOffset(LilyVisionHandler.VisionData visionData) {
        switch (colorPicker.currentColor()) {
            case RED:
                return new PosPair(visionData.redX, visionData.redY);

            case BLUE:
                return new PosPair(visionData.blueX, visionData.blueY);

            case YELLOW:
                return new PosPair(visionData.yellowX, visionData.yellowY);

            default:
                return new PosPair(0, 0);
        }
    }

    private boolean alignToSample() {
        if (wheelHandler == null) {
            return true;
        }

        wheelHandler.setPriority(PRIORITY);

        // Align to target sample
        LilyVisionHandler.VisionData visionData = visionHandler.getVisionData();
        PosPair offset = getOffset(visionData);

        if (offset.getDist() < ALIGN_MARGIN) {
            wheelHandler.setPriority(0);
            wheelHandler.relativeVectorToMotion(0, 0, 0);
            return true;
        }

        PosPair moveSpeed = offset.scale(1);
        wheelHandler.relativeVectorToMotion(moveSpeed.y, moveSpeed.x, 0, PRIORITY);
        return false;
    }

    public void beginPickup() {
        if (state != ShaiStates.TUCK) {
            return;
        }

        state = ShaiStates.ALIGNTOSAMPLE;
    }

    public void cancelPickup() {
        state = ShaiStates.TUCK;
        if (wheelHandler == null) {
            return;
        }

        wheelHandler.setPriority(0);
        wheelHandler.relativeVectorToMotion(0, 0, 0);
    }

    private void setPositions(ShaiHuludPosition shaiHuludPosition) {
        wristServo.setPosition(shaiHuludPosition.wristPos);
        spikeServo.setPosition(shaiHuludPosition.spikePos);
        if (extensionMotor.getTargetPosition() != shaiHuludPosition.motorPos) {
            extensionMotor.setTargetPosition(shaiHuludPosition.motorPos);
            extensionMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            extensionMotor.setPower(0.6);
        }
    }

    @Override
    public String getName() {
        return "Shai Hulud Handler";
    }

    public ShaiStates getState() {
        return state;
    }

    @Deprecated
    public void setState(ShaiStates state) {
        if (this.state == ShaiStates.TUCK) { // Safety function, we don't want the driver to mess with the state unless the state is tuck
            this.state = state;
        }
    }

    public void link(LilyVisionHandler visionHandler, ColorPicker colorPicker) {
        this.visionHandler = visionHandler;
        this.colorPicker = colorPicker;
    }

    public void linkWheels(WheelHandler wheelHandler) {
        this.wheelHandler = wheelHandler;
    }

    public enum ShaiStates {
        TUCK,
        ALIGNTOSAMPLE,
        BEGINEXTEND,
        WAITINGFOREXTEND,
        ROTATEDOWN,
        WAITFORROTATE,
        SPIKEGRAB,
        WAITFORGRAB,
        BEGINRETRACT,
        WAITFORRETRACT,
        EJECT,
        WAITFOREJECT;
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
