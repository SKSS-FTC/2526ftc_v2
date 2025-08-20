package org.firstinspires.ftc.teamcode.Slide;

import static org.firstinspires.ftc.teamcode.ODO.GoBildaPinpointDriver.EncoderDirection.FORWARD;
import static org.firstinspires.ftc.teamcode.ODO.GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ODO.GoBildaPinpointDriver;

public class Arm {

  private DcMotor arm_Len;
  private LinearOpMode opMode;

  public Arm(LinearOpMode opMode) {
    this.opMode = opMode;
    arm_Len = opMode.hardwareMap.get(DcMotor.class, "Arm odometry");
    arm_Len.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    arm_Len.setDirection(DcMotorSimple.Direction.REVERSE);
  }

  public void set_Arm_Pos(double len, double ang) {

  }

  public void set_Arm_Ang(int ang) {

  }

  public void set_Arm_Len(double len) {

  }

  public double get_Arm_Len() { // 12in is roughly from -199 to -56__ 
    double len = 0;
    len = arm_Len.getCurrentPosition();
    opMode.telemetry.addLine("Arm pos: " + len);
    return len;
  }

  public double get_Arm_Ang() {
    double ang = 0;

    return ang;
  }
}
