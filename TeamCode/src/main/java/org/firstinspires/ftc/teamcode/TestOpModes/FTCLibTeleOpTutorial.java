package org.firstinspires.ftc.teamcode.TestOpModes;


import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Intake.ExtendIntake;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Test.MotorCommandTutorial;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test.MotorTutorial;

@Disabled
@TeleOp
public class FTCLibTeleOpTutorial extends OpMode {

    private MotorTutorial motorSubsystem;
    private MotorCommandTutorial motorCommand;
    private GamepadEx gamepadEx1;

    private ExtendIntake extendIntake;
    private SequentialCommandGroup extendAndLower;

    @Override
    public void init() {
//        gamepadEx1 = new GamepadEx(gamepad1);
//        motorSubsystem = new MotorTutorial(hardwareMap);
//        motorCommand = new MotorCommandTutorial(motorSubsystem, gamepadEx1);
//
//        Trigger commandTrigger = new Trigger(() -> {
//            return gamepadEx1.getButton(GamepadKeys.Button.A);
//        });
//        commandTrigger.whenActive(motorCommand);
//
//        IntakeSubsystem intake = new IntakeSubsystem(hardwareMap, AllianceColor.BLUE, telemetry, () -> true, gamepad1);
//
//        extendIntake = new ExtendIntake(intake);
//        lowerIntake = new LowerIntake(intake);
//        extendAndLower = new SequentialCommandGroup(extendIntake, lowerIntake);


    }

    @Override
    public void loop(){
        gamepadEx1.readButtons();


        CommandScheduler.getInstance().run();
    }

}
