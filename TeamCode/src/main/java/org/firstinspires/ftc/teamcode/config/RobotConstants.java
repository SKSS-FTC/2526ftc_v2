package org.firstinspires.ftc.teamcode.config;

/**
 * @author Baron Henderson - 20077 The Indubitables
 * @version 2.0, 9/8/2024
 */

import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Encoder;
import com.qualcomm.robotcore.hardware.HardwareMap;

/** Everything that we want to store globally, for example positions of servos, motors, etc. goes in here. **/
public class RobotConstants {
    private static HardwareMap hardwareMap;

    /** Variables are positions for the claw servos. **/
    public static double closedClaw = 0.25;
    public static double openClaw = 0;
    public static double startClaw = 0.174;
    public static double groundClaw = 0.835;
    public static double scoringClaw = 0.25;
    public static double intake_P = 0.01;
}
