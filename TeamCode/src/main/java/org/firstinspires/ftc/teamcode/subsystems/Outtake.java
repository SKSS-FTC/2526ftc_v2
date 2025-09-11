package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Outtake {
    private final MotorEx motor;

    public Outtake(HardwareMap hardwareMap)
    {
        motor = new MotorEx(hardwareMap, "outtake");
    }

    public void stop()
    {
        motor.stopMotor();
    }

    public void setPower(double power)
    {
        motor.set(power);
    }

    //TODO: Make algorithm to determine power needed based on distance
}
