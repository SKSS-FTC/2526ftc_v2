package org.nknsd.teamcode.controlSchemes.reals;

import org.nknsd.teamcode.components.utility.GamePadHandler;
import org.nknsd.teamcode.controlSchemes.abstracts.Generic2PControlScheme;
import org.nknsd.teamcode.controlSchemes.abstracts.SpecimenControlScheme;

import java.util.concurrent.Callable;

public class KarstenGeneric2PController extends Generic2PControlScheme {
    boolean colorSwitchDelay = false;

    @Override
    public String getName() {
        return "Karsten";
    }

    public Callable<Boolean> switchColor() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean button = GamePadHandler.GamepadButtons.START.detect(gamePadHandler.getGamePad2());

                if (!colorSwitchDelay && button) {
                    colorSwitchDelay = true;
                    return true;
                } else if (!button) {
                    colorSwitchDelay = false;
                }

                return false;
            }
        };
    }
}
