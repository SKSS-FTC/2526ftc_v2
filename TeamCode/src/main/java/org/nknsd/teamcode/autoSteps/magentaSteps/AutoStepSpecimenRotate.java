package org.nknsd.teamcode.autoSteps.magentaSteps;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.SpecimenRotationHandler;
import org.nknsd.teamcode.frameworks.NKNAutoStep;
import org.nknsd.teamcode.helperClasses.AutoSkeleton;

public class AutoStepSpecimenRotate extends NKNAutoStep {
    AutoSkeleton autoSkeleton;
    private final SpecimenRotationHandler.SpecimenRotationPositions rotationPosition;

    public AutoStepSpecimenRotate(SpecimenRotationHandler.SpecimenRotationPositions rotationPosition) {
        this.rotationPosition = rotationPosition;
    }

    @Override
    public void link(AutoSkeleton autoSkeleton) {
        this.autoSkeleton = autoSkeleton;

    }

    public void begin(ElapsedTime runtime, Telemetry telemetry) {
        autoSkeleton.setSpecimenRotationTarget(rotationPosition);
    }

    @Override
    public void run(Telemetry telemetry, ElapsedTime runtime) {}

    @Override
    public boolean isDone(ElapsedTime runtime) {
        return true;
    }

    @Override
    public String getName() {
        return "Setting specimen rotation target";
    }
}
