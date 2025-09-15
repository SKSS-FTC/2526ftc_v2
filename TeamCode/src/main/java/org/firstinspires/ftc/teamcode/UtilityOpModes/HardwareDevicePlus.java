package org.firstinspires.ftc.teamcode.UtilityOpModes;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class HardwareDevicePlus {
    private HardwareDevice device;
    private double sensitivity;
    private Telemetry telemetry;
    private HardwareMap hMap;

    public HardwareDevicePlus(HardwareDevice device, Telemetry telemetry, HardwareMap hMap){
        this.device = device;
        if (device instanceof DcMotor){
            sensitivity =.50;
        } else if(device instanceof Servo){
            sensitivity = .001;
        } else if (device instanceof CRServo){
            //We'll change the power to CR Servos as our variable
            sensitivity =.05;
        }

        this.telemetry =telemetry;
        this.hMap = hMap;
    }

    public void changePos(double incBy){
        double adjustedIncBy = incBy*sensitivity;

        if(device instanceof DcMotor){
            telemetry.addLine(device.getConnectionInfo()+device.getDeviceName());
            ((DcMotor) device).setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            ((DcMotor) device).setPower(adjustedIncBy);
            telemetry.addLine((String) (hMap.getNamesOf(device).toArray()[0]));
        } else {
            telemetry.addLine("Not a motor");
        }
    }
}
