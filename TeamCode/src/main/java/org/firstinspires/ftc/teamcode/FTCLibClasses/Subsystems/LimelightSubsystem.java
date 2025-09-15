package org.firstinspires.ftc.teamcode.FTCLibClasses.Subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.AllianceColor;
import org.firstinspires.ftc.teamcode.LimelightColors;
import org.firstinspires.ftc.teamcode.NoSampleFoundException;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;


import java.util.function.DoubleSupplier;
import static org.firstinspires.ftc.teamcode.RobotConfig.LimelightConstants.*;

public class LimelightSubsystem extends SubsystemBase {

    private Limelight3A limelight3A;
    private Supplier<Pose> robotPose;


    private DoubleSupplier extendMotorPosition;
    private LimelightColors color;



    public LimelightSubsystem(HardwareMap hMap, Supplier<Pose> poseSupplier, DoubleSupplier extendMotorPosition, AllianceColor allianceColor) {
        limelight3A = hMap.get(Limelight3A.class,"limelight");
        limelight3A.setPollRateHz(100);
        limelight3A.start();
//        allianceColor=AllianceColor.RED;
//        if (allianceColor.equals(AllianceColor.RED)){
//            this.color = LimelightColors.RED;
//        } else {
//            this.color = LimelightColors.BLUE;
//        }
        this.robotPose = poseSupplier;
        this.extendMotorPosition =extendMotorPosition;
    }
    public void setColor(LimelightColors color){
        if (color.equals(LimelightColors.RED)){
            this.color = LimelightColors.RED;
        } else if (color.equals(LimelightColors.BLUE)){
            this.color = LimelightColors.BLUE;
        }
    }
    public Pose3D getAprilTagPose(){
        limelight3A.pipelineSwitch(5);
        limelight3A.updateRobotOrientation(robotPose.get().getHeading()*180/Math.PI);
        LLResult result= limelight3A.getLatestResult();
        if (result!= null){
            return result.getBotpose();
        }
        return new Pose3D(new Position(),new YawPitchRollAngles(AngleUnit.DEGREES,0,0,0,0));
    }

    public Pose processResults() throws NoSampleFoundException {
        limelight3A.pipelineSwitch(4);
        int inputColor = 0;
        if (color.equals(LimelightColors.RED)){
            inputColor =1;
        } else if (color.equals(LimelightColors.BLUE)){
            inputColor=2;
        }
        double[] inputs = new double[] {robotPose.get().getX(),
                robotPose.get().getY(),
                robotPose.get().getHeading(),
                extendMotorPosition.getAsDouble(),
                RobotConfig.IntakeConstants.extendMotorTicksPerInch,
                inputColor,
                0,
                0};
        limelight3A.updatePythonInputs(inputs);


        double[] results = limelight3A.getLatestResult().getPythonOutput();
        Pose pose = new Pose(results[0],results[1]);

        return pose;

    }
    public void captureSnapshot(){
        limelight3A.captureSnapshot("Submersible");
    }

}
