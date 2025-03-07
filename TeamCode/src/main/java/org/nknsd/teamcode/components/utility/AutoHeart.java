package org.nknsd.teamcode.components.utility;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AutoHeart implements NKNComponent {
    private NKNStep[] nknSteps;
    private HashMap<String, NKNStep> activeSteps;

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "AutoHeart";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        if (activeSteps.isEmpty()) {
            return;
        }

        for (NKNStep step : activeSteps.values()) {
            step.main();
        }
    }

    public void addStep(NKNStep step) {
        // Make sure the step doesn't already exist first!
        if (activeSteps.containsValue(step)) {
            activeSteps.put(step.name, step);
        }
    }

    public void removeStep(NKNStep step) {
        activeSteps.remove(step.name);
    }

    public void end() {
        activeSteps.clear();
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {

    }

    public void link(NKNStep startStep){
        activeSteps.put(startStep.name, startStep);
    }
}
