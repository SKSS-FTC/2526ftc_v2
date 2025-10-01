package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class ArtifactHandlingSystem {

    private final DcMotor leftOuttakeMotor;
    private final DcMotor rightOuttakeMotor;
    private final LinearOpMode linearOpMode;
    private final DcMotor intakeMotor;

    public ArtifactHandlingSystem(LinearOpMode linearOpMode) {
        this.leftOuttakeMotor = linearOpMode.hardwareMap.dcMotor.get("leftOuttakeMotor");
        this.rightOuttakeMotor = linearOpMode.hardwareMap.dcMotor.get("rightOuttakeMotor");
        this.linearOpMode = linearOpMode;
        this.intakeMotor = linearOpMode.hardwareMap.dcMotor.get("intakeMotor");

    }

    public void configureMotorModes() {
        leftOuttakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightOuttakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftOuttakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightOuttakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void shootArtifact(float shootArtifact) {
        leftOuttakeMotor.setPower(shootArtifact);
        rightOuttakeMotor.setPower(shootArtifact);
    }

    public void intakeArtifact(boolean intakeArtifact) {
        if (intakeArtifact) {
            intakeMotor.setPower(1);
        } else {
            intakeMotor.setPower(0);
        }
    }

    public void displayTelemetry() {
        linearOpMode.telemetry.addData("Left Outtake Motor Power", leftOuttakeMotor.getPower());
        linearOpMode.telemetry.addData("Right Outtake Motor Power", rightOuttakeMotor.getPower());
        linearOpMode.telemetry.addData("Intake Motor Power", intakeMotor.getPower());
    }
}
