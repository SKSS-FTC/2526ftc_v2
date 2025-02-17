package org.nknsd.teamcode.components.handlers;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNComponent;

public class TheBowlHandler implements NKNComponent {
    CRServo servo;
    private States currentState = States.not_bowlin;

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        servo = hardwareMap.crservo.get("transferShaker");
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
        return "TheBowlHandler";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        telemetry.addData("BowlState", currentState);
    }

    public void setServoPower (States state){
        servo.setPower(state.power);
        currentState = state;
    }

    public double getServoPower() {
        return servo.getPower();
    }

    public States getCurrentState() {
        return currentState;
    }

    public enum States {
        BOWLIN(0.3),
        not_bowlin(0);

        public final double power;

        States(double power) {
            this.power = power;
        }
    }
}
