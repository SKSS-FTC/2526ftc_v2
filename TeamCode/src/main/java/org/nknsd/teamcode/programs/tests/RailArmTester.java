package org.nknsd.teamcode.programs.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.RailHandler;
import org.nknsd.teamcode.components.handlers.statemachine.StateMachine;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNProgram;
import org.nknsd.teamcode.states.ArmPosWithin;

import java.util.List;

@TeleOp(name = "Rail Arm Tester", group = "Tests")
public class RailArmTester extends NKNProgram {

    class Move extends StateMachine.State {

        double speed;

        RailHandler railHandler;

        public Move(RailHandler railHandler, double speed) {
            this.speed = speed;
            this.railHandler = railHandler;
        }

        @Override
        protected void run(ElapsedTime runtime, Telemetry telemetry) {
            railHandler.setMotorSpeed(speed);
        }

        @Override
        protected void started() {

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

        RailHandler railHandler = new RailHandler();
        components.add(railHandler);
        telemetryEnabled.add(railHandler);

        stateMachine.addState("extend", new ArmPosWithin(railHandler, 450, new String[]{"retract"}, new String[]{}, 3, 0.7));
//        stateMachine.addState("middle", new ArmPosWithin(railHandler,450,new String[]{"extend"}, new String[]{}));
        stateMachine.addState("retract", new ArmPosWithin(railHandler, 0, new String[]{"extend"}, new String[]{}, 3,0.7));
        stateMachine.startState("extend");

//        stateMachine.addState("f", new Move(railHandler,0.7));
//        stateMachine.addState("b", new Move(railHandler, -0.7));
//        stateMachine.addState("Timer1",new TimerState(3000,new String[]{"f","Timer2"}, new String[]{"b"}));
//        stateMachine.addState("Timer2",new TimerState(3000,new String[]{"b","Timer1"}, new String[]{"f"}));
//        stateMachine.startState("f");
//        stateMachine.startState("Timer2");
    }
}
