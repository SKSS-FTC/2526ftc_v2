package org.nknsd.teamcode.states;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.ServoHandler;
import org.nknsd.teamcode.components.handlers.statemachine.StateMachine;

public class WufGrabState extends StateMachine.State {

    static public String STATE_NAME = "WUF_GRAB_STATE";

    private final ServoHandler servoHandler;


    public WufGrabState(ServoHandler servoHandler) {
        this.servoHandler = servoHandler;
    }


    @Override
    protected void run(ElapsedTime runtime, Telemetry telemetry) {
        double currentTime = runtime.milliseconds();

        if (currentTime - startTime <= 50) {
            servoHandler.setBothPos(0.5, 0.7);
        } else if (currentTime - startTime <= 700) {
            servoHandler.setBothPos(0.8, 0.5);
        } else if (currentTime - startTime <= 1200) {
            servoHandler.setBothPos(0.65, 0.35);
        } else if (currentTime - startTime <= 1600) {
            stateMachine.stopState(STATE_NAME);
            stateMachine.startState(WufRetractState.STATE_NAME);
        }
    }

    @Override
    protected void started() {
    }

    @Override
    protected void stopped() {
    }
}
