package org.firstinspires.ftc.teamcode.subsystems;

public class IMU {
    private final com.qualcomm.robotcore.hardware.IMU imu;// radians
    private double lastRawHeading = Double.NaN;
    private double cosHeading = 1.0;          // cos(0)
    private double sinHeading = 0.0;          // sin(0)
    private static final double EPSILON = 1e-6;

    public IMU(com.qualcomm.robotcore.hardware.IMU imu){
        this.imu = imu;
    }

    private void updateCachedHeading() {

        double rawHeading = Math.toRadians(imu.getRobotYawPitchRollAngles().getYaw());

        if (Double.isNaN(lastRawHeading) || Math.abs(rawHeading - lastRawHeading) > EPSILON) {
            cosHeading = Math.cos(rawHeading);
            sinHeading = Math.sin(rawHeading);
            lastRawHeading = rawHeading;
        }
    }

    public double getCos() {
        updateCachedHeading();
        return cosHeading;
    }

    public double getSin() {
        updateCachedHeading();
        return sinHeading;
    }

    public void resetCache() {
        lastRawHeading = Double.NaN;
        cosHeading = 1.0;
        sinHeading = 0.0;
    }

}
