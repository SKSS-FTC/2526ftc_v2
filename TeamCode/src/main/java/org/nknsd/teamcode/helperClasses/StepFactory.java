package org.nknsd.teamcode.helperClasses;

import com.qualcomm.robotcore.hardware.Blinker;

import org.nknsd.teamcode.frameworks.NKNStep;

public class StepFactory {

    /**
     *
     * @param clump the clump (represented as a step) that we want to add a new step to
     * @param newStep the new step to add to the clump
     * @return the resulting clump (represented as a step)
     */
    public NKNStep createClump (NKNStep clump, NKNStep newStep) {
        clump.addChild(newStep);
        newStep.addParent(clump);

        return newStep;
    }

    /**
     *
     * @param previousClumps takes in all clumps required to start the splitter
     * @param nextClumps clumps which the splitter will start
     * @return the splitter being created
     */
    public NKNStep createSplitter (NKNStep[] previousClumps, NKNStep[] nextClumps){

    }
}
