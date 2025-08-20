package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Slide.Arm;

@TeleOp(name = "Arm test")
public class Arm_test extends LinearOpMode {

  public Arm arm;

  @Override
  public void runOpMode() throws InterruptedException {

    Init();
    waitForStart();
    while(opModeIsActive()) {
      arm.get_Arm_Len();
      telemetry.update();
    }
  }

  public void Init(){
    arm = new Arm(this);
  }
}
