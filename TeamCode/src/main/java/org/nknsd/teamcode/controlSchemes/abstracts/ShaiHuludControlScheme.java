package org.nknsd.teamcode.controlSchemes.abstracts;

import org.nknsd.teamcode.components.utility.GamePadHandler;
import org.nknsd.teamcode.frameworks.NKNControlScheme;

import java.util.concurrent.Callable;

// NEEDS CONSTRUCTION
public abstract class ShaiHuludControlScheme extends NKNControlScheme {

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

    public Callable<Boolean> jaRest() {
        //return () -> GamePadHandler.GamepadButtons.DPAD_DOWN.detect(gamePadHandler.getGamePad2());
        return () -> GamePadHandler.GamepadButtons.X.detect(gamePadHandler.getGamePad2());
    }

    public Callable<Boolean> jaCollect() {
        //return () -> GamePadHandler.GamepadButtons.A.detect(gamePadHandler.getGamePad2());
        return () -> GamePadHandler.GamepadButtons.Y.detect(gamePadHandler.getGamePad2());
    }

    public Callable<Boolean> jaDeposit() {
        //return () -> GamePadHandler.GamepadButtons.DPAD_UP.detect(gamePadHandler.getGamePad2());
        return () -> GamePadHandler.GamepadButtons.START.detect(gamePadHandler.getGamePad2());
    }
}

