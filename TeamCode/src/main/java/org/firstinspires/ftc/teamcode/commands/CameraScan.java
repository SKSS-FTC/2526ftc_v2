package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.scheduler.Command;
import org.firstinspires.ftc.teamcode.subsystems.Pipeline;

public class CameraScan implements Command {
    private Pipeline cam;
    public CameraScan (Pipeline cam){
        this.cam = cam;
    }

    @Override
    public void start() {
        cam.start();
    }

    @Override
    public void update() {
        cam.update();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end() {
        cam.stop();
    }
}
