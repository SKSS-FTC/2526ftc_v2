package org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;

public class FollowerSubsystem extends SubsystemBase {

    private Follower follower;
    private Telemetry telemetry;

    //for teleop
    private double startHeading = 3*Math.PI/2;

    public FollowerSubsystem(Follower follower){
        this.follower = follower;
    }
    public FollowerSubsystem(HardwareMap hardwareMap){
        this.follower = new Follower(hardwareMap);

    }

    public void setTelemetry(Telemetry telemetry){
        this.telemetry = telemetry;

    }

    public Follower getFollower(){
        return follower;
    }

    public void resetHeading() {
        startHeading = follower.getPoseUpdater().getLocalizer().getPose().getHeading();
    }
    public double getStartHeading(){
        return startHeading;
    }

    @Override
    public void periodic(){
        telemetry.addData("X",follower.getPose().getX());
        telemetry.addData("Y",follower.getPose().getY());
        telemetry.addData("Heading",follower.getPose().getHeading());
    }






}
