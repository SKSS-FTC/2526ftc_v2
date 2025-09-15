package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
public class LimelightOpMode extends OpMode {

    limelighttest limelight;

    @Override
    public void init(){
        limelight= new limelighttest(hardwareMap);

    }

    @Override
    public void loop(){

        telemetry.addLine( limelight.doCamera());
        telemetry.update();
    }
}
