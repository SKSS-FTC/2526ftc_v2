//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//
//public class Intake {
//
//    private DcMotor IntakeTest = null;
//
//    private LinearOpMode opmode = null;
//
//    public Intake() {
//    }
//
//    public void init(LinearOpMode opMode) {
//        HardwareMap hwMap;
//
//        opmode = opMode;
//        hwMap = opMode.hardwareMap;
//
//        // sets the thing up :3c
//        IntakeTest = hwMap.dcMotor.get("IntakeTest");
//
//        // sets power to zero!!! :3
//        IntakeTest.setPower(0);
//    }
//
//    public void forward(double speed) {
//        IntakeTest.setPower(speed);
//    }
//    public void backwards(double speed) {
//        IntakeTest.setPower(speed);
//    }
//}
