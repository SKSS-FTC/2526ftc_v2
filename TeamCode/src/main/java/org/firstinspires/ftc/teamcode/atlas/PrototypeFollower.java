package org.firstinspires.ftc.teamcode.atlas;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Map;

import static java.lang.Math.*;

public class PrototypeFollower {
    private final LinearOpMode opMode;
    private final AtlasChassis chassis;
    private final Waypoint[] waypoints;
    private final Map<String, Runnable> eventMap;

    private static final int tolerance = 200;
    private static final int stopTolerance = 100;

    private final Telemetry telemetry;

    private int currentWaypoint = 0;

    public PrototypeFollower(LinearOpMode opMode,
                       AtlasChassis chassis,
                       Waypoint[] waypoints,
                       Map<String, Runnable> eventMap) {
        this.opMode = opMode;
        this.chassis = chassis;
        this.waypoints = waypoints;
        this.eventMap = eventMap;
        this.telemetry = opMode.telemetry;
    }

    boolean paused = false;

    public void run() {
        // Ensure all events exist in the map
        for (Waypoint wp : waypoints) {
            for (String event : wp.events) {
                if (!eventMap.containsKey(event)) {
                    throw new RuntimeException("Event map missing event " + event);
                }
            }
        }

        chassis.pose.x = waypoints[0].x;
        chassis.pose.y = waypoints[0].y;

        while (opMode.opModeIsActive()) {
            chassis.update(telemetry);
            Waypoint waypoint = waypoints[currentWaypoint];

            double distanceToTarget = distance(waypoint.x, waypoint.y,
                    chassis.pose.x, chassis.pose.y);

            telemetry.addData("Waypoint", currentWaypoint);
            telemetry.addData("Distance from waypoint", distanceToTarget);

            if (waypoint.stop || currentWaypoint == waypoints.length - 1) {
                if (distanceToTarget < stopTolerance &&
                        abs(getRotationAngle(waypoint.r, chassis.yawDeg)) < 1) {
                    chassis.movePower(0.0, 0.0, 0.0);

                    for (String event : waypoint.events) {
                        eventMap.get(event).run();
                    }

                    currentWaypoint += 1;
                    if (currentWaypoint == waypoints.length) break;
                }
            } else {
                if (distanceToTarget < tolerance) {
                    currentWaypoint += 1;
                }
                for (String event : waypoint.events) {
                    eventMap.get(event).run();
                }
            }

            double dx = positionTargetValue(waypoint.x - chassis.pose.x);
            double dy = positionTargetValue(waypoint.y - chassis.pose.y);
            double dr = rotationTargetValue(waypoint.r, chassis.yawDeg);

            telemetry.addLine("mx: " + dx);
            telemetry.addLine("my: " + dy);
            telemetry.addLine("mr: " + dr);

            chassis.moveFieldRelative(
                    clamp(dx, -1.0, 1.0),
                    clamp(dy, -1.0, 1.0),
                    dr
            );

            telemetry.update();
        }

        chassis.movePower(0.0, 0.0, 0.0);
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    private double positionTargetValue(double value) {
        if (-tolerance < value && value < tolerance) {
            return value * 0.01;
        }
        return value * 0.25;
    }

    private double rotationTargetValue(double target, double current) {
        double angle = getRotationAngle(target, current);
        if (abs(angle) < 1) return 0.0;
        return clamp(-angle / 45.0, -1.0, 1.0);
    }

    private double getRotationAngle(double target, double current) {
        return (target - current + 180) % 360 - 180;
    }

    private double clamp(double value, double min, double max) {
        return max(min, min(value, max));
    }
}
