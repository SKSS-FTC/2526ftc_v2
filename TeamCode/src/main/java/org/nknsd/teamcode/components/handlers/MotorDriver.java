package org.nknsd.teamcode.components.handlers;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.feedbackcontroller.ControlLoop;
import org.nknsd.teamcode.frameworks.NKNComponent;

public class MotorDriver implements NKNComponent {

    private final FlowHandler flowHandler;
    private final MotorHandler motorHandler;

    private ControlLoop xControlLoop;
    public void setxControlLoop(ControlLoop xControlLoop) {
        this.xControlLoop = xControlLoop;
    }

    private  ControlLoop yControlLoop;
    public void setyControlLoop(ControlLoop yControlLoop) {
        this.yControlLoop = yControlLoop;
    }

    private  ControlLoop hControlLoop;
    public void sethControlLoop(ControlLoop hControlLoop) {
        this.hControlLoop = hControlLoop;
    }

    private SparkFunOTOS.Pose2D target = new SparkFunOTOS.Pose2D(0, 0, 0);
    private SparkFunOTOS.Pose2D lastPos = new SparkFunOTOS.Pose2D(0, 0, 0);

    SparkFunOTOS.Pose2D error = new SparkFunOTOS.Pose2D(0, 0, 0);

    public SparkFunOTOS.Pose2D getErrorDelta() {
        return errorDelta;
    }

    SparkFunOTOS.Pose2D errorDelta = new SparkFunOTOS.Pose2D(0, 0, 0);

    private SparkFunOTOS.Pose2D velocity = new SparkFunOTOS.Pose2D(0, 0, 0);
    private double lastTime = 0;


    public SparkFunOTOS.Pose2D getError() {
        return error;
    }
    public SparkFunOTOS.Pose2D getVelocity() {
        return velocity;
    }


    public MotorDriver(FlowHandler flowHandler, MotorHandler motorHandler, ControlLoop xControlLoop, ControlLoop yControlLoop, ControlLoop hControlLoop) {
        this.flowHandler = flowHandler;
        this.motorHandler = motorHandler;
        this.xControlLoop = xControlLoop;
        this.yControlLoop = yControlLoop;
        this.hControlLoop = hControlLoop;
    }



    public void setTarget(SparkFunOTOS.Pose2D target) {
        this.target = target;
        error = calcDelta(target,lastPos);
    }

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {
        lastTime = runtime.milliseconds();
    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "MotorDriver";
    }

    private void getRelativeSpeeds(SparkFunOTOS.Pose2D speeds, double theta) {
        double a1 = Math.cos(theta) * speeds.x;
        double a2 = Math.sin(theta) * -speeds.y;
        double o1 = Math.sin(theta) * speeds.x;
        double o2 = Math.cos(theta) * speeds.y;


        speeds.x = (a1 + a2);
        speeds.y = (o1 + o2);
    }

    private SparkFunOTOS.Pose2D calcDelta(SparkFunOTOS.Pose2D target,SparkFunOTOS.Pose2D current){
        return new SparkFunOTOS.Pose2D(
                target.x - current.x,
                target.y - current.y,
                (target.h - current.h + 3 * Math.PI) % (2 * Math.PI) - Math.PI
        );

    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {

        SparkFunOTOS.Pose2D current = flowHandler.getPosition();
        current.h = current.h % (2 * Math.PI);
        SparkFunOTOS.Pose2D oldError = error;
        error = calcDelta(target,current);
        errorDelta = calcDelta(error, oldError);


        double interval = runtime.milliseconds() - lastTime;
        velocity = new SparkFunOTOS.Pose2D(
                (current.x - lastPos.x)*1000/interval,
                (current.y - lastPos.y)*1000/interval,
                ((current.h - lastPos.h + 3 * Math.PI) % (2 * Math.PI) - Math.PI)*1000/interval
        );

        SparkFunOTOS.Pose2D output = new SparkFunOTOS.Pose2D(
                xControlLoop.findOutput(error.x, errorDelta.x, velocity.x,interval),
                yControlLoop.findOutput(error.y, errorDelta.y, velocity.y, interval),
                hControlLoop.findOutput(error.h, errorDelta.h, velocity.h,interval)
        );

        lastTime = runtime.milliseconds();
        lastPos = current;

        getRelativeSpeeds(output, current.h);

        motorHandler.setPowers(output.x, output.y, output.h);
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        telemetry.addData("target x", target.x);
        telemetry.addData("target y", target.y);
        telemetry.addData("target h", target.h);
    }


}
