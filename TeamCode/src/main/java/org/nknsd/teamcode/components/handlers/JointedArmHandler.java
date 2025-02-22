package org.nknsd.teamcode.components.handlers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.sensors.TouchSens;
import org.nknsd.teamcode.components.utility.GamePadHandler;
import org.nknsd.teamcode.frameworks.NKNComponent;

public class JointedArmHandler implements NKNComponent {
    public static final int MAX_INDEX_OF_ROTATION_POSITIONS = 5;
    final double motorThreshold = 4, servoThreshold = 0;
    public Positions targetPosition = Positions.REST;
    private DcMotor motor; private Servo joint1, joint2, grip;
    private TouchSens touchSens;

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
        motor.setPower(0.5);

        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        setTargetPosition(Positions.REST);
    }

    @Override
    public String getName() {
        return "JointedArmRotator :D";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        if (touchSens != null) {
            if (touchSens.isTouching()) {
                motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motor.setTargetPosition(targetPosition.motorVal);
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
        }
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        telemetry.addData("JointedArm: Motor Val", motor.getCurrentPosition());
        telemetry.addData("JointedArm: Motor Target", motor.getTargetPosition());
        telemetry.addData("JointedArm: Joint1", joint1.getPosition());
        telemetry.addData("JointedArm: Joint2", joint2.getPosition());
        telemetry.addData("JointedArm: Grip", grip.getPosition());
    }

    public void setTargetPosition(Positions position) {
        if (!isAtTargetPosition()) {
            return;
        }

        if (position == Positions.DEPOSIT && targetPosition == Positions.COLLECTION) {
            setTargetPosition(Positions.REST);
            return;
        }

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
        return(Math.abs(targetPosition.motorVal - motor.getCurrentPosition()) <= motorThreshold * 2);

    }

    public enum Positions {
        REST(0, 0.48, 1, 0.71),
        COLLECTION(0, 0.029, 0.78, 0.63),
        DEPOSIT(2620, 0.35, 0.139, 0.71),
        EARLY_BIRD(0, .289, 0, .63), // Goes to a position which allows us to rotate to worm search
        WORM_SEARCH(0, .57, .02, .369), // Prep to peck a specimen off the pen
        NEST(860, .33, .769, 1), // Prepares to deposit a specimen on the wall
        FEED(1630, .33, .769, 1), // Raises the arm to deposit the specimen
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
        boolean y = GamePadHandler.GamepadButtons.Y.detect(gamePadHandler.getGamePad2());
        boolean x = GamePadHandler.GamepadButtons.X.detect(gamePadHandler.getGamePad2());

        if (delayButtons) {
            delayButtons = (a || b || right || left || up || down || y || x);
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

        if (y) {
            motor.setTargetPosition(motor.getTargetPosition() + 10);
        }

        if (x) {
            motor.setTargetPosition(motor.getTargetPosition() - 10);
        }
    }

    public void link(TouchSens touchSens) {
        this.touchSens = touchSens;
    }
}
