package org.nknsd.teamcode.states;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.FlowHandler;
import org.nknsd.teamcode.components.handlers.MotorDriver;
import org.nknsd.teamcode.components.handlers.WufSpotter;
import org.nknsd.teamcode.components.handlers.statemachine.StateMachine;

public class WufHunter extends StateMachine.State {

    private static final double GIVE_UP_TIME = 1000;
    static public String STATE_NAME = "WUF_HUNTER";

    SparkFunOTOS.Pose2D targetPos = new SparkFunOTOS.Pose2D(0, 0, 0);


    private final WufSpotter wufSpotter;
    final FlowHandler flowHandler;
    final MotorDriver motorDriver;
    final double targetDistToWuf;
    private double lastWufSeenTime;
    private double lastNotReadyTime;


    public WufHunter(WufSpotter wufSpotter, FlowHandler flowHandler, MotorDriver motorDriver, double targetDistToWuf) {
        this.wufSpotter = wufSpotter;
        this.flowHandler = flowHandler;
        this.motorDriver = motorDriver;
        this.targetDistToWuf = targetDistToWuf;
    }

    @Override
    protected void run(ElapsedTime runtime, Telemetry telemetry) {
        if (wufSpotter.doesWufExist()) {
            SparkFunOTOS.Pose2D curPos = flowHandler.getPosition();


            SparkFunOTOS.Pose2D wufPos = wufSpotter.getObjectPos();

            double deltaX = wufPos.x - curPos.x;
            double deltaY = wufPos.y - curPos.y;
            double dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            if (dist > 50) {
                targetPos.x = curPos.x + (deltaX / dist * 2);
                targetPos.y = curPos.y + (deltaY / dist * 2);
            } else {
                targetPos.x = wufPos.x - (deltaX / dist * 25);
                targetPos.y = wufPos.y - (deltaY / dist * 25);
            }
            targetPos.h = wufPos.h;
            motorDriver.setTarget(targetPos);

            if (Math.abs(curPos.h - targetPos.h) < .05 && Math.abs(curPos.x - targetPos.x) < 1 && Math.abs(curPos.y - targetPos.y) < 1 ) {
                if (runtime.milliseconds() - lastNotReadyTime >500) {
                    stateMachine.startState(WufReachState.STATE_NAME);
                    stateMachine.stopState(STATE_NAME);
                    return;
                }
            } else {
                lastNotReadyTime = runtime.milliseconds();
            }

            lastWufSeenTime = runtime.milliseconds();
        } else if (runtime.milliseconds() - lastWufSeenTime > GIVE_UP_TIME) {
            stateMachine.stopState(STATE_NAME);
            stateMachine.startState(WufSpinner.STATE_NAME);
        } else if (runtime.milliseconds() - lastWufSeenTime > GIVE_UP_TIME / 3) {
            SparkFunOTOS.Pose2D curPos = flowHandler.getPosition();
            SparkFunOTOS.Pose2D wufPos = wufSpotter.getObjectPos();
            double deltaX = wufPos.x - curPos.x;
            double deltaY = wufPos.y - curPos.y;
            double dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            targetPos.x = curPos.x - (deltaX / dist * 2);
            targetPos.y = curPos.y - (deltaY / dist * 2);
        }
    }

    @Override
    protected void started() {
        lastWufSeenTime = startTime;
        lastNotReadyTime = startTime;
    }

    @Override
    protected void stopped() {

    }
}
