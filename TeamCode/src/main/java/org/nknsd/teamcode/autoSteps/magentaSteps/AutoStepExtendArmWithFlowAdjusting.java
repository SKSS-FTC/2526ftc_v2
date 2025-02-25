package org.nknsd.teamcode.autoSteps.magentaSteps;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.ExtensionHandler;
import org.nknsd.teamcode.frameworks.NKNAutoStep;
import org.nknsd.teamcode.helperClasses.AutoSkeleton;

public class AutoStepExtendArmWithFlowAdjusting extends NKNAutoStep {
    private final ExtensionHandler.ExtensionPositions extensionPosition;
    AutoSkeleton autoSkeleton;
    private final double heading, xTarg, yTarg;

    public AutoStepExtendArmWithFlowAdjusting(ExtensionHandler.ExtensionPositions extensionPosition, double heading, double xTarg, double yTarg) {
        this.extensionPosition = extensionPosition;
        this.heading = heading;
        this.xTarg = xTarg;
        this.yTarg = yTarg;
    }

    @Override
    public void link(AutoSkeleton autoSkeleton) {
        this.autoSkeleton = autoSkeleton;

    }

    public void begin(ElapsedTime runtime, Telemetry telemetry) {
        autoSkeleton.setTargetArmExtension(extensionPosition);
        autoSkeleton.setTargetPosition(xTarg, yTarg);
        autoSkeleton.setTargetRotation(heading);
    }

    @Override
    public void run(Telemetry telemetry, ElapsedTime runtime) {}

    @Override
    public boolean isDone(ElapsedTime runtime) {
        if (autoSkeleton.isExtensionDone()) {
            autoSkeleton.relativeRun(0, 0);
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Extending to " + extensionPosition.name() + " while adjusting to reach position (" + xTarg + ", " + yTarg + ") with heading " + heading;
    }
}
