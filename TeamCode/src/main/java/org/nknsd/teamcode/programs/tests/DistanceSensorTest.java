package org.nknsd.teamcode.programs.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.nknsd.teamcode.components.sensors.DistSensor;
import org.nknsd.teamcode.components.sensors.DistSensorHub;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNProgram;

import java.util.HashMap;
import java.util.List;

@TeleOp(name = "Forward Distance Sensor Test", group="Tests") @Disabled
public class DistanceSensorTest extends NKNProgram {
    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        DistSensor backwardSensor = new DistSensor("sensorBackDist");
        DistSensor leftSensor = new DistSensor("sensorLeftDist");
        DistSensor rightSensor = new DistSensor("sensorRightDist");
        DistSensorHub allSensor = new DistSensorHub();
        components.add(backwardSensor);
        components.add(leftSensor);
        components.add(rightSensor);
        components.add(allSensor);
        telemetryEnabled.add(allSensor);

        HashMap<DistSensorHub.SensorNames, DistSensor> sensors = new HashMap<>();
        sensors.put(DistSensorHub.SensorNames.BACK, backwardSensor);
        sensors.put(DistSensorHub.SensorNames.LEFT, leftSensor);
        sensors.put(DistSensorHub.SensorNames.RIGHT, rightSensor);
        allSensor.link(sensors);
    }
}
