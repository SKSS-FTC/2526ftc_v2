    package org.firstinspires.ftc.teamcode;

    import com.qualcomm.hardware.bosch.BNO055IMU;
    import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
    import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
    import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
    import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.hardware.DcMotorSimple;
    import com.qualcomm.robotcore.hardware.IMU;
    import com.qualcomm.robotcore.util.ElapsedTime;

    import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

    @Autonomous(name = "Test")
public class DecodeLM1Auto extends LinearOpMode {

    GoBildaPinpointDriver odo;
    DcMotor frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
        int counter = 0;
    @Override
    public void runOpMode() {

        initAuto();


        while (counter < 2) {
            frontLeftMotor.setPower(AutoConstants.DRIVE_SPEED);
            backLeftMotor.setPower(AutoConstants.DRIVE_SPEED);
            frontRightMotor.setPower(AutoConstants.DRIVE_SPEED);
            backRightMotor.setPower(AutoConstants.DRIVE_SPEED);
        }
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);






    }

    private void initAuto() {
        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");
        //        odo.setOffsets(101.6, 95.25 ); //these are tuned for 3110-0002-0001 Product Insight #1
        odo.setOffsets(107.95, 95.25, DistanceUnit.CM ); //took on 12/20 by Rohan
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.REVERSED);
        odo.resetPosAndIMU();
        odo.recalibrateIMU();

        frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        backRightMotor = hardwareMap.dcMotor.get("backRightMotor");

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);



        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        //        imu = (IMU) hardwareMap.get(BNO055IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);
        imu.resetYaw();

        ElapsedTime timer = new ElapsedTime();

        if (timer.seconds() >= 1.0) {
            counter++;
            timer.reset();
            telemetry.addData("Counter:", counter);
            telemetry.update();
        }




    }
}

