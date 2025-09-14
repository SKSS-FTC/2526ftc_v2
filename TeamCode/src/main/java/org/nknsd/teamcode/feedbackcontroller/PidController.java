package org.nknsd.teamcode.feedbackcontroller;
public class PidController implements ControlLoop {

    final double pMultiplier;
    final double pCap;
    final double iMultiplier;
    final double iCap;
    final boolean integralReset;
    final double dMultiplier;
    final double dCap;

    double integral;

    public PidController(double pMultiplier, double pCap, double iMultiplier, double iCap, boolean integralReset, double dMultiplier, double dCap) {
        this.pMultiplier = pMultiplier;
        this.pCap = pCap;
        this.iMultiplier = iMultiplier;
        this.iCap = iCap;
        this.integralReset = integralReset;
        this.dMultiplier = dMultiplier;
        this.dCap = dCap;
    }

    private double proportionalCalculator(double error) {
        double speeds = error * pMultiplier;

        if (Math.abs(speeds) > pCap) {
            speeds = pCap * (speeds > 0 ? 1 : -1);
        }

        return speeds;
    }

    public double integralCalculator(double error, double speed, double interval) {
        integral += (error) * iMultiplier * interval / 1000;

        if (integralReset) {
            if ((error < 0) && ((error + speed) > 0)) {
                integral = 0;
            } else if ((error > 0) && ((error + speed) < 0)) {
                integral = 0;
            }
        }

        if ((integral > 0) && (integral > iCap)) {
            integral = iCap;
        } else if ((integral < 0) && (integral < -iCap)) {
            integral = -iCap;
        }

        return integral;
    }

    public double derivativeCalculator(double errorDelta, double interval) {
        double derivative = errorDelta * (1000.0 / interval) * dMultiplier;

        if ((derivative > 0) && (derivative > dCap)) {
            derivative = dCap;
        } else if ((derivative < 0) && (derivative < -dCap)) {
            derivative = -dCap;
        }

        return derivative;
    }

    @Override
    public double findOutput(double error, double errorDelta, double vel, double interval) {
        return proportionalCalculator(error) + integralCalculator(error, vel, interval) + derivativeCalculator(errorDelta, interval);
    }
}
