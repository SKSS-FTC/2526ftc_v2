package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Training {

//    Create motor objects here
    private DcMotor FrontRM = null;
    private DcMotor FrontLM = null;
    private DcMotor BackRM = null;
    private DcMotor BackLM = null;




    private LinearOpMode opmode = null;


    public Training() {
    }

    public void init(LinearOpMode opMode) {
        HardwareMap hwMap;

        opmode = opMode;
        hwMap = opMode.hardwareMap;

        // name motor objects here
        FrontRM = hwMap.dcMotor.get("FrontRM");
        FrontLM = hwMap.dcMotor.get("FrontLM");
        BackRM = hwMap.dcMotor.get("BackRM");
        BackLM = hwMap.dcMotor.get("BackLM");


        // directions!!!
        FrontLM.setDirection(DcMotorSimple.Direction.REVERSE);
        FrontRM.setDirection(DcMotorSimple.Direction.FORWARD);
        BackLM.setDirection(DcMotorSimple.Direction.REVERSE);
        BackRM.setDirection(DcMotorSimple.Direction.FORWARD);


        // set motor powers to 0
        FrontRM.setPower(0);
        FrontLM.setPower(0);
        BackRM.setPower(0);
        BackLM.setPower(0);
    }

    public void forward() {
        // set powers for motors to go forward here
        FrontRM.setPower(1);
        FrontLM.setPower(1);
        BackRM.setPower(1);
        BackLM.setPower(1);
    }
    public void backwards() {
        // set powers for motors to go backwards here
        FrontRM.setPower(-1);
        FrontLM.setPower(-1);
        BackRM.setPower(-1);
        BackLM.setPower(-1);
    }
    public void left() {
        // set powers for motors to go left here
        FrontRM.setPower(-1);
        FrontLM.setPower(1);
        BackRM.setPower(-1);
        BackLM.setPower(1);
    }
    public void right() {
        // set powers for motors to go right here
        FrontRM.setPower(1);
        FrontLM.setPower(-1);
        BackRM.setPower(1);
        BackLM.setPower(-1);
    }
    public void stop() {
        // set motor power to stop
        FrontRM.setPower(0);
        FrontLM.setPower(0);
        BackRM.setPower(0);
        BackLM.setPower(0);
    }

}
