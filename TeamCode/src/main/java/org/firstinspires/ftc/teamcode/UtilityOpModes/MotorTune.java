package org.firstinspires.ftc.teamcode.UtilityOpModes;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotConfig;

import java.util.function.DoubleSupplier;


public class MotorTune  {

    private Motor motor;
    private int startPos;
    private Telemetry telemetry;
    private Gamepad gamepad;
    private DoubleSupplier getPos;

    private Servo servo;


    public MotorTune(HardwareMap hMap, Gamepad gamepad, Telemetry telemetry){
        servo = hMap.get(Servo.class, RobotConfig.IntakeConstants.verticalServoName);
        //servo = new MotorEx(hMap,RobotConfig.IntakeConstants.extendMotorName);

        //servo.setRunMode(Motor.RunMode.RawPower);
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }


    public void run(){

//        servo.set(gamepad.left_stick_y*-1);
//        telemetry.addData("Power",gamepad.left_stick_y*-1);
//        telemetry.addData("Position",servo.getCurrentPosition());

        double pos = servo.getPosition() - gamepad.right_stick_y*.0005;
        pos = Range.clip(pos,0,1);
        servo.setPosition(pos);
        telemetry.addData("Servo Pos",servo.getPosition());
    }

}
