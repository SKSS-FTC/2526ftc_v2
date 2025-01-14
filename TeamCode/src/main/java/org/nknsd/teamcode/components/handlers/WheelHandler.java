package org.nknsd.teamcode.components.handlers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNComponent;

public class WheelHandler implements NKNComponent {
    private final String flName = "motorFL"; // We pass in the names of the wheels during construction so that we can change them easier
    private final String frName = "motorFR";
    private final String blName = "motorBL";
    private final String brName = "motorBR";
    private final String[] invertedNames = new String[]{"motorFL", "motorBL"}; // Names in this array are reversed during initialization

    private DcMotor motorFR; private DcMotor motorBR; private DcMotor motorFL; private DcMotor motorBL;
    private int priority = 0;


    public WheelHandler(){
    }

    // Helper function to check if the given motor name needs to be reversed
    private boolean isInReverseList(String name) {
        for (String s: invertedNames) {
            if (s.equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        //Get drive motors
        motorFL = hardwareMap.dcMotor.get(flName);
        motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if (isInReverseList(flName)) { // check if the drive motor is in the list to be reversed
            motorFL.setDirection(DcMotor.Direction.REVERSE);
        }

        motorFR = hardwareMap.dcMotor.get(frName);
        motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if (isInReverseList(frName)) {
            motorFR.setDirection(DcMotor.Direction.REVERSE);
        }

        motorBL = hardwareMap.dcMotor.get(blName);
        motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if (isInReverseList(blName)) {
            motorBL.setDirection(DcMotor.Direction.REVERSE);
        }

        motorBR = hardwareMap.dcMotor.get(brName);
        motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if (isInReverseList(brName)) {
            motorBR.setDirection(DcMotor.Direction.REVERSE);
        }

        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {
    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {}

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {}

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {}

    @Override
    public void doTelemetry(Telemetry telemetry) {
        String msgString = "[" + motorFL.getPower() + ", " + motorFR.getPower() + ", " + motorBL.getPower() + ", " + motorBR.getPower() + "]";
        telemetry.addData("FL, FR, BL, BR", msgString);
    }

    public String getName() {
        return "WheelHandler";
    }


    // Key function of the class
    // Takes in y, x, and turning components of the vector, and converts them to power instructions for omni wheels
    public void relativeVectorToMotion(double y, double x, double turning) {
        relativeVectorToMotion(y, x, turning, 0);
    }

    public void relativeVectorToMotion(double y, double x, double turning, int priority) {
        if (priority >= this.priority) {
            // some intern should change all instances of rVTM to use x, y, turning instead of y, x, turning
            turning *= 0.7;
            motorBR.setPower(y + x - turning);
            motorBL.setPower(y - x + turning);
            motorFR.setPower(y - x - turning);
            motorFL.setPower(y + x + turning);
        }
    }

    public void absoluteVectorToMotion(double x, double y, double turning, double yaw) {
        absoluteVectorToMotion(x, y, turning, yaw, 0);
    }

    public void absoluteVectorToMotion(double x, double y, double turning, double yaw, int priority) {
        double angle = (yaw * Math.PI) / 180;
        double x2 = (Math.cos(angle) * x) - (Math.sin(angle) * y);
        double y2 = (Math.sin(angle) * x) + (Math.cos(angle) * y);

        relativeVectorToMotion(y2, x2, turning, priority);
    }

    @Deprecated //this code does some strange behavior. Keeping it in in case it's still wanted but it's not used anywhere.
    public void collyMotion(double x, double y, double turning, double yaw, Telemetry telemetry) {
        double angle = (yaw * Math.PI) / 180;
        double x2 = (Math.cos(angle) * x);
        double y2 = (Math.sin(angle) * x);

        relativeVectorToMotion(y2 + y, x2, turning);
    }
    // Unless usage is needed this fnct won't be integrated into the priority system.


    // The wheel handler has a priority system. Requests to move the wheels require you to send a priority level with them (default 0) and if
    // it's equal to or higher than the set priority it can move. (Otherwise it can't)
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
