package org.nknsd.teamcode.components.handlers;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNComponent;

public class WufSpotter implements NKNComponent {

    boolean wufExist;
    double dist; // This is here so we can tele it
    private SparkFunOTOS.Pose2D objectPos = new SparkFunOTOS.Pose2D(0, 0, 0);

    final VisionHandler visionHandler;
    final MotorDriver motorDriver;
    final FlowHandler flowHandler;

    public WufSpotter(VisionHandler visionHandler, MotorDriver motorDriver, FlowHandler flowHandler) {
        this.visionHandler = visionHandler;
        this.motorDriver = motorDriver;
        this.flowHandler = flowHandler;
    }

    public SparkFunOTOS.Pose2D getObjectPos() {
        return new SparkFunOTOS.Pose2D(objectPos.x, objectPos.y, objectPos.h);
    }

    public boolean doesWufExist() {
        return wufExist;
    }

    public double areaToDist(double area) {
//        in wuf units, will need to be changed if used again
        return -4.5 * area + 30;
    }

    public double yToDist(double y) {
        return (0.01402119 * y * y * y + 0.62896063 * y * y + 12.23049798 * y + 158.73678478);
    }


    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "";
    }


    double maxTime = 0;
    double currentTime = 0;
    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        VisionHandler.VisionResult object = visionHandler.findObjectPos();
        double checkTime = runtime.milliseconds();
        if (object == null) {
            wufExist = false;
            return;
        }
        currentTime = runtime.milliseconds()-checkTime;
        if(currentTime > maxTime){
            maxTime = currentTime;
        }

        SparkFunOTOS.Pose2D currentPos = flowHandler.getPosition();

        dist= yToDist(object.yAngle)/2.54;
        objectPos.h = (object.xAngle * Math.PI / 180) + currentPos.h;
        double x = Math.sin(objectPos.h) * dist;
        double y = Math.cos(objectPos.h) * dist;
        objectPos.x = x + currentPos.x;
        objectPos.y = y + currentPos.y;

        wufExist = true;
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        if (wufExist) {
            telemetry.addData("wuf x", objectPos.x);
            telemetry.addData("wuf y", objectPos.y);
            telemetry.addData("wuf h", objectPos.h);
            telemetry.addData("wuf dist", dist);
        } else {
            telemetry.addData("wuf x", "none");
            telemetry.addData("wuf y", "none");
            telemetry.addData("wuf h", "none");
            telemetry.addData("wuf dist", "none");
        }

//        telemetry.addData("longest time", maxTime);
//        telemetry.addData("current time", currentTime);
    }
}
