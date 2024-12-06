package org.nknsd.teamcode.autoSteps;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.SpecimenClawHandler;
import org.nknsd.teamcode.frameworks.NKNAutoStep;
import org.nknsd.teamcode.helperClasses.AutoSkeleton;

import java.util.concurrent.TimeUnit;

public class AutoStepSpecimenClaw implements NKNAutoStep {
    AutoSkeleton autoSkeleton;
    private final SpecimenClawHandler.ClawPositions clawPosition;

    public AutoStepSpecimenClaw(SpecimenClawHandler.ClawPositions clawPosition) {
        this.clawPosition = clawPosition;
    }

    @Override
    public void link(AutoSkeleton autoSkeleton) {
        this.autoSkeleton = autoSkeleton;

    }

    public void begin(ElapsedTime runtime, Telemetry telemetry) {
        autoSkeleton.setSpecimenClawTarget(clawPosition);
    }

    @Override
    public void run(Telemetry telemetry, ElapsedTime runtime) {}

    @Override
    public boolean isDone(ElapsedTime runtime) {
        return true;
    }

    @Override
    public String getName() {
        return "Setting specimen claw value";
    }
}
