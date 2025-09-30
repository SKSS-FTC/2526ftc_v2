package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class ArtifactHandlingSystem {

    private final DcMotor leftOuttakeMotor;
    private final DcMotor rightOuttakeMotor;
    private final LinearOpMode linearOpMode;

    public ArtifactHandlingSystem(LinearOpMode linearOpMode) {
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

    public void shootArtifact(boolean shootArtifact) {
        if (shootArtifact) {
            leftOuttakeMotor.setPower(1);
            rightOuttakeMotor.setPower(1);

        } else {
             leftOuttakeMotor.setPower(0);
             rightOuttakeMotor.setPower(0);
        }
    }

    public void displayTelemetry() {
        linearOpMode.telemetry.addData("Left Outtake Motor Power", leftOuttakeMotor.getPower());
        linearOpMode.telemetry.addData("Right Outtake Motor Power", rightOuttakeMotor.getPower());
    }
}
