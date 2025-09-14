package org.nknsd.teamcode.states;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.RailHandler;
import org.nknsd.teamcode.components.handlers.ServoHandler;
import org.nknsd.teamcode.components.handlers.VisionHandler;
import org.nknsd.teamcode.components.handlers.statemachine.StateMachine;

public class WufReachState extends StateMachine.State {

    static public String STATE_NAME = "WUF_REACH_STATE";

    final double maxSpeed;
    final int maxLength;
    int targetFoundLength;
    final RailHandler railHandler;
    final String successStateName;
    final String failStateName;
    private final ServoHandler servoHandler;
    final VisionHandler visionHandler;

    public WufReachState(RailHandler railHandler, ServoHandler servoHandler, VisionHandler visionHandler, double maxSpeed, int maxLength, String successStateName, String failStateName) {
        this.railHandler = railHandler;
        this.servoHandler = servoHandler;
        this.maxSpeed = maxSpeed;
        this.maxLength = maxLength;
        this.successStateName = successStateName;
        this.failStateName = failStateName;
        this.visionHandler = visionHandler;
    }


    @Override
    protected void run(ElapsedTime runtime, Telemetry telemetry) {
        int rMotorPos = railHandler.getRMotorPos();
        int lMotorPos = railHandler.getLMotorPos();

        servoHandler.setBothPos(0.2, 0.4);

        if (runtime.milliseconds() - startTime > 1000) {
            if (rMotorPos >= maxLength || lMotorPos >= maxLength) {
                railHandler.setMotorSpeed(0);
                stateMachine.stopState(STATE_NAME);
                stateMachine.startState(failStateName);
            } else if (targetFoundLength > 0 && (rMotorPos >= targetFoundLength || lMotorPos >= targetFoundLength)) {
                // TODO - make rail handler take in target pos and be smart to align speeds
                railHandler.setMotorSpeed(0);
                stateMachine.stopState(STATE_NAME);
                stateMachine.startState(successStateName);
            } else if (isSuccessful()) {
                targetFoundLength = rMotorPos + 70;
                railHandler.setMotorSpeed(maxSpeed / 2);
            }
        }

    }

    private boolean isSuccessful() {
        return visionHandler.findDist() < 8.75;
    }

    @Override
    protected void started() {
        railHandler.setMotorSpeed(maxSpeed);
        targetFoundLength = 0;
    }

    @Override
    protected void stopped() {
    }
}
