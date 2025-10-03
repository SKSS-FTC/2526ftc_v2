package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GamepadStates;

import java.util.List;
// this too

@TeleOp(name = "Teleop", group = "Teleop")
// this is the thing that we run
public class teleop extends LinearOpMode {

    private Limelight3A limelight = null;


    @Override
    public void runOpMode() throws InterruptedException {

        double speed = 1;
        // this sets the speed

        Training Train = new Training();
//        Intake Intake = new Intake();
        ServoTraining Servo = new ServoTraining();
        LLOpMode LL = new LLOpMode();
        SensorTraining Sensor = new SensorTraining();

        Train.init(this);
//        Intake.init(this);
        Servo.init(this);
        LL.init(this);
        Sensor.init(this);


        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(7);
        limelight.start();

        GamepadStates newGamePad1 = new GamepadStates(gamepad1);
        GamepadStates newGamePad2 = new GamepadStates(gamepad2);
        // train

        waitForStart();

        while (opModeIsActive()) {
            // (pretty much while this is running)
            if (gamepad1.left_stick_y < -.4) {
                // run the forward function from Training program
                Train.forward();
            } else if (gamepad1.left_stick_y > .4) {
                // backwards
                Train.backwards();
            } else if (gamepad1.right_stick_x < -.4) {
                // left
                Train.left();
            } else if (gamepad1.right_stick_x > .4) {
                // right
                Train.right();
                // run the stop function from training
            } else {
                Train.stop();
            }


            if (newGamePad1.a.state) {
//            Intake.forward(speed);
            }

            if (newGamePad1.b.state) {
//            Intake.backwards(speed);
            }

            if (newGamePad1.a.released) {
                Servo.normal();
                // normal means 0
            } else if (newGamePad1.b.released) {
                Servo.right();
                // right means 1
            } else if (newGamePad1.x.released) {
                Servo.left();
                // left means -1
            }

            Sensor.color_telemetry();

            LLResult result = limelight.getLatestResult();
            if (result.isValid()) {
                // Access fiducial results
                List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
                for (LLResultTypes.FiducialResult fr : fiducialResults) {
                    telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f", fr.getFiducialId(), fr.getFamily(), fr.getTargetXDegrees(), fr.getTargetYDegrees());
                }

            } else {
                telemetry.addData("Limelight", "No data available");
            }
        }
    }
}