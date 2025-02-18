package org.nknsd.teamcode.helperClasses;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class PosPair {
    public final double x;
    public final double y;

    public PosPair(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean equalTo(PosPair o) {
        return o.x == x && o.y == y;
    }

    public double getDist(PosPair o) {
        double xDist = o.x - x;
        double yDist = o.y - y;

        return Math.sqrt(xDist * xDist + yDist * yDist);
    }

    public double getDist() {
        double xDist = 0 - x;
        double yDist = 0 - y;

        return Math.sqrt(xDist * xDist + yDist * yDist);
    }

    public PosPair scale(double scalar) {
        return new PosPair(scalar * x, scalar * y);
    }

    public PosPair dropLowValues(double dropOff) {
        double newX, newY;

        if (Math.abs(x) < dropOff) {
            newX = 0;
        } else {
            newX = x;
        }

        if (Math.abs(y) < dropOff) {
            newY = 0;
        } else {
            newY = y;
        }

        return new PosPair(x, y);
    }

    // Returns a new PosPair with an x clamped to a given minimum
    public PosPair clampValuesToMin(double min) {
        double newX, newY;

        if (Math.abs(x) < min) {
            newX = (x / Math.abs(x)) * min;
        } else {
            newX = x;
        }

        if (Math.abs(y) < min) {
            newY = (y / Math.abs(y)) * min;
        } else {
            newY = y;
        }

        return new PosPair(newX, newY);
    }

    public void doTelemetry(Telemetry telemetry) {
        doTelemetry(telemetry, "");
    }

    public void doTelemetry(Telemetry telemetry, String name) {
        telemetry.addData(name + "-X", x);
        telemetry.addData(name + "-Y", y);
    }
}
