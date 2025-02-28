package org.nknsd.teamcode.frameworks;

public abstract class NKNStep {
    private NKNStep nknStep;

    public abstract void doTask();

    public NKNStep end() {
        if (doneTask()) {
            return nknStep;
        }
        return null;
    }

    protected abstract boolean doneTask();

    public void link(NKNStep nknStep) {
        this.nknStep = nknStep;
    }
}
