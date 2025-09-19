package org.firstinspires.ftc.teamcode.subsystems;

public class IMU {
    private final com.qualcomm.robotcore.hardware.IMU imu;// radians
    private double lastRawHeading = Double.NaN;     // sin(0)
    private static final double EPSILON = 1e-5;

    public IMU(com.qualcomm.robotcore.hardware.IMU imu){
        this.imu = imu;
    }

    public void update() {
        double rawHeading = Math.toRadians(imu.getRobotYawPitchRollAngles().getYaw());

        if (Double.isNaN(lastRawHeading) || Math.abs(rawHeading - lastRawHeading) > EPSILON) {
            lastRawHeading = rawHeading;
        }
    }

    public double getYaw(){ return lastRawHeading;}
    public void resetCache() {
        lastRawHeading = Double.NaN;
    }

}
