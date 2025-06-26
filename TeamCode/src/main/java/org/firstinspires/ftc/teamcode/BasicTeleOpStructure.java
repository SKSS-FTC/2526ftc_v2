package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/** Configuration File
 * Control Hub:
 * Port 01: leftFront
 */

@Disabled
@TeleOp(group = "Primary")

public class BasicTeleOpStructure extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        while(!isStarted()){}
        waitForStart();
        while(opModeIsActive()) {}
    }

    public void initHardware() {}
}