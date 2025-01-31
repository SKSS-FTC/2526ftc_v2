package org.nknsd.teamcode.helperClasses;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.ExtensionHandler;
import org.nknsd.teamcode.components.handlers.SpecimenClawHandler;
import org.nknsd.teamcode.components.handlers.SpecimenExtensionHandler;
import org.nknsd.teamcode.components.handlers.SpecimenRotationHandler;
import org.nknsd.teamcode.components.sensors.DistSensor;
import org.nknsd.teamcode.components.sensors.FlowSensor;
import org.nknsd.teamcode.components.sensors.IMUSensor;
import org.nknsd.teamcode.components.handlers.IntakeSpinnerHandler;
import org.nknsd.teamcode.components.handlers.RotationHandler;
import org.nknsd.teamcode.components.handlers.WheelHandler;
import org.nknsd.teamcode.helperClasses.PIDModel;

public class AutoSkeleton {
    private double maxSpeed, minSpeed;                  // Maximum speed the robot can move at
    private final double movementMargin;            // Margin determines how close to the target we have to be before we are there
    private final double turnMargin;
    private WheelHandler wheelHandler;              // Class which handles wheel motions
    private FlowSensor flowSensor;    // Class which gives us our position
    public double[] targetPositions = new double[2];// Array which holds our target position as x, y
    static final double TILE_LENGTH = 20.8;
    private IMUSensor imuSensor;
    private RotationHandler rotationHandler;
    private ExtensionHandler extensionHandler;
    private SpecimenExtensionHandler specimenExtensionHandler;
    private SpecimenRotationHandler specimenRotationHandler;
    private SpecimenClawHandler specimenClawHandler;
    private DistSensor sensorForDist;
    private DistSensor sensorBackDist;
    private double targetRotation = 0;
    private IntakeSpinnerHandler intakeSpinnerHandler;
    private double[] offset;
    private double headingOffset;


    public AutoSkeleton(double maxSpeed, double minSpeed, double movementMargin, double turnMargin) {
        this.maxSpeed = maxSpeed;
        this.movementMargin = movementMargin;
        this.turnMargin = turnMargin;
        this.minSpeed = minSpeed;

        // Default offset
        offset = new double[]{0, 0};
    }

    public AutoSkeleton(double maxSpeed, double movementMargin, double turnMargin) {
        this(maxSpeed, 0.2, movementMargin, turnMargin);
    }

    public void link(WheelHandler wheelHandler, RotationHandler rotationHandler, ExtensionHandler extensionHandler, IntakeSpinnerHandler intakeSpinnerHandler, FlowSensor flowSensor, IMUSensor imuSensor) {
        this.wheelHandler = wheelHandler;
        this.flowSensor = flowSensor;
        this.imuSensor = imuSensor;
        this.rotationHandler = rotationHandler;
        this.extensionHandler = extensionHandler;
        this.intakeSpinnerHandler = intakeSpinnerHandler;
    }

    public void specimenLink(SpecimenExtensionHandler specimenExtensionHandler, SpecimenRotationHandler specimenRotationHandler, SpecimenClawHandler specimenClawHandler) {
        this.specimenExtensionHandler = specimenExtensionHandler;
        this.specimenRotationHandler = specimenRotationHandler;
        this.specimenClawHandler = specimenClawHandler;
    }

    public void distSensorLink(DistSensor sensorForDist, DistSensor sensorBackDist) {
        this.sensorForDist = sensorForDist;
        this.sensorBackDist = sensorBackDist;
    }

    public void setOffset(double[] posOffset, double headingOffset) { //Requires an array of size 2 and a heading within reasonable values
        offset = posOffset;
        this.headingOffset = (headingOffset * Math.PI) / 180;
    }

    public void setTargetPosition(double x, double y) {
        targetPositions[0] = x; targetPositions[1] = y;
    }

    public void setTargetRotation(double turning) {
        targetRotation = turning;
    }

    public void setTargetArmRotation(RotationHandler.RotationPositions rotationPosition) {
        rotationHandler.setTargetRotationPosition(rotationPosition);
    }

    public void setTargetArmExtension(ExtensionHandler.ExtensionPositions extensionPosition) {
        extensionHandler.gotoPosition(extensionPosition);
    }

    public boolean isExtensionDone() {
        return extensionHandler.isExtensionDone();
    }
    public boolean isSpecExtensionDone() {
        return specimenExtensionHandler.isExtensionDone();
    }

    /* DISABLED OLD PID CODE BECAUSE MAYBE P BETTER?

    public boolean runToPosition(Telemetry telemetry, ElapsedTime runtime) {
        // Get position
        FlowSensor.PoseData pos = flowSensor.getOdometryData().pos;
        double x = pos.x + offset[0];
        double y = pos.y + offset[1];
        double yaw = imuSensor.getYaw();
        //btw, heading offset gets applied again later :)
        x = (Math.cos(headingOffset) * x) - (Math.sin(headingOffset) * y);
        y = (Math.sin(headingOffset) * x) + (Math.cos(headingOffset) * y);

        telemetry.addData("Cur X", x);
        telemetry.addData("Cur Y", y);


        // Calculating distance
        double xTarg = targetPositions[0] * TILE_LENGTH;
        double yTarg = targetPositions[1] * TILE_LENGTH;

        double xDist = (xTarg - x);
        double yDist = (yTarg - y);
        double turnDist = targetRotation - yaw;
        double dist = Math.sqrt((xDist * xDist) + (yDist * yDist));

        telemetry.addData("Targ X", xTarg);
        telemetry.addData("Targ Y", yTarg);


        // Check if we're at our target
        if (dist <= movementMargin && Math.abs(turnDist) <= turnMargin) {
            wheelHandler.absoluteVectorToMotion(0, 0, 0, 0, telemetry);
            telemetry.addData("Done?", "Done");
            return true;
        }


        // Calculate force to use
        double xSpeed = 0;
        double ySpeed = 0;
        double turnSpeed = 0;
        if (Math.abs(xDist) > movementMargin / 1.3) { // we need to reduce movement margin to account for the rare scenarios when x & y are both within margin but combined they are not
            if (xDist > 0 ^ xDirPos) {
                movementPIDx.resetError();
                //xDirPos = !xDirPos;
            }

            telemetry.addData("PID DATA", "x");
            xSpeed = movementPIDx.calculateWithTelemetry(x, xTarg, runtime, telemetry);
        } else {
            telemetry.addData("X", "Done");
        }


        if (Math.abs(yDist) > movementMargin / 1.3) {
            if (yDist > 0 ^ yDirPos) {
                movementPIDy.resetError();
                //yDirPos = !yDirPos;
            }

            telemetry.addData("PID DATA", "y");
            ySpeed = movementPIDy.calculateWithTelemetry(y, yTarg, runtime, telemetry);
        } else {
            telemetry.addData("Y", "Done");
        }

        if (Math.abs(turnDist) > turnMargin) {
            if (turnDist > 0 ^ turnDirPos) {
                movementPIDturn.resetError();
                //yDirPos = !yDirPos;
            }

            telemetry.addData("PID DATA", "turn");
            turnSpeed = movementPIDturn.calculateWithTelemetry(yaw, targetRotation, runtime, telemetry);
        } else {
            telemetry.addData("Turn", "Done");
        }

        // Ok now we apply headingOffset
        // If we applied the offset to our position, it'd be.. bad..
        // It'd think that the one meter we travelled was 1 meter in the other direction
        // This will hopefully work
        // Apply rotation offset
        xSpeed = (Math.cos(headingOffset) * xSpeed) - (Math.sin(headingOffset) * ySpeed);
        ySpeed = (Math.sin(headingOffset) * xSpeed) + (Math.cos(headingOffset) * ySpeed);

        xSpeed = Math.max(Math.min(xSpeed, maxSpeed), -maxSpeed);
        ySpeed = Math.max(Math.min(ySpeed, maxSpeed), -maxSpeed);
        turnSpeed = Math.max(Math.min(turnSpeed, maxSpeed), -maxSpeed);

        telemetry.addData("Speed X", xSpeed);
        telemetry.addData("Speed Y", ySpeed);
        telemetry.addData("Speed Turning", turnSpeed);


        // Run motors
        wheelHandler.absoluteVectorToMotion(xSpeed, ySpeed, turnSpeed, yaw, telemetry);

        return false;
    }
     DISABLED OLD PID CODE BECAUSE MAYBE P BETTER? */

    private double clamp(double val, double min, double max) {
        double result = val;
        if (Math.abs(val) < min) {
            result = min * (val / Math.abs(val)); // Multiply minimum by the sign of val (val / |val| = 1 or -1, depending on if val is pos or not)
        } else if (Math.abs(val) > max) {
            result = max * (val / Math.abs(val)); // Same logic
        }

        return result;
    }

    public boolean runToPosition(Telemetry telemetry, ElapsedTime runtime) {
        // Step 1: Get Position
        FlowSensor.PoseData pos = flowSensor.getOdometryData().pos;
        double x = pos.x + offset[0];
        double y = pos.y + offset[1];
        double yaw = imuSensor.getYaw();
        //btw, heading offset gets applied again later :)
        x = (Math.cos(headingOffset) * x) - (Math.sin(headingOffset) * y);
        y = (Math.sin(headingOffset) * x) + (Math.cos(headingOffset) * y);

        telemetry.addData("Cur X", x);
        telemetry.addData("Cur Y", y);


        // Step 2: Calculating distance
        double xTarg = targetPositions[0] * TILE_LENGTH;
        double yTarg = targetPositions[1] * TILE_LENGTH;

        double xDist = (xTarg - x);
        double yDist = (yTarg - y);
        double turnDist = targetRotation - yaw;
        double dist = Math.sqrt((xDist * xDist) + (yDist * yDist));

        telemetry.addData("Targ X", xTarg);
        telemetry.addData("Targ Y", yTarg);

        telemetry.addData("X Dist", xDist);
        telemetry.addData("Y Dist", yDist);
        telemetry.addData("Turn Dist", turnDist);


        // Step 3: Check if we're within distance
        if (dist <= movementMargin && Math.abs(turnDist) <= turnMargin) {
            wheelHandler.absoluteVectorToMotion(0, 0, 0, 0, telemetry);
            telemetry.addData("Done?", "Done");
            return true;
        }

        // Step 4: Calculate force to use
        // Ok now we apply headingOffset
        // If we applied the offset to our position, it'd be.. bad..
        // It'd think that the one meter we travelled was 1 meter in the other direction
        // This will hopefully work
        // Apply rotation offset

        double xSpeed = 0;
        double ySpeed = 0;
        double turnSpeed = 0;
        if (Math.abs(xDist) > movementMargin / 1.3) { // we need to reduce movement margin to account for the rare scenarios when x & y are both within margin but combined they are not
            xSpeed = xDist * (maxSpeed / TILE_LENGTH) * (6 / 3); // This function makes xSpeed reach maxSpeed at 3/4 tile length away
            xSpeed = (Math.cos(headingOffset) * xSpeed) - (Math.sin(headingOffset) * ySpeed); // Rotate based off heading's OFFSET
            xSpeed = clamp(xSpeed, minSpeed, maxSpeed); // Clamp to acceptable values

        } else {
            telemetry.addData("X", "Done");
        }


        if (Math.abs(yDist) > movementMargin / 1.3) {
            ySpeed = yDist * (maxSpeed / TILE_LENGTH) * (4 / 3);
            ySpeed = (Math.sin(headingOffset) * xSpeed) + (Math.cos(headingOffset) * ySpeed);
            ySpeed = clamp(ySpeed, minSpeed, maxSpeed);

        } else {
            telemetry.addData("Y", "Done");
        }


        if (Math.abs(turnDist) > turnMargin) {
            turnSpeed = turnDist * (maxSpeed / 90) * (4 / 3); // We arbitrarily scale turnSpeed to 90*
            turnSpeed = clamp(turnSpeed, minSpeed, maxSpeed);

        } else {
            telemetry.addData("Turn", "Done");
        }



        telemetry.addData("Speed X", xSpeed);
        telemetry.addData("Speed Y", ySpeed);
        telemetry.addData("Speed Turning", turnSpeed);

        telemetry.addData("Max Speed", maxSpeed);
        telemetry.addData("Min Speed", minSpeed);

        // Run motors
        wheelHandler.absoluteVectorToMotion(xSpeed, ySpeed, turnSpeed, yaw, telemetry);

        return false;
    }

    public void runMotorsDirectly(double y, double x, double turning) {
        wheelHandler.relativeVectorToMotion(y, x, turning);
    }

    public void freeze() {
        wheelHandler.relativeVectorToMotion(0, 0, 0);
    }

    public void setServoPower(IntakeSpinnerHandler.HandStates handState) {
        intakeSpinnerHandler.setServoPower(handState);
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setMinSpeed(double minSpeed) {
        this.minSpeed = minSpeed;
    }


    public void relativeRun(double x, double y) {
        wheelHandler.relativeVectorToMotion(y, x, 0);
    }

    public void setTargetSpecArmExtension(SpecimenExtensionHandler.SpecimenExtensionPositions extensionPosition) {
        specimenExtensionHandler.gotoPosition(extensionPosition, true);
    }

    public void setSpecimenClawTarget(SpecimenClawHandler.ClawPositions clawPosition) {
        specimenClawHandler.setClawPosition(clawPosition);
    }

    public void setSpecimenRotationTarget(SpecimenRotationHandler.SpecimenRotationPositions rotationPosition) {
        specimenRotationHandler.goToPosition(rotationPosition);
    }

    public double getSensorForDist() {
        return sensorForDist.getDistance();
    }

    public double getSensorBackDist() {
        return sensorBackDist.getDistance();
    }
}
