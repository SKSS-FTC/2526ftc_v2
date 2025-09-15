package org.firstinspires.ftc.teamcode.FTCLibClasses;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandBase;

import java.util.function.BooleanSupplier;


public class BranchCommand extends CommandBase {

    private BooleanSupplier cond1;
    private BooleanSupplier cond2;
    private BranchCondition nextBranch = BranchCondition.NONE;


    public BranchCommand(BooleanSupplier cond1,BooleanSupplier cond2){

        this.cond1 = cond1;
        this.cond2 = cond2;
    }



    public boolean isFinished(){
        if (cond1.getAsBoolean()){
            nextBranch = BranchCondition.FIRST_BRANCH;
        } else if (cond2.getAsBoolean()){
            nextBranch = BranchCondition.SECOND_BRANCH;
        }
        return cond1.getAsBoolean()|| cond2.getAsBoolean();
    }
    public BranchCondition getNextBranch(){
        return nextBranch;
    }
    public static enum BranchCondition{
        FIRST_BRANCH,
        SECOND_BRANCH,
        NONE;
    }
}


