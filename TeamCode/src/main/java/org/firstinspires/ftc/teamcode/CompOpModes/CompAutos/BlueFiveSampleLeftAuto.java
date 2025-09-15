package org.firstinspires.ftc.teamcode.CompOpModes.CompAutos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.AllianceColor;
import org.firstinspires.ftc.teamcode.LimelightColors;


@Autonomous
public class BlueFiveSampleLeftAuto extends FiveSampleAutoBase{
    @Override
    public void setAllianceColor(){
        allianceColor = AllianceColor.BLUE;
    }
    @Override
    public void setLimelightColor(){
        limelightColor = LimelightColors.BLUE;
    }
}
