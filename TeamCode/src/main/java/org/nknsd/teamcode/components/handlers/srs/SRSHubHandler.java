package org.nknsd.teamcode.components.handlers.srs;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNComponent;

public class SRSHubHandler implements NKNComponent {
    private SRSHub hub;

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        // All ports default to NONE, buses default to empty
        SRSHub.Config config = new SRSHub.Config();
        config.addI2CDevice(1, new SRSHub.VL53L5CX());

        hub = hardwareMap.get(
                SRSHub.class,
                "srshub"
        );
        hub.init(config);
        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {
        telemetry.addLine("Waiting for SRSHub");
        while (!hub.ready()) ;
        telemetry.addLine("SRSHub Ready!");
    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "SRSHub";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        hub.update();
        if (hub.disconnected()) {
            telemetry.addLine("srshub disconnected");
        } else {
            SRSHub.VL53L5CX sensor = hub.getI2CDevice(
                    1,
                    SRSHub.VL53L5CX.class
            );
            sensor.getValue();
            if (sensor.disconnected) {
                telemetry.addData("VL53L5CX", "Disconnected");
            } else {
                telemetry.addData("VL53L5CX",sensor.distance);
            }
        }
    }
}
