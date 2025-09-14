package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Drive {
    Gamepad gamepad;

    public Drive(Gamepad gamepad){
        this.gamepad = gamepad;
    }

    public double getYInput(){ return -gamepad.left_stick_y; }
    public double getXInput(){ return gamepad.left_stick_x; }
    public double getRxInput(){ return gamepad.right_stick_x; }
}
