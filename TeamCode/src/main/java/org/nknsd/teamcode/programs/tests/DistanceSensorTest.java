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
        DistSensor backwardSensor = new DistSensor("sensorBackDist");
        DistSensor leftSensor = new DistSensor("sensorLeftDist");
        DistSensor rightSensor = new DistSensor("sensorRightDist");
        components.add(backwardSensor);
        components.add(leftSensor);
        components.add(rightSensor);
        telemetryEnabled.add(backwardSensor);
        telemetryEnabled.add(leftSensor);
        telemetryEnabled.add(rightSensor);
    }
}
