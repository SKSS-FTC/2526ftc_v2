package org.firstinspires.ftc.teamcode.team.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.drivers.RevServo;
import org.firstinspires.ftc.teamcode.team.states.ITDClawStateMachine;

public class ITDClawSubsystem implements ISubsystem<ITDClawStateMachine, ITDClawStateMachine.State> {
    private static ITDClawStateMachine itdClawStateMachine;
    private RevServo ClawServo;


    public ITDClawSubsystem(RevServo clawServo){
        setClawStateMachine(new ITDClawStateMachine());
        setGripperServo(clawServo);
    }

    @Override
    public ITDClawStateMachine getStateMachine() {
        return itdClawStateMachine;
    }

    @Override
    public ITDClawStateMachine.State getState() {
        return getStateMachine().getState();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public String getName() {
        return "Gripper Subsystem";
    }

    @Override
    public void writeToTelemetry(Telemetry telemetry) {

    }

    @Override
    public void update(double dt) {
        getStateMachine().update(dt);
        getClawServo().setPosition(getState().getPosition());
    }

    public static void setClawStateMachine(ITDClawStateMachine gripperStateMachine) {
        ITDClawSubsystem.itdClawStateMachine = gripperStateMachine;
    }

    public RevServo getClawServo() {
        return ClawServo;
    }


    public void setGripperServo(RevServo clawServo) {
        this.ClawServo = clawServo;
    }
}