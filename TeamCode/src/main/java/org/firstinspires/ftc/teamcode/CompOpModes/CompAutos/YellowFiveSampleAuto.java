package org.firstinspires.ftc.teamcode.CompOpModes.CompAutos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.AllianceColor;
import org.firstinspires.ftc.teamcode.LimelightColors;

@Autonomous
public class YellowFiveSampleAuto extends FiveSampleAutoBase{
    @Override
    public void setAllianceColor(){
        allianceColor = AllianceColor.RED;
    }

    @Override
    public void setLimelightColor(){
        limelightColor = LimelightColors.YELLOW;
    }
}
