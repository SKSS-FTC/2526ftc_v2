package org.nknsd.teamcode.components.sensors;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNComponent;

import java.util.HashMap;

public class DistSensorHub implements NKNComponent {
    private HashMap<SensorNames, DistSensor> sensors;

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        return true;

    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "DistanceSensor Hub";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    // Dist Hub will perform the telemetry operation for all linked sensors automatically! Isn't that nice?
    @Override
    public void doTelemetry(Telemetry telemetry) {
        for (DistSensor sensor : sensors.values()) {
            sensor.doTelemetry(telemetry);
        }

        // This line is a little clunky and could be improved
        // The intent was for this Forward Dist only to run if we have the left and right but not the forward (because the forward would be called already if we had forward)
        if (sensors.containsKey(SensorNames.LEFT) && sensors.containsKey(SensorNames.RIGHT) && !sensors.containsKey(SensorNames.FORWARD)) {
            telemetry.addData("Forward Dist", getForwardDist());
        }
    }

    // This is the format for a getter for a sensor name
    // We can access a particular name in the SensorNames hashmap, but if it doesn't exist, it throws a NullPointerException
    // These are here for convenience essentially
    public double getBackDist() throws NullPointerException {
        return getDist(SensorNames.BACK);
    }

    public double getLeftDist() throws NullPointerException {
        return getDist(SensorNames.LEFT);
    }

    public double getRightDist() throws NullPointerException {
        return getDist(SensorNames.RIGHT);
    }

    // More generic getter, but requires the user to put in the sensor's name
    public double getDist(SensorNames name) throws NullPointerException {
        DistSensor sensor = sensors.get(name);
        if (sensor == null) {
            throw new NullPointerException();
        }

        return sensor.getDistance();
    }

    // This getter differs from the other ones because there are two situations where the forward sensor is an accessible value
    // Either we have a forward sensor
    // Or we have a left and right sensor that can be averaged to get a more consistent value
    // More of these could be added or created if we get into a situation where we need more dual sensor values
    // Or this could be modified to average forward, left AND right all at once, if a robot has those components
    public double getForwardDist() throws NullPointerException {
        if (sensors.containsKey(SensorNames.FORWARD)) {
            return getDist(SensorNames.FORWARD);
        }

        if (sensors.containsKey(SensorNames.LEFT) && sensors.containsKey(SensorNames.RIGHT)) {
            return getDualDist(SensorNames.LEFT, SensorNames.RIGHT);
        }

        throw new NullPointerException();
    }

    // This function is a generic getter for the average of two different sensors
    // (not very useful most times, except when called by the hub looking for a specific, more reliable, value [like forward])
    public double getDualDist(SensorNames sensor1, SensorNames sensor2) {
        return (getDist(sensor1) + getDist(sensor2)) / 2;
    }

    public void link(HashMap<SensorNames, DistSensor> sensors) {
        this.sensors = sensors;
    }

    // Here, we declare the different sensor names that could theoretically be attached to a DistanceSensorHub
    public enum SensorNames {
        BACK,
        LEFT,
        RIGHT,
        FORWARD;
    }
}
