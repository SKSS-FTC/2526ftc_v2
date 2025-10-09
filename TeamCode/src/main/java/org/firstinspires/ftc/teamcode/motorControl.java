package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(group = "TeleOP")
public class motorControl extends LinearOpMode {
    private HardwareMap hardwareMap;
    private DcMotor lefttop;
    private DcMotor leftdown;
    private DcMotor righttop;
    private DcMotor rightdown;

    @Override
    public void runOpMode() {
        lefttop = hardwareMap.get(DcMotorEx.class, "LeftTopIntakeMotor");
        lefttop.setDirection(DcMotorSimple.Direction.FORWARD);
        lefttop.setPower(0);
        leftdown = hardwareMap.get(DcMotorEx.class, "LeftDownIntakeMotor");
        leftdown.setDirection(DcMotorSimple.Direction.FORWARD);
        leftdown.setPower(0);
        righttop = hardwareMap.get(DcMotorEx.class, "RightTopIntakeMotor");
        righttop.setDirection(DcMotorSimple.Direction.REVERSE);
        righttop.setPower(0);
        rightdown = hardwareMap.get(DcMotorEx.class, "RightDownIntakeMotor");
        rightdown.setDirection(DcMotorSimple.Direction.REVERSE);
        rightdown.setPower(0);
        while(opModeIsActive()){
            if(gamepad1.dpad_up){
                lefttop.setPower(0.2);
                leftdown.setPower(0.2);
                righttop.setPower(0.2);
                rightdown.setPower(0.2);
            } else if (gamepad1.dpad_down) {
                lefttop.setPower(-0.2);
                leftdown.setPower(-0.2);
                righttop.setPower(-0.2);
                rightdown.setPower(-0.2);
            } else if (gamepad1.dpad_left) {
                lefttop.setPower(-0.2);
                leftdown.setPower(0.2);
                righttop.setPower(0.2);
                rightdown.setPower(-0.2);
            } else if (gamepad1.dpad_right) {
                lefttop.setPower(0.2);
                leftdown.setPower(-0.2);
                righttop.setPower(-0.2);
                rightdown.setPower(0.2);

            }

        }
    }
}
