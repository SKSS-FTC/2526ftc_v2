package org.nknsd.teamcode.components.handlers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNComponent;

import java.util.concurrent.TimeUnit;

public class SpecimenExtensionHandler implements NKNComponent {
    private SpecimenClawHandler clawHandler;
    private SpecimenRotationHandler rotationHandler;
    private final String extenderName = "motorSpecimenExtend";
    private final boolean doInvertMotor = true;
    private final double motorPower = 1;
    private DcMotor motor;
    int extenderPrevious = 0;
    private double lastResetAttempt = 200;
    private Telemetry telemetry;

    private SpecimenExtensionPositions target = SpecimenExtensionPositions.RESTING;

    public boolean isExtensionDone() {
        return (Math.abs(motor.getCurrentPosition() - target.position) <= 15);
    }

    public enum SpecimenExtensionPositions {
        RESTING(0),
        SPECIMEN_READY(1220),
        SPECIMEN_CLIP(2270);

        final int position;

        SpecimenExtensionPositions(int position) {
            this.position = position;
        }
    }
    
    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        motor = hardwareMap.dcMotor.get(extenderName);
        if (doInvertMotor) {
            motor.setDirection(DcMotor.Direction.REVERSE);
        }

        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {
        //motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //this was the line you were supposed to cut!!
        motor.setPower(motorPower);
        motor.setTargetPosition(SpecimenExtensionPositions.RESTING.position);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "SpecimenExtensionHandler";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        double RESET_DELAY = 400; //adjusts delay
        if ((runtime.now(TimeUnit.MILLISECONDS) - RESET_DELAY) > lastResetAttempt && target == SpecimenExtensionPositions.RESTING && motor.getCurrentPosition() <= 600) {
                if (motor.getCurrentPosition() == extenderPrevious) {
                    motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                }
                extenderPrevious = motor.getCurrentPosition();
            lastResetAttempt = runtime.now(TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        telemetry.addData("Ext Current Position", motor.getCurrentPosition());
        telemetry.addData("Ext Target Position", motor.getTargetPosition());
        telemetry.addData("Ext State", target.name());
    }

    // There's a return boolean here because it's a copy of the other extension handler's code.
    // It currently isn't used, but if we want to have skippable extensionPositions values we'll need it
    public boolean gotoPosition(SpecimenExtensionHandler.SpecimenExtensionPositions specimenExtensionPosition) {
        // This code sucks. When I eventually get around to fixing things, FIX THIS.
        // God I am way too busy
        if (clawHandler.firstClosedPosition == null) {
            motor.setTargetPosition(specimenExtensionPosition.position);
            target = specimenExtensionPosition;
            return true;

        } else if (clawHandler.firstClosedPosition != rotationHandler.targetPosition()) {
            motor.setTargetPosition(specimenExtensionPosition.position);
            target = specimenExtensionPosition;
            return true;

        } else {
            return false;
        }
    }

    public boolean gotoPosition(SpecimenExtensionHandler.SpecimenExtensionPositions specimenExtensionPosition, boolean doSkipSafety) {
        //HELP
        // HELP
        if (clawHandler.firstClosedPosition == null || doSkipSafety) {
            motor.setTargetPosition(specimenExtensionPosition.position);
            target = specimenExtensionPosition;
            return true;

        } else if (clawHandler.firstClosedPosition != rotationHandler.targetPosition()) {
            motor.setTargetPosition(specimenExtensionPosition.position);
            target = specimenExtensionPosition;
            return true;

        } else {
            return false;
        }
    }

    public SpecimenExtensionPositions targetPosition() {
        return target;
    }

    public void link(SpecimenClawHandler clawHandler, SpecimenRotationHandler rotationHandler) {
        this.clawHandler = clawHandler; this.rotationHandler = rotationHandler;
    }

    // DO NOT RUN UNLESS YOU ARE CONFIDENT
    public void resetEncoder() {
        //reset encoder
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //go to new position
        target = SpecimenExtensionPositions.RESTING;
        motor.setTargetPosition(target.position);

        //run to the current position :D
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
