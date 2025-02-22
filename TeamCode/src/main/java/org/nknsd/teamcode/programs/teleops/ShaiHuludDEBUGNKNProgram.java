package org.nknsd.teamcode.programs.teleops;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.nknsd.teamcode.components.handlers.JointedArmHandler;
import org.nknsd.teamcode.components.handlers.ShaiHuludHandler;
import org.nknsd.teamcode.components.handlers.TheBowlHandler;
import org.nknsd.teamcode.components.handlers.WheelHandler;
import org.nknsd.teamcode.components.sensors.IMUSensor;
import org.nknsd.teamcode.components.sensors.hummelvision.LilyVisionHandler;
import org.nknsd.teamcode.components.utility.ColorPicker;
import org.nknsd.teamcode.components.utility.GamePadHandler;
import org.nknsd.teamcode.controlSchemes.reals.CollyWheelController;
import org.nknsd.teamcode.controlSchemes.reals.DefaultShaiHuludController;
import org.nknsd.teamcode.controlSchemes.reals.KarstenGeneric2PController;
import org.nknsd.teamcode.drivers.ShaiHuludDriver;
import org.nknsd.teamcode.drivers.WheelDriver;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNProgramTrue;

import java.util.List;

@TeleOp(name = "Hail DEBUG Mary")
public class ShaiHuludDEBUGNKNProgram extends NKNProgramTrue {
    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        // Misc
        GamePadHandler gamePadHandler = new GamePadHandler();
        components.add(gamePadHandler);
        //telemetryEnabled.add(gamePadHandler);

        ColorPicker colorPicker = new ColorPicker();
        components.add(colorPicker);
        telemetryEnabled.add(colorPicker);


        // Sensor
        IMUSensor imuSensor = new IMUSensor();
        components.add(imuSensor);

        LilyVisionHandler lilyVisionHandler = new LilyVisionHandler();
        components.add(lilyVisionHandler);
        telemetryEnabled.add(lilyVisionHandler);


        // Handlers
        WheelHandler wheelHandler = new WheelHandler();
        components.add(wheelHandler);

        ShaiHuludHandler shaiHuludHandler = new ShaiHuludHandler();
        components.add(shaiHuludHandler);
        telemetryEnabled.add(shaiHuludHandler);

        JointedArmHandler jointedArmHandler = new JointedArmHandler();
        components.add(jointedArmHandler);
        telemetryEnabled.add(jointedArmHandler);

        TheBowlHandler bowlHandler = new TheBowlHandler();
        components.add(bowlHandler);
        telemetryEnabled.add(bowlHandler);


        // Driver
        WheelDriver wheelDriver = new WheelDriver(0, 1, 5, GamePadHandler.GamepadSticks.LEFT_JOYSTICK_Y, GamePadHandler.GamepadSticks.LEFT_JOYSTICK_X, GamePadHandler.GamepadSticks.RIGHT_JOYSTICK_X);
        components.add(wheelDriver);
        telemetryEnabled.add(wheelDriver);

        ShaiHuludDriver shaiHuludDriver = new ShaiHuludDriver();
        components.add(shaiHuludDriver);
        telemetryEnabled.add(shaiHuludDriver);


        // Controllers
        CollyWheelController wheelController = new CollyWheelController();
        DefaultShaiHuludController shaiHuludController = new DefaultShaiHuludController();
        KarstenGeneric2PController generic2PController = new KarstenGeneric2PController();


        // Link the components to each other
        colorPicker.link(gamePadHandler, generic2PController);
        lilyVisionHandler.link(colorPicker);
        shaiHuludHandler.link(lilyVisionHandler, colorPicker);
        //shaiHuludHandler.linkWheels(wheelHandler);

        wheelDriver.link(gamePadHandler, wheelHandler, wheelController);
        shaiHuludDriver.link(gamePadHandler, shaiHuludHandler, jointedArmHandler, bowlHandler, shaiHuludController);

        wheelController.link(gamePadHandler);
        shaiHuludController.link(gamePadHandler);
        generic2PController.link(gamePadHandler);

        shaiHuludDriver.enableDebug();
    }
}
