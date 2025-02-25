package org.nknsd.teamcode.components.handlers;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.drivers.ShaiHuludDriver;
import org.nknsd.teamcode.frameworks.NKNComponent;

import java.util.concurrent.TimeUnit;

public class TheBowlHandler implements NKNComponent {
    CRServo servo;
    private States currentState = States.not_bowlin;

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        servo = hardwareMap.crservo.get("transferShaker");
        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "TheBowlHandler";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        telemetry.addData("BowlState", currentState);
    }

    public void setServoPower (States state){
        servo.setPower(state.power);
        currentState = state;
    }

    public double getServoPower() {
        return servo.getPower();
    }

    public States getCurrentState() {
        return currentState;
    }

    private long bowlStartTime, bowlDelayStart;
    private BowlStates currentBowlState = BowlStates.STOPPED;
    private final static long BOWL_DELAY = 750, BOWL_LENGTH = 1000;
    public void doBowlin(ShaiHuludHandler shaiHuludHandler, ElapsedTime runtime) {
        if (shaiHuludHandler.getState() == ShaiHuludHandler.ShaiStates.EJECT && currentBowlState == BowlStates.STOPPED) {
            bowlDelayStart = runtime.now(TimeUnit.MILLISECONDS);
            currentBowlState = BowlStates.WAITING_TO_START;
        }

        if (runtime.now(TimeUnit.MILLISECONDS) > bowlDelayStart + BOWL_DELAY && currentBowlState == BowlStates.WAITING_TO_START) {
            bowlStartTime = runtime.now(TimeUnit.MILLISECONDS);
            currentBowlState = BowlStates.ACTIVE;
        }

        if (runtime.now(TimeUnit.MILLISECONDS) > bowlStartTime + BOWL_LENGTH && currentBowlState == BowlStates.ACTIVE) {
            currentBowlState = BowlStates.STOPPED;
        }

        switch (currentBowlState) {
            case ACTIVE:
                setServoPower(TheBowlHandler.States.BOWLIN);
                break;

            case STOPPED:
                setServoPower(TheBowlHandler.States.not_bowlin);
                break;
        }
    }

    public void beginBowlin() {
        // What this does is essentially guarentee that the bowl will immediately start by setting the targetted start time to 1000 milliseconds
        bowlDelayStart = 0;
        currentBowlState = BowlStates.WAITING_TO_START;
    }

    public enum States {
        BOWLIN(0.325),
        not_bowlin(0);

        public final double power;

        States(double power) {
            this.power = power;
        }
    }

    private enum BowlStates {
        WAITING_TO_START,
        ACTIVE,
        STOPPED;
    }
}
