package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SensorTraining {
    private LinearOpMode opmode = null;

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

        sensorColor = hwMap.get(ColorSensor.class, "Color");
    }

    public void telemetry() {
        opmode.telemetry.addData("Red value: ", sensorColor.red());
        opmode.telemetry.addData("Green value: ", sensorColor.green());
        opmode.telemetry.addData("Blue value: ", sensorColor.blue());
        opmode.telemetry.update();
    }

    public int getBlueV() {
        return sensorColor.blue();
    }

    public int getRedV() {
        return sensorColor.red();
    }

    public int getGreenV() {
        return sensorColor.green();
    }


}

