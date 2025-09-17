package org.firstinspires.ftc.teamcode.Swerve;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ODO.GoBildaPinpointDriver;

public class Swerve {

  LinearOpMode opMode;
  public DcMotor brMotor, blMotor, frMotor, flMotor;
  public Servo brServo, blServo, frServo, flServo;
  private final Telemetry telemetry;
  public double robot_Angle;
  public final GoBildaPinpointDriver odo;

  public Swerve(LinearOpMode opMode, GoBildaPinpointDriver odo) {
    this.opMode = opMode;
    telemetry = opMode.telemetry;
    this.odo = odo;
  }

  public void drive(double x, double y, double rotate) {
    odo.update();
    double vector_ang = Math.atan2(x, y) * (180 / Math.PI);
    double vector_pow = Math.sqrt((x * x + y * y));
    telemetry.addLine("input vector angle: " + vector_ang);
    telemetry.addLine("input vector power: " + vector_pow);
    vector_ang = (vector_ang - 90) % 360;
    vector_ang /= 360;
    telemetry.addLine("input vector angle: " + vector_ang);
  }
}
