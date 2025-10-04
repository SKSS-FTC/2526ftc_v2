package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


public class ArtifactHandlingSystem {

    private final DcMotor leftOuttakeMotor;
    private final DcMotor rightOuttakeMotor;
    private final LinearOpMode linearOpMode;

    private final ColorSensor colorSensor;

    private final Servo rgbIndicator;

    private final Servo containerServo;


    public ArtifactHandlingSystem(LinearOpMode linearOpMode) {
        this.colorSensor = linearOpMode.hardwareMap.colorSensor.get("colorSensor");
        this.rgbIndicator = linearOpMode.hardwareMap.servo.get("rgbIndicator");
        this.containerServo = linearOpMode.hardwareMap.servo.get("containerServo");

        this.leftOuttakeMotor = linearOpMode.hardwareMap.dcMotor.get("leftOuttakeMotor");
        this.rightOuttakeMotor = linearOpMode.hardwareMap.dcMotor.get("rightOuttakeMotor");
        this.linearOpMode = linearOpMode;
    }

    public void configureMotorModes() {
        leftOuttakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightOuttakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftOuttakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightOuttakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void shootArtifact(float shootArtifact) {
        leftOuttakeMotor.setPower(shootArtifact);
        rightOuttakeMotor.setPower(shootArtifact);
    }

    public String detectColor() {
        int red = colorSensor.red();
        int green = colorSensor.green();
        int blue = colorSensor.blue();

        float[] hsv = new float[3];
        Color.RGBToHSV(red, green, blue, hsv);
        float hue = hsv[0];

        if ((hue >= 0 && hue < 80) || (hue > 140 && hue < 277)) {
            return "Not an Artifact";
        } else if (hue > 276 && hue < 300) {
            return "Purple";
        } else if (hue > 79 && hue < 140) {
            return "Green";
        } else {
            return "Unknown";
        }
    }

    public void runContainer(String color){
        if (!color.equals("Green") && !color.equals("Purple")){
            return;
        }
    }

    public void displayTelemetry() {
        linearOpMode.telemetry.addData("Left Outtake Motor Power", ".2f", leftOuttakeMotor.getPower());
        linearOpMode.telemetry.addData("Right Outtake Motor Power", ".2f", rightOuttakeMotor.getPower());
        // containerServo.setPosition(0.95);
    }
}
