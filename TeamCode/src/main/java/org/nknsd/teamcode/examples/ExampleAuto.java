package org.nknsd.teamcode.examples;

import org.nknsd.teamcode.components.utility.AutoHeart;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNProgram;
import org.nknsd.teamcode.frameworks.NKNStep;
import org.nknsd.teamcode.helperClasses.StepFactory;

import java.util.List;

public class ExampleAuto extends NKNProgram {
    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        StepFactory stepFactory = new StepFactory();
        AutoHeart autoHeart = new AutoHeart();
        components.add(autoHeart);





        NKNStep startStep = stepFactory.stepStart();
        autoHeart.link(startStep);
    }
}
