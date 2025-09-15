package org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.RobotConfig;

import static org.firstinspires.ftc.teamcode.RobotConfig.OuttakeConstants.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;



public class ArmSubsystem extends SubsystemBase {
    int startPos;
    MotorEx armSwing;
    private Telemetry telemetry;

    private int lastPosition;
    //nanoseconds
    private double lastTime;

    //multiply coefficients by these
    private double derivative = 0;

    public ArmSubsystem(HardwareMap hMap, Telemetry telemetry){
        armSwing = new MotorEx(hMap, armOuttakeName);
        armSwing.setRunMode(Motor.RunMode.PositionControl);


        armSwing.setInverted(false);
        armSwing.setPositionTolerance(20);
        armSwing.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        lastPosition = armSwing.getCurrentPosition();
        setLastTime();
        //armSwing.encoder.reset();
        this.telemetry = telemetry;
    }


    public void dunkHighPos(){
        armSwing.setRunMode(Motor.RunMode.RawPower);
        armSwing.set(armSwingPower);
    }
    public void goToHangPos(){
        armSwing.setRunMode(Motor.RunMode.RawPower);
        armSwing.set(-.75);
    }
    public boolean isAtHangPos(){
        return Math.abs(armSwing.getCurrentPosition())<176;
    }

    public void dunkLowPos(){
        armSwing.setRunMode(Motor.RunMode.PositionControl);
        armSwing.setTargetPosition(RobotConfig.OuttakeConstants.armSwingLowDunkPos);
        armSwing.set(.25);

    }
    public boolean hasLowDunked(){
        return Math.abs(armSwing.getCurrentPosition()-RobotConfig.OuttakeConstants.armSwingLowDunkPos)<15;
    }
    public boolean hasDunked(){
        return armSwing.getCurrentPosition()-RobotConfig.OuttakeConstants.armSwingHighDunkPos>-15;
    }

    public void setPower(double power){
        armSwing.setRunMode(Motor.RunMode.RawPower);

        armSwing.set(power);
    }
    public void stop(){
        armSwing.set(0);
    }

    public void goDown(){


        armSwing.set(-.75);
    }
    public boolean isDown(){
        return Math.abs(armSwing.getCurrentPosition()-RobotConfig.OuttakeConstants.armSwingDefaultPos)<150;
    }
    public void setBrake(){
        armSwing.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
    }

    public void setFloat(){
        armSwing.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
    }


    private void setLastTime(){
        lastTime = System.nanoTime()/Math.pow(10,9);

    }

    private double getTimeSeconds(){
        return System.nanoTime()/Math.pow(10,9);
    }

    private double getAngle(){
        return Range.scale(armSwing.getCurrentPosition(),armSwing90DegreesVerticalPos,armSwingParallelVerticalPos,90,0);
    }

    @Override
    public void periodic(){
        telemetry.addData("Dunk Pos", armSwing.getCurrentPosition());
        telemetry.addData("Arm Swing Derivative",derivative);
        telemetry.addData("Angle To Vertical, Degrees",getAngle());
        derivative = (armSwing.getCurrentPosition()-lastPosition)/(getTimeSeconds()-lastTime);
        derivative = armSwing.getVelocity();
        lastPosition = armSwing.getCurrentPosition();
        setLastTime();
    }



}
