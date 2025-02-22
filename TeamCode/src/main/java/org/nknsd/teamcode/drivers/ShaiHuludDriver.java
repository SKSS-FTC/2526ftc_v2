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

    private final Runnable shExtend = new Runnable() {
        @Override
        public void run() {
            shaiHuludHandler.beginPickup();
        }
    };

    private final Runnable shRetract = new Runnable() {
        @Override
        public void run() {
            shaiHuludHandler.cancelPickup();
        }
    };

    private final Runnable shInterruptGrab = new Runnable() {
        @Override
        public void run() {
            shaiHuludHandler.beginFalseGrab();
        }
    };

    private final Runnable jaRest = new Runnable() {
        @Override
        public void run() {
           jointedArmHandler.setTargetPosition(JointedArmHandler.Positions.REST);
           if (shaiHuludHandler.getState() == ShaiHuludHandler.ShaiStates.SPECIMEN){
              shaiHuludHandler.cancelPickup();
           }
        }
    };

    private final Runnable jaCollect = new Runnable() {
        @Override
        public void run() {
            jointedArmHandler.setTargetPosition(JointedArmHandler.Positions.COLLECTION);
        }
    };

    private final Runnable jaDeposit = new Runnable() {
        @Override
        public void run() {
            jointedArmHandler.setTargetPosition(JointedArmHandler.Positions.DEPOSIT);
        }
    };
    private final Runnable jaSpecimenCollect = new Runnable() {
        @Override
        public void run() {
            if (jointedArmHandler.targetPosition == JointedArmHandler.Positions.EARLY_BIRD) {
                jointedArmHandler.setTargetPosition(JointedArmHandler.Positions.WORM_SEARCH);
            } else {
                jointedArmHandler.setTargetPosition(JointedArmHandler.Positions.EARLY_BIRD);
            }
        }
    };
    private final Runnable jaSpecimenDeposit = new Runnable() {
        @Override
        public void run() {
            if (jointedArmHandler.targetPosition == JointedArmHandler.Positions.NEST) {
                jointedArmHandler.setTargetPosition(JointedArmHandler.Positions.FEED);
            } else {
                jointedArmHandler.setTargetPosition(JointedArmHandler.Positions.NEST);
                shaiHuludHandler.specimenPickup();
            }
        }
    };
    private final Runnable jaClose = new Runnable() {
        @Override
        public void run() {
            jointedArmHandler.setClawPosition(JointedArmHandler.Positions.GRAB_CLOSE);
        }
    };
    private final Runnable jaOpen = new Runnable() {
        @Override
        public void run() {
            jointedArmHandler.setClawPosition(JointedArmHandler.Positions.GRAB_OPEN);
        }
    };
    private final Runnable theBowl = new Runnable() {
        @Override
        public void run() {
            bowlHandler.beginBowlin();
        }
    };
    private final Runnable manualSHRetract = new Runnable() {
        @Override
        public void run() {
            shaiHuludHandler.manualRetract();
        }
    };
    private final Runnable endManualSHRetract = new Runnable() {
        @Override
        public void run() {
            shaiHuludHandler.endManualRetract();
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
        gamePadHandler.addListener(controlScheme.shInterruptGrab(), shInterruptGrab, "SH Interrupt grab");
        gamePadHandler.addListener(controlScheme.manualSHRetract(), manualSHRetract, "SH Manual Retract Begin");
        gamePadHandler.addListener(controlScheme.endManualSHRetract(), endManualSHRetract, "SH End Manual Retract");

        if (debug) {
            return;
        }
        gamePadHandler.addListener(controlScheme.jaRest(), jaRest, "JA Rest");
        gamePadHandler.addListener(controlScheme.jaCollect(), jaCollect, "JA Collect");
        gamePadHandler.addListener(controlScheme.jaDeposit(), jaDeposit, "JA Deposit");

        gamePadHandler.addListener(controlScheme.jaClose(), jaClose, "JA Close");
        gamePadHandler.addListener(controlScheme.jaOpen(), jaOpen, "JA Open");

        gamePadHandler.addListener(controlScheme.jaSpecimenCollect(), jaSpecimenCollect, "JA SPEC COLLECT");
        gamePadHandler.addListener(controlScheme.jaSpecimenDeposit(), jaSpecimenDeposit, "JA SPEC DEPOSIT");

        gamePadHandler.addListener(controlScheme.specimenSwitch(), "JA Specimen Switch");

        gamePadHandler.addListener(controlScheme.theBowl(), theBowl, "BOWL");
    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "ShaiHuludDriver";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        if (debug) {
            jointedArmHandler.runStuff(gamePadHandler);
//            if (GamePadHandler.GamepadButtons.BACK.detect(gamePadHandler.getGamePad2())) {
//                bowlDelayStart = runtime.now(TimeUnit.MILLISECONDS);
//                currentBowlState = BowlStates.WAITING_TO_START;
//            }

            if (GamePadHandler.GamepadButtons.LEFT_TRIGGER.detect(gamePadHandler.getGamePad2())) {
                shaiHuludHandler.specimenPickup();
            }
        }

        bowlHandler.doBowlin(shaiHuludHandler, runtime);
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        if (controlScheme.getDoingSpecimen()) telemetry.addData("JA", "Doing Specimens");
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
