package org.nknsd.teamcode.programs.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.nknsd.teamcode.components.utility.ColorPicker;
import org.nknsd.teamcode.components.utility.GamePadHandler;
import org.nknsd.teamcode.controlSchemes.reals.KarstenGeneric2PController;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.components.sensors.hummelvision.LilyVisionHandler;
import org.nknsd.teamcode.frameworks.NKNProgram;

import java.util.List;

@TeleOp(name = "Vision Tester", group="Tests")@Disabled
// @Disabled
public class VisionSensorTester extends NKNProgram {
    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        GamePadHandler gamePadHandler = new GamePadHandler();
        components.add(gamePadHandler);

        ColorPicker colorPicker = new ColorPicker();
        components.add(colorPicker);
        telemetryEnabled.add(colorPicker);

        LilyVisionHandler lilyVisionHandler = new LilyVisionHandler();
        components.add(lilyVisionHandler);
        telemetryEnabled.add(lilyVisionHandler);

        KarstenGeneric2PController controller = new KarstenGeneric2PController();
        controller.link(gamePadHandler);

        colorPicker.link(gamePadHandler, controller);
        lilyVisionHandler.link(colorPicker);
    }
}
