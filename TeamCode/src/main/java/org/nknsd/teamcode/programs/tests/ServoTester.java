package org.nknsd.teamcode.programs.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.ServoHandler;
import org.nknsd.teamcode.components.handlers.statemachine.StateMachine;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNProgram;
import org.nknsd.teamcode.states.TimerState;

import java.util.List;

@TeleOp(name = "Servo Tester", group = "Tests")
public class ServoTester extends NKNProgram {

    class Move extends StateMachine.State{

        ServoHandler servoHandler;
        final double rPos;
        final double lPos;

        public Move(ServoHandler servoHandler, double rPos, double lPos){
            this.servoHandler = servoHandler;
            this.rPos = rPos;
            this.lPos = lPos;
        }

        @Override
        protected void run(ElapsedTime runtime, Telemetry telemetry) {
            servoHandler.setLeftPos(lPos);
            servoHandler.setRightPos(rPos);
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
        ServoHandler servoHandler = new ServoHandler("rServo","lServo");
        components.add(servoHandler);
        telemetryEnabled.add(servoHandler);

        StateMachine stateMachine = new StateMachine();
        components.add(stateMachine);
        telemetryEnabled.add(stateMachine);

        stateMachine.addState("uc",new Move(servoHandler,0.65,0.35));
        stateMachine.addState("dc",new Move(servoHandler,0.75,0.45));
        stateMachine.addState("hc", new Move(servoHandler,0.3,0));
        stateMachine.addState("uo",new Move(servoHandler,0.4,0.6));
        stateMachine.addState("do",new Move(servoHandler,0.5,0.7));
        stateMachine.addState("ho", new Move(servoHandler,0,0.2));

        stateMachine.addState("TimerUO",new TimerState(3000,new String[]{"do","TimerDO"}, new String[]{"uo"}));
        stateMachine.addState("TimerDO",new TimerState(3000,new String[]{"dc","TimerDC"}, new String[]{"do"}));
        stateMachine.addState("TimerDC",new TimerState(3000,new String[]{"uc","TimerUC"}, new String[]{"dc"}));
        stateMachine.addState("TimerUC",new TimerState(3000,new String[]{"hc","TimerHC"}, new String[]{"uc"}));
        stateMachine.addState("TimerHC",new TimerState(3000,new String[]{"ho","TimerHO"}, new String[]{"hc"}));
        stateMachine.addState("TimerHO",new TimerState(3000,new String[]{"uo","TimerUO"}, new String[]{"ho"}));

        stateMachine.startState("uo");
        stateMachine.startState("TimerUO");
    }
}
