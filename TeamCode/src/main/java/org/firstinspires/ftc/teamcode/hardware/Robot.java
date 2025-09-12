package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Pipeline;

public class Robot {
    public final Pipeline pipeline;

    public Robot(HardwareMap hmap){
        pipeline = new Pipeline(
                hmap.get(Limelight3A.class, "Limelight")
        );
    }

    public void update(){

    }

}
