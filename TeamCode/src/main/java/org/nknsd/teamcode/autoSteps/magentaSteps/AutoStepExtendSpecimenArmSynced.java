package org.nknsd.teamcode.autoSteps.magentaSteps;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.SpecimenExtensionHandler;
import org.nknsd.teamcode.frameworks.NKNAutoStep;
import org.nknsd.teamcode.helperClasses.AutoSkeleton;

public class AutoStepExtendSpecimenArmSynced extends NKNAutoStep {
    private final SpecimenExtensionHandler.SpecimenExtensionPositions extensionPosition;
    AutoSkeleton autoSkeleton;

    public AutoStepExtendSpecimenArmSynced(SpecimenExtensionHandler.SpecimenExtensionPositions extensionPosition) {
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
        return true;
    }

    @Override
    public String getName() {
        return "Extending to " + extensionPosition.name();
    }
}
