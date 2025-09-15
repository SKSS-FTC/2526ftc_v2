package org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotConfig;
// ADD MORE COMMENTS FOR FUTURE REFERENCE

public class LiftSubsystem extends SubsystemBase {

    int startPos;
    MotorEx armLift;
    MotorEx hangMotor;
    private Telemetry telemetry;


    public LiftSubsystem(HardwareMap hMap, Telemetry telemetry){
        armLift = new MotorEx(hMap, RobotConfig.OuttakeConstants.armLiftName);

        hangMotor = new MotorEx(hMap,RobotConfig.HangConstants.hangLiftName);
        hangMotor.setRunMode(Motor.RunMode.RawPower);

        armLift.setInverted(false);
        armLift.setPositionTolerance(20);
        armLift.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        this.telemetry = telemetry;
    }


    public void setBrake(){
        armLift.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
    }
    public void setFloat(){
        armLift.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
    }

    public int getValue(){return armLift.getCurrentPosition()-startPos;}



    public void stop(){
        armLift.set(0);
        hangMotor.set(0);
    }
    public void reset(){
        startPos = armLift.getCurrentPosition();
    }

    @Override
    public void periodic(){
        telemetry.addData("Lift Position", armLift.getCurrentPosition());
    }

    public void goToHighDunk(){
        armLift.setRunMode(Motor.RunMode.RawPower);
        armLift.setTargetPosition(RobotConfig.OuttakeConstants.armLiftHighDunkPos);
        armLift.set(1*RobotConfig.OuttakeConstants.armLiftDirection);
        hangMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
    }

    public boolean finishedHighDunk(){
        return armLift.getCurrentPosition()-RobotConfig.OuttakeConstants.armLiftHighDunkPos< RobotConfig.OuttakeConstants.armLiftError;
    }

    public void pullHangDown(){
        hangMotor.set(1);
    }
    
    public void goDown(){
        armLift.setRunMode(Motor.RunMode.RawPower);
        armLift.setTargetPosition(RobotConfig.OuttakeConstants.armLiftDefaultPos);
        armLift.set(1*-RobotConfig.OuttakeConstants.armLiftDirection);
    }

    public boolean isDown(){
        return Math.abs(armLift.getCurrentPosition()-RobotConfig.OuttakeConstants.armLiftDefaultPos)<RobotConfig.OuttakeConstants.armLiftError;
    }



}