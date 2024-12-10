package org.nknsd.teamcode.autoSteps;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNAutoStep;
import org.nknsd.teamcode.helperClasses.AutoSkeleton;

public class AutoStepMoveBackwardWithSensor extends NKNAutoStep {
    AutoSkeleton autoSkeleton;
    private final double targ; private final double speed; private final double margin;

    public AutoStepMoveBackwardWithSensor(double target, double speed, double margin) {
        this.targ = target;
        this.speed = -speed;
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
    public void run(Telemetry telemetry, ElapsedTime runtime) {
        // should adjust speed based on distance (P controller)
        // better at least
//        double adjSpeed = speed * (targ - autoSkeleton.getSensorForDist()) / Math.abs(targ - autoSkeleton.getSensorForDist()); //maintains speed while changing direction based on under/overshoot
//        autoSkeleton.relativeRun(0, adjSpeed);
        // robot stops working with this code because it's BAD
    }

    @Override
    public boolean isDone(ElapsedTime runtime) {
        // naive implementation
        if (Math.abs(targ - autoSkeleton.getSensorBackDist()) < margin) {
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
