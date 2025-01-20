package org.nknsd.teamcode.programs.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.nknsd.teamcode.components.sensors.hummelvision.LilyVisionHandler;
import org.nknsd.teamcode.components.utility.ColorPicker;
import org.nknsd.teamcode.components.utility.GamePadHandler;
import org.nknsd.teamcode.controlSchemes.reals.KarstenGeneric2PController;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNProgramTrue;

import java.util.List;

@TeleOp(name = "Color Picker Tester", group="Tests")
// @Disabled
public class ColorPickerTester extends NKNProgramTrue {
    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        GamePadHandler gamePadHandler = new GamePadHandler();
        components.add(gamePadHandler);

        ColorPicker colorPicker = new ColorPicker();
        components.add(colorPicker);

        KarstenGeneric2PController controller = new KarstenGeneric2PController();
        controller.link(gamePadHandler);
        colorPicker.link(gamePadHandler, controller);

        telemetryEnabled.add(colorPicker);
    }
}
