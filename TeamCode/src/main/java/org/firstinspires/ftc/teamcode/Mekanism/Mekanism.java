// Copyright (c) 2024-2025 FTC 13532
// All rights reserved.

package org.firstinspires.ftc.teamcode.Mekanism;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.concurrent.atomic.AtomicBoolean;

/** This class is for the mechanism */
public class Mekanism {

  // Allows calling of LinearOpMode functions
  public final LinearOpMode myOp;

  DcMotor pivot, slide;
  Servo intake, wrist, ramp1, ramp2;
  DigitalChannel homePivot;

  // TODO: Find proper numbers for this
  int COUNTS_PER_INCH = 100; // Encoder counts per inch slide movement
  int COUNTS_PER_DEGREE = 30; // Encoder counts per degree

  ElapsedTime homeTimer = new ElapsedTime();

  public AtomicBoolean autoClipRun = new AtomicBoolean(false);

  Thread autoClip =
      new Thread() {
        public void run() {
          autoClipRun.set(true);

          // If the clip is too close, move it out, so it doesn't hit the clip holder
          while (slide.getCurrentPosition() < 200 && autoClipRun.get() && myOp.opModeIsActive()) {
            slide.setTargetPosition(210);
            wrist.setPosition(0.1);
            myOp.sleep(10);
          }

          ramp1.setPosition(1.0);
          ramp2.setPosition(1.0);
          myOp.sleep(750);
          ramp1.setPosition(0.0);
          ramp2.setPosition(0.0);
          myOp.sleep(250);
          ramp1.setPosition(0.25);
          ramp2.setPosition(0.25);
          myOp.sleep(250);
          ramp1.setPosition(0.0);
          ramp2.setPosition(0.0);

          // Move the pivot to above the clip holder
          while (pivot.getCurrentPosition() > 1500 && autoClipRun.get() && myOp.opModeIsActive()) {
            pivot.setTargetPosition(1510);
            myOp.sleep(10);
          }

          // Bring the slide down to the initial clipping position
          while (slide.getTargetPosition() > 200 && autoClipRun.get() && myOp.opModeIsActive()) {
            slide.setTargetPosition(190);
            myOp.sleep(10);
          }
        }
      };

  /**
   * Main constructor for the Mekansim class
   *
   * @param myOp LinearOpMode class
   */
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
    wrist.scaleRange(0, 1); // TODO: Set ranges for wrist positions

    ramp1 = myOp.hardwareMap.get(Servo.class, "ramp 1");
    ramp2 = myOp.hardwareMap.get(Servo.class, "ramp 2");
    ramp1.scaleRange(0, 1); // TODO: Set proper scale range
    ramp2.scaleRange(0, 1); // TODO: Set proper scale range

    homePivot = myOp.hardwareMap.get(DigitalChannel.class, "homePivot"); // TODO: Get proper name
  }

  /** Homes the slide by running it at a low speed until it is in the proper position */
  public void homeMek() {
    slide.setMode(RUN_WITHOUT_ENCODER);
    slide.setPower(-0.3);

    homeTimer.reset();
    while (homeTimer.seconds() < 2 && myOp.opModeIsActive()) myOp.idle();

    slide.setMode(STOP_AND_RESET_ENCODER);
    slide.setMode(RUN_TO_POSITION);
    slide.setPower(1.0);

    pivot.setMode(RUN_WITHOUT_ENCODER);
    pivot.setPower(0.3);
    while (!homePivot.getState() && myOp.opModeIsActive()) myOp.idle();

    pivot.setMode(STOP_AND_RESET_ENCODER);
    pivot.setMode(RUN_TO_POSITION);
    pivot.setPower(1.0);
  }

  /**
   * Set the position and angle for arm independently
   *
   * <p>In inches and degrees
   */
  public void basicMoveArm(int position, int angle) {
    pivot.setTargetPosition(angle * COUNTS_PER_DEGREE);
    slide.setTargetPosition(position * COUNTS_PER_INCH);
  }

  /**
   * Uses x and y to calculate arm angle and length
   *
   * <p>In inches and degrees.
   */
  public void moveXY(double x, double y) {

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
   * @param position Input (-1) to 1
   */
  public void wristAngle(double position) {
    position = (position + 1) / 2;
    wrist.setPosition(position);
  }

  /**
   * Sets the power of the spintake
   *
   * @param power (-1) to 1
   */
  public void setIntake(double power) {
    power = (power + 1) / 2;
    intake.setPosition(power);
  }

  /** Moves the slide to clip an element */
  public void clipElement() {}
}
