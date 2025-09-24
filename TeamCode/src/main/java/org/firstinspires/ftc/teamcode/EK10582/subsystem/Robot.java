package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.EK10582.EKLinear;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.Arrays;
import java.util.List;


public class Robot {

    static Robot robot = null;

    HardwareMap hardwareMap;
    EKLinear linearOpMode;

    public DcMotorEx leftFront, leftBack, rightFront, rightBack, leftShooter, rightShooter, intakeMotor;


    //Declare subsystems here: Ex. mecanumDrive, collection, slides, sorting, etc.
    public MecanumDrive mecanumDrive = new MecanumDrive();
    public Shooter shooter = new Shooter();
    public Intake intake = new Intake();

    //Lists of active subsystems and telemetry
    public List<Subsystem> subsystems = Arrays.asList(mecanumDrive,shooter,intake);
    public List<Subsystem> telemetrySubsystems = Arrays.asList(mecanumDrive,shooter,intake);


    //Creates an arraylist called actions that stores all the actions that are currently being done


    //Resets Cycle timer whenever the cycle finishes
    public ElapsedTime cycleTimer = new ElapsedTime();

    public void init(HardwareMap hardwareMap, LinearOpMode linearOpMode) {

        //Sets the instance variable to the parameter
        //The parameters come from EKLinear
        this.hardwareMap = hardwareMap;
        this.linearOpMode = (EKLinear)linearOpMode;

        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");
//
        leftShooter = hardwareMap.get(DcMotorEx.class, "leftShooter");
        rightShooter = hardwareMap.get(DcMotorEx.class, "rightShooter");

        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");

//

        //When the motor power is set to zero it brakes instead of coasting
        leftFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);


        for(Subsystem subsystem : subsystems) {
            //initialize all the subsystems in our list of active subsystems
            subsystem.init(false);
        }

        cycleTimer.reset();
    }

    //checks if a robot object is created
    //it is unnecessary and can get complicated if there multiple robot objects
    public static Robot getInstance() {
        if(robot == null) robot = new Robot();
        return robot;
    }
    //Adds actions to the list of active actions so they can be updated


    public void update() {
        //Update every single subsystem in the subsystems array initialized earlier
        for(Subsystem subsystem : subsystems) {
            subsystem.update(linearOpMode.isAuton);
            if(linearOpMode.isStopRequested()){
                return;
            }
        }
        //Cycles through all actions and updates them


        //telemetry
        linearOpMode.allTelemetry.addData("Match Time", linearOpMode.matchTimer.milliseconds());
        linearOpMode.allTelemetry.addData("Cycle Time", cycleTimer.milliseconds());
//        linearOpMode.allTelemetry.addData("Active Actions", getActionLength());
//        linearOpMode.allTelemetry.addData("Distance Sensor", distance.getDistance(DistanceUnit.MM));
//        linearOpMode.allTelemetry.addData("actions: ", actions);
        cycleTimer.reset();
        for(Subsystem subsystem : telemetrySubsystems) {
            linearOpMode.allTelemetry.addData("  --  ", (subsystem.getClass().getSimpleName() + "  --  "));
            subsystem.printToTelemetry(linearOpMode.allTelemetry);
        }
        linearOpMode.allTelemetry.update();
    }
}