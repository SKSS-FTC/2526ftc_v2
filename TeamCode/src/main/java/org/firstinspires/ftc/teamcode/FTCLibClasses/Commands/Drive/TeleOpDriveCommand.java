package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Drive;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems.DriveSubsystem;

public class TeleOpDriveCommand extends CommandBase {
    private DriveSubsystem drive;

    public TeleOpDriveCommand(DriveSubsystem drive){
        this.drive = drive;
        addRequirements(drive);
    }

    @Override
    public void execute(){
        drive.driverControlDrive();
    }


}
