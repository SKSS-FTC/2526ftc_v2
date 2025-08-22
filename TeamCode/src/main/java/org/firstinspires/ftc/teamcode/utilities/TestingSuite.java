package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.configuration.Settings;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Comprehensive Testing Suite for FTC Robot Hardware
 * Combines manual testing controls with automated validation
 *
 * @noinspection unused
 */
@Config
@TeleOp(name = "Testing Suite", group = "TeleOp")
public class TestingSuite extends LinearOpMode {
    // ==================== HARDWARE CONFIGURATION ====================

    // Individual Motor Testing Options
    private static final String[] MOTOR_OPTIONS = {
            Settings.Hardware.IDs.FRONT_LEFT_MOTOR,
            Settings.Hardware.IDs.FRONT_RIGHT_MOTOR,
            Settings.Hardware.IDs.REAR_LEFT_MOTOR,
            Settings.Hardware.IDs.REAR_RIGHT_MOTOR,
            Settings.Hardware.IDs.SLIDE_VERTICAL_LEFT,
            Settings.Hardware.IDs.SLIDE_VERTICAL_RIGHT,
            Settings.Hardware.IDs.SLIDE_HORIZONTAL,
    };

    // Individual Servo Testing Options
    private static final String[] SERVO_OPTIONS = {
            Settings.Hardware.IDs.WRIST,
            Settings.Hardware.IDs.ROTATOR,
            Settings.Hardware.IDs.LEFT_SHOULDER,
            Settings.Hardware.IDs.RIGHT_SHOULDER,
            Settings.Hardware.IDs.INTAKE_CLAW,
            Settings.Hardware.IDs.OUTTAKE_CLAW,
    };

    // Dual Motor Testing Options
    private static final String[] DUAL_MOTOR_OPTIONS = {
            "DUAL_MOTOR_SLIDE_VERTICAL",
    };

    // Dual Servo Testing Options
    private static final String[] DUAL_SERVO_OPTIONS = {
            "DUAL_SERVO_SHOULDERS",
    };

    // Sensor Testing Options
    private static final String[] SENSOR_OPTIONS = {
            Settings.Hardware.IDs.SLIDE_VERTICAL_TOUCH_SENSOR,
            Settings.Hardware.IDs.LIMELIGHT + " yellow detection",
            Settings.Hardware.IDs.LIMELIGHT + " red detection",
            Settings.Hardware.IDs.LIMELIGHT + " blue detection",
            Settings.Hardware.IDs.COLOR_SENSOR,
    };

    // System Testing Options
    private static final String[] SYSTEM_OPTIONS = {
            "AUTOMATED_HARDWARE_CHECK",
            "ENCODER_VALIDATION",
    };

    // Combined Options List
    private static final String[] LIST_OPTIONS = Stream.of(
            Arrays.stream(MOTOR_OPTIONS),
            Arrays.stream(DUAL_MOTOR_OPTIONS),
            Arrays.stream(SERVO_OPTIONS),
            Arrays.stream(DUAL_SERVO_OPTIONS),
            Arrays.stream(SENSOR_OPTIONS),
            Arrays.stream(SYSTEM_OPTIONS)).flatMap(stream -> stream).toArray(String[]::new);

    // ==================== MAIN OPMODE METHOD ====================

    @Override
    public void runOpMode() {
        // Menu state variables
        AtomicBoolean menuActive = new AtomicBoolean(true);
        AtomicInteger listSelection = new AtomicInteger(0);
        AtomicBoolean listConfirmed = new AtomicBoolean(false);
        final String[] selectedItem = new String[1];

        // Type determination variables
        AtomicBoolean isMotor = new AtomicBoolean(false);
        AtomicBoolean isServo = new AtomicBoolean(false);
        AtomicBoolean isSensor = new AtomicBoolean(false);
        AtomicBoolean isSystem = new AtomicBoolean(false);

        // Main menu loop
        while (opModeIsActive() || (!isStopRequested() && menuActive.get())) {
            displayMainMenu(listConfirmed.get(), listSelection.get());

            // Handle gamepad input for both controllers
            handleMenuInput(gamepad1, listConfirmed, listSelection, selectedItem,
                    isMotor, isServo, isSensor, isSystem, menuActive);
            handleMenuInput(gamepad2, listConfirmed, listSelection, selectedItem,
                    isMotor, isServo, isSensor, isSystem, menuActive);

            telemetry.update();

            // Execute selected test
            if (!menuActive.get()) {
                try {
                    if (isMotor.get()) {
                        runMotorTest(selectedItem[0]);
                    } else if (isServo.get()) {
                        runServoTest(selectedItem[0]);
                    } else if (isSensor.get()) {
                        runSensorTest(selectedItem[0]);
                    } else if (isSystem.get()) {
                        runSystemTest(selectedItem[0]);
                    }
                } catch (Exception e) {
                    telemetry.addData("❌ ERROR", "Failed to test %s: %s", selectedItem[0], e.getMessage());
                    telemetry.update();
                    sleep(3000);
                }

                // Return to menu
                listConfirmed.set(false);
                menuActive.set(true);
            }
        }
    }

    // ==================== MENU HELPER METHODS ====================

    private void displayMainMenu(boolean listConfirmed, int selection) {
        telemetry.addLine("=== Comprehensive Testing Suite ===");
        telemetry.addLine("Navigate: D-Pad Up/Down | Select: A/Cross");

        if (!listConfirmed) {
            telemetry.addLine("\n📋 Select Component to Test:");
            MenuHelper.displayMenuOptions(telemetry, LIST_OPTIONS, selection);

            telemetry.addLine("\n🎮 Controls:");
            telemetry.addLine("  Motors: LT (reverse) / RT (forward)");
            telemetry.addLine("  Servos: LT (decrease) / RT (increase)");
        }
    }

    private void handleMenuInput(Gamepad gamepad,
                                 AtomicBoolean listConfirmed, AtomicInteger listSelection,
                                 String[] selectedItem, AtomicBoolean isMotor, AtomicBoolean isServo,
                                 AtomicBoolean isSensor, AtomicBoolean isSystem, AtomicBoolean menuActive) {

        MenuHelper.handleControllerInput(this, gamepad, !listConfirmed.get(), () -> {
            if (gamepad.dpad_up) {
                listSelection.set((listSelection.get() - 1 + LIST_OPTIONS.length) % LIST_OPTIONS.length);
            } else if (gamepad.dpad_down) {
                listSelection.set((listSelection.get() + 1) % LIST_OPTIONS.length);
            } else if (gamepad.a || gamepad.cross) {
                selectedItem[0] = LIST_OPTIONS[listSelection.get()];
                determineTestType(listSelection.get(), isMotor, isServo, isSensor, isSystem);
                listConfirmed.set(true);
                menuActive.set(false);
            }
        });
    }

    private void determineTestType(int selection, AtomicBoolean isMotor, AtomicBoolean isServo,
                                   AtomicBoolean isSensor, AtomicBoolean isSystem) {
        // Reset all flags
        isMotor.set(false);
        isServo.set(false);
        isSensor.set(false);
        isSystem.set(false);

        int motorEnd = MOTOR_OPTIONS.length + DUAL_MOTOR_OPTIONS.length;
        int servoEnd = motorEnd + SERVO_OPTIONS.length + DUAL_SERVO_OPTIONS.length;
        int sensorEnd = servoEnd + SENSOR_OPTIONS.length;

        if (selection < motorEnd) {
            isMotor.set(true);
        } else if (selection < servoEnd) {
            isServo.set(true);
        } else if (selection < sensorEnd) {
            isSensor.set(true);
        } else {
            isSystem.set(true);
        }
    }

    // ==================== MOTOR TESTING METHODS ====================

    private void runMotorTest(String motorId) {
        if (motorId.equals("DUAL_MOTOR_SLIDE_VERTICAL")) {
            runDualMotorTest();
        } else {
            runSingleMotorTest(motorId);
        }
    }

    private void runSingleMotorTest(String motorId) {
        DcMotor testMotor = hardwareMap.get(DcMotor.class, motorId);
        testMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        testMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addLine("🔧 Testing Motor: " + motorId);
        telemetry.addLine("Press START to begin");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            float power = calculateMotorPower();
            testMotor.setPower(power);

            telemetry.addLine("=== Motor Test: " + motorId + " ===");
            telemetry.addData("💪 Power", "%.2f", power);
            telemetry.addData("📍 Position", testMotor.getCurrentPosition());
            telemetry.addData("🎮 Controls", "LT: Reverse | RT: Forward");
            telemetry.addData("⏹️ Stop", "Press BACK to return to menu");

            if (gamepad1.back || gamepad2.back)
                break;
            telemetry.update();
        }
        testMotor.setPower(0);
    }

    private void runDualMotorTest() {
        DcMotor motor1 = hardwareMap.get(DcMotor.class, Settings.Hardware.IDs.SLIDE_VERTICAL_LEFT);
        DcMotor motor2 = hardwareMap.get(DcMotor.class, Settings.Hardware.IDs.SLIDE_VERTICAL_RIGHT);

        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addLine("🔧 Testing Dual Vertical Slides");
        telemetry.addLine("Press START to begin");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            float power = calculateMotorPower();
            motor1.setPower(power);
            motor2.setPower(power);

            telemetry.addLine("=== Dual Motor Test: Vertical Slides ===");
            telemetry.addData("💪 Power", "%.2f", power);
            telemetry.addData("📍 Left Position", motor1.getCurrentPosition());
            telemetry.addData("📍 Right Position", motor2.getCurrentPosition());
            telemetry.addData("🔄 Position Diff", Math.abs(motor1.getCurrentPosition() - motor2.getCurrentPosition()));
            telemetry.addData("🎮 Controls", "LT: Down | RT: Up");
            telemetry.addData("⏹️ Stop", "Press BACK to return to menu");

            if (gamepad1.back || gamepad2.back)
                break;
            telemetry.update();
        }
        motor1.setPower(0);
        motor2.setPower(0);
    }

    private float calculateMotorPower() {
        return -gamepad1.left_trigger - gamepad2.left_trigger + gamepad1.right_trigger + gamepad2.right_trigger;
    }

    // ==================== SERVO TESTING METHODS ====================

    private void runServoTest(String servoId) {
        if (servoId.equals("DUAL_SERVO_SHOULDERS")) {
            runDualServoTest();
        } else {
            runSingleServoTest(servoId);
        }
    }

    private void runSingleServoTest(String servoId) {
        Servo testServo = hardwareMap.get(Servo.class, servoId);
        double position = 0.5;
        testServo.setPosition(position);

        telemetry.addLine("🔧 Testing Servo: " + servoId);
        telemetry.addLine("Press START to begin");
        telemetry.update();
        waitForStart();

        boolean lastLeftTrigger = false;
        boolean lastRightTrigger = false;

        while (opModeIsActive()) {
            boolean currentLeftTrigger = gamepad1.left_trigger > 0.5 || gamepad2.left_trigger > 0.5;
            boolean currentRightTrigger = gamepad1.right_trigger > 0.5 || gamepad2.right_trigger > 0.5;

            if (currentLeftTrigger && !lastLeftTrigger) {
                position = Math.max(0, position - 0.05);
            } else if (currentRightTrigger && !lastRightTrigger) {
                position = Math.min(1, position + 0.05);
            }

            testServo.setPosition(position);
            lastLeftTrigger = currentLeftTrigger;
            lastRightTrigger = currentRightTrigger;

            telemetry.addLine("=== Servo Test: " + servoId + " ===");
            telemetry.addData("📍 Position", "%.3f", position);
            telemetry.addData("🎮 Controls", "LT: Decrease | RT: Increase");
            telemetry.addData("⏹️ Stop", "Press BACK to return to menu");

            if (gamepad1.back || gamepad2.back)
                break;
            telemetry.update();
        }
    }

    private void runDualServoTest() {
        Servo servo1 = hardwareMap.get(Servo.class, Settings.Hardware.IDs.LEFT_SHOULDER);
        Servo servo2 = hardwareMap.get(Servo.class, Settings.Hardware.IDs.RIGHT_SHOULDER);
        servo2.setDirection(Servo.Direction.REVERSE);

        double position = 0.5;
        servo1.setPosition(position);
        servo2.setPosition(position);

        telemetry.addLine("🔧 Testing Dual Shoulders");
        telemetry.addLine("Press START to begin");
        telemetry.update();
        waitForStart();

        boolean lastLeftTrigger = false;
        boolean lastRightTrigger = false;

        while (opModeIsActive()) {
            boolean currentLeftTrigger = gamepad1.left_trigger > 0.5 || gamepad2.left_trigger > 0.5;
            boolean currentRightTrigger = gamepad1.right_trigger > 0.5 || gamepad2.right_trigger > 0.5;

            if (currentLeftTrigger && !lastLeftTrigger) {
                position = Math.max(0, position - 0.05);
            } else if (currentRightTrigger && !lastRightTrigger) {
                position = Math.min(1, position + 0.05);
            }

            servo1.setPosition(position);
            servo2.setPosition(position);
            lastLeftTrigger = currentLeftTrigger;
            lastRightTrigger = currentRightTrigger;

            telemetry.addLine("=== Dual Servo Test: Shoulders ===");
            telemetry.addData("📍 Position", "%.3f", position);
            telemetry.addData("🎮 Controls", "LT: Decrease | RT: Increase");
            telemetry.addData("⏹️ Stop", "Press BACK to return to menu");

            if (gamepad1.back || gamepad2.back)
                break;
            telemetry.update();
        }
    }

    // ==================== SENSOR TESTING METHODS ====================

    private void runSensorTest(String sensorId) {
        if (sensorId.equals(Settings.Hardware.IDs.SLIDE_VERTICAL_TOUCH_SENSOR)) {
            runTouchSensorTest(sensorId);
        } else if (sensorId.contains("limelight") || sensorId.contains("LIMELIGHT")) {
            runLimelightTest(sensorId);
        } else if (sensorId.equals(Settings.Hardware.IDs.COLOR_SENSOR)) {
            runColorSensorTest();
        }
    }

    private void runTouchSensorTest(String sensorId) {
        RevTouchSensor touchSensor = hardwareMap.get(RevTouchSensor.class, sensorId);

        telemetry.addLine("🔧 Testing Touch Sensor");
        telemetry.addLine("Press START to begin");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            boolean isPressed = touchSensor.isPressed();

            telemetry.addLine("=== Touch Sensor Test ===");
            telemetry.addData("📱 Sensor State", isPressed ? "🔴 PRESSED" : "⚪ NOT PRESSED");
            telemetry.addData("⏹️ Stop", "Press BACK to return to menu");

            if (gamepad1.back || gamepad2.back)
                break;
            telemetry.update();
        }
    }

    private void runLimelightTest(String sensorId) {
        Limelight3A limelight = hardwareMap.get(Limelight3A.class, Settings.Hardware.IDs.LIMELIGHT);
        telemetry.setMsTransmissionInterval(10);

        // Determine pipeline based on detection type
        int pipeline = 1; // Default yellow
        if (sensorId.contains("red"))
            pipeline = 2;
        else if (sensorId.contains("blue"))
            pipeline = 3;

        limelight.pipelineSwitch(pipeline);
        limelight.start();
        limelight.setPollRateHz(100);

        String detectionType = sensorId.replace(Settings.Hardware.IDs.LIMELIGHT + " ", "").replace(" detection", "");

        telemetry.addLine("🔧 Testing Limelight - " + detectionType.toUpperCase());
        telemetry.addLine("Press START to begin");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();

            telemetry.addLine("=== Limelight Test: " + detectionType.toUpperCase() + " ===");
            telemetry.addData("🎯 Target X", "%.2f", result.getTx());
            telemetry.addData("🎯 Target Y", "%.2f", result.getTy());
            telemetry.addData("📏 Target Size", "%.1f%%", result.getTa() * 100);
            telemetry.addData("🔍 Valid Target", result.isValid() ? "✅ YES" : "❌ NO");
            telemetry.addData("⏹️ Stop", "Press BACK to return to menu");

            if (gamepad1.back || gamepad2.back)
                break;
            telemetry.update();
        }
    }

    private void runColorSensorTest() {
        RevColorSensorV3 colorSensor = hardwareMap.get(RevColorSensorV3.class, Settings.Hardware.IDs.COLOR_SENSOR);
        colorSensor.initialize();

        telemetry.addLine("🔧 Testing Color Sensor");
        telemetry.addLine("Press START to begin");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addLine("=== Color Sensor Test ===");
            telemetry.addData("📏 Distance", "%.1f cm", colorSensor.getDistance(DistanceUnit.CM));
            telemetry.addData("💡 Light Level", "%.3f", colorSensor.getLightDetected());
            telemetry.addData("🔴 Red", colorSensor.red());
            telemetry.addData("🟢 Green", colorSensor.green());
            telemetry.addData("🔵 Blue", colorSensor.blue());
            telemetry.addData("⏹️ Stop", "Press BACK to return to menu");

            if (gamepad1.back || gamepad2.back)
                break;
            telemetry.update();
        }
    }

    // ==================== SYSTEM TESTING METHODS ====================

    private void runSystemTest(String testType) {
        if (testType.equals("AUTOMATED_HARDWARE_CHECK")) {
            runAutomatedHardwareCheck();
        } else if (testType.equals("ENCODER_VALIDATION")) {
            runEncoderValidation();
        }
    }

    private void runAutomatedHardwareCheck() {
        telemetry.addLine("🔧 Automated Hardware Check");
        telemetry.addLine("Checking hardware configuration...");
        telemetry.update();

        // Check individual motors
        for (String motorId : MOTOR_OPTIONS) {
            if (validateHardware(motorId, DcMotor.class)) {
                telemetry.addData("✅ Motor", motorId);
            } else {
                telemetry.addData("❌ Motor", motorId + " - NOT FOUND");
            }
            telemetry.update();
        }

        // Check individual servos
        for (String servoId : SERVO_OPTIONS) {
            if (validateHardware(servoId, Servo.class)) {
                telemetry.addData("✅ Servo", servoId);
            } else {
                telemetry.addData("❌ Servo", servoId + " - NOT FOUND");
            }
            telemetry.update();
        }

        telemetry.addLine("\nPress START to run automated tests");
        telemetry.update();
        waitForStart();

        // Run automated motor tests
        for (String motorId : MOTOR_OPTIONS) {
            if (!opModeIsActive())
                return;
            if (validateHardware(motorId, DcMotor.class)) {
                runAutomatedMotorTest(motorId);
            }
        }

        // Run automated servo tests
        for (String servoId : SERVO_OPTIONS) {
            if (!opModeIsActive())
                return;
            if (validateHardware(servoId, Servo.class)) {
                runAutomatedServoTest(servoId);
            }
        }

        telemetry.addLine("\n=== Automated Check Complete ===");
        telemetry.addLine("All components have been tested");
        telemetry.addLine("Press BACK to return to menu");
        telemetry.update();

        while (opModeIsActive() && !gamepad1.back && !gamepad2.back) {
            idle();
        }
    }

    private void runEncoderValidation() {
        telemetry.addLine("🔧 Encoder Validation Test");
        telemetry.addLine("Press START to begin");
        telemetry.update();
        waitForStart();

        for (String motorId : MOTOR_OPTIONS) {
            if (!opModeIsActive())
                return;
            if (validateHardware(motorId, DcMotor.class)) {
                validateMotorEncoder(motorId);
            }
        }

        telemetry.addLine("\n=== Encoder Validation Complete ===");
        telemetry.addLine("Press BACK to return to menu");
        telemetry.update();

        while (opModeIsActive() && !gamepad1.back && !gamepad2.back) {
            idle();
        }
    }

    // ==================== UTILITY METHODS ====================

    private boolean validateHardware(String id, Class<?> hardwareClass) {
        try {
            hardwareMap.get(hardwareClass, id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void runAutomatedMotorTest(String motorId) {
        telemetry.addLine("\n=== Testing Motor: " + motorId + " ===");
        telemetry.update();

        try {
            DcMotor motor = hardwareMap.get(DcMotor.class, motorId);
            motor.setDirection(DcMotor.Direction.FORWARD);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            // Test forward
            telemetry.addData("Testing", "Forward (0.3 power)");
            telemetry.update();
            motor.setPower(0.3);
            sleep(1000);
            motor.setPower(0);

            // Test reverse
            telemetry.addData("Testing", "Reverse (-0.3 power)");
            telemetry.update();
            motor.setPower(-0.3);
            sleep(1000);
            motor.setPower(0);

            telemetry.addData("✅", "Motor test complete");
            telemetry.update();
            sleep(500);

        } catch (Exception e) {
            telemetry.addData("❌ ERROR", "Motor test failed: " + e.getMessage());
            telemetry.update();
            sleep(1000);
        }
    }

    private void runAutomatedServoTest(String servoId) {
        telemetry.addLine("\n=== Testing Servo: " + servoId + " ===");
        telemetry.update();

        try {
            Servo servo = hardwareMap.get(Servo.class, servoId);
            servo.setDirection(Servo.Direction.FORWARD);

            // Test positions
            double[] positions = {0.0, 0.5, 1.0, 0.5};
            for (double position : positions) {
                telemetry.addData("Testing", "Position: %.2f", position);
                telemetry.update();
                servo.setPosition(position);
                sleep(1000);
            }

            telemetry.addData("✅", "Servo test complete");
            telemetry.update();
            sleep(500);

        } catch (Exception e) {
            telemetry.addData("❌ ERROR", "Servo test failed: " + e.getMessage());
            telemetry.update();
            sleep(1000);
        }
    }

    private void validateMotorEncoder(String motorId) {
        telemetry.addLine("\n=== Validating Encoder: " + motorId + " ===");
        telemetry.update();

        try {
            DcMotor motor = hardwareMap.get(DcMotor.class, motorId);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            sleep(100);
            int initialPosition = motor.getCurrentPosition();

            // Run motor briefly
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setPower(0.3);
            sleep(500);
            motor.setPower(0);

            int finalPosition = motor.getCurrentPosition();
            int encoderChange = Math.abs(finalPosition - initialPosition);

            if (encoderChange < 10) {
                telemetry.addData("❌ WARNING", "Encoder may not be working");
                telemetry.addData("Change", encoderChange + " ticks");
            } else {
                telemetry.addData("✅ ENCODER OK", "Change: " + encoderChange + " ticks");
            }
            telemetry.update();
            sleep(1000);

        } catch (Exception e) {
            telemetry.addData("❌ ERROR", "Encoder validation failed: " + e.getMessage());
            telemetry.update();
            sleep(1000);
        }
    }
}