package org.nknsd.teamcode.autoSteps;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNAutoStep;
import org.nknsd.teamcode.helperClasses.AutoSkeleton;

import java.util.concurrent.TimeUnit;

public class AutoStepMoveForwardWithSensor implements NKNAutoStep {
    AutoSkeleton autoSkeleton;
    private final double targ; private final double speed; private final double margin;

    public AutoStepMoveForwardWithSensor(double target, double speed, double margin) {
        this.targ = target;
        this.speed = speed;
        this.margin = margin;
    }

    @Override
    public void link(AutoSkeleton autoSkeleton) {
        this.autoSkeleton = autoSkeleton;
    }

    public void begin(ElapsedTime runtime, Telemetry telemetry) {
        autoSkeleton.relativeRun(0, speed);
    }

    @Override
    public void run(Telemetry telemetry, ElapsedTime runtime) {}

    @Override
    public boolean isDone(ElapsedTime runtime) {
        if (Math.abs(targ - autoSkeleton.getSensorForDist()) < margin) {
            autoSkeleton.relativeRun(0, 0);
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Moving without flow sensor";
    }
}
