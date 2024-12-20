package org.nknsd.teamcode.components.testfiles;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.ShaiHuludHandler;
import org.nknsd.teamcode.components.handlers.ShaiHuludHandler.ShaiStates;
import org.nknsd.teamcode.frameworks.NKNComponent;

public class ShaiHuludMonkey implements NKNComponent {
    private ShaiHuludHandler shaiHuludHandler;

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
        return "ShaiHuludMonkey";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        if (shaiHuludHandler.getState() == ShaiStates.TUCK) {
            shaiHuludHandler.setState(ShaiStates.BEGINEXTEND);
        }
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {

    }


    public void link(ShaiHuludHandler shaiHuludHandler) {
        this.shaiHuludHandler = shaiHuludHandler;
    }
}
