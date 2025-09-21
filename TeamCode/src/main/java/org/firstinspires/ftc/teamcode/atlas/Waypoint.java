package org.firstinspires.ftc.teamcode.atlas;

import java.util.Arrays;

public class Waypoint {
    public final double x;
    public final double y;
    public final double r;
    public final double speed;
    public final double rotSpeed;
    public final boolean stop;
    public final String[] events;

    public Waypoint(double x, double y, double r,
                    double speed, double rotSpeed,
                    boolean stop, String[] events) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.speed = speed;
        this.rotSpeed = rotSpeed;
        this.stop = stop;
        this.events = events;
    }

    @Override
    public String toString() {
        return "Waypoint{" +
                "x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", speed=" + speed +
                ", rotSpeed=" + rotSpeed +
                ", stop=" + stop +
                ", events=" + Arrays.toString(events) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Waypoint)) return false;
        Waypoint wp = (Waypoint) o;
        return Double.compare(wp.x, x) == 0 &&
                Double.compare(wp.y, y) == 0 &&
                Double.compare(wp.r, r) == 0 &&
                Double.compare(wp.speed, speed) == 0 &&
                Double.compare(wp.rotSpeed, rotSpeed) == 0 &&
                stop == wp.stop &&
                Arrays.equals(events, wp.events);
    }

    @Override
    public int hashCode() {
        int result = java.util.Objects.hash(x, y, r, speed, rotSpeed, stop);
        result = 31 * result + Arrays.hashCode(events);
        return result;
    }
}
