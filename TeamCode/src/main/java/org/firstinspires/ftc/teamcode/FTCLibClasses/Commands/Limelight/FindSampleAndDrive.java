package org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Limelight;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.Subsystem;

import org.firstinspires.ftc.teamcode.FTCLibClasses.Commands.Drive.LimelightDriveCommand;


public class FindSampleAndDrive extends SequentialCommandGroup {

    private LimelightDriveCommand limelightDriveCommand;
    public FindSampleAndDrive(LimelightDriveCommand limelightDriveCommand){
        this.limelightDriveCommand = limelightDriveCommand;
        addRequirements(limelightDriveCommand.getRequirements().toArray(new Subsystem[0]));
        addCommands(
                limelightDriveCommand.getSampleFinder(),
                limelightDriveCommand
        );
    }

    public FindSampleAndDrive copy(){
        return new FindSampleAndDrive(limelightDriveCommand);
    }
}
