package org.nknsd.teamcode.states;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.RailHandler;
import org.nknsd.teamcode.components.handlers.statemachine.StateMachine;

public class ArmPosWithin extends StateMachine.State {

    final String[] toStop;
    final String[] toStart;
    final int target;
    final int error;
    final double maxSpeed;

    final RailHandler railHandler;

    boolean goForward;


    //    900 is the end from fully retracted
    public ArmPosWithin(RailHandler railHandler, int target, String[] toStart, String[] toStop, int error, double maxSpeed) {
        this.target = target;
        this.toStop = toStop;
        this.toStart = toStart;
        this.railHandler = railHandler;
        this.error = error;
        this.maxSpeed = maxSpeed;

    }

    private boolean isWithin(int current, int target) {
        int delta = target - current;
        if (goForward && delta <= 0)
            return true;
        else return !goForward && delta >= 0;

    }


    @Override
    protected void run(ElapsedTime runtime, Telemetry telemetry) {
        int rMotorPos = railHandler.getRMotorPos();
        int lMotorPos = railHandler.getLMotorPos();

        double rSpeed = maxSpeed * (goForward ? 1 : -1);


        railHandler.setMotorSpeed(rSpeed);

        if (isWithin(rMotorPos, target) && isWithin(lMotorPos, target)) {
            stateMachine.stopState(name);
        }
    }

    @Override
    protected void started() {
        int rMotorPos = railHandler.getRMotorPos();
        int lMotorPos = railHandler.getLMotorPos();
        goForward = (rMotorPos+lMotorPos < 2* target);
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
