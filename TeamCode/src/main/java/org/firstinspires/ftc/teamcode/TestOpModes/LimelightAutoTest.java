package org.firstinspires.ftc.teamcode.TestOpModes;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.PerpetualCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.AllianceColor;
import org.firstinspires.ftc.teamcode.CompOpModes.RobotOpMode;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Drive.PedroPathAutoCommand;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;


@Autonomous
public class LimelightAutoTest extends RobotOpMode {

    @Override
    public void createLogic(){




        SequentialCommandGroup grabSample = new SequentialCommandGroup(
                robot.findSampleAndDrive.copy(),

                robot.extendIntake.copy(),
                robot.getVerticalAndSpin(750),
                robot.retractIntake.copy(),
                new InstantCommand(robot.spinIntakeSubsystem::stopIntakeWheels),
                robot.passIntoBucket.copy(2000)
        );

        GamepadEx gamepadEx = new GamepadEx(gamepad1);
        gamepadEx.getGamepadButton(GamepadKeys.Button.X).whenPressed(grabSample);
        robot.followerSubsystem.setDefaultCommand(robot.followerTeleOpCommand);
    }

    @Override
    public void setAllianceColor(){
        allianceColor = AllianceColor.RED;
    }
}
