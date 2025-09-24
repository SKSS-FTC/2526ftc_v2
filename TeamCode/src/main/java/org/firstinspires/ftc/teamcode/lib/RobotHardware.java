package org.firstinspires.ftc.teamcode.lib;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class RobotHardware {
    // drivetrain
    public DcMotorEx dtFL;
    public DcMotorEx dtFR;
    public DcMotorEx dtBL;
    public DcMotorEx dtBR;
    // shooter
    public DcMotorEx flywheel;
    public Servo hood;
    // turret
    public DcMotorEx turret;
    // intake
    public DcMotorEx intake;
    // transfer
    public Servo transfer;
    // spindexer
    public Servo spindexer;
}
