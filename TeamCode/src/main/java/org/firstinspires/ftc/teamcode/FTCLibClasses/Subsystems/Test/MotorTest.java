package org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MotorTest extends SubsystemBase {
    MotorEx motor;
    int startPos;

    public  MotorTest(HardwareMap hMap){
        motor = new MotorEx(hMap, "testMotor");
        motor.setRunMode(Motor.RunMode.PositionControl);
        motor.set(1);

        motor.setInverted(false);
        motor.setPositionTolerance(20);
        motor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        reset();
    }

    public void setToValue(int value){
        motor.set(1);
        motor.setTargetPosition(value+startPos);

    }
    public int getValue(){
        return motor.getCurrentPosition()-startPos;
    }
//    public void setPower(double power){
//        motor.set(power);
//    }
    public void stop(){
        motor.set(0);
    }
    public void reset(){
        startPos = motor.getCurrentPosition();
    }
}
