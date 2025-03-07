package org.nknsd.teamcode.frameworks;

import org.nknsd.teamcode.components.utility.AutoHeart;

import java.util.ArrayList;

public abstract class NKNStep {
    public String name;
    private ArrayList<NKNStep> parentSteps, childSteps;
    private AutoHeart autoHeart;

    private boolean shouldStart() {
        for (NKNStep parent : parentSteps) {
            if (!parent.doneTask()) {
                return false;
            }
        }

        return true;
    }

    private boolean hasStarted = false;
    public void main() {
        if (!hasStarted) {
            if (shouldStart()) {
                hasStarted = true;
                startUp();

            } else {
                return;

            }
        }

        doTask();

        // On completion, create children
        if (doneTask()) {
            for(NKNStep child : childSteps){
               autoHeart.addStep(child);

            }

        }
    }

    protected abstract void startUp();
    protected abstract void doTask();
    public abstract boolean doneTask();

    protected void killParents() {
        for (NKNStep parent : parentSteps) {
            autoHeart.removeStep(parent);
        }
    }

    public void link(AutoHeart autoHeart) {
        this.autoHeart = autoHeart;
    }

    public void addChild(NKNStep child) {
        childSteps.add(child);
    }

    public void addParent(NKNStep parent) {
        parentSteps.add(parent);
    }
}
