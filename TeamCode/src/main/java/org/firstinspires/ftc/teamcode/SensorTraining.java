package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;


public class SensorTraining {

    Limelight3A limelight;
    private LinearOpMode opmode = null;

    NormalizedColorSensor colorSensor;


    private ColorSensor sensorColor;
    int blueV;
    int redV;
    int greenV;
    public int Color_Threshold = 150;
    boolean blue;
    boolean red;
    boolean green;

    public SensorTraining() {
    }

    public void init(LinearOpMode opMode) {
        HardwareMap hwMap;
        opMode.telemetry.addLine("Color Control initialize");
        opMode.telemetry.update();

        opmode = opMode;
        hwMap = opMode.hardwareMap;

        limelight = hwMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.start();
        opMode.telemetry.addLine("Color siers");
        limelight.pipelineSwitch(7);

        View relativeLayout;

        int relativeLayoutId = hwMap.appContext.getResources().getIdentifier("RelativeLayout",
                "id", hwMap.appContext.getPackageName());
        relativeLayout = ((Activity) hwMap.appContext).findViewById(relativeLayoutId);


        sensorColor = hwMap.get(ColorSensor.class, "Color");
    }

    // adds telemetry which im pretty sure is what makes it show up on the driver hub i think
//    public void changeColor(int relativeLayout) {
//        relativeLayout.post(new Runnable() {
//            relativeLayout.setBackgroundColor(Color.HSVToColor(hsvValues));
//        }
//    }

    public void color_telemetry() {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        opmode.telemetry.addLine()
                .addData("Red", "%.3f", colors.red)
                .addData("Green", "%.3f", colors.green)
                .addData("Blue", "%.3f", colors.blue);
        opmode.telemetry.update();
    }


    // gets blue red and green values!!! ^w^
    public int getBlueV() {
        return sensorColor.blue();
    }

    public int getRedV() {
        return sensorColor.red();
    }

    public int getGreenV() {
        return sensorColor.green();
    }

/*    public void LL_telemetry(){
        opmode.telemetry.addData("", void);
*/
}



