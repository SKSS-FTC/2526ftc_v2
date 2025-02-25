package org.nknsd.teamcode.autoSteps.magentaSteps;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.ExtensionHandler;
import org.nknsd.teamcode.frameworks.NKNAutoStep;
import org.nknsd.teamcode.helperClasses.AutoSkeleton;

public class AutoStepExtendArmSynced extends NKNAutoStep {
    private final ExtensionHandler.ExtensionPositions extensionPosition;

    public AutoStepExtendArmSynced(ExtensionHandler.ExtensionPositions extensionPosition) {
        this.extensionPosition = extensionPosition;
    }

    public void begin(ElapsedTime runtime, Telemetry telemetry) {
        autoSkeleton.setTargetArmExtension(extensionPosition);
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
