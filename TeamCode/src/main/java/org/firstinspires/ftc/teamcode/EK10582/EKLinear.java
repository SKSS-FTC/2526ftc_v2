package org.firstinspires.ftc.teamcode.EK10582;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;
import org.firstinspires.ftc.teamcode.EK10582.teleop.DriverStation;

public abstract class EKLinear extends LinearOpMode {
    public Robot robot = Robot.getInstance();
    public Telemetry allTelemetry;
    public ElapsedTime matchTimer;
    public DriverStation driverStation;
    public boolean isAuton = false;

    @Override
    public void waitForStart() {
        //initing things
        allTelemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), this.telemetry);
        matchTimer = new ElapsedTime();
        driverStation = new DriverStation(gamepad1, gamepad2);
        robot.init(hardwareMap, this);

        while(!isStarted() && !isStopRequested()){
            telemetry.addData("Waiting for Start","");
            telemetry.update();
        }

        //after start
        matchTimer.reset();
    }
}
