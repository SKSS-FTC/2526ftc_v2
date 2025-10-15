package org.firstinspires.ftc.teamcode.config.subsystem;

import static org.firstinspires.ftc.teamcode.config.RobotConstants.intake_P;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Intake {
    private Telemetry telemetry;
    private ElapsedTime intakeTimer = new ElapsedTime();
    private final DcMotor left,right;
    private final CRServo leftIntake, rightIntake;
    private final ColorSensor C1;
    private final DistanceSensor D1;
    private final TouchSensor T1;
    private boolean busy = false,set = false,intake = false;
    private ElapsedTime transferTimer = new ElapsedTime();
    public enum intakeColour {red,yellowAndRed,yellowAndBlue,yellow,blue,none};
    private intakeColour currentIntakecolour;
    public enum intakeStatus {auto,manual,transfer,transferToReady,intakeToTransfer,ReadyToIntake,free};
    private intakeStatus currentIntakeStatus;



    public  Intake(HardwareMap hardwareMap){
        left = hardwareMap.get(DcMotorEx .class, "leftIntakeMotor");
        right = hardwareMap.get(DcMotorEx .class, "rightIntakeMotor");
        leftIntake = hardwareMap.get(CRServo.class,"leftIntake");
        rightIntake = hardwareMap.get(CRServo.class,"rightIntake");
        C1 = hardwareMap.get(RevColorSensorV3.class,"C1");
        D1 = hardwareMap.get(RevColorSensorV3.class,"C1");
        T1 = hardwareMap.get(TouchSensor.class, "T1");

        right.setDirection(DcMotorSimple.Direction.REVERSE);
        rightIntake.setDirection(CRServo.Direction.REVERSE);

        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }

    public void setIntake(boolean target){intake = target;}



    private void IntakeIn(){
        leftIntake.setPower(1);
        rightIntake.setPower(1);
    }


    private void IntakeStop(){
        leftIntake.setPower(0);
        rightIntake.setPower(0);
    }


    private void IntakeOut(){
        leftIntake.setPower(-1);
        rightIntake.setPower(-1);
    }

    private void intakeTransfer(){
        leftIntake.setPower(-0.1);
        rightIntake.setPower(-0.1);
    }



    private void setIntakePosition(int targetPosition){
        double error = left.getCurrentPosition() - right.getCurrentPosition();
        if (!set){
            busy = true;
            left.setTargetPosition(targetPosition);
            right.setTargetPosition(targetPosition);

            left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            left.setPower(1);
            right.setPower(1);
            set = true;
        }else {
            if (error <10 ){
                error = 0;
            }
            left.setPower(1 - (error * intake_P));
            right.setPower(1 + (error * intake_P));
            if (!left.isBusy()) {
                left.setPower(0);
                right.setPower(0);
                busy = false;
            }
        }
        if(targetPosition == 0 && T1.isPressed()){
            left.setPower(0);
            right.setPower(0);
            left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            busy = false;
        }
    }

    private boolean red(){
        if((double) C1.red() / C1.green() > 1.5) {
            return true;
        } else{
           return false;
        }
    }

    private boolean yellow(){
        if((double) C1.green() /C1.red()<1.7 && (double) C1.green() /C1.red()>1.2) {
            return true;
        } else{
            return false;
        }
    }

    private boolean blue() {
        if((double) C1.blue() / C1.green() >1.8 ){
            return true;
        }else{
            return false;
        }
    }

    public void setIntakeStatus(intakeStatus targetStatus){
        set = false;
        currentIntakeStatus = targetStatus;
        if(targetStatus == intakeStatus.transfer){
            transferTimer.reset();
        }else if (targetStatus == intakeStatus.manual || targetStatus == intakeStatus.auto){
            busy = true;
        }
    }

    public void setIntakeColour(intakeColour targetColour){
        currentIntakecolour = targetColour;
    }


    public void update(){
        double error;
        error = left.getCurrentPosition() - right.getCurrentPosition();
        switch (currentIntakeStatus){
            case auto:
                break;


            case manual:
                if (!intake) {
                    switch (currentIntakecolour) {
                        case red:
                            if (red()&&D1.getDistance(DistanceUnit.MM)<20) {
                                IntakeStop();
                                intake = true;
                            }else if((blue() || yellow()) && D1.getDistance(DistanceUnit.MM) <80 ){
                                IntakeOut();
                                intakeTimer.reset();
                            }else if (intakeTimer.milliseconds()<200){
                                IntakeOut();
                            }else {
                                IntakeIn();
                            }
                            break;

                        case blue:
                            if (blue()&&D1.getDistance(DistanceUnit.MM)<20) {
                                IntakeStop();
                                intake = true;
                            }else if((red()||yellow()) && D1.getDistance(DistanceUnit.MM) <80 ){
                                IntakeOut();
                                intakeTimer.reset();
                            }else if (intakeTimer.milliseconds()<200){
                                IntakeOut();
                            }else {
                                IntakeIn();
                            }
                            break;

                        case yellow:
                            if (yellow() &&D1.getDistance(DistanceUnit.MM)<20) {
                                IntakeStop();
                                intake = true;
                            }else if((red()||blue()) && D1.getDistance(DistanceUnit.MM) <80 ){
                                IntakeOut();
                                intakeTimer.reset();
                            }else if (intakeTimer.milliseconds()<200){
                                IntakeOut();
                            }else {
                                IntakeIn();
                            }
                            break;

                        case yellowAndRed:
                            if ((yellow()||red()) &&D1.getDistance(DistanceUnit.MM)<20) {
                                IntakeStop();
                                intake = true;
                            }else if(blue() && D1.getDistance(DistanceUnit.MM) <80 ){
                                IntakeOut();
                                intakeTimer.reset();
                            }else if (intakeTimer.milliseconds()<200){
                                IntakeOut();
                            }else {
                                IntakeIn();
                            }
                            break;

                        case yellowAndBlue:
                            if ((blue()||yellow()) &&D1.getDistance(DistanceUnit.MM)<13) {
                                IntakeStop();
                                intake = true;
                            }else if(red()&& D1.getDistance(DistanceUnit.MM) <80 ){
                                IntakeOut();
                                intakeTimer.reset();
                            }else if (intakeTimer.milliseconds()<200){
                                IntakeOut();
                            }else {
                                IntakeIn();
                            }
                            break;

                        case none:
                            IntakeStop();
                            break;

                        default:
                            currentIntakecolour = intakeColour.yellow;

                    }
                }
                break;


            case transfer:
                if(transferTimer.milliseconds() < 500){
                    intakeTransfer();
                }else{
                    IntakeStop();
                }
                break;


            case ReadyToIntake:
                setIntakePosition(1000);
                break;


            case intakeToTransfer:
                setIntakePosition(0);
                break;


            case transferToReady:
                setIntakePosition(900);
                break;

            case free:
                break;

            default:
                currentIntakeStatus = intakeStatus.free;
        }
    }


    public boolean isBusy(){return busy;}

    public boolean getIntakeSuccess(){return intake;}
}