package org.firstinspires.ftc.teamcode.commands;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import org.firstinspires.ftc.teamcode.scheduler.Command;
import org.firstinspires.ftc.teamcode.subsystems.Pipeline;

public class CameraScan implements Command {
    private Pipeline cam;
    TelemetryPacket packet = new TelemetryPacket();
    FtcDashboard dashboard = FtcDashboard.getInstance();

    public CameraScan (Pipeline cam){
        this.cam = cam;
    }

    @Override
    public void start() {
        cam.start();
    }

    @Override
    public void update() {
        packet.put("detection", cam.isGreen() || cam.isPurple());
        dashboard.sendTelemetryPacket(packet);
        cam.getColor();
    }

    @Override
    public boolean isFinished() {
        return cam.isPurple() || cam.isGreen();
    }

    @Override
    public void end() {
        cam.stop();
    }
}
