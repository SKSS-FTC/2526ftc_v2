package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Movement extends LinearOpMode{
    DcMotor leftFrontDrive;
    DcMotor rightFrontDrive;
    DcMotor rightBackDrive;
    DcMotor leftBackDrive;
    DcMotor linearSlide;
    DcMotor arm;
    Servo claw;
    Servo clawPivot;

    double integralSum = 0;
    double Kp = 0.1;
    double Ki = 0;
    double Kd = 0;
    // double Kp = 0.05;
    // double Ki = 0.0150;
    // double Kd = 0.000001;
    ElapsedTime timer = new ElapsedTime();

    public void initialize(HardwareMap hardwareMap){
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        arm = hardwareMap.get(DcMotor.class, "arm");
        linearSlide = hardwareMap.get(DcMotor.class, "linear_slide");
        claw = hardwareMap.get(Servo.class, "claw");
        clawPivot = hardwareMap.get(Servo.class, "claw_pivot");

        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        linearSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);

        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    private double PIDControl(double reference, double lastError, DcMotor motor) {
        double state = motor.getCurrentPosition();
        double error = reference - state;
        if(error < 100 && error > -100) {
            error = 0;
        }
        integralSum += error * timer.seconds();
        double derivative = (error-lastError) / timer.seconds();

        lastError = error;

        timer.reset();

        double out = (error*Kp) + (derivative * Kd) + (integralSum * Ki);
        return out;
    }

    private double armPIDControl(double reference, double lastError, DcMotor motor) {
        Kp = .3;
        double state = motor.getCurrentPosition();
        double error = reference - state;
        if(error < 100 && error > -100) {
            error = 0;
        }
        integralSum += error * timer.seconds();
        double derivative = (error-lastError) / timer.seconds();

        lastError = error;

        timer.reset();

        double out = (error*Kp) + (derivative * Kd) + (integralSum * Ki);
        return out;
    }
    // tuned power
    public void strafe(int reference, int variance, Telemetry telemetry) {
        while(leftFrontDrive.getCurrentPosition() > reference - variance
                || leftFrontDrive.getCurrentPosition() < reference + variance
        ) {
            double power = PIDControl(reference, reference, leftFrontDrive);
            leftFrontDrive.setPower(power);
            rightFrontDrive.setPower(power);
            rightBackDrive.setPower(-power);
            leftBackDrive.setPower(-power);
            if(leftFrontDrive.getCurrentPosition() >= reference - (variance+1)
                    && leftFrontDrive.getCurrentPosition() <= reference + (variance+1)) {
                leftFrontDrive.setPower(0);
                rightFrontDrive.setPower(0);
                rightBackDrive.setPower(0);
                leftBackDrive.setPower(0);
                break;
            }
            telemetry.addData("pos(strafe)", leftFrontDrive.getCurrentPosition());
            telemetry.update();
        }
    }

    public void moveForward(int reference, int variance, Telemetry telemetry) {
        while(leftFrontDrive.getCurrentPosition() > reference + variance
                || leftFrontDrive.getCurrentPosition() < reference - variance) {
            double power = PIDControl(reference, reference, leftFrontDrive);
            leftFrontDrive.setPower(power * 0.5);
            rightFrontDrive.setPower(-power * 0.5);
            leftBackDrive.setPower(power * 0.5);
            rightBackDrive.setPower(-power * 0.5);
            telemetry.addData("drive", leftFrontDrive.getCurrentPosition());
        }
    }
    public void turn(int reference, int variance) {
        while(leftFrontDrive.getCurrentPosition() < reference - variance
                || leftFrontDrive.getCurrentPosition() > reference + variance) {
            double power = PIDControl(reference, reference, leftFrontDrive);
            leftFrontDrive.setPower(power);
            leftBackDrive.setPower(power);
            rightBackDrive.setPower(power);
            rightFrontDrive.setPower(power);
        }
    }
    // public void claw(boolean expand) {
    //     if(expand){
    //         claw.setPosition(0);
    //     }else {
    //         claw.setPosition(1);
    //     }
    // }
    // public void scoreOnBoard(int reference, int variance, Telemetry telemetry) {

    //     while(linearSlide.getCurrentPosition() < reference - variance
    //             || linearSlide.getCurrentPosition() > reference + variance) {
    //         double power = PIDControl (reference, reference, linearSlide);
    //         linearSlide.setPower(power);
    //         telemetry.addData("position: ", linearSlide.getCurrentPosition());
    //         telemetry.update();
    //     }
    //     telemetry.addData("finished looping", "a");
    //     telemetry.update();
    //     linearSlide.setPower(0);
    //     tray.setPower(-0.5);
    //     sleep(500);
    //     tray.setPower(0);
    // }
    public void armMove(int reference, int variance, Telemetry telemetry) {
        while(arm.getCurrentPosition() < reference - variance || arm.getCurrentPosition() > reference + variance) {
            double power = PIDControl(reference, reference, arm);
            arm.setPower(power);
            telemetry.addData("armPos", arm.getCurrentPosition());
            telemetry.update();
        }
        arm.setPower(0);
    }

    public void linearSlide(int reference, int variance, Telemetry telemetry) {
        int y = linearSlide.getCurrentPosition();
        while(linearSlide.getCurrentPosition() < reference - variance
                || linearSlide.getCurrentPosition() > reference + variance) {
            double power = PIDControl (reference, reference, linearSlide);
            linearSlide.setPower(power);
            telemetry.addData("position: ", linearSlide.getCurrentPosition());
            telemetry.update();
        }
        if(arm.getCurrentPosition() < -1000) {
            linearSlide.setPower(-0.1);
        }else {
            linearSlide.setPower(0);
        }
    }
    public void stopSlide() {
        linearSlide.setPower(0);
    }

    public void claw(boolean close) {
        if(close) {
            claw.setPosition(.15);
        }else{
            claw.setPosition(.5);
        }
    }

    public void pivot(double pos) {
        clawPivot.setPosition(pos);
    }

    // public void retract(int reference, int variance, Telemetry telemetry) {
    //     reference = -reference;
    //     tray.setPower(-0.5);
    //     sleep(500);
    //     tray.setPower(0);
    //     while(linearSlide.getCurrentPosition() < reference - variance
    //             || linearSlide.getCurrentPosition() > reference + variance) {
    //         double power = PIDControl (reference, reference, linearSlide);
    //         linearSlide.setPower(power);
    //         telemetry.addData("position: ", linearSlide.getCurrentPosition());
    //         telemetry.update();
    //     }
    // }
    public void resetPower() {
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
        linearSlide.setPower(0);
    }
    public void runOpMode() {
    }

}