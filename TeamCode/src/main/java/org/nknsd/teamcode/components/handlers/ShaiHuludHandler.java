package org.nknsd.teamcode.components.handlers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

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
    private ShaiHuludPosition[] positions = new ShaiHuludPosition[7];
    private ShaiStates state = ShaiStates.TUCK;
    private LilyVisionHandler visionHandler; private ColorPicker colorPicker; private WheelHandler wheelHandler;
    private final double ALIGN_MARGIN = 8;
    private final int PRIORITY = 1;
    private Gamepad gamepad; private Telemetry telemetry;
    private boolean skipStates = true;

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        extensionMotor = hardwareMap.dcMotor.get("motorShaiHulud");
        wristServo = hardwareMap.servo.get("servoShaiHuludWrist");
        spikeServo = hardwareMap.servo.get("servoShaiHuludSpike");
        //extensionMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        extensionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        positions[0] = new ShaiHuludPosition(-300, 0.8, 1); // tuck
        positions[1] = new ShaiHuludPosition(-1800, 0.8, 1); // extend
        positions[2] = new ShaiHuludPosition(-1800, 0.28, 0.6); // rotate down
        positions[3] = new ShaiHuludPosition(-1800, 0.28, 0.2); // spike grab
        positions[4] = new ShaiHuludPosition(-300, 0.8, 0.2); // retract
        positions[5] = new ShaiHuludPosition(0, 0.8, 1); // eject
        positions[6] = new ShaiHuludPosition(0,0.6,0.6); // specimen grab

        gamepad = gamepad2; //super botched way to implement the e-stop for the shai hulud movement
        this.telemetry = telemetry;

        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    private long startTime;
    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {
        startTime = runtime.now(TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    private boolean oneTimeThing = false;
    private long grabbyCounter = 0; // This is a timer used in the grabby state to control its behavior.
    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        // Code to run once after a delay
        if (!oneTimeThing && runtime.now(TimeUnit.MILLISECONDS) > 3000 + startTime) {
            extensionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            extensionMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            extensionMotor.setPower(1);
            extensionMotor.setTargetPosition(0);
            extensionMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            oneTimeThing = true;
        }

        if (!oneTimeThing && !(runtime.now(TimeUnit.MILLISECONDS) > 2000 + startTime)) {
            return;
        }
        // OK so the code for this 'oneTimeThing' is super botched
        // Basically, if its less than 2s, then we can move the servo (no continue)
        // And if it's more than 3s, we can start the motor

        // Under manual control, must skip states
        if (skipStates) {
            return;
        }

        // Main Loop Code
        switch (state) {
            case TUCK:
                setPositions(positions[0]);
                break;

            case ALIGNTOSAMPLE:
                if (alignToSample()) {
                    if (continueExtension) {
                        state = ShaiStates.BEGINEXTEND;
                    } else {
                        if (wheelHandler != null) {
                            wheelHandler.setPriority(0);
                            wheelHandler.relativeVectorToMotion(0, 0, 0);
                        }
                        state = ShaiStates.TUCK;
                    }
                }
                break;

            case BEGINEXTEND:
                alignToSample();
                setPositions(positions[1]);
                state = ShaiStates.WAITINGFOREXTEND;
                break;

            case WAITINGFOREXTEND:
                alignToSample();
                if (!extensionMotor.isBusy()) {
                    if (wheelHandler != null) {
                        wheelHandler.setPriority(0);
                        wheelHandler.relativeVectorToMotion(0, 0, 0);
                    }
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
                if (runtime.time(TimeUnit.MILLISECONDS) - stateStartTime >= 800) {
                    state = ShaiStates.TUCK;
                }
                break;

            case SPECIMEN:
                setPositions(positions[6]);
                break;

            case PREGRABBY:
                state = ShaiStates.GRABBY;
                grabbyCounter = runtime.now(TimeUnit.MILLISECONDS);
                break;

            case GRABBY:
                long now = runtime.now(TimeUnit.MILLISECONDS);

                // Time to eject
                if (grabbyCounter < now + 300) {
                    ejectSpike();
                }

                // Time to reset
                else if (grabbyCounter < now + 600) {
                    neutralSpike();
                }

                // Time to rotate down and grab
                if (grabbyCounter < now + 900) {
                    state = ShaiStates.BEGINEXTEND;
                }
                break;
        }
    }

    private void ejectSpike() {
        spikeServo.setPosition(1);
    }

    private void neutralSpike() {
        spikeServo.setPosition(0.6);
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        telemetry.addData("SH State", state.name());
        telemetry.addData("SH Motor Position", extensionMotor.getCurrentPosition());
    }

    private PosPair getOffset(LilyVisionHandler.VisionData visionData) {
        PosPair offsetMod = new PosPair(0, 1);
        switch (colorPicker.currentColor()) {
            case RED:
                return offsetMod.add(visionData.redX, visionData.redY);

            case BLUE:
                return offsetMod.add(visionData.blueX, visionData.blueY);

            case YELLOW:
                return offsetMod.add(visionData.yellowX, visionData.yellowY);

            default:
                return new PosPair(0, 0);
        }
    }

    private boolean continueExtension = false;
    private final PosPair noSample = new PosPair(Integer.MAX_VALUE, -Integer.MAX_VALUE);
    private boolean alignToSample() {
        // Check to amke sure there's a wheel handler to use
        if (wheelHandler == null) return true;

        // Get data on where the sample is
        LilyVisionHandler.VisionData visionData = visionHandler.getVisionData();
        PosPair offset = getOffset(visionData);
        offset.doTelemetry(telemetry, "offset");

        // If there is no sample, do not continue
        if (offset.equalTo(noSample)) {
            telemetry.addData("Exit", "NO-SAMPLE");
            return true;
        }

        // Check if we're aligned
        if (offset.getDist() < ALIGN_MARGIN) {
            continueExtension = true;
            telemetry.addData("Exit", "ALIGN-DONE");
            return true;
        }

        // Now that we know we have to move, claim priority over the wheel handler
        wheelHandler.setPriority(PRIORITY);

        // Set speed to move closer to sample
        PosPair moveSpeed = offset.scale(0.005).dropLowValues(0.13).clampValuesToMin(0.15);

        wheelHandler.relativeVectorToMotion(moveSpeed.y, moveSpeed.x, 0, PRIORITY);
        telemetry.addData("X", moveSpeed.x);
        telemetry.addData("Y", moveSpeed.y);
        telemetry.addData("Exit", "ALIGN-PROGRESS");
        return false;
    }

    public void beginPickup() {
        if (!state.exitState) { // Safety function, we don't want the driver to mess with the state unless the state is tuck or specimen (or pause)
            return;
        }

        if (wheelHandler == null || gamepad.y) {
            state = ShaiStates.BEGINEXTEND;
            return;
        }

        continueExtension = false; // Reset continueExtension so that it can be set back to true when needed
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
    public void specimenPickup() {
        if(state == ShaiStates.TUCK){
            state = ShaiStates.SPECIMEN;
        }
    }

    public void beginFalseGrab() {
        state = ShaiStates.PREGRABBY;
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
        if (state.exitState) { // Safety function, we don't want the driver to mess with the state unless the state is tuck or specimen (or pause)
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

    public void pause() {
        if (state != ShaiStates.ALIGNTOSAMPLE && state != ShaiStates.BEGINEXTEND && state != ShaiStates.WAITINGFOREXTEND) {
            // We're not allowed to pause from these three states
            return;
        }
        state = ShaiStates.PAUSE;
    }

    // Toggle ON manual retract
    public void manualRetract() {
        extensionMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        extensionMotor.setPower(0.3);
        skipStates = true;
    }

    // Toggle OFF manual retract
    public void endManualRetract() {
        extensionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extensionMotor.setTargetPosition(0);
        extensionMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extensionMotor.setPower(1);
        skipStates = false;
    }

    public enum ShaiStates {
        TUCK(true),
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
        WAITFOREJECT,
        SPECIMEN(true),
        PAUSE(true),
        PREGRABBY, // Immediately triggers grabby, so that other programs can reference it (directly referencing grabby won't work, obviously
        GRABBY; // This state lets another program initiate a brand new rotate-down grab.

        public final boolean exitState; // Represents if a state is a free-for-all that other states can choose to jump from or not.
        ShaiStates(boolean exitState) {
            this.exitState = exitState;
        }

        ShaiStates() {
            this.exitState = false;
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
