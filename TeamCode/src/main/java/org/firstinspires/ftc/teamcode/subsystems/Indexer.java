package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Indexer {

    //TODO: Optimize the algorithms, it'll work as is though

    // 0, 120, 240, 360 are the possible angles
    // since 0 and 360 are the same ball, no matter where you are,
    // you can "quick spin" to shoot all 3 balls quickly

    private final int ANGLE_ONE = 0;
    private final int ANGLE_TWO = 120;
    private final int ANGLE_THREE = 240;
    private final int ANGLE_ALT = 360;


    private IndexerState state;

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

    public void moveTo(int toPos)
    {
        switch(toPos){
            case 0:
                if(state == IndexerState.two) {
                    indexerServo.turnToAngle(ANGLE_ONE);
                    state = IndexerState.one;
                }
                if(state == IndexerState.three) {
                    indexerServo.turnToAngle(ANGLE_ALT);
                    state = IndexerState.oneAlt;
                }
                break;
            case 1:
                indexerServo.turnToAngle(ANGLE_TWO);
                state = IndexerState.two;
                break;
            case 2:
                indexerServo.turnToAngle(ANGLE_THREE);
                state = IndexerState.three;
                break;
        }
    }

}
