package org.nknsd.teamcode.autoSteps;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.JointedArmHandler;
import org.nknsd.teamcode.frameworks.NKNAutoStep;
import org.nknsd.teamcode.helperClasses.AutoSkeleton;

public class AutoStepJointedArmGripper extends NKNAutoStep {
    private final boolean shouldClose;
    AutoSkeleton autoSkeleton;

    public AutoStepJointedArmGripper(boolean closed) {
        shouldClose = closed;
    }

    @Override
    public void link(AutoSkeleton autoSkeleton) {
        this.autoSkeleton = autoSkeleton;

    }

    public void begin(ElapsedTime runtime, Telemetry telemetry) {
        if (shouldClose) {
            autoSkeleton.jointedArmGrip();
        } else {
            autoSkeleton.jointedArmRelease();
        }
    }

    @Override
    public void run(Telemetry telemetry, ElapsedTime runtime) {}

    @Override
    public boolean isDone(ElapsedTime runtime) {
        return true;
    }

    @Override
    public String getName() {
        return "Extending";
    }
}
