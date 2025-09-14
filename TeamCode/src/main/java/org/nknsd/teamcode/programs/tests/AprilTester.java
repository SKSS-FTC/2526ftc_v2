package org.nknsd.teamcode.programs.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.nknsd.teamcode.components.sensors.AprilTag;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNProgram;

import java.util.List;
@TeleOp(name = "AprilTagTester", group="Tests")
public class AprilTester extends NKNProgram {
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        AprilTag aprilTag = new AprilTag();
        components.add(aprilTag);
        telemetryEnabled.add(aprilTag);
    }
}
