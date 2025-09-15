package org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Intake;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.RobotConfig;

public class VerticalIntakeSubsystem extends SubsystemBase {

    private ServoEx verticalServo;
    private IntakeHeight verticalServoTarget= IntakeHeight.UP;
    private Bot bot;
    private Telemetry telemetry;


    public void setBotType(Bot bot){
        this.bot = bot;
    }

    public VerticalIntakeSubsystem(HardwareMap hMap, Telemetry telemetry, Bot bot){
        verticalServo = new SimpleServo(
                hMap,
                RobotConfig.IntakeConstants.verticalServoName,
                RobotConfig.IntakeConstants.verticalServoMinDegrees,
                RobotConfig.IntakeConstants.verticalServoMaxDegrees
        );
        verticalServo.setPosition(RobotConfig.IntakeConstants.verticalServoUpPosition);
        this.telemetry = telemetry;
        this.bot = bot;
    }

    public VerticalIntakeSubsystem(HardwareMap hMap, Telemetry telemetry){
        this(hMap,telemetry,Bot.COMP);
    }

    public void moveIntakeUp(){
        switch (bot){
            case PRACTICE:
                verticalServo.setPosition(.34);
                break;
            case COMP:
                verticalServo.setPosition(RobotConfig.IntakeConstants.verticalServoUpPosition);
                break;

        }
        verticalServoTarget = IntakeHeight.UP;
    }

    public void moveIntakeDown(){
        switch (bot){
            case PRACTICE:
                verticalServo.setPosition(0);
                break;
            case COMP:
                verticalServo.setPosition(RobotConfig.IntakeConstants.verticalServoDownPosition);
                break;

        }
        verticalServoTarget = IntakeHeight.DOWN;
    }
    public void moveIntakeToOutOfSubReady(){
        verticalServo.setPosition(RobotConfig.IntakeConstants.verticalServoOutOfSubReadyPos);
    }
    public void moveIntakeDownSubmersible(){
        verticalServo.setPosition(RobotConfig.IntakeConstants.verticalServoSubmersibleIntakePos);
    }
    public double getServoPos(){
        return verticalServo.getPosition();
    }
    @Override
    public void periodic(){
        telemetry.addLine("============Vertical Servo============");
        telemetry.addData("Vertical Servo Position",verticalServo.getPosition());
        telemetry.addData("Vertical Servo Target",verticalServoTarget);
    }


    public enum IntakeHeight{
        UP,
        DOWN
    }
}
