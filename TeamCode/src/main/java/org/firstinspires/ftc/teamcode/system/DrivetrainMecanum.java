package org.firstinspires.ftc.teamcode.system;

import com.qualcomm.hardware.bosch.BHI260IMU;
//import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMU;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import java.util.Arrays;
import java.util.List;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.utility.RobotConstants;

public class DrivetrainMecanum {

    // System HardwareMap
    private HardwareMap hardwareMap;

    // Define Hardware for subsystem
    // ----------------------------------------

    // Motor(s)
    private DcMotorEx driveFrontLeft, driveFrontRight, driveRearLeft, driveRearRight;
    private List<DcMotorEx> listMotorDrivetrain;

    // Define Hardware - Control Hub
    private RevHubOrientationOnRobot controlHubOrientation = null;
    private RevHubOrientationOnRobot.LogoFacingDirection controlHubLogoDirection = null;
    private RevHubOrientationOnRobot.UsbFacingDirection controlHubUsbDirection = null;

    // Define Hardware - IMU
    private IMU imuDevice = null;
    private IMU.Parameters imuParameters;

    private double trackHeadingRobot, trackHeadingOffset, trackHeadingError;

    // Constructor
    public DrivetrainMecanum(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        this.init();
    }

    // Init method - initialization steps for subsystem
    public void init() {

        // Telemetry - Initialize - Start
//        opMode.telemetry.addData(">", "------------------------------------");
//        opMode.telemetry.addData(">", "System: Drivetrain");
//        opMode.telemetry.addData(">", "------------------------------------");

        // Define and Initialize Motor(s)
        driveFrontLeft = hardwareMap.get(DcMotorEx.class, RobotConstants.HardwareConfiguration.kLabelDrivetrainMotorLeftFront);
        driveFrontRight = hardwareMap.get(DcMotorEx.class, RobotConstants.HardwareConfiguration.kLabelDrivetrainMotorRightFront);
        driveRearLeft = hardwareMap.get(DcMotorEx.class, RobotConstants.HardwareConfiguration.kLabelDrivetrainMotorLeftBack);
        driveRearRight = hardwareMap.get(DcMotorEx.class, RobotConstants.HardwareConfiguration.kLabelDrivetrainMotorRightBack);

        // Add Motors to Array for like configuration(s)
        listMotorDrivetrain = Arrays.asList(driveFrontLeft, driveFrontRight, driveRearLeft, driveRearRight);

        // Set common configuration for drive motor(s)
        for (DcMotorEx itemMotor : listMotorDrivetrain) {

            // clone motor configuration
            MotorConfigurationType motorConfigurationType = itemMotor.getMotorType().clone();

            // Set motor configuration properties
            motorConfigurationType.setAchieveableMaxRPMFraction(RobotConstants.Drivetrain.Configuration.kMotorAchievableMaxRpmFraction);

            // Write out motor configuration to motor
            itemMotor.setMotorType(motorConfigurationType);
        }


        // Set Zero Power mode for drivetrain
        setDriveMotorZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        // Set Non-common motor configuration(s)
        driveFrontLeft.setDirection(DcMotorEx.Direction.FORWARD);
        driveFrontRight.setDirection(DcMotorEx.Direction.REVERSE);
        driveRearLeft.setDirection(DcMotorEx.Direction.FORWARD);
        driveRearRight.setDirection(DcMotorEx.Direction.REVERSE);


        // Set motor mode(s)


        // Define IMU Device
        controlHubLogoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        controlHubUsbDirection = RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

        controlHubOrientation = new RevHubOrientationOnRobot(controlHubLogoDirection, controlHubUsbDirection);

        // Initialize the IMU board/unit on the Rev Control Hub
        imuDevice = hardwareMap.get(IMU.class, RobotConstants.HardwareConfiguration.kLabelDrivetrainIMUDeviceAlt);

        imuParameters = new IMU.Parameters(controlHubOrientation);

        // Set the Angle Unit to Radians
//        imuParameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;

        // Initialize the IMU unit
        imuDevice.initialize(imuParameters);

        // Reset Drive Motor Encoder(s)
        setDriveMotorRunMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        resetRobotHeading();

        // Telemetry - Initialize - End
//        sysOpMode.telemetry.addData(">", "------------------------------------");
//        opMode.telemetry.addData(">", "System: Drivetrain (Initialized)");
//        opMode.telemetry.addData(">", "------------------------------------");
//        opMode.telemetry.update();


    }


    // ----------------------------------------------
    // Action Methods
    // ----------------------------------------------


    public void driveFieldCentric(double axial, double lateral, double yaw, double outputPowerPercent) {

        double modMaintainMotorRatio;

        double modAxial = (axial * outputPowerPercent);  // Note: pushing stick forward gives negative value
        double modLateral = (lateral * outputPowerPercent) * RobotConstants.Drivetrain.Configuration.kMotorLateralMovementStrafingCorrection; // Mod to even out strafing
        double modYaw = (yaw * outputPowerPercent);

        // Get heading value from the IMU
//        updateOdometry();
        double botHeading = getRobotHeadingAdj();

        // Adjust the lateral and axial movements based on heading
        double adjLateral = modLateral * Math.cos(botHeading) - modAxial * Math.sin(botHeading);
        double adjAxial = modLateral * Math.sin(botHeading) + modAxial * Math.cos(botHeading);

        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.
        modMaintainMotorRatio = Math.max(Math.abs(modAxial) + Math.abs(modLateral) + Math.abs(modYaw), RobotConstants.Drivetrain.Configuration.kMotorOutputPowerMax);

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        double leftFrontPower  = (adjAxial + adjLateral + modYaw) / modMaintainMotorRatio;
        double rightFrontPower = (adjAxial - adjLateral - modYaw) / modMaintainMotorRatio;
        double leftBackPower   = (adjAxial - adjLateral + modYaw) / modMaintainMotorRatio;
        double rightBackPower  = (adjAxial + adjLateral - modYaw) / modMaintainMotorRatio;

        // Use existing function to drive both wheels.
        setDriveMotorPower(leftFrontPower, rightFrontPower, leftBackPower, rightBackPower);
    }

//    public void updateOdometry() {
//        pinpoint.update();
//    }

    public void resetRobotHeading() {

        // Set the Heading Offset to the IMU raw heading
        trackHeadingOffset = getRobotHeadingRaw();

        // Reset the Robot Heading to Zero
        trackHeadingRobot = 0;
    }

    public void resetZeroRobotHeading() {

        // Set the Heading Offset to the IMU raw heading
        imuDevice.resetYaw();
//        RobotConstants.CommonSettings.setImuTransitionAdjustment(0);
    }


    // ----------------------------------------------
    // Get Methods
    // ----------------------------------------------
    public double getRobotHeadingRaw() {
        // Variable for output heading value
        double outRobotHeadingValue;

        // Get heading value from the IMU
        // Read inverse IMU heading, as the IMU heading is CW positive
        outRobotHeadingValue = -(imuDevice.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        // Should the IMU heading be inversed? Does it matter?
        // Will need to view the heading readout on the driver hub
        return outRobotHeadingValue;
    }

    public double getRobotHeadingAdj() {

        // Variable for output heading value
        double outRobotHeadingValue;


        // TODO: Check on input directional inversion... may not need the inversion here!

        // Get heading value from the IMU
        // Read inverse IMU heading, as the IMU heading is CW positive
        outRobotHeadingValue = getRobotHeadingRaw() * -1; //+ RobotConstants.CommonSettings.getImuTransitionAdjustment();

        // Should the IMU heading be inversed? Does it matter?
        // Will need to view the heading readout on the driver hub

        //imuUnit.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle

        return outRobotHeadingValue;
    }

    // ----------------------------------------------
    // Set Methods
    // ----------------------------------------------

    public void setDriveMotorRunMode(DcMotorEx.RunMode inRunMode) {
        for (DcMotorEx itemMotor: listMotorDrivetrain) {
            itemMotor.setMode(inRunMode);
        }
    }

    public void setDriveMotorZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior inZeroPowerBehavior) {
        for (DcMotorEx itemMotor : listMotorDrivetrain) {
            itemMotor.setZeroPowerBehavior(inZeroPowerBehavior);
        }
    }

    private void setDriveMotorPower(double powerLeftFront, double powerRightFront, double powerLeftBack, double powerRightBack) {

        // Send calculated power to wheels
        driveFrontLeft.setPower(powerLeftFront);
        driveFrontRight.setPower(powerRightFront);
        driveRearLeft.setPower(powerLeftBack);
        driveRearRight.setPower(powerRightBack);
    }


}
