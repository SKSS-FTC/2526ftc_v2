package org.nknsd.teamcode.autoSteps.magentaSteps;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.SpecimenExtensionHandler;
import org.nknsd.teamcode.frameworks.NKNAutoStep;
import org.nknsd.teamcode.helperClasses.AutoSkeleton;

import java.util.concurrent.TimeUnit;

public class AutoStepExtendSpecAndOrientBackDist extends NKNAutoStep {
    private final SpecimenExtensionHandler.SpecimenExtensionPositions extensionPosition;
    private final double targ; private final double speed; private final double margin;
    AutoSkeleton autoSkeleton;
    private boolean flag = false; // God this is horrible code. Quick patch fix for the alternate way to access this step.. which also disables the extension part. Check specimenFancyDepositDriver

    public AutoStepExtendSpecAndOrientBackDist(SpecimenExtensionHandler.SpecimenExtensionPositions extensionPosition, double target, double speed, double margin ) {
        this.extensionPosition = extensionPosition;
        this.targ = target;
        this.speed = speed;
        this.margin = margin;
    }

    @Override
    public void link(AutoSkeleton autoSkeleton) {
        this.autoSkeleton = autoSkeleton;

    }

    public void begin(ElapsedTime runtime, Telemetry telemetry) {
        autoSkeleton.setTargetSpecArmExtension(extensionPosition);

    }

    public void begin() {
        flag = true;
    }

    @Override
    public void run(Telemetry telemetry, ElapsedTime runtime) {
        telemetry.addData("Sensing", autoSkeleton.getSensorBackDist());

        if (Math.abs(targ - autoSkeleton.getSensorBackDist()) < margin) {
            autoSkeleton.relativeRun(0,0);
            telemetry.addData("Running", "NOT");
        }
        else if (targ > autoSkeleton.getSensorBackDist()) {
            autoSkeleton.relativeRun(0, speed);
            telemetry.addData("Running", "Closer");
        }
        else {
            autoSkeleton.relativeRun(0, -speed);
            telemetry.addData("Running", "Farther");
        }

    }

    @Override
    public boolean isDone(ElapsedTime runtime) {
        if (autoSkeleton.isSpecExtensionDone() && !flag){
            autoSkeleton.relativeRun(0,0);
            return true;
        } else if (Math.abs(targ - autoSkeleton.getSensorBackDist()) < margin && flag) {
            autoSkeleton.relativeRun(0,0);
            return true;
        }

        return false;
    }

    @Override
    public String getName() {
        return "Extending to " + extensionPosition.name();
    }
}
