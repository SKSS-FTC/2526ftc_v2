package org.nknsd.teamcode.controlSchemes.reals;

import org.nknsd.teamcode.components.utility.GamePadHandler;
import org.nknsd.teamcode.controlSchemes.abstracts.ShaiHuludControlScheme;
import org.nknsd.teamcode.controlSchemes.abstracts.WheelControlScheme;

import java.util.concurrent.Callable;

public class DefaultShaiHuludController extends ShaiHuludControlScheme {
    @Override
    public String getName() {
        return "Default";
    }


}
