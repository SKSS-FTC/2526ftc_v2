package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Test;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test.TestGroupS1;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test.TestGroupS2;

public class CommandsGroup extends ParallelCommandGroup {

    public CommandsGroup(TestGroupS1 testGroupS1, TestGroupS2 testGroupS2) {
        addCommands(
            new TestGroupC1(testGroupS1),
            new TestGroupC2(testGroupS2)
        );
        addRequirements(testGroupS1, testGroupS2);
    }
}
