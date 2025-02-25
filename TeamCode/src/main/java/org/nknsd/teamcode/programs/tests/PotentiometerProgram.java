package org.nknsd.teamcode.programs.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.components.sensors.PotentiometerSensor;
import org.nknsd.teamcode.frameworks.NKNProgram;

import java.util.List;

<<<<<<< HEAD
@TeleOp(name = "Pot Tester", group="Tests") @Disabled
public class PotentiometerProgram extends NKNProgramTrue {
=======
@TeleOp(name = "Pot Tester", group="Tests")
public class PotentiometerProgram extends NKNProgram {
>>>>>>> 67db820610588792885f3f6102b14615e92e5d38
    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        // Pot
        PotentiometerSensor potentiometerSensor = new PotentiometerSensor();
        components.add(potentiometerSensor);
        telemetryEnabled.add(potentiometerSensor);
    }
}
