package org.nknsd.teamcode.controlSchemes.abstracts;

import org.nknsd.teamcode.frameworks.NKNControlScheme;

import java.util.concurrent.Callable;

public abstract class Generic2PControlScheme extends NKNControlScheme {

    public abstract Callable<Boolean> switchColor();

    public Callable<Boolean> switchColorInit() {
        return switchColor();
    }
}

