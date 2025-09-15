package org.firstinspires.ftc.teamcode.TestOpModes;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.FunctionalCommand;
import com.arcrobotics.ftclib.command.ParallelDeadlineGroup;
import com.arcrobotics.ftclib.command.ParallelRaceGroup;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp
public class TestParallelRaceGroup extends OpMode {
    private DcMotor dcMotor;
    private Servo servo;


    @Override
    public void init(){
        dcMotor = hardwareMap.get(DcMotor.class,"motor");
        servo = hardwareMap.get(Servo.class,"servo");
        ParallelDeadlineGroup parallelRaceGroup = new ParallelDeadlineGroup(
                new WaitUntilCommand(()->{return servo.getPosition()==1;}),
                new FunctionalCommand(
                        ()->{},
                        ()->{dcMotor.setPower(1);},
                        (Boolean b) ->{dcMotor.setPower(0);},
                        () ->false
                )

        );
        parallelRaceGroup.schedule();
    }

    @Override
    public void loop(){
        CommandScheduler.getInstance().run();
        telemetry.addData("Servo Pos",servo.getPosition());
        servo.setPosition(gamepad1.right_stick_y*.002+servo.getPosition());
        telemetry.update();
    }
}
