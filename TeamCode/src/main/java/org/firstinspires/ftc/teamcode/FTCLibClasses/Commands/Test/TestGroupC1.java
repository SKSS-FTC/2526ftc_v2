package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Test;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test.TestGroupS1;

public class TestGroupC1 extends CommandBase {
    private TestGroupS1 testGroupS1;

    public TestGroupC1(TestGroupS1 testGroupS1) {
        this.testGroupS1 = testGroupS1;
    }

    @Override
    public void initialize() {
        testGroupS1.runMotor();
    }

    @Override
    public void end(boolean bool) {
        testGroupS1.resetPower();
    }

    @Override
    public boolean isFinished() {
        return testGroupS1.isFinished();
    }
}
