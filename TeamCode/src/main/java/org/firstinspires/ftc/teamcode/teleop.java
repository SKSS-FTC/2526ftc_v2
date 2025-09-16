package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GamepadStates;
// this too

@TeleOp(name = "Teleop", group = "Teleop")
// this is the thing that we run
public class teleop extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        double speed = 1;
        // this sets the speed

        Training Train = new Training();
//        ServoTraining Servo = new ServoTraining();
        // im pretty sure this defines "train"

        Train.init(this);
//        Servo.init(this);

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
        }

//        if (newGamePad1.a.released) {
//            Servo.normal();
//            // normal means 0
//        } else if (newGamePad1.b.released) {
//            Servo.right();
//            // right means 1
//        } else if (newGamePad1.x.released) {
//            Servo.left();
//            // left means -1
//        }
        // no need for a "Servo.stop();" apparently
    }
}
