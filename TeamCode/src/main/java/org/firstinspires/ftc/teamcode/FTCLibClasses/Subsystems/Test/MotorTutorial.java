package org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MotorTutorial extends SubsystemBase {
    public DcMotor motor;

    public MotorTutorial(HardwareMap hMap){
        motor = hMap.get(DcMotor.class,"testMotor");
    }

    public void runMotor(){
        motor.setPower(1);
    }

    public void stopMotor(){
        motor.setPower(0);
    }


}
