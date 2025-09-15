package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandGroupBase;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Drive.LimelightDriveCommand;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Hang.HangHighRungCommand;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Hang.HangLowRungCommand;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Hang.HangPullDownCommand;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Intake.ExtendIntakeToClearPos;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Intake.MoveIntakeDownSubmersible;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Intake.ReadyVerticalNormal;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Limelight.FindSampleAndDrive;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Limelight.SampleFinder;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Outtake.ArmDownCommand;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Outtake.ArmHangPosCommand;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Outtake.ArmHighDunkCommand;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Drive.FollowerTeleOpCommand;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Intake.ExtendIntake;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Intake.MoveIntakeDown;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Intake.MoveIntakeUp;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Intake.PassIntoBucket;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Intake.RetractIntake;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Intake.SpinIntake;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Outtake.ArmLowDunkCommand;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Outtake.LiftDownCommand;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Outtake.LiftHighBasketCommand;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Drive.TeleOpDriveCommand;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Outtake.PullHangDownCommand;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.ArmSubsystem;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Hang.HangSubsystem;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Intake.ExtendMotorSubsystem;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.FollowerSubsystem;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.LiftSubsystem;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Intake.SpinIntakeSubsystem;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Intake.VerticalIntakeSubsystem;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.LimelightSubsystem;

public class Robot {
    public GamepadEx drivePad;
    public GamepadEx gamepadEx2;

    public DriveSubsystem driveSubsystem;
    public TeleOpDriveCommand driveCommand;
    public FollowerSubsystem followerSubsystem;
    public FollowerTeleOpCommand followerTeleOpCommand;
    public FindSampleAndDrive findSampleAndDrive;

    public SpinIntakeSubsystem spinIntakeSubsystem;
    public VerticalIntakeSubsystem verticalIntakeSubsystem;
    public ExtendMotorSubsystem extendMotorSubsystem;

    public ExtendIntake extendIntake;
    public SpinIntake spinIntake;
    public RetractIntake retractIntake;
    public PassIntoBucket passIntoBucket;
    public MoveIntakeUp moveIntakeUp;
    public MoveIntakeDown moveIntakeDown;
    public MoveIntakeDownSubmersible moveIntakeDownSubmersible;
    public ReadyVerticalNormal readyVerticalNormal;
    public ExtendIntakeToClearPos extendIntakeToClearPos;
    public ParallelCommandGroup verticalAndSpin;
    public PullHangDownCommand pullHangDownCommand;


    public LiftSubsystem liftSubsystem;
    public LiftHighBasketCommand liftCommand;
    public LiftDownCommand liftDownCommand;


    public ArmSubsystem armSubsystem;
    public ArmHighDunkCommand armHighDunkCommand;
    public ArmDownCommand armDownCommand;
    public ArmLowDunkCommand armLowDunkCommand;
    public ArmHangPosCommand armHangPosCommand;



    public LimelightSubsystem limelightSubsystem;
    public LimelightDriveCommand limelightDriveCommand;

    public HangSubsystem hangSubsystem;
    public HangLowRungCommand hangLowRungCommand;
    public HangHighRungCommand hangHighRungCommand;
    public HangPullDownCommand hangPullDownCommand;
    public SequentialCommandGroup hangSequence;

    public Robot(HardwareMap hMap, GamepadEx drivePad, GamepadEx gamepadEx2, Telemetry telemetry,AllianceColor color){
        this.drivePad = drivePad;
        this.gamepadEx2 = gamepadEx2;


        /*
        ====================Intake====================
         */
        verticalIntakeSubsystem = new VerticalIntakeSubsystem(hMap,telemetry);
        spinIntakeSubsystem = new SpinIntakeSubsystem(hMap,color,telemetry);
        extendMotorSubsystem = new ExtendMotorSubsystem(hMap,telemetry);

        extendIntake = new ExtendIntake(extendMotorSubsystem);
        retractIntake = new RetractIntake(extendMotorSubsystem);
        spinIntake = new SpinIntake(spinIntakeSubsystem);
        passIntoBucket = new PassIntoBucket(spinIntakeSubsystem);
        moveIntakeDown = new MoveIntakeDown(verticalIntakeSubsystem);
        moveIntakeUp = new MoveIntakeUp(verticalIntakeSubsystem);
        moveIntakeDownSubmersible = new MoveIntakeDownSubmersible(verticalIntakeSubsystem);
        extendIntakeToClearPos = new ExtendIntakeToClearPos(extendMotorSubsystem);
        readyVerticalNormal = new ReadyVerticalNormal(verticalIntakeSubsystem);
        CommandGroupBase.clearGroupedCommands();

        verticalAndSpin = new ParallelCommandGroup(
                new SequentialCommandGroup(
                        moveIntakeDown,
                        new WaitCommand(RobotConfig.IntakeConstants.intakeWaitAtBottomTimeMs),
                        moveIntakeUp
                ),

                new InstantCommand(spinIntakeSubsystem::spinWheelsUp)
        );


        /*
        ====================Lift/Dunk====================
         */
        liftSubsystem = new LiftSubsystem(hMap,telemetry);
        armSubsystem = new ArmSubsystem(hMap,telemetry);

        liftCommand = new LiftHighBasketCommand(liftSubsystem);
        liftDownCommand = new LiftDownCommand(liftSubsystem);
        armHighDunkCommand = new ArmHighDunkCommand(armSubsystem);
        armDownCommand = new ArmDownCommand(armSubsystem);
        armLowDunkCommand = new ArmLowDunkCommand(armSubsystem);
        armHangPosCommand = new ArmHangPosCommand(armSubsystem);
        pullHangDownCommand = new PullHangDownCommand(liftSubsystem);


        /*
        ====================Drive====================
         */
        IMU imu = hMap.get(IMU.class,"imu");
        driveSubsystem = new DriveSubsystem(hMap,drivePad,imu);
        driveCommand = new TeleOpDriveCommand(driveSubsystem);

        followerSubsystem = new FollowerSubsystem(hMap);
        followerSubsystem.setTelemetry(telemetry);
        followerTeleOpCommand = new FollowerTeleOpCommand(followerSubsystem,telemetry,drivePad);

        /*
        ====================Limelight====================
         */
        limelightSubsystem = new LimelightSubsystem(hMap,followerSubsystem.getFollower()::getPose,extendMotorSubsystem::getPosition, color);
        SampleFinder sampleFinder = new SampleFinder(limelightSubsystem,telemetry);

        limelightDriveCommand = new LimelightDriveCommand(
                sampleFinder,
                followerSubsystem,
                telemetry
        );
        findSampleAndDrive =new FindSampleAndDrive(limelightDriveCommand);
        CommandGroupBase.clearGroupedCommands();

        /*
        ========================Hang======================
         */
        hangSubsystem = new HangSubsystem(hMap, telemetry,gamepadEx2);
        hangHighRungCommand = new HangHighRungCommand(hangSubsystem);
        hangLowRungCommand = new HangLowRungCommand(hangSubsystem);

        hangPullDownCommand = new HangPullDownCommand(hangSubsystem);

    }


    public void setBot(Bot bot){
        driveSubsystem.setBot(bot);
        spinIntakeSubsystem.setBot(bot);
        verticalIntakeSubsystem.setBotType(bot);
        followerSubsystem.getFollower().setBot(bot);
        extendMotorSubsystem.setBot(bot);
    }

    public Command getVerticalAndSpin(int millis){
        return new ParallelCommandGroup(
                new SequentialCommandGroup(
                        moveIntakeDown,
                        new WaitCommand(millis),
                        moveIntakeUp
                ),

                new InstantCommand(spinIntakeSubsystem::spinWheelsUp)
        );
    }
}







