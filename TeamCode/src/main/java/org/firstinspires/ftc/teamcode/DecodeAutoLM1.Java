    package org.firstinspires.ftc.teamcode;

    import org.firstinspires.ftc.teamcode.AutoConstants;

    import com.qualcomm.hardware.bosch.BNO055IMU;
    import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
    import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
    import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
    import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.hardware.DcMotorSimple;
    import com.qualcomm.robotcore.hardware.IMU;
    import com.qualcomm.robotcore.util.ElapsedTime;

    import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
    @Autonomous(name = "Auto LM1")
    public class DecodeAutoLM1 extends LinearOpMode{
        // Timer and counter variables
        private ElapsedTime timer;
        private int counter;
        
        GoBildaPinpointDriver odo;
        private AutoConstants autoConstants;
        DcMotor frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
/*      TODO: Add in the Renamed Subsystem Classes  
        private CrumblePivot crumblePivot;
        private CrumbleSlider crumbleSlider;
        private CrumbleIntakeSystem crumbleIntakeSystem;
        private CrumbleSampleLocator crumbleSampleLocator;
*/        IMU imu;

        @Override
        public void runOpMode() {
            // Initialize timer and counter
            timer = new ElapsedTime();
            counter = 0;

            initAuto();
            telemetry.addData("Init status:", "Initialized required components");
            telemetry.addData("Readiness:", "Auto Basket is ready to start");
            telemetry.update();
            waitForStart();
            if(isStopRequested()) return;
            
            // Start the counter loop
            while (opModeIsActive()) {
                // Update counter every second
                if (timer.seconds() >= 1.0) {
                    counter++;
                    timer.reset();
                    telemetry.addData("Counter:", counter);
                    telemetry.update();
                }
                
                // Small delay to prevent excessive CPU usage
                sleep(10);
            }
/*    Treading Example
            try {  //temp remove this
                Thread.sleep(5000);
            }
            catch (Exception ex) {}
            //2100 is the max for basket
*/
        }

        private void printTelemetry(String status){
            odo.update();
            telemetry.clear();
            telemetry.addData("Status", status);

            telemetry.update();
        }

        private void printTelemetry(){
            printTelemetry("Printing Current Location");
        }

        private void initAuto() {
            odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");
    // We need to change this to the new offsets
    //        odo.setOffsets(107.95, 95.25 ); //took on 12/20 by Rohan

            
            odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
            odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                    GoBildaPinpointDriver.EncoderDirection.REVERSED);
            odo.resetPosAndIMU();
            odo.recalibrateIMU();
            //Remember to change this to the new motor names
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
            imu = hardwareMap.get(IMU.class, "imu");
    //        imu = (IMU) hardwareMap.get(BNO055IMU.class, "imu");
            // Adjust the orientation parameters to match your robot
            IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                    RevHubOrientationOnRobot.UsbFacingDirection.UP));
            // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
            imu.initialize(parameters);
            imu.resetYaw();
        }

        private void driveToPos(double targetX, double targetY) {
            odo.update();
            boolean telemAdded = false;

            while (opModeIsActive()
//                    ((Math.abs(targetX - odo.getPosX()) > 30) || (Math.abs(targetY - odo.getPosY()) > 30))
            ){
                odo.update();

//                double x = 0.001*(targetX - odo.getPosX());
//                double y = -0.001*(targetY - odo.getPosY());

                double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS); // getRobotOrientationAsQuaternion().  .().firstAngle(); //.getHeading();

//                double rotY = y * Math.cos(-botHeading) - x * Math.sin(-botHeading);
//                double rotX = y * Math.sin(-botHeading) + x * Math.cos(-botHeading);

/*                if (!telemAdded) {
                    telemetry.addData("x: ", x);
                    telemetry.addData("y: ", y);
                    telemetry.addData("rotX: ", rotX);
                    telemetry.addData("rotY: ", rotY);
//                   telemetry.addData("sliderPos:", crumbleSlider.getSliderCurrentPosition() );
                    telemetry.update();
                    telemAdded = true;
                }

                if (Math.abs(rotX) < 0.15) {
                    rotX = Math.signum(rotX) * 0.15;
                }

                if (Math.abs(rotY) < 0.15) {
                    rotY = Math.signum(rotY) * 0.15;
                }
*/
          //      double denominator = Math.max(Math.abs(y) + Math.abs(x), 1);
        //        double frontLeftPower = (rotX + rotY)
            //            / denominator;
                //double backLeftPower = (rotX - rotY) / denominator;
              //  double frontRightPower = (rotX - rotY) / denominator;
            //    double backRightPower = (rotX + rotY) / denominator;

                    frontLeftMotor.setPower(AutoConstants.DRIVE_SPEED);
                backLeftMotor.setPower(AutoConstants.DRIVE_SPEED);
                frontRightMotor.setPower(AutoConstants.DRIVE_SPEED);
                backRightMotor.setPower(AutoConstants.DRIVE_SPEED);

          //      telemetry.addData("X: ", odo.getPosX());
        //        telemetry.addData("Y: ", odo.getPosY());
      //          telemetry.addData("Heading Odo: ", Math.toDegrees(odo.getHeading()));
    //            telemetry.addData("Heading IMU: ", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
                telemetry.update();
            }

            frontLeftMotor.setPower(0);
            backLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backRightMotor.setPower(0);
        }

        private void gyroTurnToAngle(double turnAngle) {
            double error, currentHeadingAngle, driveMotorsPower;
            imu.resetYaw();

            error = turnAngle;

            while (opModeIsActive() && ((error > 1) || (error < -1))) {
                odo.update();
                currentHeadingAngle = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
                error = turnAngle - currentHeadingAngle;
//                telemetry.addData("X: ", odo.getPosX());
//                telemetry.addData("Y: ", odo.getPosY());
//                telemetry.addData("Heading Odo: ", Math.toDegrees(odo.getHeading()));
                telemetry.addData("Heading IMU: ", currentHeadingAngle);
                telemetry.addData("Error: ", error);
                telemetry.update();

                /*driveMotorsPower = error / 200;

                if ((driveMotorsPower < 0.2) && (driveMotorsPower > 0)) {
                    driveMotorsPower = 0.2;
                } else if ((driveMotorsPower > -0.2) && (driveMotorsPower < 0)) {
                    driveMotorsPower = -0.2;
                }*/
                driveMotorsPower = error / 50;

                if ((driveMotorsPower < 0.35) && (driveMotorsPower > 0)) {
                    driveMotorsPower = 0.35;
                } else if ((driveMotorsPower > -0.35) && (driveMotorsPower < 0)) {
                    driveMotorsPower = -0.35;
                }
                // Positive power causes left turn
                frontLeftMotor.setPower(-driveMotorsPower);
                backLeftMotor.setPower(-driveMotorsPower);
                frontRightMotor.setPower(driveMotorsPower);
                backRightMotor.setPower(driveMotorsPower);
            }
            frontLeftMotor.setPower(0);
            backLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backRightMotor.setPower(0);


        }
    }
