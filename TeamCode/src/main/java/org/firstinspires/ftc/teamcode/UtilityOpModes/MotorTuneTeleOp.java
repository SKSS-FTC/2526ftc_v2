package org.firstinspires.ftc.teamcode.UtilityOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotConfig;


@TeleOp(name = "Hardware Tune",group = "Utility")
public class MotorTuneTeleOp extends OpMode {

    private MotorTune motorTune;


    @Override
    public void init(){

        motorTune = new MotorTune(hardwareMap,gamepad1,telemetry);
    }

    @Override
    public void loop(){
        motorTune.run();
        telemetry.update();
    }


}
