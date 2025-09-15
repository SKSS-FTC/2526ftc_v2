package org.firstinspires.ftc.teamcode.TestOpModes;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Test.TestGroupC1;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Test.TestGroupC2;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Test.CommandsGroup;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test.TestGroupS1;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test.TestGroupS2;

@Disabled
@TeleOp
public class CommandTestMain extends OpMode {
    // Subsystems
    private TestGroupS1 testGroupS1;
    private TestGroupS2 testGroupS2;

    // Commands
    private TestGroupC1 testGroupC1;
    private TestGroupC2 testGroupC2;

    // Commands group
    private CommandsGroup commandsTest;

    // Gamepad
    private GamepadEx gamepadEx1;

    // Make my commands group
    private CommandsGroup commandsGroup;

    @Override
    public void init() {
        // Setting up Gamepad
        gamepadEx1 = new GamepadEx(gamepad1);

        // Setting up my subsystems
        testGroupS1 = new TestGroupS1(hardwareMap);
        testGroupS2 = new TestGroupS2(hardwareMap);

        // Setting up command groups
        testGroupC1 = new TestGroupC1(testGroupS1);
        testGroupC2 = new TestGroupC2(testGroupS2);

        // Setting up my commands group
        commandsGroup = new CommandsGroup(testGroupS1, testGroupS2);

        Trigger trigger = new Trigger(() -> {return gamepadEx1.getButton(GamepadKeys.Button.A);});
        trigger.whenActive(commandsGroup);
    }

    @Override
    public void loop() {
        commandsGroup.schedule();
        CommandScheduler.getInstance().run();

    }

}
