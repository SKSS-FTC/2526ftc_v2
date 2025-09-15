package org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Hang;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import static org.firstinspires.ftc.teamcode.RobotConfig.HangConstants.*;
public class HangSubsystem extends SubsystemBase {

    int StartPos;
    Telemetry telemetry;
    MotorEx hangLift;
    HardwareMap hMap;
    GamepadEx gamepadEx;

    public HangSubsystem(HardwareMap hMap, Telemetry telemetry, GamepadEx gamepadEx){
        hangLift = new MotorEx(hMap, hangLiftName);
        hangLift.setRunMode(Motor.RunMode.PositionControl);


        hangLift.setInverted(false);
        hangLift.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        hangLift.setPositionTolerance(20);
        hangLift.setPositionCoefficient(1000000000);
        this.hMap = hMap;
        this.telemetry = telemetry;
        this.gamepadEx = gamepadEx;
    }

    public int getValue(){
       return hangLift.getCurrentPosition()-StartPos;
    }

    public void stop(){
        hangLift.set(0);
    }
    public void reset(){
        StartPos = hangLift.getCurrentPosition();
    }
    @Override
    public void periodic(){
        telemetry.addData("Hang Position",hangLift.getCurrentPosition());

    }

    public boolean isThere(){
        return Math.abs(hangLift.getCurrentPosition()- HangMaxPos)<150;
    }
    public boolean isThereDown(){   
        return Math.abs(hangLift.getCurrentPosition()-HangMinPos)<150;
    }
    public void goToMaxHeight(){
        hangLift.setTargetPosition(HangMaxPos);

        hangLift.set(1);
        hangLift.setRunMode(Motor.RunMode.PositionControl);
    }
    public void goDown(){
        hangLift.setTargetPosition(-1*HangMaxPos);
        hangLift.set(1);
        hangLift.setRunMode(Motor.RunMode.PositionControl);
    }
    public boolean isDown(){
        return Math.abs(hangLift.getCurrentPosition()- HangMinPos)<150;
    }










}