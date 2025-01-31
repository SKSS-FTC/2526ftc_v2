package org.nknsd.teamcode.autoSteps;

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
        autoSkeleton.setTargetSpecArmExtension(extensionPosition);

    }

    @Override
    public void run(Telemetry telemetry, ElapsedTime runtime) {
       if (Math.abs(targ - autoSkeleton.getSensorForDist()) > margin) {
           autoSkeleton.relativeRun(0, speed);
       } else if (Math.abs(targ - autoSkeleton.getSensorForDist()) < margin){
           autoSkeleton.relativeRun(0, -speed);
       } else {
           autoSkeleton.relativeRun(0,0);
       }
    }

    @Override
    public boolean isDone(ElapsedTime runtime) {
        if (autoSkeleton.isSpecExtensionDone()){
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
