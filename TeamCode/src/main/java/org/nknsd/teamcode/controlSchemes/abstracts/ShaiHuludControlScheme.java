package org.nknsd.teamcode.controlSchemes.abstracts;

import org.nknsd.teamcode.components.utility.GamePadHandler;
import org.nknsd.teamcode.frameworks.NKNControlScheme;

import java.util.concurrent.Callable;

// NEEDS CONSTRUCTION
public abstract class ShaiHuludControlScheme extends NKNControlScheme {
    private boolean doingSpecimen = false;

    public Callable<Boolean> shRetract() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return GamePadHandler.GamepadButtons.LEFT_BUMPER.detect(gamePadHandler.getGamePad2()) || GamePadHandler.GamepadButtons.X.detect(gamePadHandler.getGamePad2());
            }
        };
    }

    public Callable<Boolean> shExtend() {
        return () -> GamePadHandler.GamepadButtons.RIGHT_BUMPER.detect(gamePadHandler.getGamePad2());
    }

    private boolean delaySHInterruptGrab = false;
    public Callable<Boolean> shInterruptGrab() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean button = GamePadHandler.GamepadButtons.RIGHT_TRIGGER.detect(gamePadHandler.getGamePad2());

                if (!delaySHInterruptGrab && button) {
                    delaySHInterruptGrab = true;
                    return true;
                } else if (!button) {
                    delaySHInterruptGrab = false;
                }

                return false;
            }
        };
    }

    public Callable<Boolean> jaRest() {
        return () -> GamePadHandler.GamepadButtons.DPAD_DOWN.detect(gamePadHandler.getGamePad2()) && !doingSpecimen;
//        return () -> GamePadHandler.GamepadButtons.X.detect(gamePadHandler.getGamePad2());
    }

    public Callable<Boolean> jaCollect() {
        return () -> GamePadHandler.GamepadButtons.A.detect(gamePadHandler.getGamePad2()) && !doingSpecimen;
        //return () -> GamePadHandler.GamepadButtons.Y.detect(gamePadHandler.getGamePad2());

    }
    private boolean delayJASpecCollect = false;
    public Callable<Boolean> jaSpecimenCollect() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean button = GamePadHandler.GamepadButtons.DPAD_DOWN.detect(gamePadHandler.getGamePad2()) && doingSpecimen;

                if (!delayJASpecCollect && button) {
                    delayJASpecCollect = true;
                    return true;
                } else if (!button) {
                    delayJASpecCollect = false;
                }

                return false;
            }
        };
    }

    private boolean delayJASpecDeposit = false;
    public Callable<Boolean> jaSpecimenDeposit() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean button = GamePadHandler.GamepadButtons.DPAD_UP.detect(gamePadHandler.getGamePad2()) && doingSpecimen;

                if (!delayJASpecDeposit && button) {
                    delayJASpecDeposit = true;
                    return true;
                } else if (!button) {
                    delayJASpecDeposit = false;
                }

                return false;
            }
        };
    }

    private boolean delayJADeposit = false;
    public Callable<Boolean> jaDeposit() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean button = GamePadHandler.GamepadButtons.DPAD_UP.detect(gamePadHandler.getGamePad2()) && !doingSpecimen;

                if (!delayJADeposit && button) {
                    delayJADeposit = true;
                    return true;
                } else if (!button) {
                    delayJADeposit = false;
                }

                return false;
            }
        };
    }

    public Callable<Boolean> jaClose() {
        return () -> GamePadHandler.GamepadButtons.DPAD_LEFT.detect(gamePadHandler.getGamePad2());
        //return () -> GamePadHandler.GamepadButtons.START.detect(gamePadHandler.getGamePad2());
    }

    public Callable<Boolean> jaOpen() {
        return () -> GamePadHandler.GamepadButtons.DPAD_RIGHT.detect(gamePadHandler.getGamePad2());
        //return () -> GamePadHandler.GamepadButtons.START.detect(gamePadHandler.getGamePad2());
    }

    public Callable<Boolean> theBowl() {
//        return () -> false;
        return () -> GamePadHandler.GamepadButtons.Y.detect(gamePadHandler.getGamePad2());
        //return () -> GamePadHandler.GamepadButtons.START.detect(gamePadHandler.getGamePad2());
    }

    private boolean switchDelay;
    public Callable<Boolean> specimenSwitch() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() {
                if (GamePadHandler.GamepadButtons.BACK.detect(gamePadHandler.getGamePad2())){
                    if(!switchDelay){
                        doingSpecimen = !doingSpecimen;
                        switchDelay = true;
                        return true;
                    }
                } else {
                    switchDelay = false;
                }
                return false;
            }
        };
    }

    private boolean isManuallyMoving = false;
    public Callable<Boolean> manualSHRetract() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean button = GamePadHandler.GamepadButtons.LEFT_TRIGGER.detect(gamePadHandler.getGamePad2());

                if (button) {
                    isManuallyMoving = true;
                }
                return button;
            }
        };
    }

    public Callable<Boolean> endManualSHRetract() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean button = !GamePadHandler.GamepadButtons.LEFT_TRIGGER.detect(gamePadHandler.getGamePad2()) && isManuallyMoving;

                if (button) {
                    isManuallyMoving = false;
                }

                return button;
            }
        };
    }

    public boolean getDoingSpecimen() {
        return doingSpecimen;
    }
}

