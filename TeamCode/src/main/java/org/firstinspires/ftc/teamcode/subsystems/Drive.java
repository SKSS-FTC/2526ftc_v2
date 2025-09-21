package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;
/** sa nu uiti sa bagi in loc de imu kalman filter (: */

public class Drive {
    private final Gamepad gamepad;
    private final IMU imu;
    public Drive(Gamepad gamepad, IMU imu){
        this.gamepad = gamepad;
        this.imu = imu;
    }

    public double getYInput(){ return gamepad.left_stick_x * Math.toRadians(Math.cos(imu.getYaw())) + gamepad.left_stick_y * Math.toRadians(Math.sin(imu.getYaw())); }
    public double getXInput(){ return gamepad.left_stick_x * Math.toRadians(Math.sin(imu.getYaw())) - gamepad.left_stick_y * Math.toRadians(Math.cos(imu.getYaw())); }
    public double getRxInput(){ return gamepad.right_stick_x; }
}
