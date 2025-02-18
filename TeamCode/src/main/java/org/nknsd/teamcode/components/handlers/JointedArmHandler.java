package org.nknsd.teamcode.components.handlers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.sensors.PotentiometerSensor;
import org.nknsd.teamcode.components.utility.GamePadHandler;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.helperClasses.PIDModel;

import java.util.concurrent.TimeUnit;

public class JointedArmHandler implements NKNComponent {
    public static final int MAX_INDEX_OF_ROTATION_POSITIONS = 5;
    final double motorThreshold = 0, servoThreshold = 0;
    public Positions targetPosition = Positions.REST;
    private DcMotor motor; private Servo joint1, joint2, grip;

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        motor = hardwareMap.dcMotor.get("clawArm");
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        joint1 = hardwareMap.servo.get("clawElbow");
        joint2 = hardwareMap.servo.get("clawWrist");
        grip = hardwareMap.servo.get("clawFinger");

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

    private boolean oneTimeThing = false;
    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        if (runtime.now(TimeUnit.MILLISECONDS) > 500 && !oneTimeThing) {
            motor.setPower(0.5);

            motor.setTargetPosition(0);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            setTargetPosition(Positions.REST);

            oneTimeThing = true;
        }
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        telemetry.addData("JointedArm: Motor Val", motor.getCurrentPosition());
        telemetry.addData("JointedArm: Motor Target", targetPosition.motorVal);
        telemetry.addData("JointedArm: Joint1", joint1.getPosition());
        telemetry.addData("JointedArm: Joint2", joint2.getPosition());
        telemetry.addData("JointedArm: Grip", grip.getPosition());
    }

    public void setTargetPosition(Positions position) {
        motor.setTargetPosition(position.motorVal);
        grip.setPosition(position.gripVal); // same as below but just for better operation
        joint2.setPosition(position.joint2Val); // joint 2 is before 1 because the robot hits itself if joint 1 moves first (or i'm just insane but don't change it because it works)
        joint1.setPosition(position.joint1Val);
        targetPosition = position;
    }
    public void setClawPosition(Positions position){
        grip.setPosition(position.gripVal);
    }

    public boolean isAtTargetPosition() {
        // Checks each of the four different vals, comparing it to the positions of the components and their respective thresholds
        return(Math.abs(targetPosition.motorVal - motor.getCurrentPosition()) <= motorThreshold * 2);
//        if (Math.abs(targetPosition.joint1Val - joint1.getPosition()) <= servoThreshold * 2) return false;
//        if (Math.abs(targetPosition.joint2Val - joint2.getPosition()) <= servoThreshold * 2) return false;
//        return (Math.abs(targetPosition.gripVal - grip.getPosition()) <= servoThreshold * 2);

    }

    public enum Positions {
        REST(0, 0.6, 1, 0.71),
        COLLECTION(0, 0.375, 0.789, 0.63),
        COLLECTING(0,0.375,0.789,0.71), //Someone else see if there is a better way to do this because I am not competent enough to know
        SPECIMEN_COLLECTION(0,0.6,1,0.71), // same as REST, please put correct values in before usage
        SPECIMEN_DEPOSIT(0,0.6,1,0.71), // same as REST, please put correct values in before usage
        DEPOSIT(2267, 0.8194, 0.6994, 0.71),

        GRAB_OPEN(0,0,0,0.63),
        GRAB_CLOSE(0,0,0,0.71);

        public final int motorVal;
        public final double joint1Val, joint2Val, gripVal;

        Positions(int motorVal, double joint1Val, double joint2Val, double gripVal) {
            this.motorVal = motorVal;
            this.joint1Val = joint1Val;
            this.joint2Val = joint2Val;
            this.gripVal = gripVal;
        }
    }

    private boolean delayButtons = false;
    public void runStuff(GamePadHandler gamePadHandler) {
        boolean a = GamePadHandler.GamepadButtons.A.detect(gamePadHandler.getGamePad2());
        boolean b = GamePadHandler.GamepadButtons.B.detect(gamePadHandler.getGamePad2());
        boolean right = GamePadHandler.GamepadButtons.DPAD_RIGHT.detect(gamePadHandler.getGamePad2());
        boolean left = GamePadHandler.GamepadButtons.DPAD_LEFT.detect(gamePadHandler.getGamePad2());
        boolean up = GamePadHandler.GamepadButtons.DPAD_UP.detect(gamePadHandler.getGamePad2());
        boolean down = GamePadHandler.GamepadButtons.DPAD_DOWN.detect(gamePadHandler.getGamePad2());

        if (delayButtons) {
            delayButtons = (a || b || right || left || up || down);
            return;
        }

        if (a) {
            joint1.setPosition(joint1.getPosition() + 0.01);
            delayButtons = true;
        }
        if (b) {
            joint1.setPosition(joint1.getPosition() - 0.01);
            delayButtons = true;
        }

        if (right) {
            joint2.setPosition(joint2.getPosition() + 0.01);
            delayButtons = true;
        }
        if (left) {
            joint2.setPosition(joint2.getPosition() - 0.01);
            delayButtons = true;
        }

        if (up) {
            grip.setPosition(grip.getPosition() + 0.005);
            delayButtons = true;
        }
        if (down) {
            grip.setPosition(grip.getPosition() - 0.005);
            delayButtons = true;
        }
    }
}
