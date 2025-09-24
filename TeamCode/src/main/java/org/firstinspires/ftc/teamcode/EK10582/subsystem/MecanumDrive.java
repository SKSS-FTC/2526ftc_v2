package org.firstinspires.ftc.teamcode.EK10582.subsystem;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MecanumDrive extends Subsystem {
    public double ly,lx,rx;
    public double speed = SubsystemConstants.SPEED;
    public double ratio;
    public double slowMode;

    @Override
    public void init(boolean auton){
        slowMode = 0.5;
    }

    @Override
    public void update(boolean auton){
        //For async auton: make sure mecanumdrive doesn't interfere with RR
        if(auton){
            return;
        }


        //Set motor powers to linear combination of controller input vectors
        double lf = ly + lx + rx;
        double rf = ly - lx - rx;
        double lb = ly - lx + rx;
        double rb = ly + lx - rx;

        //Scales the values so none of them exceeds one while still maintaining max possible speed
        double max = Math.max(Math.max(Math.abs(lb), Math.abs(lf)), Math.max(Math.abs(rb), Math.abs(rf)));
        double magnitude = Math.sqrt((lx * lx) + (ly * ly) + (rx * rx));
        //Avoids dividing by 0
        if (max == 0) {
            ratio = 0;
        }
        //If magnitude is greater than 1 divide by max speed
        else if (magnitude > 1){
            ratio = 1/max;
        }
        //If magnitude is less than 1 scale by magnitude to maintain lower speeds
        else {
            ratio = magnitude / max * speed;
        }

        Robot.getInstance().leftFront.setPower(lf*ratio*slowMode);
        Robot.getInstance().rightFront.setPower(rf*ratio*slowMode);
        Robot.getInstance().leftBack.setPower(lb*ratio*slowMode);
        Robot.getInstance().rightBack.setPower(rb*ratio*slowMode);

    }
    @Override
    public void stop(){}

    @Override
    public void printToTelemetry(Telemetry telemetry){
        telemetry.addData("position: ", ratio);

    }

}