package org.nknsd.teamcode.components.utility;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.controlSchemes.abstracts.Generic2PControlScheme;
import org.nknsd.teamcode.frameworks.NKNComponent;

public class ColorPicker implements NKNComponent {
    private boolean isBlueRobot = true;
    private Colors currentColor = Colors.YELLOW;
    private GamePadHandler gamePadHandler;
    private Generic2PControlScheme controlScheme;

    Runnable switchColorInit = new Runnable() {
        @Override
        public void run() {
            isBlueRobot = !isBlueRobot;
        }
    };

    Runnable switchColor = new Runnable() {
        @Override
        public void run() {
            boolean done = false;
            int index = currentColor.ordinal();

            while (!done) {
                index ++;

                if (index >= Colors.values().length) {
                    index = 0;
                }

                if (Colors.values()[index].equals(isBlueRobot ? Colors.RED : Colors.BLUE)) {
                    continue;
                }

                done = true;
            }

            currentColor = Colors.values()[index];
        }
    };

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        gamePadHandler.addListener(controlScheme.switchColorInit(), switchColorInit, "Switch Color Init");
        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {
        telemetry.addData("Color", isBlueRobot ? "Blue" : "Red");
    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {
        gamePadHandler.removeListener("Switch Color Init");
        gamePadHandler.addListener(controlScheme.switchColor(), switchColor, "Switch Color");
    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "ColorPicker";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        telemetry.addData("Robot Color", isBlueRobot ? "Blue" : "Red");
        telemetry.addData("Current Color", currentColor.name());
    }

    public void link(GamePadHandler gamePadHandler, Generic2PControlScheme controlScheme) {
        this.gamePadHandler = gamePadHandler;
        this.controlScheme = controlScheme;
    }

    public boolean isCurrentYellow() {
        return (currentColor.equals(Colors.YELLOW));
    }

    public boolean isCurrentBlue() {
        return (currentColor.equals(Colors.BLUE));
    }

    public boolean isCurrentRed() {
        return (currentColor.equals(Colors.RED));
    }

    public Colors currentColor() {
        return currentColor;
    }

    public boolean isBaseBlue() {
        return isBlueRobot;
    }

    public boolean isBaseRed() {
        return !isBlueRobot;
    }

    public enum Colors {
        YELLOW,
        BLUE,
        RED
    }
}
