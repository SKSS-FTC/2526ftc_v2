package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(group = "TeleOP")
public class motorControl extends LinearOpMode {
    private HardwareMap hardwareMap;
    private DcMotor left;
    @Override
    public void runOpMode() {
        left = hardwareMap.get(DcMotorEx.class, "leftIntakeMotor");
        left.setDirection(DcMotorSimple.Direction.FORWARD);
        left.setPower(0);
        while(opModeIsActive()){
            if(gamepad1.dpad_up){
                left.setPower(0.2);
            } else if (gamepad1.dpad_down) {
                left.setPower(-0.2);
            } else if (gamepad1.dpad_left) {
                left.setPower(0);
            }
        }
    }
}
