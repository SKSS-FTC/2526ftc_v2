package org.nknsd.teamcode.states;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.statemachine.StateMachine;

public class TimerState extends StateMachine.State {
    String[] toStop;
    String[] toStart;
    double timerMS;

    public TimerState(double timerMS, String[] toStart, String[] toStop) {
        this.timerMS = timerMS;
        this.toStop = toStop;
        this.toStart = toStart;
    }

    @Override
    protected void run(ElapsedTime runtime, Telemetry telemetry) {
        if (runtime.milliseconds() > (startTime + timerMS)) {
            stateMachine.stopAnonymous(this);
        }
    }

    @Override
    protected void started() {

    }

    @Override
    protected void stopped() {
        for (String stateName : this.toStop) {
            stateMachine.stopState(stateName);
        }
        for (String stateName : this.toStart) {
            stateMachine.startState(stateName);
        }
    }
}
