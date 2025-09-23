package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "EncodedMovement", group = "Autonomous")
public class EncodedWheelsQuestionMark extends LinearOpMode {

    // Declare the motor
    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;

    // Number of encoder ticks per revolution (adjust this based on your motor)
    private static final int TICKS_PER_REV = 480; // ticks-per-rev of a 20:1 Tetrix
                                                  // TorqueNADO gearbox
    private static final int ROTATIONS = 12;
     

    @Override
    public void runOpMode() throws InterruptedException {
        
        // Initialize the motor
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
        
        //Set motor directions
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        
        // Set motor to run with encoder
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Calculate the target position for 1 rotations
        int targetPosition = 5000;

        // Set the target position
        frontLeftMotor.setTargetPosition(targetPosition);
        frontRightMotor.setTargetPosition(targetPosition);
        backLeftMotor.setTargetPosition(targetPosition);
        backRightMotor.setTargetPosition(targetPosition);
        
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        waitForStart();
        // Set power to the motor (adjust the value as needed)
        frontLeftMotor.setPower(0.01);
        backLeftMotor.setPower(0.01);
        frontRightMotor.setPower(0.01);
        backRightMotor.setPower(0.01);
        // Wait for the motor to reach the target position

        // Stop the motors
        if (Math.abs(frontLeftMotor.getCurrentPosition()) > 2000) {
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);
        }
        // Optionally, switch back to the idle mode
        //frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
