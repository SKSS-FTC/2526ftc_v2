package org.nknsd.teamcode.programs.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.components.sensors.PotentiometerSensor;
import org.nknsd.teamcode.frameworks.NKNProgram;

import java.util.List;

@TeleOp(name = "Pot Tester", group="Tests")
public class PotentiometerProgram extends NKNProgram {
    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        // Pot
        PotentiometerSensor potentiometerSensor = new PotentiometerSensor();
        components.add(potentiometerSensor);
        telemetryEnabled.add(potentiometerSensor);
    }
}
