package org.firstinspires.ftc.teamcode.CompOpModes.CompTeleOps;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.AllianceColor;

@TeleOp(name = "Red Comp TeleOp", group = "Comp")
public class RedCompTeleOp extends CompTeleOpTemplate{

    @Override
    public void setAllianceColor(){
        allianceColor = AllianceColor.RED;
    }
}
