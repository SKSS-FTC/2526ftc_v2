package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ServoTraining {
    private LinearOpMode opmode = null;

    private Servo servo = null;


    public ServoTraining() {
    }

    public void init(LinearOpMode opMode) {
        HardwareMap hwMap;

        opmode = opMode;
        hwMap = opMode.hardwareMap;
        servo = hwMap.servo.get("Servo");
    }

    public void normal(){
        servo.setPosition(0);
    }
    public void right(){
        servo.setPosition(1);
    }
    public void left(){
        servo.setPosition(-1);
    }
}



    // run until the end of the match (driver presses STOP)

