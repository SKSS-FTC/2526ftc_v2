// Copyright (c) 2024-2025 FTC 13532
// All rights reserved.


package org.firstinspires.ftc.teamcode.Mekanism;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * This class is for the mechanism
 */
public class Mekanism {

    // Allows calling of LinearOpMode functions
    public final LinearOpMode myOp;


    DcMotor pivot, slide;
    Servo spintake, wrist;

    // TODO: Find proper numbers for this
    int COUNTS_PER_INCH = 1120; // Encoder counts per inch slide movement
    int COUNTS_PER_DEGREE = 1120; // Encoder counts per degree


    // Constructor for Mekanism stuff
    public Mekanism(LinearOpMode myOp) {
        this.myOp = myOp;

        // Init slaw, claw, and pivot
        pivot = myOp.hardwareMap.get(DcMotor.class, "pivot");
        slide = myOp.hardwareMap.get(DcMotor.class, "slide");

        // Initial target of the motors
        pivot.setTargetPosition(0);
        slide.setTargetPosition(0);

        // Directions
        // TODO: Set proper directions
        pivot.setDirection(DcMotor.Direction.FORWARD);
        slide.setDirection(DcMotor.Direction.FORWARD);

        pivot.setMode(STOP_AND_RESET_ENCODER);
        slide.setMode(STOP_AND_RESET_ENCODER);

        pivot.setMode(RUN_USING_ENCODER);
        slide.setMode(RUN_USING_ENCODER);

        pivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Sets maximum allowed power to 1
        pivot.setPower(1);
        slide.setPower(1);


        // Servos for the end effector
        wrist = myOp.hardwareMap.get(Servo.class, "wrist");
        spintake = myOp.hardwareMap.get(Servo.class, "spintake"); // TODO: Get proper name
        wrist.scaleRange(0, 1); // TODO: Set ranges for wrist positions
    }

    /**
     * Set the position and angle for arm independently
     */
    public void basicMoveArm(int position, int angle) {

        slide.setTargetPosition(position);
        pivot.setTargetPosition(angle);
    }

    /**
     * In IN.
     * Uses x and y to calculate arm angle and length
     */
    public void moveXY(int x, int y) {

        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }

        int armLen = (int) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

        int armAngle = (int) Math.atan((double) y / (double) x);

        slide.setTargetPosition(armLen * COUNTS_PER_INCH);

        pivot.setTargetPosition(armAngle * COUNTS_PER_DEGREE);
    }

    /**
     * Sets the position of the claw
     *
     * @param position Input
     */
    public void moveClaw(double position) {
        spintake.setPosition(position);
    }
}
