package org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Test;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RobotConfig;


//This class is intended to be in use until the actual intake class is made
public class ColorSensorTest extends SubsystemBase {

    private ColorSensor colorSensor;

    public ColorSensorTest(HardwareMap hMap){
        colorSensor = hMap.get(ColorSensor.class, RobotConfig.IntakeConstants.colorSensor1Name);
    }



    public SampleColor getSampleColor(){
        double red = colorSensor.red();
        double green = colorSensor.green();
        double blue = colorSensor.blue();

        double max = Math.max(Math.max(red,blue),green);

        red/=max;
        blue/=max;
        green/=max;


        if(blue>red&&blue>green){
            return SampleColor.BLUE;
        } else if (red>blue&&red>green){
            return SampleColor.RED;
        } else if (Math.abs(red-green) < RobotConfig.IntakeConstants.colorSensorRedToGreenThreshold){
            return SampleColor.YELLOW;
        } else {
            return SampleColor.YELLOW_BY_ELIMINATION;
        }
    }
}
enum SampleColor {
    RED(0),
    BLUE(1),
    YELLOW(2),
    YELLOW_BY_ELIMINATION(2);

    public int num;
    SampleColor(int num){
        this.num = num;
    }
}
