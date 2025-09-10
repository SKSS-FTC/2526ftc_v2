package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Indexer {

    // 0, 120, 240, 360 are the possible angles
    // since 0 and 360 are the same ball, no matter where you are,
    // you can "quick spin" to shoot all 3 balls quickly

    private final int ANGLE_ONE = 0;
    private final int ANGLE_TWO = 120;
    private final int ANGLE_THREE = 240;
    private final int ANGLE_ALT = 360;

    public enum IndexerState
    {
        //i swear these names are temporary we'll do some color coding or sum
        one,
        two,
        three,
        oneAlt
    }
    private final SimpleServo indexerServo;

    public Indexer (HardwareMap hardwareMap)
    {
        indexerServo = new SimpleServo(hardwareMap, "index",0,360);

    }

}
