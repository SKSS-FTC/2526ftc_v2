package org.firstinspires.ftc.teamcode.subsystems;//package nullrobotics.subsystems;
//
//import com.arcrobotics.ftclib.command.SubsystemBase;
//import com.arcrobotics.ftclib.util.MathUtils;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//
//import nullrobotics.lib.RobotHardware;

public class MecanumSubsystem extends SubsystemBase {
    RobotHardware robot;
    public MecanumSubsystem() {
        robot = RobotHardware.getInstance();
    }

    public void powerMotors(double powerFL, double powerFR, double powerBL, double powerBR) {
        robot.dtFL.setPower(powerFL);
        robot.dtFR.setPower(powerFR);
        robot.dtBL.setPower(powerBL);
        robot.dtBR.setPower(powerBR);
    }

    // positive is forward
    public void drive(double power) {
        power = MathUtils.clamp(power, -1, 1);

    }

    // positive is left
    public void strafe(double power) {
        power = MathUtils.clamp(power, -1, 1);
        robot.dtFL.setPower(-power);
        robot.dtFR.setPower(power);
        robot.dtBL.setPower(power);
        robot.dtBR.setPower(-power);
    }
}
