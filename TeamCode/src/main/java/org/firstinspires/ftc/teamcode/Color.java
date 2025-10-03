package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


public class Color {
    private LinearOpMode opmode = null;

    private ColorSensor color;

    boolean purple = false;
    boolean green = false;

    public Color() {
    }

    public void init(LinearOpMode opMode) {
        HardwareMap hwMap;
        opmode = opMode;
        hwMap = opMode.hardwareMap;

        color = hwMap.get(ColorSensor.class, "Color");
    }

    public void outputColor() {
        opmode.telemetry.addData("Red: ", color.red());
        opmode.telemetry.addData("Green: ", color.green());
        opmode.telemetry.addData("Blue: ", color.blue());
        opmode.telemetry.addData("Alpha: ", color.alpha());
        opmode.telemetry.update();
    }


}
