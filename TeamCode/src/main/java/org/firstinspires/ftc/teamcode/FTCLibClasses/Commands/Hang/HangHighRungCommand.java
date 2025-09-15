package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Hang;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.Hang.HangSubsystem;

public class HangHighRungCommand extends CommandBase {
    HangSubsystem hangLift;

        public HangHighRungCommand(HangSubsystem hangLift){
            this.hangLift = hangLift;
        }

        @Override
        public void execute(){
            hangLift.goDown();
        }
        @Override
        public void end(boolean b){
            hangLift.stop();
        }
        @Override
        public boolean isFinished(){
            return hangLift.isThere();

        }

        public HangHighRungCommand copy(){ return new HangHighRungCommand(hangLift);}
}
