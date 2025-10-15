package org.firstinspires.ftc.teamcode.config.subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Slider {
    private Telemetry telemetry;
    private ElapsedTime intakeTimer = new ElapsedTime();
    private  DcMotor slider;
    private Servo leftBasket,rightBasket,specimen;

    private boolean busy = false,set = false,intake = false;
    private ElapsedTime specimenTimer = new ElapsedTime();
    private ElapsedTime basketTimer = new ElapsedTime();
    public enum sliderStatus {transfer,transferToHigh,transferToLow,basket,
        specimenIntake,specimenToHigh,specimenToLow,specimen};
    private sliderStatus currentIntakeStatus;



    public  Slider(HardwareMap hardwareMap){
        leftBasket = hardwareMap.get(Servo .class, "rightBasket");
        rightBasket = hardwareMap.get(Servo .class, "leftBasket");
        specimen = hardwareMap.get(Servo.class,"specimen");
        slider = hardwareMap.get(DcMotor.class,"specimen");

        leftBasket.setDirection(Servo.Direction.REVERSE);

        slider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }

    public void setIntake(boolean target){intake = target;}



    public void setSliderPosition(int targetPosition){
        if (slider.getMode()!= DcMotor.RunMode.RUN_TO_POSITION){
            busy = true;
            slider.setTargetPosition(targetPosition);

            slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            
            slider.setPower(1);

            set = true;
        }else {
            if (!slider.isBusy()) {
                slider.setPower(0);
                busy = false;
                slider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
        }
    }

    public boolean isBusy(){return busy;}
}
