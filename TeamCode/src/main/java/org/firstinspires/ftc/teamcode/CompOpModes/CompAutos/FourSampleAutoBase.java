package org.firstinspires.ftc.teamcode.CompOpModes.CompAutos;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandGroupBase;
import com.arcrobotics.ftclib.command.FunctionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.ScheduleCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.CompOpModes.RobotOpMode;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Drive.PedroPathAutoCommand;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;

import static org.firstinspires.ftc.teamcode.RobotConfig.AutoPoseStorage.*;

import java.util.concurrent.TimeUnit;


abstract public class FourSampleAutoBase extends RobotOpMode {

    public double time=0;
    public ElapsedTime timer;
    @Override
    public void createLogic(){
        RobotConfig.GlobalConstants.startTime=System.currentTimeMillis();
        robot.followerSubsystem.getFollower().setPose(startPose);
        robot.armSubsystem.setBrake();
        //robot.followerSubsystem.getFollower().setMaxPower(.75);

        Path startToGoal = new Path.PathBuilder(
                new BezierLine(
                        new Point(robot.followerSubsystem.getFollower().getPose()),
                        path1StartGoal
                )
        )
                .setConstantHeadingInterpolation(blueGoalHeading)
                .setPathEndTranslationalConstraint(15)
                .setPathEndVelocityConstraint(10)
                .build();
        Path fromGoalToRightSpike = new Path.PathBuilder(
                new BezierLine(
                        blueGoal,
                        rightWhiteSpike
                )
        )
                .setLinearHeadingInterpolation(blueGoalHeading,rightWhiteSpikeHeading)
                .setZeroPowerAccelerationMultiplier(RobotConfig.AutoConstants.bucketToRightSpikeZPAM)
                .build();
        Path fromRightSpikeToGoal = new Path.PathBuilder(
                new BezierLine(
                        rightWhiteSpike,
                        blueGoal
                )
        )
                .setConstantHeadingInterpolation(blueGoalHeading)
                .setPathEndTranslationalConstraint(.3)
                .setPathEndVelocityConstraint(1)

                .build();
        Path fromGoalToMiddleSpike = new Path.PathBuilder(
                new BezierLine(
                        blueGoal,
                        middleWhiteSpike
                )
        )
                .setLinearHeadingInterpolation(blueGoalHeading,middleWhiteSpikeHeading)
                .setZeroPowerAccelerationMultiplier(RobotConfig.AutoConstants.bucketToMiddleSpikeZPAM)

                .build();

        Path fromMiddleSpikeToGoal = new Path.PathBuilder(
                new BezierLine(
                        middleWhiteSpike,
                        blueGoal
                )
        )
                .setConstantHeadingInterpolation(blueGoalHeading)
                .setPathEndTranslationalConstraint(.3)
                .setPathEndVelocityConstraint(1)

//                .setPathEndVelocityConstraint(.5)
//                .setConstantHeadingInterpolation(5*PI/4)
                .build();
        Path fromGoalToLeftSpike = new Path.PathBuilder(
                new BezierLine(
                        blueGoal,
                        leftWhiteSpike
                )
        )
                .setLinearHeadingInterpolation(blueGoalHeading,leftWhiteSpikeHeading)
                .setPathEndTranslationalConstraint(.3)
                .setPathEndVelocityConstraint(1)
                .setZeroPowerAccelerationMultiplier(RobotConfig.AutoConstants.bucketToLeftSpikeZPAM)

                .build();

        Path fromLeftSpikeToGoal = new Path.PathBuilder(
                new BezierLine(
                        leftWhiteSpike,
                        blueGoal
                )
        )
                .setConstantHeadingInterpolation(blueGoalHeading)
                .setPathEndTranslationalConstraint(.3)
                .setPathEndVelocityConstraint(1)
                //.setPathEndVelocityConstraint(.5)
                .build();

        Path firstGoalToSubmersible = new Path.PathBuilder(
                new BezierCurve(
                        blueGoal,
                        submersibleParameterPoint,
                        submersibleStart
                )
        )
                .setLinearHeadingInterpolation(blueGoalHeading,submersibleHeading)
                .build();


        Path fromSubmersibleToGoal = new Path.PathBuilder(
                new BezierCurve(
                        submersibleStart,
                        submersibleParameterPoint,

                        blueGoal
                )
        )

                .setConstantHeadingInterpolation(blueGoalHeading)
                .setPathEndTranslationalConstraint(.3)
                .setPathEndVelocityConstraint(1)
                //.setPathEndVelocityConstraint(.5)
                .build();


        //makes paths into commands
        PedroPathAutoCommand startToGoalCommand = new PedroPathAutoCommand(robot.followerSubsystem,startToGoal);
        PedroPathAutoCommand fromGoalToRightSpikeCommand = new PedroPathAutoCommand(robot.followerSubsystem, fromGoalToRightSpike);
        PedroPathAutoCommand fromRightSpikeToGoalCommand = new PedroPathAutoCommand(robot.followerSubsystem, fromRightSpikeToGoal);
        PedroPathAutoCommand fromGoalToMiddleSpikeCommand = new PedroPathAutoCommand(robot.followerSubsystem,fromGoalToMiddleSpike);
        PedroPathAutoCommand fromMiddleSpikeToGoalCommand = new PedroPathAutoCommand(robot.followerSubsystem,fromMiddleSpikeToGoal);
        PedroPathAutoCommand fromGoalToLeftSpikeCommand = new PedroPathAutoCommand(robot.followerSubsystem,fromGoalToLeftSpike);
        PedroPathAutoCommand fromLeftSpikeToGoalCommand = new PedroPathAutoCommand(robot.followerSubsystem,fromLeftSpikeToGoal);
        PedroPathAutoCommand fromGoalToSubmersibleCommand = new PedroPathAutoCommand(robot.followerSubsystem,firstGoalToSubmersible);
        PedroPathAutoCommand fromSubmersibleToGoalCommand = new PedroPathAutoCommand(robot.followerSubsystem,fromSubmersibleToGoal);


        SequentialCommandGroup groupRetractIntake = new SequentialCommandGroup(
                robot.moveIntakeUp.copy(),
                new ParallelCommandGroup(
                        robot.retractIntake.copy(),
                        new InstantCommand(robot.spinIntakeSubsystem::spinWheelsUp)
                ),

                robot.passIntoBucket.copy(RobotConfig.IntakeConstants.passThroughTimeMs)
        );

        //have to clear the grouped commands so that we can reuse the commands for multiple command groups
        CommandGroupBase.clearGroupedCommands();



        SequentialCommandGroup dunk = new SequentialCommandGroup(
                robot.liftCommand,
                robot.armHighDunkCommand,

                new WaitCommand(200)
        );
        SequentialCommandGroup outtakeDown = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        robot.liftDownCommand,
                        robot.armDownCommand
                )
        );
        CommandGroupBase.clearGroupedCommands();


        //goes from the start to the goal and dunks
        ParallelCommandGroup startGoalDunk = new ParallelCommandGroup(
                startToGoalCommand,
                new SequentialCommandGroup(
                        robot.extendIntakeToClearPos.copy(),
                        new ParallelCommandGroup(
                                dunk,
                                robot.extendIntake.copy()
                        )
                )
        );

        CommandGroupBase.clearGroupedCommands();

        //goes from the goal to the right spike, intakes the sample, and passes the sample into the bucket
        SequentialCommandGroup goalRightSpikeIntakePass = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        fromGoalToRightSpikeCommand,
                        outtakeDown
                ),
//                new ParallelCommandGroup(
//                        robot.moveIntakeDown,
//                        robot.spinIntake
//                ),
                robot.getVerticalAndSpin(RobotConfig.IntakeConstants.intakeWaitAtBottomTimeMs)

        );

        CommandGroupBase.clearGroupedCommands();


        //goes to the goal from the right spike, extends the intake and dunks
        SequentialCommandGroup rightSpikeGoalDunk = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new WaitCommand(50),
                                groupRetractIntake,

                                robot.extendIntakeToClearPos.copy(),
                                robot.liftCommand.copy(),
                                //new ScheduleCommand(robot.extendIntake.copy()),
                                new ScheduleCommand(robot.readyVerticalNormal.copy())
                        ),
                        fromRightSpikeToGoalCommand
                ),
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                robot.armHighDunkCommand.copy(),
                                new WaitCommand(RobotConfig.OuttakeConstants.highDunkLingerTimeMs)
                        ),
                        robot.extendIntake.copy()
                )

        );

        CommandGroupBase.clearGroupedCommands();

        //goes to the middle spike from the goal, intakes and passes it in
        SequentialCommandGroup goalMiddleSpikeIntakePass = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        fromGoalToMiddleSpikeCommand,
                        outtakeDown
                ),
//                new ParallelCommandGroup(
//                        robot.moveIntakeDown,
//                        robot.spinIntake
//                ),
                robot.getVerticalAndSpin(RobotConfig.IntakeConstants.intakeWaitAtBottomTimeMs)
        );
        CommandGroupBase.clearGroupedCommands();

        //goes from the middle spike to the middle spike and dunks
        SequentialCommandGroup middleSpikeGoalDunk = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new WaitCommand(50),
                                groupRetractIntake,
                                robot.extendIntake.copy(),


                                        robot.liftCommand.copy(),


                                new InstantCommand(robot.readyVerticalNormal.copy()::schedule)

                        ),
                        fromMiddleSpikeToGoalCommand
                ),
                new SequentialCommandGroup(
                        robot.armHighDunkCommand.copy(),
                        new WaitCommand(RobotConfig.OuttakeConstants.highDunkLingerTimeMs)
                )
        );
        CommandGroupBase.clearGroupedCommands();

        SequentialCommandGroup goalLeftSpikeIntakePass = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        fromGoalToLeftSpikeCommand,
                        outtakeDown
                ),
//                new ParallelCommandGroup(
//                        robot.moveIntakeDown,
//                        robot.spinIntake
//                ),
                new SequentialCommandGroup(
                        new InstantCommand(robot.spinIntakeSubsystem::spinWheelsUp),
                        robot.moveIntakeDownSubmersible.copy(),

                        new WaitCommand(460),
                        robot.moveIntakeUp.copy()

                ),

                new InstantCommand(robot.spinIntakeSubsystem::stopIntakeWheels)
                //groupRetractIntake
        );

        CommandGroupBase.clearGroupedCommands();

        //goes from the middle spike to the middle spike and dunks
        SequentialCommandGroup leftSpikeGoalDunk = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new SequentialCommandGroup(

                                robot.retractIntake.copy(),
                                robot.passIntoBucket.copy(350),

                                robot.extendIntake.copy(),
                                robot.liftCommand.copy()

                        ),
                        fromLeftSpikeToGoalCommand
                ),
                new SequentialCommandGroup(
                        robot.armHighDunkCommand.copy(),
                        new WaitCommand(RobotConfig.OuttakeConstants.highDunkLingerTimeMs)
                )
        );

        //scan for samples in submersible


        CommandGroupBase.clearGroupedCommands();

        //back to submersible and score
        Command submersibleToBasketAndScore = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                robot.retractIntake.copy(),
                                robot.passIntoBucket.copy(450),
                                robot.extendIntake.copy(),
                                robot.liftCommand.copy()

                        ),
                        fromSubmersibleToGoalCommand
                ),
                robot.armHighDunkCommand.copy(),
                new InstantCommand(()->robot.armSubsystem.setPower(RobotConfig.OuttakeConstants.highDunkLingerPower))

        );

        CommandGroupBase.clearGroupedCommands();
        timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        FunctionalCommand setTime = new FunctionalCommand(
                ()->{timer.reset();},
                ()->{time = timer.time(TimeUnit.MILLISECONDS)/1000.;},
                (Boolean b)->{},
                ()->{return false;}
        );
        Command telemetryTimer = new FunctionalCommand(()->{},()->{multipleTelemetry.addData("time",time); },(Boolean b)->{},()->false);
        //puts all commands together
        SequentialCommandGroup autoRoutine = new SequentialCommandGroup(
                new InstantCommand(timer::reset),

                new InstantCommand(()->robot.followerSubsystem.getFollower().setMaxPower(.95)),
                startGoalDunk,

                goalRightSpikeIntakePass,
                rightSpikeGoalDunk,
                goalMiddleSpikeIntakePass,
                middleSpikeGoalDunk,
                goalLeftSpikeIntakePass,
                leftSpikeGoalDunk,
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                fromGoalToSubmersibleCommand
                                //fromGoalToSubmersibleCommand2
                        ),
                        outtakeDown

                ),
                new InstantCommand(this::requestOpModeStop),
                robot.findSampleAndDrive.copy(),
                new InstantCommand(robot.spinIntakeSubsystem::spinWheelsUp),
                robot.moveIntakeDownSubmersible.copy(),
                new WaitCommand(500),
                robot.moveIntakeUp.copy(),
                submersibleToBasketAndScore,
                new InstantCommand(setTime::cancel)
//                scanForSamples,
//                submersibleToBasketAndScore
        );
        setTime.schedule();
        telemetryTimer.schedule();
        autoRoutine.schedule();

    }
}
