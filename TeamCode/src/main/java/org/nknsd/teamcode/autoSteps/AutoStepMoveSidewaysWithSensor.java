package org.nknsd.teamcode.autoSteps;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNAutoStep;
import org.nknsd.teamcode.helperClasses.AutoSkeleton;

import java.util.concurrent.TimeUnit;

public class AutoStepMoveSidewaysWithSensor extends NKNAutoStep {
    private final double target, sensorSpeed, margin, sideSpeed;
    private final long time; private long startTime;
    AutoSkeleton autoSkeleton;

    public AutoStepMoveSidewaysWithSensor (double target, double sensorSpeed, double margin, double sideSpeed, long time) {
        this.target = target;
        this.sensorSpeed = sensorSpeed;
        this.margin = margin;
        this.sideSpeed = sideSpeed;
        this.time = time;
    }

    @Override
    public void link(AutoSkeleton autoSkeleton) {
        this.autoSkeleton = autoSkeleton;

    }

    public void begin(ElapsedTime runtime, Telemetry telemetry) {
        startTime = runtime.now(TimeUnit.MILLISECONDS);
    }

    @Override
    public void run(Telemetry telemetry, ElapsedTime runtime) {
        if (Math.abs(target - autoSkeleton.getSensorForDist()) > margin) {
            autoSkeleton.relativeRun(sideSpeed, sensorSpeed);
        } else if (Math.abs(target - autoSkeleton.getSensorForDist()) < margin){
            autoSkeleton.relativeRun(sideSpeed, -sensorSpeed);
        } else {
            autoSkeleton.relativeRun(sideSpeed,0);
        }
    }

    @Override
    public boolean isDone(ElapsedTime runtime) {
        if (runtime.now(TimeUnit.MILLISECONDS) - startTime > time) {
            autoSkeleton.relativeRun(0, 0);
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Moving Sideways with sensor correction";
    }
}
