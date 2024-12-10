package org.nknsd.teamcode.autoSteps;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.ExtensionHandler;
import org.nknsd.teamcode.components.handlers.SpecimenExtensionHandler;
import org.nknsd.teamcode.frameworks.NKNAutoStep;
import org.nknsd.teamcode.helperClasses.AutoSkeleton;

public class AutoStepExtendSpecimenArm implements NKNAutoStep {
    private final SpecimenExtensionHandler.SpecimenExtensionPositions extensionPosition;
    AutoSkeleton autoSkeleton;

    public AutoStepExtendSpecimenArm(SpecimenExtensionHandler.SpecimenExtensionPositions extensionPosition) {
        this.extensionPosition = extensionPosition;
    }

    @Override
    public void link(AutoSkeleton autoSkeleton) {
        this.autoSkeleton = autoSkeleton;

    }

    public void begin(ElapsedTime runtime, Telemetry telemetry) {
        autoSkeleton.setTargetSpecArmExtension(extensionPosition);
    }

    @Override
    public void run(Telemetry telemetry, ElapsedTime runtime) {}

    @Override
    public boolean isDone(ElapsedTime runtime) {
        return autoSkeleton.isSpecExtensionDone();
    }

    @Override
    public String getName() {
        return "Extending to " + extensionPosition.name();
    }
}
