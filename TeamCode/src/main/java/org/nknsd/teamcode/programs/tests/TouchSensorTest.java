package org.nknsd.teamcode.programs.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.nknsd.teamcode.components.sensors.TouchSensor;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNProgramTrue;

import java.util.HashMap;
import java.util.List;

@TeleOp(name = "Touch Sensor Test", group="Tests")
public class TouchSensorTest extends NKNProgramTrue {
    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        TouchSensor touchSensor = new TouchSensor("touchSensor");
        components.add(touchSensor);
        touchSensor.isTouching();
        telemetryEnabled.add(touchSensor);
    }
}
