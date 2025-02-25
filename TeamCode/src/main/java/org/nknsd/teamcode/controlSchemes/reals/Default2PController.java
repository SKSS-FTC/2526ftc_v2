package org.nknsd.teamcode.controlSchemes.reals;

import org.nknsd.teamcode.controlSchemes.abstracts.Generic2PControlScheme;

import java.util.concurrent.Callable;

public class Default2PController extends Generic2PControlScheme {
    @Override
    public Callable<Boolean> switchColor() {
        return () -> false;
    }
}
