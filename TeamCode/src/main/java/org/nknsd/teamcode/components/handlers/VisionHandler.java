package org.nknsd.teamcode.components.handlers;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.nknsd.teamcode.frameworks.NKNComponent;

public class VisionHandler implements NKNComponent {


    public VisionHandler(int pipeline) {
        this.pipeline = pipeline;
    }

    public static class VisionResult {
        public final double xAngle, yAngle, size;

        public VisionResult(double xAngle, double yAngle, double size) {
            this.xAngle = xAngle;
            this.yAngle = yAngle;
            this.size = size;
        }
    }

    private Limelight3A limelight;

    private int pipeline;

    private Rev2mDistanceSensor distSensor;


    private VisionResult lastResult;
    private double dist;

    public VisionResult findObjectPos() {
        LLResult result = limelight.getLatestResult();
        if (limelight.getLatestResult().isValid()) {
            lastResult = new VisionResult(result.getTx(), result.getTy(), result.getTa());
        } else {
            lastResult = null;
        }
        return lastResult;
    }

    public double findDist(){
        dist = distSensor.getDistance(DistanceUnit.INCH);
        return dist;
    }

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        if (limelight == null){
            throw new NullPointerException("No Limelight Camera Detected");
        }
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start(); // This tells Limelight to start looking!distSensor = hardwareMap.get(Rev2mDistanceSensor.class,"distanceSensor");
        distSensor = hardwareMap.get(Rev2mDistanceSensor.class,"distanceSensor");
        distSensor.initialize();

        return limelight.pipelineSwitch(pipeline); // Switch pipeline
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
        return "VisionHandler";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        if (lastResult == null) {
            telemetry.addData("object x","none");
            telemetry.addData("object y", "none");
            telemetry.addData("object size", "none");
        } else {
            telemetry.addData("object x", lastResult.xAngle);
            telemetry.addData("object y", lastResult.yAngle);
            telemetry.addData("object size", lastResult.size);
        }
        dist = findDist();
        telemetry.addData("distance",dist);
    }
}
