package org.firstinspires.ftc.teamcode.command.logic;

import com.qualcomm.robotcore.util.ElapsedTime;

public class WaitCommand implements Command {
    private double time;
    private ElapsedTime timer = new ElapsedTime();

    public WaitCommand(double time) {
        this.time = time;
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished() {
        return timer.seconds() >= time;
    }

    @Override
    public void end() {

    }
}