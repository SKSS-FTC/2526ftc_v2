package org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TestGroupS1 extends SubsystemBase {
    private DcMotor motor1;
    private int startPos;

    public TestGroupS1(HardwareMap hardwareMap) {
        motor1 = hardwareMap.get(DcMotor.class, "TestGroupMotor1");
        startPos = motor1.getCurrentPosition();
    }

    public void runMotor() {
        motor1.setTargetPosition(startPos + 2000);
        motor1.setPower(1);
    }

    public void resetPower() {
        motor1.setPower(0);
    }

    public boolean isFinished() {
        return motor1.isBusy();
    }
}
