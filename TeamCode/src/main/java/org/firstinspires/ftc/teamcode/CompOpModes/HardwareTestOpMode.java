package org.firstinspires.ftc.teamcode.CompOpModes;

import com.arcrobotics.ftclib.command.CommandGroupBase;
import com.arcrobotics.ftclib.command.FunctionalCommand;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.AllianceColor;


@TeleOp(name = "Manual Override OpMode", group = "Comp")
public class HardwareTestOpMode extends RobotOpMode {
    @Override
    public void setAllianceColor(){
        allianceColor = AllianceColor.BLUE;
    }
    @Override
    public void createLogic(){
        drivePad.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenActive(robot.extendIntake);
        drivePad.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenActive(robot.retractIntake);


        drivePad.getGamepadButton(GamepadKeys.Button.Y).whileActiveContinuous(
                new FunctionalCommand(
                        ()->{},
                        ()->robot.spinIntakeSubsystem.spinWheelsUp(),
                        (Boolean b) ->{robot.spinIntakeSubsystem.stopIntakeWheels();},
                        ()-> false,
                        robot.spinIntakeSubsystem
                )
        );
        drivePad.getGamepadButton(GamepadKeys.Button.B).whileActiveContinuous(
                new FunctionalCommand(
                        ()->{},
                        ()->robot.spinIntakeSubsystem.spinWheelsDown(),
                        (Boolean b) ->{robot.spinIntakeSubsystem.stopIntakeWheels();},
                        ()-> false,
                        robot.spinIntakeSubsystem
                )
        );

        CommandGroupBase.clearGroupedCommands();
        drivePad.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenActive(robot.moveIntakeUp);
        drivePad.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenActive(robot.moveIntakeDown);


        gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenActive(robot.liftCommand);
        gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenActive(robot.liftDownCommand);

        gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenActive(robot.armDownCommand);
        gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenActive(robot.armHighDunkCommand);

        robot.followerSubsystem.setDefaultCommand(robot.followerTeleOpCommand);
        gamepadEx2.getGamepadButton(GamepadKeys.Button.B).whenActive(robot.extendIntakeToClearPos);
    }

    @Override
    public String getTelemetry(){
        return "==========INTAKE==========\n" +
                "Intake Extend: Driver 1 DPAD LEFT\n" +
                "Intake Retract: Driver 1 DPAD RIGHT\n" +
                "Spin Wheels Up: Driver 1 Button Y\n" +
                "Spin Wheels Down: Driver 1 Button B\n" +
                "Move Intake Up: Driver 1 DPAD UP\n" +
                "Move Intake Down: Diver 1 DPAD DOWN\n" +
                "==========OUTTAKE==========\n" +
                "Move Lift Up: Driver 2 DPAD UP\n" +
                "Move Lift Down: Driver 2 DPAD DOWN\n" +
                "Swing Bucket Up: Driver 2 DPAD LEFT\n" +
                "Swing Bucket Down: Driver 2 DPAD RIGHT\n" +
                "==========DRIVE==========\n" +
                "Drive is left and right joystick driver 1\n" +
                "Reset Heading: Driver 1 Button A";
    }
}
