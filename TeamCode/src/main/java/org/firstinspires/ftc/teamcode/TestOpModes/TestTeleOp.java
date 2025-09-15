package org.firstinspires.ftc.teamcode.TestOpModes;


import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Test.MotorCommandTest;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Test.ServoCommandTest;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test.MotorTest;
import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test.ServoTest;
import org.firstinspires.ftc.teamcode.RobotConfig;

import java.util.List;

@Disabled
@TeleOp
public class TestTeleOp extends OpMode {

    private GamepadEx gamepad1Ex;
    private MotorTest testSubsystem;
    private MotorCommandTest testMotorCommand;
    private Command testCommand;
    private ServoCommandTest servoCommandTest;
    private ColorSensor cSensor;


    @Override
    public void init(){
        gamepad1Ex = new GamepadEx(gamepad1);



        testSubsystem = new MotorTest(hardwareMap);
        ServoTest testServoSubsystem = new ServoTest(hardwareMap);

        testMotorCommand = new MotorCommandTest(testSubsystem);
        servoCommandTest = new ServoCommandTest(testServoSubsystem);

        testCommand = new ParallelCommandGroup(
                testMotorCommand,
                new SequentialCommandGroup(
                        new WaitCommand(5000),
                        servoCommandTest
                )
        );
        Trigger commandTrigger  = new Trigger(() ->gamepad1Ex.getButton(GamepadKeys.Button.X));
        commandTrigger.whenActive(testCommand);
        cSensor = hardwareMap.get(ColorSensor.class,"cSensor");
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }

    public void loop(){
        gamepad1Ex.readButtons();

        telemetry.addData("Motor Spot",testSubsystem.getValue());
        if (testCommand.isFinished()){
            telemetry.addLine("Command Finished");
        }


        double red = cSensor.red();
        double blue = cSensor.blue();
        double green = cSensor.green();



        telemetry.addData("Color Sensor Red",red);
        telemetry.addData("Color Sensor Blue", blue);
        telemetry.addData("Color Sensor Green", green);

        double max = Math.max(Math.max(red,blue),green);

        red/=max;
        blue/=max;
        green/=max;
        telemetry.addData("Adjusted Sensor Red",red);
        telemetry.addData("Adjusted Sensor Blue", blue);
        telemetry.addData("Adjusted Sensor Green", green);

        if(blue>red&&blue>green){
            telemetry.addLine("Blue Sample");
        } else if (red>blue&&red>green){
            telemetry.addLine("Red Sample");
        } else if (Math.abs(red-green) < RobotConfig.IntakeConstants.colorSensorRedToGreenThreshold){
            telemetry.addLine("Yellow Sample");
        } else {
            telemetry.addLine("IDK");
        }

        telemetry.update();

        CommandScheduler.getInstance().run();

    }
}
