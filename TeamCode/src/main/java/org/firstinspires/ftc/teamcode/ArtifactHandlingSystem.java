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

    public void shootArtifact(float shootArtifact) {
        leftOuttakeMotor.setPower(shootArtifact);
        rightOuttakeMotor.setPower(shootArtifact);
    }

    public void displayTelemetry() {
        linearOpMode.telemetry.addData("Left Outtake Motor Power", ".2f", leftOuttakeMotor.getPower());
        linearOpMode.telemetry.addData("Right Outtake Motor Power", ".2f", rightOuttakeMotor.getPower());
    }
}
