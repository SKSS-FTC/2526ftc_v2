package org.nknsd.teamcode.frameworks;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.helperClasses.AutoSkeleton;

public abstract class NKNAutoStep {
    protected Telemetry telemetry;
    protected AutoSkeleton autoSkeleton;
    public abstract void run(Telemetry telemetry, ElapsedTime runtime);
    public abstract boolean isDone(ElapsedTime runtime);
    public abstract String getName();
    public abstract void begin(ElapsedTime runtime, Telemetry telemetry);
    public void linkTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }
    public void link(AutoSkeleton autoSkeleton) {
        this.autoSkeleton = autoSkeleton;
    }
}
