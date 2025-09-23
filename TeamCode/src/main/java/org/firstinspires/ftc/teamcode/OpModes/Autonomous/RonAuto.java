package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Implementations.AndroidLog;
import org.firstinspires.ftc.teamcode.Implementations.LinearOpModeImpl;
import org.firstinspires.ftc.teamcode.Implementations.SystemTimeSource;
import org.firstinspires.ftc.teamcode.Pathing.PathController;
import org.firstinspires.ftc.teamcode.Pathing.PidParamCollection;
import org.firstinspires.ftc.teamcode.Pathing.PidParams;
import org.firstinspires.ftc.teamcode.RobotHardware.Hardware;

@Autonomous
public class RonAuto extends LinearOpMode {

    // This runs as soon as init it pushed.
    @Override
    public void runOpMode() throws InterruptedException {
        hw = new Hardware(hardwareMap);
        hw.imuPos.reset();

        // wait for start to be pushed.
        waitForStart();
        LinearOpModeImpl ourOp = new LinearOpModeImpl(this);

        PathController pathing = new PathController(hw, ourOp, 0.5,
                PidParamCollection.ParamSetName.UNIT_TEST_SIM,
                new SystemTimeSource(), new AndroidLog());
    }

    private void pause() {
            sleep(250);
    }

    Hardware hw;
    double power = 1;
}
