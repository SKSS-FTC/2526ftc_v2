package org.firstinspires.ftc.teamcode.Mekanism;

import static com.qualcomm.robotcore.hardware.Servo.Direction.FORWARD;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Carla_Differential {

  LinearOpMode opMode;
  public Servo left, right;

  public Carla_Differential(LinearOpMode opMode) {
    this.opMode = opMode;
    left = opMode.hardwareMap.get(Servo.class, "left differential");
    right = opMode.hardwareMap.get(Servo.class, "right differential");

    left.setDirection(FORWARD);
    right.setDirection(FORWARD);

    left.scaleRange(-1,1);
    right.scaleRange(-1,1);

    left.setPosition(0);
    right.setPosition(0);
  }

  public void move(double pos, double rotation) {
    double left_diff = pos;
    double right_diff = -pos;
    left.setPosition(left_diff);
    right.setPosition(right_diff);
  }
}
