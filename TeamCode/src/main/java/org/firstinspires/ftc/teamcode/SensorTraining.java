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

import org.firstinspires.ftc.robotcontroller.external.samples.SensorColor;


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
        opmode.telemetry.addLine()
                .addData("Red", "%.3f", getRedV())
                .addData("Green", "%.3f", getGreenV())
                .addData("Blue", "%.3f", getBlueV());
//        opmode.telemetry.update();

        if (getGreenV() > 20  || getBlueV() > 10) {
            opmode.telemetry.addLine("green");
            opmode.telemetry.update();
        } else if (getGreenV() < 20){
            opmode.telemetry.addLine("purple");
            opmode.telemetry.update();
        } else {
            opmode.telemetry.addLine("no ball detected");
            opmode.telemetry.update();
        }
    }


    // gets blue red and green values!!! ^w^
    public double getBlueV() {
        return sensorColor.blue();
    }

    public double getRedV() {
        return sensorColor.red();
    }

    public double getGreenV() {
        return sensorColor.green();
    }

/*    public void LL_telemetry(){
        opmode.telemetry.addData("", void);
*/
}



