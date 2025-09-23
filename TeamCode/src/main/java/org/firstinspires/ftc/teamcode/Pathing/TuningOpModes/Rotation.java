package org.firstinspires.ftc.teamcode.Pathing.TuningOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Implementations.AndroidLog;
import org.firstinspires.ftc.teamcode.Implementations.LinearOpModeImpl;
import org.firstinspires.ftc.teamcode.Implementations.SystemTimeSource;
import org.firstinspires.ftc.teamcode.Interfaces.HardwareI;
import org.firstinspires.ftc.teamcode.Pathing.PathController;
import org.firstinspires.ftc.teamcode.Pathing.PidParamCollection;
import org.firstinspires.ftc.teamcode.Pathing.PidParams;
import org.firstinspires.ftc.teamcode.RobotHardware.Hardware;

import java.util.zip.Adler32;

@TeleOp
public class Rotation extends LinearOpMode {
    double kp, ki, kd;

    @Override
    public void runOpMode() throws InterruptedException {

        HardwareI hw = new Hardware(hardwareMap);
        hw.resetImu();
        kp = 0.02; // Initial guess.  If 10 degrees away set power to .2?
        ki = 0.0;
        kd = 0.0;

        waitForStart();

        double targetDeg = 90.0;
        LinearOpModeImpl ourOp = new LinearOpModeImpl(this);

        PathController pc = new PathController(
                hw, ourOp, 0.5,
                PidParamCollection.ParamSetName.UNIT_TEST_SIM,
                new SystemTimeSource(), new AndroidLog());
        pc.setTargetHeadingDeg(targetDeg);
        while (!this.isStopRequested()) {
            hw.updateImuPos();
            pc.drive();
            adjustValues();
            pc.setRotPidCoeff(kp, ki, kd);
            double heading = hw.getImuHeading(AngleUnit.DEGREES);

            // 1. Start with kp.
            // 2. adjust ki for steady state error.
            // 3. adjust kd to reduce overshoot.
            pc.updateTelemetry();
//            TelemetryHelper.UpdateTelemetry(telemetry,
//                    "target", targetDeg,
//                    "heading", heading,
//                    "kp - circle", kp,
//                    "ki - square ", ki,
//                    "kd - cross", kd);

            if (gamepad1.left_bumper) {
                targetDeg -= -1;
                pc.setTargetHeadingDeg(targetDeg);
            }
        }
    }

    private void adjustValues() {
        if (gamepad1.circle) {
            kp = gamepadSet(kp);
        } else if (gamepad1.square) {
            ki = gamepadSet(ki);
        } else if (gamepad1.cross) {
            kd = gamepadSet(kd);
        }
    }

    private double gamepadSet(double value) {

        if (gamepad1.dpad_up)
            value += 0.1;
        else if (gamepad1.dpad_down)
            value -= 0.1;
        return value;
    }
}
