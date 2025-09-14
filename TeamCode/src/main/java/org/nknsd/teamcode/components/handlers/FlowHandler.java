package org.nknsd.teamcode.components.handlers;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.nknsd.teamcode.frameworks.NKNComponent;

public class FlowHandler implements NKNComponent {


    private SparkFunOTOS odometry1;
    private SparkFunOTOS odometry2;
    private SparkFunOTOS.Pose2D pos1 = new SparkFunOTOS.Pose2D(0, 0, 0);
    private SparkFunOTOS.Pose2D pos2 = new SparkFunOTOS.Pose2D(0, 0, 0);
    private SparkFunOTOS.Pose2D avPos = new SparkFunOTOS.Pose2D(0, 0, 0);
    final private SparkFunOTOS.Pose2D offset1 = new SparkFunOTOS.Pose2D(0, 0, Math.PI / 2);
    final private SparkFunOTOS.Pose2D offset2 = new SparkFunOTOS.Pose2D(0, 0, Math.PI / 2);

    private void configureSensors() {
        odometry1.setLinearUnit(DistanceUnit.INCH);
        odometry1.setAngularUnit(AngleUnit.RADIANS);
        odometry1.setOffset(offset1);
        odometry1.setLinearScalar(1.0);
        odometry1.setAngularScalar(1.0);
        odometry1.calibrateImu();
        odometry1.resetTracking();

        odometry2.setLinearUnit(DistanceUnit.INCH);
        odometry2.setAngularUnit(AngleUnit.RADIANS);
        odometry2.setOffset(offset2);
        odometry2.setLinearScalar(1.0);
        odometry2.setAngularScalar(1.0);
        odometry2.calibrateImu();
        odometry2.resetTracking();
    }

    //    both sensors
    public SparkFunOTOS.Pose2D getPosition() {
        pos1 = odometry1.getPosition();
        pos2 = odometry2.getPosition();
        avPos = new SparkFunOTOS.Pose2D();

        double hSign = -1;
        if (((Math.abs(pos1.h) + Math.abs(pos2.h)) / 2 > Math.PI / 2) || ((Math.abs(pos1.h) + Math.abs(pos2.h)) / 2 < -Math.PI / 2)) {
            if (Math.abs(pos1.h) < Math.abs(pos2.h)) {
                if (pos1.h > 0) {
                    hSign = 1;
                }
            } else {
                if (pos2.h > 0) {
                    hSign = 1;
                }
            }
            avPos.h = ((Math.abs(pos1.h) + Math.abs(pos2.h)) / 2) * hSign;
        } else {
            avPos.h = (pos1.h + pos2.h) / 2;
        }
        //   Heading/X flipped because... it works?
        avPos.h = -avPos.h;
        avPos.x = (pos1.x + pos2.x) / 2;
        avPos.y = (pos1.y + pos2.y) / 2;

        return avPos;
    }

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        odometry1 = hardwareMap.get(SparkFunOTOS.class, "flowsensor1");
        odometry2 = hardwareMap.get(SparkFunOTOS.class, "flowsensor2");
        configureSensors();
        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "Flow Handler";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        getPosition();
        telemetry.addData("od1", printPose2D(pos1));
        telemetry.addData("od2", printPose2D(pos2));
        telemetry.addData("pos", printPose2D(avPos));
    }

    static public String printPose2D(SparkFunOTOS.Pose2D pos) {
        return "x: " + String.format("%.2f", pos.x) + " y: " + String.format("%.2f", pos.y) + " h: " + String.format("%.2f", pos.h);
    }
}
