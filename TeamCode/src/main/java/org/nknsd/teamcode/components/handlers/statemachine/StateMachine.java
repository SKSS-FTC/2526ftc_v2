package org.nknsd.teamcode.components.handlers.statemachine;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNComponent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class StateMachine implements NKNComponent {


    static public abstract class State {
        protected double startTime = -1;
        protected StateMachine stateMachine;
        protected String name;

        protected abstract void run(ElapsedTime runtime, Telemetry telemetry);

        protected abstract void started();

        protected abstract void stopped();
    }

    final private HashMap<String, State> stateMap = new HashMap<>();
    final private List<State> runList = new LinkedList<>();

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "stateMachine";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        List<State> copyList = new LinkedList<>(runList);
        for (State state : copyList) {
            if (state.startTime == -1) {
                state.startTime = runtime.milliseconds();
                state.started();
            }
            state.run(runtime, telemetry);
        }
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        String nameString = "";
        for (State state : runList) {
            nameString = nameString + "|" + state.name;
        }
        telemetry.addData("States",nameString);
    }

    public void addState(String name, State state) {
        stateMap.put(name, state);
        state.stateMachine = this;
        state.name = name;
    }

    public void startState(String name) {
        State state = stateMap.get(name);
        if (state == null) {
            throw new NullPointerException("State: " + name + " not found!");
        }
        runList.add(state);
    }

    public void stopState(String name) {
        State state = stateMap.get(name);
        if (state == null) {
            throw new NullPointerException("State: " + name + " not found!");
        }
        state.stopped();
        state.startTime = -1;
        runList.remove(state);
    }

    public void startAnonymous(State state){
        state.stateMachine = this;
        state.name="anon_"+state.getClass().getSimpleName();
        runList.add(state);
    }
    public void stopAnonymous(State state){
        state.stopped();
        state.startTime = -1;
        runList.remove(state);
    }
}
