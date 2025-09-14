package org.nknsd.teamcode.states;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.MotorDriver;
import org.nknsd.teamcode.components.handlers.statemachine.StateMachine;

import java.util.logging.Logger;

public class RobotPosWithin extends StateMachine.State {

    Logger logger = Logger.getLogger(RobotPosWithin.class.getName());

    final MotorDriver motorDriver;
    final double angleError;
    final double speedError;
    final double xError;
    final double yError;
    String[] toStop;
    String[] toStart;

    // If one of the errors is not necessary (like separate x and y errors), just set as a very large number.
    public RobotPosWithin(MotorDriver motorDriver,  double angleError, double speedError, double xError, double yError, String[] toStart, String[] toStop) {
        this.motorDriver = motorDriver;
        this.angleError = angleError;
        this.speedError = speedError;
        this.xError = xError;
        this.yError = yError;
        this.toStop = toStop;
        this.toStart = toStart;
    }

    //    velocity is inches per second and radians per second

    private boolean isWithin(double currentError, double allowedError) {
        return (Math.abs(currentError) < allowedError);
    }

    @Override
    protected void run(ElapsedTime runtime, Telemetry telemetry) {
        SparkFunOTOS.Pose2D delta = motorDriver.getError();
        SparkFunOTOS.Pose2D vel = motorDriver.getVelocity();


        boolean angleCheck = false;
        boolean speedCheck = false;
        boolean xyCheck = false;

        if (isWithin(delta.h, angleError)) {
            angleCheck = true;
        }
        if (isWithin(vel.x, speedError) && isWithin(vel.y, speedError) && isWithin(vel.h, speedError)) {
            speedCheck = true;
        }
        if (isWithin(delta.x, xError) && isWithin(delta.y, yError)) {
            xyCheck = true;
        }

//        logger.info("DC:" + distCheck + " SC:" + speedCheck);

        if (angleCheck && speedCheck && xyCheck) {
            stateMachine.stopAnonymous(this);
        }
    }

    @Override
    protected void started() {

    }

    @Override
    protected void stopped() {
        for (String stateName : this.toStop) {
            stateMachine.stopState(stateName);
        }
        for (String stateName : this.toStart) {
            stateMachine.startState(stateName);
        }
    }
}
