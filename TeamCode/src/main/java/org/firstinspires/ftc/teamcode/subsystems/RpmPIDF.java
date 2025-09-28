package org.firstinspires.ftc.teamcode.subsystems;

public class RpmPIDF {
    // to tune
    public double Kp = 0.001;
    public double Ki = 0.0001;
    public double Kd = 0.0001;

    // Feedforward gains (empirical)
    public double kS = 0.0;   // static friction
    public double kV = 0.001; // power per RPM (power = kV * rpm)

    // state
    private double lastError = 0.0;
    private double integral = 0.0;
    private double lastMeasurement = 0.0;

    private double integralLimit = 0.5; // clamping integral

    // smooths out derivitave for noisy encoder data
    private double derivAlpha = 0.6;
    private double lastDerivativeFiltered = 0.0;

    public double update(double setpointRPM, double measuredRPM, double dt) {
        double error = setpointRPM - measuredRPM;

        // Integral with limiting
        integral += error * dt;
        if (integral > integralLimit) integral = integralLimit;
        if (integral < -integralLimit) integral = -integralLimit;

        // Derivative on measurement to reduce derivative kick
        double rawDerivative = -(measuredRPM - lastMeasurement) / dt; // derivative of error but using measurement derivative
        // low pass fiter derivative
        double derivativeFiltered = derivAlpha * lastDerivativeFiltered + (1 - derivAlpha) * rawDerivative;
        lastDerivativeFiltered = derivativeFiltered;

        // PID type shift (small adjsutmetns)
        double pidOut = Kp * error + Ki * integral + Kd * derivativeFiltered;

        // Feedforward (to hit the gas)
        double ff = kS * Math.signum(setpointRPM) + kV * setpointRPM;

        // total command
        double output = ff + pidOut;

        // clamp to motor power range
        output = Math.max(-1.0, Math.min(1.0, output));

        // update state
        lastError = error;
        lastMeasurement = measuredRPM;

        return output;
    }

    public void reset() {
        integral = 0;
        lastError = 0;
        lastDerivativeFiltered = 0;
        lastMeasurement = 0;
    }
}
