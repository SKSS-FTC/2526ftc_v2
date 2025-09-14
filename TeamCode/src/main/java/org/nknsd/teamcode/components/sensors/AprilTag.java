package org.nknsd.teamcode.components.sensors;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNComponent;

public class AprilTag implements NKNComponent {

    Limelight3A limelight;



    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start(); // This tells Limelight to start looking!
        limelight.pipelineSwitch(2); // Switch to aprilTag pipeline

//        Pipeline 2 is PGP
//        Pipeline 3 is GPP
//        Pipeline 4 is PPG
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

    double tx; // How far left or right the target is (degrees)
    double ty; // How far up or down the target is (degrees)
    double ta; // How big the target looks (0%-100% of the image)

    int pattern; // this should probably be an enum, it tells what pattern we have, 4 is PPG, 2 is PGP, 3 is GPP, 1 is nothing;

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        // TODO - You should consider using :
        // result.getDetectorResults()
        // I think that will give you april tag details (including id).
        // I would put the logic into a handler, where you initiate a camera and single pipeline
        // That focuses on the 5 IDs we care about

        LLResult result = limelight.getLatestResult();

        limelight.pipelineSwitch(2);
        if (result != null && result.isValid()) {

            pattern = 2;
        } else {
            limelight.pipelineSwitch(3);
            if (result != null && result.isValid()) {
                pattern = 3;
            } else {
                limelight.pipelineSwitch(4);
                if (result != null && result.isValid()) {
                    pattern = 4;
                } else {
                    pattern = 1;
                    ta = 0;
                }
            }

        }

        if (pattern != 1) {
            tx = result.getTx();
            ty = result.getTy();
            ta = result.getTa();
        }
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        if (pattern == 1) {
            telemetry.addLine("Not seen");
        } else {
            telemetry.addData("Target X", tx);
            telemetry.addData("Target Y", ty);
            telemetry.addData("Target Area", ta);

            if(pattern == 2){
                telemetry.addLine("PGP");
            } else if(pattern == 3){
                telemetry.addLine("GPP");
            } else{
                telemetry.addLine("PPG");
            }
        }

    }
}
