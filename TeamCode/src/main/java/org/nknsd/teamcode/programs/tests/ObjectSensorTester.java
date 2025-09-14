package org.nknsd.teamcode.programs.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.nknsd.teamcode.components.handlers.FlowHandler;
import org.nknsd.teamcode.components.handlers.MotorDriver;
import org.nknsd.teamcode.components.handlers.MotorHandler;
import org.nknsd.teamcode.components.handlers.VisionHandler;
import org.nknsd.teamcode.components.handlers.WufSpotter;
import org.nknsd.teamcode.components.handlers.statemachine.StateMachine;
import org.nknsd.teamcode.feedbackcontroller.PidController;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNProgram;
import org.nknsd.teamcode.states.RobotPosWithin;
import org.nknsd.teamcode.states.WufHunter;

import java.util.List;

@TeleOp(name = "ObjectSensing Tester", group = "Tests")
public class ObjectSensorTester extends NKNProgram {
    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        VisionHandler visionHandler = new VisionHandler(1);
        FlowHandler flowHandler = new FlowHandler();
        MotorHandler motorHandler = new MotorHandler();
        motorHandler.setEnabled(true);
        StateMachine stateMachine = new StateMachine();
        PidController xpController = new PidController(0.08, .5, 0.1, .25, true, 0.03, 0.3);
        PidController ypController = new PidController(0.08     , .5, 0.1, .25, true, 0.03, 0.3);
        PidController hpController = new PidController(0.4, .5, 0.1, .2, true, 0.1, 0.5);
        MotorDriver motorDriver = new MotorDriver(flowHandler, motorHandler, xpController, ypController, hpController);
        WufSpotter wufSpotter = new WufSpotter(visionHandler, motorDriver,flowHandler);

        components.add(visionHandler);
        telemetryEnabled.add(visionHandler);
        components.add(motorHandler);
        components.add(flowHandler);
        telemetryEnabled.add(flowHandler);
        components.add(motorDriver);
        telemetryEnabled.add(motorDriver);
        components.add(stateMachine);
        telemetryEnabled.add(stateMachine);
        components.add(wufSpotter);
        telemetryEnabled.add(wufSpotter);

        WufHunter wufHunter = new WufHunter(wufSpotter,flowHandler,motorDriver,35);
        stateMachine.addState(WufHunter.STATE_NAME, wufHunter);
        stateMachine.addState("visionWithin",new RobotPosWithin(motorDriver,1,1,1,1, new String[]{"distancing"}, new String[]{WufHunter.STATE_NAME}));


//        RobotPosWithin robotPosWithin = new RobotPosWithin(motorDriver, 3, 100, 0.1, 100, 100, new String[]{WufHunter.STATE_NAME}, new String[]{WufDriver.STATE_NAME});
//        stateMachine.addState(WufDriver.STATE_NAME + "_WITHIN", robotPosWithin);
//        WufDriver wufDriver = new WufDriver(wufSpotter, motorDriver,flowHandler,20);
//        stateMachine.addState(WufDriver.STATE_NAME, wufDriver);

        stateMachine.startState(WufHunter.STATE_NAME);

    }
}
