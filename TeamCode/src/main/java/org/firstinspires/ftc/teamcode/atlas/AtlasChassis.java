package org.firstinspires.ftc.teamcode.atlas;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static java.lang.Math.*;

import java.lang.reflect.Parameter;

public class AtlasChassis {
    public DcMotorEx backLeft, backRight, frontLeft, frontRight;
    public IMU imu;
    public AtlasPose pose;

    public double yawRads = 0.0;
    public double yawDeg = 0.0;
    public double limeLightYawOffset = 0.0;

    public int backLeftTicks = 0;
    public int backRightTicks = 0;
    public int frontLeftTicks = 0;
    public int frontRightTicks = 0;

    public double metersPerTick = 1.0;
    public double fieldRelativeOffset = 0.0;

    private long lastUpdateTime = System.currentTimeMillis();

    public static final double RAD_TO_DEG = 180.0 / Math.PI;

    public AtlasChassis(HardwareMap hardwareMap) {
        backLeft = getDcMotorEx(hardwareMap, "rearLeft");
        backRight = getDcMotorEx(hardwareMap, "rearRight");
        frontLeft = getDcMotorEx(hardwareMap, "frontLeft");
        frontRight = getDcMotorEx(hardwareMap, "frontRight");

        // Make sure imu exists in hardware map
        imu = hardwareMap.get(IMU.class, "imu");

        pose = new AtlasPose(metersPerTick);

        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        imu.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                ))
        );
        imu.resetYaw();
    }

    private DcMotorEx getDcMotorEx(HardwareMap hardwareMap, String name) {
        DcMotorEx motor = (DcMotorEx) hardwareMap.get(DcMotor.class, name);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        return motor;
    }

    public void moveFieldRelative(double x, double y, double rx) {
        double yaw = yawRads + fieldRelativeOffset;
        double rotX = x * sin(yaw) + y * cos(yaw);
        double rotY = x * cos(yaw) - y * sin(yaw);
        movePower(rotX, rotY, rx);
    }

    public void movePower(double x, double y, double r) {
        double denominator = max(abs(y) + abs(x) + abs(r), 1.0);
        frontLeft.setPower((y + x + r) / denominator);
        backLeft.setPower((y - x + r) / denominator);
        frontRight.setPower((y - x - r) / denominator);
        backRight.setPower((y + x - r) / denominator);
    }

    public void update(Telemetry telemetry) {
        long currentTime = System.currentTimeMillis();
        double deltaTimeMS = currentTime - lastUpdateTime;
        double deltaTime = deltaTimeMS * 0.001;
        lastUpdateTime = currentTime;

        Orientation orientation = imu.getRobotOrientation(
                AxesReference.INTRINSIC,
                AxesOrder.ZYX,
                AngleUnit.RADIANS
        );

        yawRads = orientation.firstAngle;
        yawDeg = orientation.firstAngle * RAD_TO_DEG;

        int[] positions = new int[]{
                backLeft.getCurrentPosition(),
                backRight.getCurrentPosition(),
                frontLeft.getCurrentPosition(),
                frontRight.getCurrentPosition()
        };

        int deltaBackLeft = positions[0] - backLeftTicks;
        int deltaBackRight = positions[1] - backRightTicks;
        int deltaFrontLeft = positions[2] - frontLeftTicks;
        int deltaFrontRight = positions[3] - frontRightTicks;

        backLeftTicks = positions[0];
        backRightTicks = positions[1];
        frontLeftTicks = positions[2];
        frontRightTicks = positions[3];

        pose.updateEncoders(deltaFrontLeft, deltaFrontRight, deltaBackLeft, deltaBackRight, yawRads);

        if (telemetry != null) {
            telemetry.addLine("Chassis debug data:");
            telemetry.addLine("position (" + pose.x + ", " + pose.y + ")");
            telemetry.addLine("yaw " + yawDeg);
        }
    }
}
