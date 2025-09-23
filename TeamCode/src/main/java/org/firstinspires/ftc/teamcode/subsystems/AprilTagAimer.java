package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class AprilTagAimer {
    private final IMU imu;
    private final DcMotor leftFront, rightFront, leftBack, rightBack;
    private final double kP = 0.01; // Full PID can be done later
    private Double targetAngle = null;

    public AprilTagAimer(HardwareMap hardwareMap) {
        Movement movement = new Movement(hardwareMap);
        leftFront = movement.getLeftFront();
        rightFront = movement.getRightFront();
        leftBack = movement.getLeftBack();
        rightBack = movement.getRightBack();
        imu = movement.getImu();
    }

    private double angleWrapDegrees(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

    public void startTurnToAprilTag(double bearing) {
        double currentYaw = imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle;
        targetAngle = angleWrapDegrees(currentYaw + bearing);
    }

    public boolean updateTurn() {
        if (targetAngle == null) return true;

        double currentYaw = imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle;
        double error = angleWrapDegrees(targetAngle - currentYaw);

        if (Math.abs(error) < 1) { // within 1 degree
            stopMotors();
            targetAngle = null;
            return true;
        }
        else {
            double power = kP * error;
            power = Math.max(-1, Math.min(1, power));

            leftFront.setPower(power);
            rightFront.setPower(-power);
            leftBack.setPower(power);
            rightBack.setPower(-power);
            return false;
        }
    }

    private void stopMotors() {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }
}