package org.firstinspires.ftc.teamcode.Vision.AprilTag;

import com.qualcomm.robotcore.util.ElapsedTime;

// The FTC SDK does not include a built-in PID controller class.
// A team would typically create their own or use a library.
// For this example, we will implement a basic version of a PIDF controller.
class PIDFController {
    private double kP, kI, kD, kF;
    private double lastError = 0;
    private double integralSum = 0;
    private ElapsedTime timer = new ElapsedTime();

    public PIDFController(double kP, double kI, double kD, double kF) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
    }

    public double calculate(double setpoint, double currentVelocity) {
        // Calculate error
        double error = setpoint - currentVelocity;

        // Proportional component
        double proportional = error * kP;

        // Integral component
        integralSum += error * timer.seconds();
        double integral = integralSum * kI;

        // Derivative component
        double derivative = (error - lastError) / timer.seconds();
        double derivativeComponent = derivative * kD;

        // Feedforward component (for motors, a constant power to overcome friction)
        double feedforward = kF * setpoint;

        // Update last error and reset timer
        lastError = error;
        timer.reset();

        return proportional + integral + derivativeComponent + feedforward;
    }

    public void reset() {
        lastError = 0;
        integralSum = 0;
        timer.reset();
    }
}
