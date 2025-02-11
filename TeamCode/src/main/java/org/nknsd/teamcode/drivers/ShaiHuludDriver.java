package org.nknsd.teamcode.drivers;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.JointedArmHandler;
import org.nknsd.teamcode.components.handlers.ShaiHuludHandler;
import org.nknsd.teamcode.components.handlers.TheBowlHandler;
import org.nknsd.teamcode.components.handlers.WheelHandler;
import org.nknsd.teamcode.components.utility.GamePadHandler;
import org.nknsd.teamcode.controlSchemes.abstracts.ShaiHuludControlScheme;
import org.nknsd.teamcode.controlSchemes.abstracts.WheelControlScheme;
import org.nknsd.teamcode.frameworks.NKNComponent;

import java.util.concurrent.TimeUnit;

public class ShaiHuludDriver implements NKNComponent {

    private GamePadHandler gamePadHandler;
    private ShaiHuludHandler shaiHuludHandler; JointedArmHandler jointedArmHandler;
    private ShaiHuludControlScheme controlScheme;
    private boolean debug = false;

    private Runnable shExtend = new Runnable() {
        @Override
        public void run() {
            shaiHuludHandler.beginPickup();
        }
    };

    private Runnable shRetract = new Runnable() {
        @Override
        public void run() {
            shaiHuludHandler.cancelPickup();
        }
    };

    private Runnable jaRest = new Runnable() {
        @Override
        public void run() {
           jointedArmHandler.setTargetPosition(JointedArmHandler.Positions.REST);
        }
    };

    private Runnable jaCollect = new Runnable() {
        @Override
        public void run() {
            jointedArmHandler.setTargetPosition(JointedArmHandler.Positions.COLLECTION);
        }
    };

    private Runnable jaDeposit = new Runnable() {
        @Override
        public void run() {
            jointedArmHandler.setTargetPosition(JointedArmHandler.Positions.DEPOSIT);
        }
    };
    private TheBowlHandler bowlHandler;

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {
        // Add event listeners
        gamePadHandler.addListener(controlScheme.shExtend(), shExtend, "SH Begin Extend");
        gamePadHandler.addListener(controlScheme.shRetract(), shRetract, "SH CANCEL BACK UP AHHH");

        gamePadHandler.addListener(controlScheme.jaRest(), jaRest, "JA Rest");
        gamePadHandler.addListener(controlScheme.jaCollect(), jaCollect, "JA Collect");
        gamePadHandler.addListener(controlScheme.jaDeposit(), jaDeposit, "JA Deposit");
    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "ShaiHuludDriver";
    }


    private long bowlStartTime, bowlDelayStart;
    private final static long BOWL_DELAY = 500, BOWL_LENGTH = 3000;
    private BowlStates currentBowlState = BowlStates.STOPPED;
    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        if (debug) {
            jointedArmHandler.runStuff(gamePadHandler);
            if (GamePadHandler.GamepadButtons.BACK.detect(gamePadHandler.getGamePad2())) {
                bowlDelayStart = runtime.now(TimeUnit.MILLISECONDS);
                currentBowlState = BowlStates.WAITING_TO_START;
            }
        }

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
                bowlHandler.setServoPower(TheBowlHandler.States.BOWLIN);
                break;

            case STOPPED:
                bowlHandler.setServoPower(TheBowlHandler.States.not_bowlin);
                break;
        }
    }

    private enum BowlStates {
        WAITING_TO_START,
        ACTIVE,
        STOPPED;
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {

    }

    public void link(GamePadHandler gamePadHandler, ShaiHuludHandler shaiHuludHandler, JointedArmHandler jointedArmHandler, TheBowlHandler bowlHandler, ShaiHuludControlScheme controlScheme) {
        this.gamePadHandler = gamePadHandler;
        this.shaiHuludHandler = shaiHuludHandler;
        this.jointedArmHandler = jointedArmHandler;
        this.bowlHandler = bowlHandler;
        this.controlScheme = controlScheme;
    }

    public void enableDebug() {
        debug = true;
    }
}
