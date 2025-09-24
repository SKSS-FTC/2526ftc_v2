package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.atlas.PrototypeFollower;
import org.firstinspires.ftc.teamcode.atlas.Waypoint;

@TeleOp(name = ".AutoTest", group = "MecanumBot")
public class AutoTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        Waypoint[] waypoints = new Waypoint[]{
                new Waypoint(0.0, 0.0, 0.0, 1.0, 1.0, false, new String[]{}),
                new Waypoint(1400.0, 0.0, 0.0, 1.0, 1.0, false, new String[]{}),
                new Waypoint(1400.0, 1400.0, 0.0, 1.0, 1.0, false, new String[]{}),
                new Waypoint(0.0, 1400.0, 0.0, 1.0, 1.0, false, new String[]{}),
                new Waypoint(0.0, 0.0, 0.0, 1.0, 1.0, false, new String[]{})
        };

        Chassis chassis = new Chassis(hardwareMap);
        PrototypeFollower drive = new PrototypeFollower(this, chassis, waypoints, java.util.Collections.emptyMap());

        waitForStart();
        drive.run();
    }
}
