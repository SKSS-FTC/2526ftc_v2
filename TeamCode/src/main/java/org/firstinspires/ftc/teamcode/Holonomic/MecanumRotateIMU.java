/*
 * MIT License
 *
 * Copyright (c) 2024 ParkCircus Productions; All Rights Reserved
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.Holonomic;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name = "Mecanum: Rotate IMU", group = "Mecanum")
public class MecanumRotateIMU extends LinearOpMode {

    // --- ROBOT HARDWARE DECLARATIONS ---
    private DcMotorEx frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
    private IMU imu;

    // --- ROTATION CONSTANTS ---
    // Target heading to rotate to, in degrees.
    private static final double TARGET_HEADING_DEGREES = 90.0;
    // Proportional constant (P) for the rotation control loop.
    // Tune this value to adjust the speed and stability of the rotation.
    private static final double ROTATION_KP = 0.02;
    // Tolerance in degrees. The robot will stop rotating when it is within this range of the target.
    private static final double ROTATION_TOLERANCE_DEGREES = 2.0;

    @Override
    public void runOpMode() {
        // --- HARDWARE INITIALIZATION ---
        // This is where hardware is mapped and configured.
        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "motorLeftFront");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "motorRightFront");
        backLeftMotor = hardwareMap.get(DcMotorEx.class, "motorLeftBack");
        backRightMotor = hardwareMap.get(DcMotorEx.class, "motorRightBack");

        // Set motor directions. This is standard for a mecanum drive.
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

        // Set motor run modes. RUN_USING_ENCODER is generally preferred for controlled movements.
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // --- IMU INITIALIZATION ---
        imu = hardwareMap.get(IMU.class, "imu");

        // The constructor for RevHubOrientationOnRobot requires both logo and USB facing directions.
        // You MUST tune these for your specific robot's setup.
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        )));

        // Reset the yaw angle to zero. This is the only reset method now available.
        imu.resetYaw();

        telemetry.addData("Status", "Initialized. Press Play to start rotation.");
        telemetry.addData("Target Heading", "%.2f degrees", TARGET_HEADING_DEGREES);
        telemetry.update();

        waitForStart();

        // --- Main OpMode Loop ---
        try {
            while (opModeIsActive()) {
                // Get the current heading from the IMU.
                double currentHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

                // Calculate the error between the target and current heading.
                double headingError = TARGET_HEADING_DEGREES - currentHeading;

                // Normalize the error to be between -180 and 180 degrees.
                if (headingError > 180) {
                    headingError -= 360;
                } else if (headingError <= -180) {
                    headingError += 360;
                }

                // Check if we are within the desired tolerance.
                if (Math.abs(headingError) <= ROTATION_TOLERANCE_DEGREES) {
                    // Stop all motors if we are at the target.
                    frontLeftMotor.setPower(0);
                    frontRightMotor.setPower(0);
                    backLeftMotor.setPower(0);
                    backRightMotor.setPower(0);
                    telemetry.addData("Status", "Rotation Complete!");
                    telemetry.addData("Final Heading", "%.2f", currentHeading);
                    telemetry.update();
                    break; // Exit the loop
                } else {
                    // Calculate the rotation power using a simple proportional controller.
                    double rotationPower = headingError * ROTATION_KP;

                    // Cap the power to prevent overly aggressive rotation.
                    rotationPower = Math.max(-0.5, Math.min(0.5, rotationPower));

                    // Apply the rotation power to all motors.
                    frontLeftMotor.setPower(-rotationPower);
                    frontRightMotor.setPower(rotationPower);
                    backLeftMotor.setPower(-rotationPower);
                    backRightMotor.setPower(rotationPower);

                    telemetry.addData("Status", "Rotating...");
                    telemetry.addData("Current Heading", "%.2f", currentHeading);
                    telemetry.addData("Heading Error", "%.2f", headingError);
                    telemetry.addData("Rotation Power", "%.2f", rotationPower);
                    telemetry.update();
                }
            }
        } catch (Exception e) {
            // This catch block will log any exceptions that occur during the OpMode's run
            // and should give you more information than the emergency stop.
            telemetry.addData("FATAL ERROR", "An exception occurred. Check logcat for details.");
            telemetry.addData("Exception Class", e.getClass().getName());
            telemetry.addData("Exception Message", e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                telemetry.addData("Stack Trace", element.toString());
            }
            telemetry.update();
            sleep(10000); // Give time for the message to be read
        }
    }
}