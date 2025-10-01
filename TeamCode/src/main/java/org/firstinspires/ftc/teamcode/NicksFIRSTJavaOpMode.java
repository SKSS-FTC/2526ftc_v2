package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Nick's OpMode", group="Example OpMode")
public class NicksFIRSTJavaOpMode extends OpMode {
    @Override
    public void init() {
        telemetry.addData("Status", "Initialised");
        telemetry.update();
    }

    @Override
    public void loop() {
        telemetry.addData("Status", "Running");
        telemetry.update();
    }

    @Override
    public  void stop() {
        telemetry.addData("Status", "Stopped");
        telemetry.update();
    }
}
