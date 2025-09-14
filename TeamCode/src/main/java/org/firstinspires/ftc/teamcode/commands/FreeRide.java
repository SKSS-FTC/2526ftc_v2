package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.scheduler.Command;

public class FreeRide implements Command {
    private final Robot robot;

    public FreeRide(Robot robot){
        this.robot = robot;
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        double y = robot.input.getYInput();
        double x = robot.input.getXInput();
        double rx = robot.input.getRxInput();
        robot.mecanumDrive.drive(y, x, rx, robot.imu.getSin(), robot.imu.getCos());
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end() {
        robot.mecanumDrive.drive(0, 0, 0, 0, 0);
    }
}
