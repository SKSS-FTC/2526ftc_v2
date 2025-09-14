package org.nknsd.teamcode.states;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.MotorDriver;
import org.nknsd.teamcode.components.handlers.statemachine.StateMachine;

public class DriveToPosState extends StateMachine.State {

    private final MotorDriver motorDriver;
    private SparkFunOTOS.Pose2D target;

    public DriveToPosState(SparkFunOTOS.Pose2D target, MotorDriver motorDriver) {
        this.target = target;
        this.motorDriver = motorDriver;
    }

    public void setTarget(SparkFunOTOS.Pose2D target){
        this.target = target;
    }

    @Override
    protected void run(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    protected void started() {
        motorDriver.setTarget(target);
    }

    @Override
    protected void stopped() {

    }
}
