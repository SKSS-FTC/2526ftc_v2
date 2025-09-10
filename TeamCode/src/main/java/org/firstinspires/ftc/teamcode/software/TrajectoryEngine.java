package org.firstinspires.ftc.teamcode.software;


import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.hardware.submechanisms.LimelightManager;


public class TrajectoryEngine {
    private final GoBildaPinpointDriver pinpoint;
    private final MatchSettings matchSettings;
    private final LimelightManager limelightManager;

    public TrajectoryEngine(LimelightManager limelightManager, GoBildaPinpointDriver pinpoint, MatchSettings matchSettings) {
        this.limelightManager = limelightManager;
        this.pinpoint = pinpoint;
        this.matchSettings = matchSettings;
    }
}
