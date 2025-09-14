package org.nknsd.teamcode.programs.tests;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.nknsd.teamcode.components.handlers.FlowHandler;
import org.nknsd.teamcode.components.handlers.MotorDriver;
import org.nknsd.teamcode.components.handlers.MotorHandler;
import org.nknsd.teamcode.components.handlers.RailHandler;
import org.nknsd.teamcode.components.handlers.ServoHandler;
import org.nknsd.teamcode.components.handlers.VisionHandler;
import org.nknsd.teamcode.components.handlers.WufSpotter;
import org.nknsd.teamcode.components.handlers.statemachine.StateMachine;
import org.nknsd.teamcode.feedbackcontroller.PidController;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNProgram;
import org.nknsd.teamcode.states.WufGrabState;
import org.nknsd.teamcode.states.WufHunter;
import org.nknsd.teamcode.states.WufReachState;
import org.nknsd.teamcode.states.WufRetractState;
import org.nknsd.teamcode.states.WufSpinner;

import java.util.List;

@TeleOp(name = "Wuf Killer Tester", group = "Tests")
public class WufKillerTester extends NKNProgram {

    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        VisionHandler visionHandler = new VisionHandler(1);
        FlowHandler flowHandler = new FlowHandler();
        MotorHandler motorHandler = new MotorHandler();
        motorHandler.setEnabled(true);
        StateMachine stateMachine = new StateMachine();
        PidController xpController = new PidController(0.08, .5, 0.1, .25, true, 0.03, 0.3);
        PidController ypController = new PidController(0.08, .5, 0.1, .25, true, 0.03, 0.3);
        PidController hpController = new PidController(0.4, .5, 0.1, .2, true, 0.1, 0.5);
        MotorDriver motorDriver = new MotorDriver(flowHandler, motorHandler, xpController, ypController, hpController);
        WufSpotter wufSpotter = new WufSpotter(visionHandler, motorDriver, flowHandler);

        RailHandler railHandler = new RailHandler();
        components.add(railHandler);
        telemetryEnabled.add(railHandler);
        ServoHandler servoHandler = new ServoHandler("rServo","lServo");
        components.add(servoHandler);
        telemetryEnabled.add(servoHandler);

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

        motorDriver.setTarget(new SparkFunOTOS.Pose2D(0,0,0));

        WufHunter wufHunter = new WufHunter(wufSpotter, flowHandler, motorDriver, 35);
        stateMachine.addState(WufHunter.STATE_NAME, wufHunter);
        stateMachine.addState(WufSpinner.STATE_NAME, new WufSpinner(wufSpotter, motorDriver, flowHandler));

        stateMachine.startState(WufSpinner.STATE_NAME);

        stateMachine.addState(WufReachState.STATE_NAME, new WufReachState(railHandler, servoHandler, visionHandler, 0.7,650, WufGrabState.STATE_NAME, WufRetractState.STATE_NAME));
        stateMachine.addState(WufGrabState.STATE_NAME, new WufGrabState(servoHandler));
        stateMachine.addState(WufRetractState.STATE_NAME, new WufRetractState(railHandler,servoHandler,0.7));
//
//        stateMachine.startState(WufReachState.STATE_NAME);
    }
}
