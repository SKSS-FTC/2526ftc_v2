// Copyright (c) 2024-2025 FTC 13532
// All rights reserved.

package org.firstinspires.ftc.teamcode.CompBot;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Mekanism.*;

@Disabled
@TeleOp(name = "CompBot Swerve", group = "CompBot")
public class CompBot extends LinearOpMode {

  SwerveConfig swerve = new SwerveConfig(this);
  Mekanism mek = new Mekanism(this);
  Utils utils = new Utils(this);
  GraphicTelemetry graph = new GraphicTelemetry(this);

  double xLen = 0, yLen = 0;
  double wristAngle = 0;

  Runnable mekRunnable =
      new Runnable() {

        @Override
        public void run() {
          while (opModeIsActive()) {
            if (gamepad2.dpad_down) mek.clipElement();
            else if (gamepad2.left_stick_x != 0) {
              xLen += gamepad2.left_stick_x;
            } else if (gamepad2.left_stick_y != 0) {
              yLen += -gamepad2.left_stick_y;
            }

            mek.setSpintake(gamepad2.right_trigger);
            mek.wristAngle(gamepad2.left_trigger);

            mek.moveXY(xLen, yLen);
          }
        }
      };
  Thread mekThread = new Thread(mekRunnable);

  /**
   * Controls for Gamepad 1: Right trigger: Forwards Left trigger: Reverse Right stick X: Rotate
   * Left stick X Strafe
   *
   * <p>Controls for Gamepad 2: Left stick y: In and out of arm Right stick y: Up and down of arm
   * Left trigger: Claw intake Right trigger: Claw out Presets for: Attaching clip to sample
   * Attaching specimen(clip + sample) to top rung Presets for bucket 1 and 2
   */
  public void runOpMode() throws InterruptedException {

    swerve.initSwerve(); // Inits all the stuff related to swerve drive

    mekThread.start();

    waitForStart();
    while (opModeIsActive()) {

      idle();
    }
  }
}
