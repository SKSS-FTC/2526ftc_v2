package org.nknsd.teamcode.components.handlers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNComponent;

public class ShaiHuludHandler implements NKNComponent {
    private DcMotor extensionMotor;
    private Servo wristServo;
    private Servo spikeServo;


    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        extensionMotor = hardwareMap.dcMotor.get("name");
        wristServo = hardwareMap.servo.get("name");
        spikeServo = hardwareMap.servo.get("name");
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
        return "Shai Hulud Handler";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void doTelemetry(Telemetry telemetry) {

    }


    public enum WristPositions {
        TUCK(0),
        ALIGNED(0);

        double pos;
        WristPositions(double pos) {
            this.pos = pos;
        }
    }

    public enum SpikePositions {
        DROP(0),
        LOOSE(0),
        GRAB(0);

        double pos;
        SpikePositions(double pos) {
            this.pos = pos;
        }
    }

    public enum ExtensionPositions {
        EXTENDED(0),
        RETRACTED(0);

        double pos;
        ExtensionPosition(double pos) {
            this.pos = pos;
        }
    }
}
