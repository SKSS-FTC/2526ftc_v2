package org.nknsd.teamcode.programs.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.nknsd.teamcode.components.sensors.DistSensor;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNProgramTrue;

import java.util.List;

@TeleOp(name = "Forward Distance Sensor Test", group="Tests")
public class DistanceSensorTest extends NKNProgramTrue {
    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        DistSensor forwardSensor = new DistSensor("sensorForDist");
        DistSensor backwardSensor = new DistSensor("sensorBackDist");
        components.add(forwardSensor);
        components.add(backwardSensor);
        telemetryEnabled.add(forwardSensor);
        telemetryEnabled.add(backwardSensor);
    }
}
