package org.nknsd.teamcode.components.handlers;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNComponent;

public class ServoHandler implements NKNComponent {

    final private String rServoName;
    final private String lServoName;

    public ServoHandler(String rServoName, String lServoName) {
        this.rServoName = rServoName;
        this.lServoName = lServoName;
    }

    public void setRightPos(double pos){
        rServo.setPosition(pos);
    }
    public void setLeftPos(double pos){
        lServo.setPosition(1-pos);
    }

    public void setBothPos(double rPos, double lPos){
        rServo.setPosition(rPos);
        lServo.setPosition(1-lPos);
    }

    Servo rServo;
    Servo lServo;

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        rServo = hardwareMap.servo.get(rServoName);
        lServo = hardwareMap.servo.get(lServoName);
        rServo.setPosition(0.5);
        lServo.setPosition(0.5);
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
        return "";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void doTelemetry(Telemetry telemetry) {

    }
}
