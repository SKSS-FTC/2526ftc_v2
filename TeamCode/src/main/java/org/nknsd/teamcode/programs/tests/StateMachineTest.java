package org.nknsd.teamcode.programs.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.statemachine.StateMachine;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNProgram;
import org.nknsd.teamcode.states.TimerState;

import java.util.List;

@TeleOp(name = "StateMachine Test", group = "Tests")
public class StateMachineTest extends NKNProgram {

    private int myCounter = 0;

    class State1 extends StateMachine.State {


        @Override
        protected void run(ElapsedTime runtime, Telemetry telemetry) {
            telemetry.addLine("1b");
        }

        @Override
        protected void started() {
            telemetry.addLine("making timer");
            TimerState timer = new TimerState(6000, new String[]{"2b"}, new String[]{"1b"});
            telemetry.addLine("starting timer");
            stateMachine.startAnonymous(timer);
        }

        @Override
        protected void stopped() {

        }
    }

    static class State2 extends StateMachine.State {

        @Override
        protected void run(ElapsedTime runtime, Telemetry telemetry) {
            telemetry.addLine("2b");
        }

        @Override
        protected void started() {
            TimerState timer = new TimerState(6000, new String[]{"1b"}, new String[]{"2b"});
            stateMachine.startAnonymous(timer);
        }

        @Override
        protected void stopped() {

        }
    }

    static class State1ExtTimer extends StateMachine.State {


        @Override
        protected void run(ElapsedTime runtime, Telemetry telemetry) {
            telemetry.addLine("1x");
        }

        @Override
        protected void started() {
            stateMachine.startState("1Timer");
        }

        @Override
        protected void stopped() {

        }
    }

    static class State2ExtTimer extends StateMachine.State {

        @Override
        protected void run(ElapsedTime runtime, Telemetry telemetry) {
            telemetry.addLine("2x");
        }

        @Override
        protected void started() {
            stateMachine.startState("2Timer");
        }

        @Override
        protected void stopped() {

        }
    }


    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        StateMachine stateMachine = new StateMachine();
        components.add(stateMachine);
        telemetryEnabled.add(stateMachine);

        stateMachine.addState("1", new State1ExtTimer());
        stateMachine.addState("2", new State2ExtTimer());
        stateMachine.addState("1Timer",new TimerState(4000,new String[]{"2"}, new String[]{"1"}));
        stateMachine.addState("2Timer",new TimerState(4000,new String[]{"1"}, new String[]{"2"}));
        stateMachine.startState("1");

        stateMachine.addState("1b", new State1());
        stateMachine.addState("2b", new State2());
        stateMachine.startState("1b");
    }
}
