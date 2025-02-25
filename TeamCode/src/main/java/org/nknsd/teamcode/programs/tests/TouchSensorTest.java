package org.nknsd.teamcode.programs.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.nknsd.teamcode.components.sensors.TouchSens;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNProgramTrue;

import java.util.List;

@TeleOp(name = "Touch Sensor Test", group="Tests")@Disabled
public class TouchSensorTest extends NKNProgramTrue {
    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        TouchSens touchSens = new TouchSens("touchSensor");
        components.add(touchSens);
        touchSens.isTouching();
        telemetryEnabled.add(touchSens);
    }
}
