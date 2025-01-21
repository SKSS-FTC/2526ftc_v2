package org.nknsd.teamcode.drivers;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.components.handlers.ShaiHuludHandler;
import org.nknsd.teamcode.components.handlers.WheelHandler;
import org.nknsd.teamcode.components.utility.GamePadHandler;
import org.nknsd.teamcode.controlSchemes.abstracts.ShaiHuludControlScheme;
import org.nknsd.teamcode.controlSchemes.abstracts.WheelControlScheme;
import org.nknsd.teamcode.frameworks.NKNComponent;

public class ShaiHuludDriver implements NKNComponent {

    private GamePadHandler gamePadHandler;
    private ShaiHuludHandler shaiHuludHandler;
    private ShaiHuludControlScheme controlScheme;

    private Runnable shExtend = new Runnable() {
        @Override
        public void run() {
            shaiHuludHandler.beginPickup();
        }
    };

    private Runnable shRetract = new Runnable() {
        @Override
        public void run() {
            shaiHuludHandler.cancelPickup();
        }
    };

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {
        // Add event listeners
        gamePadHandler.addListener(controlScheme.shExtend(), shExtend, "SH Begin Extend");
        gamePadHandler.addListener(controlScheme.shRetract(), shRetract, "SH CANCEL BACK UP AHHH");
    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "ShaiHuludDriver";
    }


    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void doTelemetry(Telemetry telemetry) {

    }

    public void link(GamePadHandler gamePadHandler, ShaiHuludHandler shaiHuludHandler, ShaiHuludControlScheme controlScheme) {
        this.gamePadHandler = gamePadHandler;
        this.shaiHuludHandler = shaiHuludHandler;
        this.controlScheme = controlScheme;
    }
}
