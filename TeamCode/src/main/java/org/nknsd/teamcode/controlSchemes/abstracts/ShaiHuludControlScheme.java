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
                return GamePadHandler.GamepadButtons.LEFT_BUMPER.detect(gamePadHandler.getGamePad1());
            }
        };
    }

    public Callable<Boolean> shExtend() {
        return () -> GamePadHandler.GamepadButtons.RIGHT_BUMPER.detect(gamePadHandler.getGamePad1());
    }
}

