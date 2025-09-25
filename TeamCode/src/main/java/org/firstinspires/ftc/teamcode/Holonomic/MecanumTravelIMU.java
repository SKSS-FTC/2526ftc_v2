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

/*
  A basic program that:
  <ul>
  <li> travels in a linear movement</li>
  <li> turns 90 degrees when the touch sensor is pressed</li>
  <li> the turn direction is controlled with bumper buttons on the gamepad</li>
  <li> relies on IMU data to travel in a straight line as well to perform the turns</li>
  <li> displays a few status messages</li>
  </ul>
  @author modified by armw
 * @version 1.1
 * @param none
 * @return none
 * @exception none
 * @see https://stemrobotics.cs.pdx.edu/node/7266
 * <p>
 * This program registers as Autonomous OpMode in the FtcRobotController app.
 * The robot travels forward in a linear movement. When the touch sensor is pressed
 * it backs up a little enable a 90 degree turn. The bumper buttons on gamepad2
 * select the direction of the turn - left or right.
 * The program relies on the IMU sensor in the REV Robotics Control Hub that
 * runs the FtcRobotController app.
 * </p>
 * <p>
 * forward travel:
 * ^                   ^
 * |                   |
 * 0 left front        2 right front
 * X
 * ^                   ^
 * |                   |
 * 1 left back         3 right back
 *
 * hard coded numbers to avoid the use of enum construct for such a simple program
 * motor positions:
 * <ul>
 * <li>0 = left front (or forward or fore)</li>
 * <li>1 = left back (or rear or aft)</li>
 * <li>2 = right front (or forward or fore)</li>
 * <li>3 = right back (or rear or aft)</li>
 *</ul>
 * Initialize the hardware variables. Note that the strings used here as parameters
 * to 'get' must correspond to the names assigned during the robot configuration
 * step (using the FTC Robot Controller app on the phone).
 * Moon Mechanics nomenclature options for motors:
 * <device><port|starboard>|<stern/aft>
 * <role><qualifier>
 * </p>
 * @see https://first-tech-challenge.github.io/SkyStone/com/qualcomm/robotcore/hardware/DcMotor.html
 *
 * @see https://docs.revrobotics.com/rev-control-system/sensors/encoders/motor-based-encoders
 * HD Hex Motor (REV-41-1291) Encoder Specifications
 * HD Hex Motor Reduction                  Bare Motor      40:1            20:1
 * Free speed, RPM                         6,000           150             300
 * Cycles per rotation of encoder shaft    28 (7 Rises)    28 (7 Rises)    28 (7 Rises)
 * Ticks per rotation of output shaft      28              1120            560
 * TICKS_PER_MOTOR_REV = 560            REV HD Hex UltraPlanetary 20:1 cartridge
 * DRIVE_GEAR_REDUCTION = 1.0
 * WHEEL_DIAMETER_MM = 75.0             REV Mecanum wheel
 * MM_TO_INCH = 0.03937008
 * TICKS_PER_INCH = (TICKS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_MM * MM_TO_INCH * PI)
 * = 56.9887969189608
 * <p>
 * Hardware map
 * Device name      Control Hub setting
 * imu              I2C bus 0
 * motorLeftFront   port 0
 * motorLeftBack    port 1
 * motorRightFront  port 2
 * motorRightBack   port 3
 * sensorTouch      n/a
 * sensorLED        n/a
 * gamepad2         USB2
 * </p>
 * The RevHubOrientationOnRobot.LogoFacingDirection enum has the following six values:
 *  UP: The REV Hub logo faces upwards, away from the robot chassis.
 *  DOWN: The REV Hub logo faces downwards, towards the robot chassis.
 *  FORWARD: The REV Hub logo faces forward, in the direction of the robot's front.
 *  BACKWARD: The REV Hub logo faces backward, in the direction of the robot's rear.
 *  LEFT: The REV Hub logo faces left, relative to the robot's forward direction.
 *  RIGHT: The REV Hub logo faces right, relative to the robot's forward direction
 * The values for the USB port direction are:
 *  UP: The USB ports face upwards, away from the robot chassis.
 *  DOWN: The USB ports face downwards, towards the robot chassis.
 *  FORWARD: The USB ports face forward, in the direction of the robot's front.
 *  BACKWARD: The USB ports face backward, in the direction of the robot's rear.
 *  LEFT: The USB ports face left, relative to the robot's forward direction.
 *  RIGHT: The USB ports face right, relative to the robot's forward direction.
 */

package org.firstinspires.ftc.teamcode.Holonomic;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import org.firstinspires.ftc.teamcode.Utility.Datalogger;

@TeleOp(name = "Mecanum: Travel IMU", group = "Test")
// @Disabled
public class MecanumTravelIMU extends LinearOpMode {
    private static final String TAG = MecanumTravelIMU.class.getSimpleName(); // for use in logging
    Datalog datalog = new Datalog(TAG);

    static final double TICKS_PER_INCH = 29.96; // SWYFT Drive: AndyMark NeveRest motor, goBILDA 96 mm mecanum wheel

    TouchSensor touch;
    IMU imu; // Universal IMU interface
    YawPitchRollAngles lastAngles;
    double globalAngle, initialPower = .40, correction;
    boolean aButton, bButton, touched;
    private final ElapsedTime runtime = new ElapsedTime();

    // motor entities for drivetrain
    String[] motorLabels = {
            "motorLeftFront",           // port 0 Control Hub
            "motorLeftBack",            // port 1 Control Hub
            "motorRightFront",          // port 2 Control Hub
            "motorRightBack"            // port 3 Control Hub
    };
    DcMotorEx[] motor = new DcMotorEx[]{null, null, null, null};
    int[] motorTicks = {0, 0, 0, 0};    // current tick count from encoder for the respective motors

    private void LinearTravel(double travelLength, double travelPower) {
        double appliedPower = travelPower;
        boolean travelCompleted = false;
        double ticksError;
        double ticksToGo;
        double travelTicks = TICKS_PER_INCH * travelLength;
        double yawError;

        for (DcMotorEx dcMotor : motor) {
            dcMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            dcMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        }
        for (int i = 0; i < motor.length; i++) {
            motorTicks[i] = 0;
        }

        resetAngle();

        while (opModeIsActive() && !travelCompleted) {
            ticksError = ((motorTicks[1] + motorTicks[2]) - (motorTicks[0] + motorTicks[3])) * 0.007;
            yawError = checkDirection();
            correction = -ticksError + yawError;

            for (int i = 0; i < motor.length; i++) {
                motorTicks[i] = motor[i].getCurrentPosition();
            }

            ticksToGo = (Math.abs(travelTicks) - Math.abs(motorTicks[0])) / Math.abs(travelTicks);
            if (ticksToGo < 0.10) {
                appliedPower = travelPower * ticksToGo;
                correction = correction * ticksToGo;
            }

            if (Math.abs(motorTicks[0]) < Math.abs(travelTicks)) {
                motor[0].setPower(appliedPower - correction);
                motor[1].setPower(appliedPower + correction);
                motor[2].setPower(appliedPower + correction);
                motor[3].setPower(appliedPower - correction);

                telemetry.addData("1 IMU Heading", getAngle());
                telemetry.addData("2 Correction", correction);
                telemetry.addData("3 Current ticks:", motorTicks[0]);
                telemetry.addData("4 Target ticks:", travelTicks);
                telemetry.addData("5 Motor power:", motor[0].getPower());
                telemetry.update();

                datalog.yaw.set(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
                datalog.pitch.set(imu.getRobotYawPitchRollAngles().getPitch(AngleUnit.DEGREES));
                datalog.roll.set(imu.getRobotYawPitchRollAngles().getRoll(AngleUnit.DEGREES));
                datalog.yawError.set(yawError);
                datalog.ticksError.set(ticksError);
                datalog.correction.set(correction);
                datalog.targetTicks.set(travelTicks);
                datalog.motorTicks.set(motorTicks[0]);
                datalog.ticksToGo.set(ticksToGo);
                datalog.appliedPower.set(appliedPower);
                datalog.leftFront.set(motor[0].getPower());
                datalog.leftBack.set(motor[1].getPower());
                datalog.rightFront.set(motor[2].getPower());
                datalog.rightBack.set(motor[3].getPower());
                datalog.writeLine();

                aButton = gamepad1.a;
                bButton = gamepad1.b;

                if (aButton) {
                    backup();
                    rotate(-90, travelPower);
                }

                if (bButton) {
                    backup();
                    rotate(90, travelPower);
                }
            } else {
                travelCompleted = true;
            }
        }
        fullStop();
    }

    private void backup() {
        for (DcMotorEx dcMotor : motor) {
            dcMotor.setPower(-0.3);
        }
        sleep(500);
        fullStop();
    }

    private void fullStop() {
        for (DcMotorEx dcMotor : motor) {
            dcMotor.setPower(0.0);
            dcMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            dcMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            dcMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        }
        sleep(250);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize motors
        for (int i = 0; i < motor.length; i++) {
            motor[i] = hardwareMap.get(DcMotorEx.class, motorLabels[i]);
            motor[i].setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            motor[i].setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            motor[i].setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        }
        motor[0].setDirection(DcMotorEx.Direction.FORWARD);
        motor[1].setDirection(DcMotorEx.Direction.FORWARD);
        motor[2].setDirection(DcMotorEx.Direction.REVERSE);
        motor[3].setDirection(DcMotorEx.Direction.REVERSE);

        // Initialize IMU
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.FORWARD;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.UP;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        IMU.Parameters parameters = new IMU.Parameters(orientationOnRobot);
        imu.initialize(parameters);

        telemetry.addData("Status", "IMU initialized with orientation");
        telemetry.addData("Robot Orientation", "Logo Facing: FORWARD, USB Facing: UP");
        telemetry.addData("Mode", "Select Start");
        telemetry.update();

        waitForStart();

        telemetry.addData("Mode", "running");
        telemetry.update();

        double travelLength = 0.0;
        double deltaTravel = 3.0;

        for (int i = 0; i < 3; i++) {
            travelLength += deltaTravel;
            LinearTravel(travelLength, initialPower);
            long pauseInterval = 5000;
            telemetry.addData("Traveled", travelLength + " inches");
            telemetry.addData("Paused", pauseInterval + " milliseconds");
            telemetry.update();
            sleep(pauseInterval);
        }
    }

    private void resetAngle() {
        imu.resetYaw();
    }

    private double getAngle() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    private double checkDirection() {
        double correction, angle, gain = 0.0055;
        angle = getAngle();
        if (angle == 0)
            correction = 0;
        else
            correction = -angle;
        correction = correction * gain;
        return correction;
    }

    private void rotate(int degrees, double power) {
        double leftPower, rightPower;

        resetAngle();

        if (degrees < 0) {
            leftPower = power;
            rightPower = -power;
        } else if (degrees > 0) {
            leftPower = -power;
            rightPower = power;
        } else return;

        motor[0].setPower(leftPower);
        motor[1].setPower(rightPower);
        motor[2].setPower(rightPower);
        motor[3].setPower(leftPower);

        if (degrees < 0) {
            while (opModeIsActive() && getAngle() > degrees) {
            }
        } else {
            while (opModeIsActive() && getAngle() < degrees) {
            }
        }

        fullStop();
        imu.resetYaw();
    }

    public static class Datalog {
        private final Datalogger datalogger;
        public Datalogger.GenericField opModeStatus = new Datalogger.GenericField("OpModeStatus");
        public Datalogger.GenericField yaw = new Datalogger.GenericField("Yaw");
        public Datalogger.GenericField pitch = new Datalogger.GenericField("Pitch");
        public Datalogger.GenericField roll = new Datalogger.GenericField("Roll");
        public Datalogger.GenericField yawError = new Datalogger.GenericField("YawError");
        public Datalogger.GenericField ticksError = new Datalogger.GenericField("TicksError");
        public Datalogger.GenericField correction = new Datalogger.GenericField("Correction");
        public Datalogger.GenericField targetTicks = new Datalogger.GenericField("TargetTicks");
        public Datalogger.GenericField motorTicks = new Datalogger.GenericField("MotorTicks");
        public Datalogger.GenericField ticksToGo = new Datalogger.GenericField("TicksToGo");
        public Datalogger.GenericField appliedPower = new Datalogger.GenericField("AppliedPower");
        public Datalogger.GenericField leftFront = new Datalogger.GenericField("motorLeftFront");
        public Datalogger.GenericField leftBack = new Datalogger.GenericField("motorLeftBack");
        public Datalogger.GenericField rightFront = new Datalogger.GenericField("motorRightFront");
        public Datalogger.GenericField rightBack = new Datalogger.GenericField("motorRightBack");

        public Datalog(String name) {
            datalogger = new Datalogger.Builder()
                    .setFilename(name)
                    .setAutoTimestamp(Datalogger.AutoTimestamp.DECIMAL_SECONDS)
                    .setFields(
                            opModeStatus,
                            yaw,
                            pitch,
                            roll,
                            yawError,
                            ticksError,
                            correction,
                            targetTicks,
                            motorTicks,
                            leftFront,
                            leftBack,
                            rightFront,
                            rightBack
                    )
                    .build();
        }

        public void writeLine() {
            datalogger.writeLine();
        }
    }
}