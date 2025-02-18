package org.nknsd.teamcode.autoSteps;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.ExtensionHandler;
import org.nknsd.teamcode.frameworks.NKNAutoStep;
import org.nknsd.teamcode.helperClasses.AutoSkeleton;
import org.nknsd.teamcode.components.handlers.JointedArmHandler;

public class AutoStepExtendJointedArm extends NKNAutoStep {
    private final JointedArmHandler.Positions position;
    AutoSkeleton autoSkeleton;

    public AutoStepExtendJointedArm(JointedArmHandler.Positions position) {
        this.position = position;
    }

    @Override
    public void link(AutoSkeleton autoSkeleton) {
        this.autoSkeleton = autoSkeleton;

    }

    public void begin(ElapsedTime runtime, Telemetry telemetry) {
        autoSkeleton.setJointedArmPosition(position);
    }

    @Override
    public void run(Telemetry telemetry, ElapsedTime runtime) {}

    @Override
    public boolean isDone(ElapsedTime runtime) {
        return autoSkeleton.isJointedArmExtensionDone();
    }

    @Override
    public String getName() {
        return "Extending";
    }
}
