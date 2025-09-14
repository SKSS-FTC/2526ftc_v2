package org.nknsd.teamcode.components.handlers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNComponent;


public class RailHandler implements NKNComponent {

    DcMotor rMotor;
    DcMotor lMotor;

    public void setMotorSpeed(double speed){
        rMotor.setPower(speed);
        lMotor.setPower(-speed);
    }

    public int getRMotorPos(){
        return rMotor.getCurrentPosition();
    }
    public int getLMotorPos(){
        return -lMotor.getCurrentPosition();
    }

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        rMotor = hardwareMap.dcMotor.get("rArm");
        rMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lMotor = hardwareMap.dcMotor.get("lArm");
        lMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
        return "Rail Handler";
    }


    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        // TODO - make rail handler take in target pos and be smart to align speeds
       if (rMotor.getCurrentPosition() > 700 || lMotor.getCurrentPosition() < -700){
           rMotor.setPower(0);
           lMotor.setPower(0);
       }
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        int rMotorPos = rMotor.getCurrentPosition();
        telemetry.addData("right", rMotorPos);
        int lMotorPos = -lMotor.getCurrentPosition();
        telemetry.addData("left", lMotorPos);
    }
}
