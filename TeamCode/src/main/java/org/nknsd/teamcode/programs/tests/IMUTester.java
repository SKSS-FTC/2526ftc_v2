package org.nknsd.teamcode.programs.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.nknsd.teamcode.components.sensors.IMUSensor;
import org.nknsd.teamcode.components.sensors.PotentiometerSensor;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNProgramTrue;

import java.util.List;

@TeleOp(name = "IMU Tester", group="Tests")
public class IMUTester extends NKNProgramTrue {
    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        // IMU
        IMUSensor imu = new IMUSensor();
        components.add(imu);
        telemetryEnabled.add(imu);
    }
}
