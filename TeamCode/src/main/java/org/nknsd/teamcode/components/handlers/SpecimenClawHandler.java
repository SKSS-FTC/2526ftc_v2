package org.nknsd.teamcode.components.handlers;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNComponent;

public class SpecimenClawHandler implements NKNComponent {
    private final String clawName = "specimenClaw";
    private Servo servo;
    private ClawPositions target = ClawPositions.GRIP   ; // damn you karsten, you've introduced a merge conflict !!!!

    public void setClawPosition(ClawPositions clawPositions) {
        servo.setPosition(clawPositions.position);
        target = clawPositions;
    }

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        servo = hardwareMap.servo.get(clawName);
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
        return "SpecimenClawHandler";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        servo.setPosition(target.position);
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        telemetry.addData("Claw Position", target.name());
    }

    public enum ClawPositions {
        GRIP(0),
        RELEASE(0.5);
        
        public final double position;
        ClawPositions(double position) { this.position = position;}
    }
}
