package org.nknsd.teamcode.states;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.RailHandler;
import org.nknsd.teamcode.components.handlers.ServoHandler;
import org.nknsd.teamcode.components.handlers.statemachine.StateMachine;

public class WufRetractState extends StateMachine.State {

    static public String STATE_NAME = "WUF_RETRACT_STATE";

    final double maxSpeed;
    final RailHandler railHandler;
    private final ServoHandler servoHandler;


    public WufRetractState(RailHandler railHandler, ServoHandler servoHandler, double maxSpeed) {
        this.railHandler = railHandler;
        this.maxSpeed = maxSpeed;
        this.servoHandler = servoHandler;
    }


    @Override
    protected void run(ElapsedTime runtime, Telemetry telemetry) {
        int rMotorPos = railHandler.getRMotorPos();
        int lMotorPos = railHandler.getLMotorPos();

        railHandler.setMotorSpeed(-maxSpeed);
        servoHandler.setBothPos(0.45, 0.15);

        if (rMotorPos <5 && lMotorPos <5 ) {
            railHandler.setMotorSpeed(0);
            servoHandler.setBothPos(.45,0.65);
            stateMachine.stopState(STATE_NAME);
            stateMachine.startState(WufHunter.STATE_NAME);
        }


    }

    @Override
    protected void started() {
    }

    @Override
    protected void stopped() {
    }
}
