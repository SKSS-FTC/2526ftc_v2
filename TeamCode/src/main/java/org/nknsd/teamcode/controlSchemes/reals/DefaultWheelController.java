package org.nknsd.teamcode.controlSchemes.reals;

import org.nknsd.teamcode.components.utility.GamePadHandler;
import org.nknsd.teamcode.controlSchemes.abstracts.WheelControlScheme;

import java.util.concurrent.Callable;

public class DefaultWheelController extends WheelControlScheme {
    @Override
    public String getName() {
        return "Colly";
    }
}
