package org.nknsd.teamcode.states;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.FlowHandler;
import org.nknsd.teamcode.components.handlers.MotorDriver;
import org.nknsd.teamcode.components.handlers.WufSpotter;
import org.nknsd.teamcode.components.handlers.statemachine.StateMachine;

public class WufSpinner extends StateMachine.State {

    static public String STATE_NAME = "WUF_SPINNER";

    final WufSpotter wufSpotter;
    final MotorDriver motorDriver;
    final FlowHandler flowHandler;

    public WufSpinner(WufSpotter wufSpotter, MotorDriver motorDriver, FlowHandler flowHandler) {
        this.wufSpotter = wufSpotter;
        this.motorDriver = motorDriver;
        this.flowHandler = flowHandler;
    }

    double lastTime;
    SparkFunOTOS.Pose2D pos = new SparkFunOTOS.Pose2D();
    double startH;

    @Override
    protected void run(ElapsedTime runtime, Telemetry telemetry) {
        if (wufSpotter.doesWufExist()) {
            stateMachine.stopAnonymous(this);
        }

        pos.h = (runtime.milliseconds() - startTime) / 10000 * Math.PI * 2 + startH;
        motorDriver.setTarget(pos);

    }

    @Override
    protected void started() {
        pos = flowHandler.getPosition();
        startH = pos.h;
    }

    @Override
    protected void stopped() {
        stateMachine.startState(WufHunter.STATE_NAME);
    }
}
