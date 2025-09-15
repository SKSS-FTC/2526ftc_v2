package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Outtake;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.LiftSubsystem;

public class LiftHighBasketCommand extends ParallelCommandGroup {
    LiftSubsystem armLift;
    GamepadEx armPad;
    int numTimes = 0;

    public LiftHighBasketCommand(LiftSubsystem armLift) {
        this.armLift = armLift;
        addRequirements(armLift);

    }

    @Override
    public void initialize() {


    }

    @Override
    public void execute() {
//        armLift.run();
        armLift.goToHighDunk();


    }

    @Override
    public void end(boolean b) {
        armLift.stop();

    }

    @Override
    public boolean isFinished() {

        return armLift.finishedHighDunk();

    }

    public LiftHighBasketCommand copy(){
        return new LiftHighBasketCommand(armLift);
    }
}


