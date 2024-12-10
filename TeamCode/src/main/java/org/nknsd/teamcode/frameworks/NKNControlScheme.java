package org.nknsd.teamcode.frameworks;

import org.nknsd.teamcode.components.utility.GamePadHandler;

public abstract class NKNControlScheme {
    public GamePadHandler gamePadHandler;

    public String getName() {
        return "Default (BAD, GET DILLON)";
    }

    public void link(GamePadHandler gamePadHandler) {
        this.gamePadHandler = gamePadHandler;
    }
}
