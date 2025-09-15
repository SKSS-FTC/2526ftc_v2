package org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Intake;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.RobotConfig;

public class ExtendMotorSubsystem extends SubsystemBase {
    private Motor extendMotor;
    private ExtendPos extendPos = ExtendPos.IN;

    private int posMultiplier = 1;

    private Telemetry telemetry;
    private Bot bot = Bot.COMP;
    public ExtendMotorSubsystem(HardwareMap hMap, Telemetry telemetry){
        extendMotor = new MotorEx(hMap, RobotConfig.IntakeConstants.extendMotorName);
        extendMotor.setRunMode(Motor.RunMode.PositionControl);
        extendMotor.setInverted(true);
        extendMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        extendMotor.setPositionCoefficient(RobotConfig.IntakeConstants.motorPCoefficient);

        this.telemetry = telemetry;
    }
    public void setBrake(){
        extendMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
    }
    public void setFloat(){
        extendMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
    }

    public void setBot(Bot bot){
        this.bot = bot;
        if (bot == Bot.PRACTICE){
            posMultiplier =-1;
        } else {
            posMultiplier =1;
        }
    }

    public int getPosition(){
        return extendMotor.getCurrentPosition();
    }

    public void periodic(){
        telemetry.addLine("============Extend Motor============");
        telemetry.addData("Motor Position",extendMotor.getCurrentPosition()*posMultiplier);
        telemetry.addData("Extend Target",extendPos);
    }

    public void extendMotorOutFully(){
        extendMotor.setRunMode(Motor.RunMode.RawPower);
//        extendMotor.setTargetPosition(RobotConfig.IntakeConstants.motorMaxPosition*posMultiplier);
        extendMotor.set(RobotConfig.IntakeConstants.motorExtendSpeed*posMultiplier);
        extendPos = ExtendPos.OUT;
    }

    public void retractMotorFully(){
        extendMotor.setRunMode(Motor.RunMode.RawPower);
        extendMotor.set(RobotConfig.IntakeConstants.motorRetractSpeed*posMultiplier);
        extendPos = ExtendPos.IN;
    }



    public void stopExtend(){
        extendMotor.set(0);
    }

    public boolean extendFinished(){
        switch (extendPos){
            case IN:
                return extendMotor.getCurrentPosition()*posMultiplier<
                        (RobotConfig.IntakeConstants.motorMinPosition+RobotConfig.IntakeConstants.motorDegreeOfError);
            case OUT:
                return (extendMotor.getCurrentPosition()*posMultiplier)>
                        (RobotConfig.IntakeConstants.motorMaxPosition-RobotConfig.IntakeConstants.motorDegreeOfError);
            case CLEAR:
                return Math.abs(extendMotor.getCurrentPosition()*posMultiplier-RobotConfig.IntakeConstants.motorClearPos)<
                        (RobotConfig.IntakeConstants.motorDegreeOfError);

        }
        return extendMotor.atTargetPosition();
    }

    public void extendToClearPos(){
        extendMotor.setRunMode(Motor.RunMode.PositionControl);
        extendMotor.setTargetPosition(RobotConfig.IntakeConstants.motorClearPos*posMultiplier);
        extendMotor.set(posMultiplier);
        extendPos = ExtendPos.CLEAR;
    }

    public enum ExtendPos{
        OUT("OUT"),
        CLEAR("CLEAR"),
        IN("IN");
        private String string;
        ExtendPos (String desc){
            this.string = desc;
        }

        public String toString(){
            return string;
        }
    }
}
