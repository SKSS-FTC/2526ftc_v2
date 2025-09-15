package org.firstinspires.ftc.teamcode.CompOpModes.CompTeleOps;

import static java.lang.Math.PI;

import com.arcrobotics.ftclib.command.CommandGroupBase;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.FunctionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.ScheduleCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.CompOpModes.RobotOpMode;
import org.firstinspires.ftc.teamcode.FTCLibClasses.BranchCommand;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Drive.CurrentPoseStartPathCommand;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Drive.PedroPathAutoCommand;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;


public abstract class CompTeleOpTemplate extends RobotOpMode {

    @Override
    public final void createLogic(){
        setAllianceColor();
        robot.followerSubsystem.getFollower().setPose(new Pose());
        //robot.followerSubsystem.getFollower().setPose(new Pose(0,0,3*PI/2));
        SequentialCommandGroup groupRetractIntake = new SequentialCommandGroup(robot.retractIntake.copy(),robot.passIntoBucket.copy());
        CommandGroupBase.clearGroupedCommands();


        ParallelCommandGroup verticalAndSpin = new ParallelCommandGroup(
                new SequentialCommandGroup(
                        robot.moveIntakeDownSubmersible.copy(),
                        new FunctionalCommand(
                                () ->{},
                                () ->{},
                                (Boolean b) ->{},
                                () -> drivePad.getButton(GamepadKeys.Button.RIGHT_BUMPER)|| drivePad.getButton(GamepadKeys.Button.Y)
                        ),
                        robot.moveIntakeUp.copy()
                ),

                new FunctionalCommand(
                        robot.spinIntakeSubsystem::spinWheelsUp,
                        () ->{},
                        (Boolean b) ->{
//                                    robot.spinIntakeSubsystem.stopIntakeWheels();

                        },
                        ()-> true,
                        robot.spinIntakeSubsystem
                )
        );


        CommandGroupBase.clearGroupedCommands();

        CurrentPoseStartPathCommand fromSubmersibleToBasket = new CurrentPoseStartPathCommand(
                robot.followerSubsystem,
                5* PI/4,
                RobotConfig.AutoPoseStorage.blueGoal
        );
        CurrentPoseStartPathCommand fromSubmersibleToLowBasket = new CurrentPoseStartPathCommand(
                robot.followerSubsystem,
                3*PI/4,
                new Point(new Pose(-42,29.4))

        );

        //====================Intake Routine=============================
        //extends, intakes, and passes it through if the driver wants to
        BranchCommand intakeBranchCommand = new BranchCommand(
                ()-> drivePad.getButton(GamepadKeys.Button.RIGHT_BUMPER),
                () -> drivePad.getButton(GamepadKeys.Button.Y)
        );
        SequentialCommandGroup intakeRoutine = new SequentialCommandGroup(
                robot.extendIntake.copy(),
                new ParallelCommandGroup(
                        verticalAndSpin,
                        intakeBranchCommand
                ),
                new ConditionalCommand(
                        new SequentialCommandGroup(
                                groupRetractIntake,
                                robot.extendIntake.copy()
                        ),
                        new InstantCommand(()->{}),
                        () -> intakeBranchCommand.getNextBranch() == BranchCommand.BranchCondition.FIRST_BRANCH
                ),
                new InstantCommand(robot.spinIntakeSubsystem::stopIntakeWheels)

        );


//        SequentialCommandGroup intakeRoutine = new SequentialCommandGroup(
//                robot.extendIntake,
//                robot.verticalAndSpin,
//                new SelectCommand(
//                        new HashMap<Object, Command>() {{
//                            put(SpinIntakeSubsystem.SampleState.CORRESPONDING_SAMPLE,finishRoutine);
//                            put(SpinIntakeSubsystem.SampleState.YELLOW_SAMPLE,finishRoutine);
//                            put(SpinIntakeSubsystem.SampleState.WRONG_SAMPLE,
//                                    new SequentialCommandGroup(
//                                            new RelativePathCommand(
//                                                    robot.followerSubsystem,
//                                                    new Point(10,0,1),
//                                                    0
//                                                    ),
//                                            robot.passIntoBucket,
//                                            new RelativePathCommand(
//                                                    robot.followerSubsystem,
//                                                    new Point(-10,0,1),
//                                                    0
//                                            )
//
//                                    )
//                            );
//                        }},
//                        robot.spinIntakeSubsystem::hasCorrectSample
//                )
//        );


//        intakeRoutine.interruptOn(() -> drivePad.getButton(GamepadKeys.Button.LEFT_BUMPER));

        CommandGroupBase.clearGroupedCommands();


        CommandGroupBase.clearGroupedCommands();


        SequentialCommandGroup hangGroup = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        robot.extendIntake.copy(),
                        new WaitCommand(500),
                        robot.hangHighRungCommand.copy()
                ),
                new WaitCommand(2000),
                robot.hangLowRungCommand.copy()

        );


        SequentialCommandGroup dunk = new SequentialCommandGroup(
                robot.liftCommand.copy(),

                robot.armHighDunkCommand.copy(),
                new InstantCommand(()->{robot.armSubsystem.setPower(.2);}),
                new WaitCommand(RobotConfig.OuttakeConstants.highDunkLingerTimeMs),


                new ParallelCommandGroup(
                        robot.liftDownCommand.copy(),
                        robot.armDownCommand.copy()
                )
//                new WaitCommand(RobotConfig.OuttakeConstants.highDunkLingerTimeMs),
//                new InstantCommand(robot.armSubsystem::setFloat)
        );
        SequentialCommandGroup autoLowDunk = new SequentialCommandGroup(
                fromSubmersibleToLowBasket,
                robot.armLowDunkCommand.copy(),
                new WaitCommand(RobotConfig.OuttakeConstants.highDunkLingerTimeMs),
                robot.armDownCommand

        );

        SequentialCommandGroup autoHighDunk = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        fromSubmersibleToBasket,
                        new SequentialCommandGroup(
                                robot.retractIntake.copy(),
                                robot.passIntoBucket.copy(500),
                                robot.extendIntake.copy(),
                                robot.liftCommand.copy()
                        )
                ),
                new ScheduleCommand(
                        new SequentialCommandGroup(
                                robot.armHighDunkCommand.copy(),
                                new InstantCommand(()->{robot.armSubsystem.setPower(.2);}),
                                new WaitCommand(RobotConfig.OuttakeConstants.highDunkLingerTimeMs),


                                    new SequentialCommandGroup(

                                            robot.armDownCommand.copy(),
                                            robot.liftDownCommand.copy()
                                    )
                                )
                )

        );
        //====================Button Keybinds====================

        drivePad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
                new SequentialCommandGroup(
                        robot.extendIntake.copy(),
                        robot.passIntoBucket.copy()
                )
        );
        drivePad.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(
                ()->robot.followerSubsystem.getFollower().setPose(new Pose(RobotConfig.AutoPoseStorage.blueGoal.getX(),RobotConfig.AutoPoseStorage.blueGoal.getY(), robot.followerSubsystem.getFollower().getPose().getHeading()))
        );
        gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenActive(
                new ParallelCommandGroup(
                        robot.armDownCommand.copy(),
                        robot.liftDownCommand.copy()

                )
        );

        Trigger takeOutOfBucket = new Trigger(() -> drivePad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)>.7);
        takeOutOfBucket.whenActive(
                new SequentialCommandGroup(
                        robot.retractIntake.copy(),
                        robot.moveIntakeDown.copy(),
                        new FunctionalCommand(
                                robot.spinIntakeSubsystem::spinWheelsUp,
                                () ->{},
                                (Boolean b) ->{
                                },
                                ()-> true,
                                robot.spinIntakeSubsystem
                        ),
                        new WaitCommand(RobotConfig.IntakeConstants.takeOutOfBucketTimeMs),
                        robot.moveIntakeUp.copy(),
                        robot.extendIntake.copy(),
                        robot.passIntoBucket.copy()
                )
        );


        new Trigger(()-> drivePad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)>.7).whenActive(intakeRoutine);

        drivePad.getGamepadButton(GamepadKeys.Button.X).whenActive(autoHighDunk);
        drivePad.getGamepadButton(GamepadKeys.Button.B).whenActive(autoLowDunk);
        new Trigger(()-> Math.abs(drivePad.getLeftY())>0.1
                ||Math.abs(drivePad.getRightY())>.1
                ||Math.abs(drivePad.getLeftX())>.1).whenActive(new InstantCommand(()->{autoHighDunk.cancel();autoLowDunk.cancel();}));
        drivePad.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenActive(dunk);



        CommandGroupBase.clearGroupedCommands();
        gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenActive(dunk);
        gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenActive(
                new SequentialCommandGroup(
                        robot.armLowDunkCommand.copy(),
                        new WaitCommand(500),
                        robot.armDownCommand.copy()
                )
        );
        CommandGroupBase.clearGroupedCommands();
        //gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenHeld(hangGroup);
        CommandGroupBase.clearGroupedCommands();

        gamepadEx2.getGamepadButton(GamepadKeys.Button.Y).whenActive(robot.liftCommand.copy());
        gamepadEx2.getGamepadButton(GamepadKeys.Button.X).whenActive(
                new SequentialCommandGroup(
                        robot.armHighDunkCommand.copy()
                )
        );
        gamepadEx2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenActive(robot.pullHangDownCommand.copy());



        robot.followerSubsystem.setDefaultCommand(robot.followerTeleOpCommand);
    }

//    @Override
//    public String getTelemetry(){
//        return "Last Pose\n"+RobotConfig.GlobalConstants.lastPose;
//    }

    public abstract void setAllianceColor();
}
