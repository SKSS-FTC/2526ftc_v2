package org.firstinspires.ftc.teamcode.CompOpModes.CompAutos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.AllianceColor;


@Autonomous
public class RedFourSampleLeftAuto extends FourSampleAutoBase {

    @Override
    public void setAllianceColor(){
        allianceColor = AllianceColor.RED;
    }
}
