package org.nknsd.teamcode.components.handlers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.sensors.PotentiometerSensor;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.helperClasses.PIDModel;

import java.util.concurrent.TimeUnit;

public class JointedArmHandler implements NKNComponent {
    public static final int MAX_INDEX_OF_ROTATION_POSITIONS = 5;
    final double motorThreshold = 0, servoThreshold = 0;
    public Positions targetPosition = Positions.A;
    private DcMotor motor; private Servo joint1, joint2, grip;

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        motor = hardwareMap.dcMotor.get("");
        motor.setDirection(DcMotorSimple.Direction.REVERSE);

        joint1 = hardwareMap.servo.get("");
        joint2 = hardwareMap.servo.get("");
        grip = hardwareMap.servo.get("");

        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "JointedArmRotator :D";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void doTelemetry(Telemetry telemetry) {

    }

    public void setTargetPosition(Positions position) {
        motor.setTargetPosition(position.motorVal);
        joint1.setPosition(position.joint1Val);
        joint2.setPosition(position.joint2Val);
        grip.setPosition(position.gripVal);
    }

    public boolean isAtTargetPosition() {
        // Checks each of the four different vals, comparing it to the positions of the components and their respective thresholds
        if (Math.abs(targetPosition.motorVal - motor.getCurrentPosition()) <= motorThreshold * 2) return false;
        if (Math.abs(targetPosition.joint1Val - joint1.getPosition()) <= servoThreshold * 2) return false;
        if (Math.abs(targetPosition.joint2Val - joint2.getPosition()) <= servoThreshold * 2) return false;
        return (Math.abs(targetPosition.gripVal - grip.getPosition()) <= servoThreshold * 2);

    }

    public enum Positions {
        A(0, 0, 0, 0);

        public final int motorVal;
        public final double joint1Val, joint2Val, gripVal;

        Positions(int motorVal, double joint1Val, double joint2Val, double gripVal) {
            this.motorVal = motorVal;
            this.joint1Val = joint1Val;
            this.joint2Val = joint2Val;
            this.gripVal = gripVal;
        }
    }
}
