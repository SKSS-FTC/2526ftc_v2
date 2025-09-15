package org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TestGroupS2 extends SubsystemBase {
    private DcMotor motor2;
    private int startPos;

    public TestGroupS2(HardwareMap hardwareMap) {
        motor2 = hardwareMap.get(DcMotor.class, "TestGroupMotor2");
        startPos = motor2.getCurrentPosition();
    }

    public void runMotor() {
        motor2.setTargetPosition(startPos + 2000);
        motor2.setPower(1);
    }

    public void resetPower() {
        motor2.setPower(0);
    }

    public boolean isFinished() {
        return motor2.isBusy();
    }
}
