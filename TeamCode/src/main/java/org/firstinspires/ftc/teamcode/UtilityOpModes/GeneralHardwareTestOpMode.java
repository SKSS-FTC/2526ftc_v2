package org.firstinspires.ftc.teamcode.UtilityOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import java.util.ArrayList;
import java.util.List;


@TeleOp(name = "Manual Hardware Test",group = "Utility")
public class GeneralHardwareTestOpMode extends OpMode {

    private List<HardwareDevice> devices;
    private List<HardwareDevicePlus> devicePluses;
    private int listSize;


    private int curDevice = 0;
    private boolean lastTickRBChanged = false;
    private boolean lastTickLBChanged = false;


    @Override
    public void init(){
        devices = hardwareMap.getAll(HardwareDevice.class);
        devicePluses = new ArrayList<HardwareDevicePlus>();
        for (HardwareDevice device:devices){
            devicePluses.add(new HardwareDevicePlus(device,telemetry,hardwareMap));
        }
        listSize = devicePluses.size();
    }

    @Override
    public void loop(){
        if (gamepad1.right_bumper&&!lastTickRBChanged){
            curDevice++;
            lastTickRBChanged = true;
            lastTickLBChanged = false;
        } else if (gamepad1.left_bumper&&!lastTickLBChanged){
            curDevice--;
            lastTickLBChanged = true;
            lastTickRBChanged = false;
        } else {
            lastTickLBChanged = false;
            lastTickRBChanged = false;
        }

        lastTickRBChanged = gamepad1.right_bumper;
        lastTickLBChanged = gamepad1.left_bumper;

        devicePluses.get(curDevice%listSize).changePos(gamepad1.right_stick_y);
        telemetry.update();
    }
}
