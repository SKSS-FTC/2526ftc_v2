package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class MecanumDrive {
    private final DcMotorEx lf;
    private final DcMotorEx lb;
    private final DcMotorEx rf;
    private final DcMotorEx rb;

    public MecanumDrive(DcMotorEx lf, DcMotorEx lb, DcMotorEx rf, DcMotorEx rb){
        this.lb = lb;
        this.rb = rb;
        this.lf = lf;
        this.rf = rf;

        lb.setDirection(DcMotorSimple.Direction.FORWARD);
        lf.setDirection(DcMotorSimple.Direction.FORWARD);
        rf.setDirection(DcMotorSimple.Direction.REVERSE);
        rb.setDirection(DcMotorSimple.Direction.REVERSE);
    }

        public void drive(double y, double x, double rot, double sin, double cos){
            double last_front_left = 0;
            double last_front_right = 0;
            double last_back_left = 0;
            double last_back_right = 0;
            double threshold = 0.02;

            double x_field = x * cos - y * sin;
            double y_field = x * sin + y * cos;

            double denominator = Math.max(Math.abs(y_field) + Math.abs(x_field) + Math.abs(rot), 1);

            double front_left = (y_field + x_field + rot) / denominator;
            double front_right = (y_field - x_field - rot) / denominator;
            double back_left = (y_field - x_field + rot) / denominator;
            double back_right = (y_field + x_field - rot) / denominator;

            if (Math.abs(front_left - last_front_left) > threshold) {
                lf.setPower(front_left);
                last_front_left = front_left;
            }

            if (Math.abs(front_right - last_front_right) > threshold) {
                rf.setPower(front_right);
                last_front_right = front_right;
            }

            if (Math.abs(back_left - last_back_left) > threshold) {
                lb.setPower(back_left);
                last_back_left = back_left;
            }

            if (Math.abs(back_right - last_back_right) > threshold) {
                rb.setPower(back_right);
                last_back_right = back_right;
            }

        }
}
