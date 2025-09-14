package org.nknsd.teamcode.feedbackcontroller;

public class SimplePController implements ControlLoop {

    final double multiplier;
    final double cap;

    public SimplePController(double multiplier, double cap) {
        this.multiplier = multiplier;
        this.cap = cap;
    }

    @Override
    public double findOutput(double error, double errorDelta, double velocity, double interval) {
        double output = error * multiplier;

        if (Math.abs(output) > cap) {
            output = cap * (output > 0 ? 1 : -1);
        }

        return output;
    }
}
