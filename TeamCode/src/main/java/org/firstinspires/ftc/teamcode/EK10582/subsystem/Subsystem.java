package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class Subsystem {
    public abstract void init(boolean isAuton);
    public abstract void update(boolean isAuton);

    public abstract void stop();
    public abstract void printToTelemetry(Telemetry telemetry);


}
