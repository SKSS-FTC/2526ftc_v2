package org.firstinspires.ftc.teamcode.opmode.example;

import org.firstinspires.ftc.teamcode.config.subsystem.Camera;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "aprilTagTest2")
public class AprilTagTest2 extends LinearOpMode {
    private Camera camera;

    public void runOpMode(){
        camera = new Camera(hardwareMap);
        camera.enableCamera();
        waitForStart();
        while(opModeIsActive()){
            camera.update();
            if (camera.getID()!= 0){
                telemetry.addData("ID", camera.getID());
                telemetry.addData("x", camera.getX());
                telemetry.addData("y", camera.getY());
                telemetry.addData("z", camera.getZ());
                telemetry.addData("yaw", camera.getYaw());
                telemetry.addData("pitch", camera.getPitch());
                telemetry.addData("roll", camera.getRoll());
            }else {
                telemetry.addData("ID", "unknown");
                telemetry.addData("x", "unknown");
                telemetry.addData("y","unknown");
                telemetry.addData("z", "unknown");
                telemetry.addData("yaw", "unknown");
                telemetry.addData("pitch", "unknown");
                telemetry.addData("roll","unknown");
            }
            telemetry.update();
        }
    }
}
