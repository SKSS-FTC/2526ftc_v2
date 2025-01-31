package org.nknsd.teamcode.helperClasses;

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
}
