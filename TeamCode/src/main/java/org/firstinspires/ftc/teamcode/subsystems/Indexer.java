package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.hardware.ServoEx;

public class Indexer {
    private final double POS_1 = 0.0;
    private final double POS_2 = 0.0;
    private final double POS_3 = 0.0;
    public enum IndexerState
    {
        //i swear these names are temporary we'll do some color coding or sum
        one,
        two,
        three
    }
    private final ServoEx indexerServo;

}
