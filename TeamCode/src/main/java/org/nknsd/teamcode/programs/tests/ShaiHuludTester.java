package org.nknsd.teamcode.programs.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.nknsd.teamcode.components.handlers.ShaiHuludHandler;
import org.nknsd.teamcode.components.testfiles.ShaiHuludMonkey;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNProgramTrue;

import java.util.List;
@TeleOp(name = "Shai Hulud Tester", group="Tests")@Disabled
public class ShaiHuludTester extends NKNProgramTrue {


    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        ShaiHuludHandler shaiHuludHandler = new ShaiHuludHandler();
        components.add(shaiHuludHandler);
        telemetryEnabled.add(shaiHuludHandler);

        ShaiHuludMonkey shaiHuludMonkey = new ShaiHuludMonkey();
        components.add(shaiHuludMonkey);

        shaiHuludMonkey.link(shaiHuludHandler);
    }
}
