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


    /**
     * Takes in a combination of x, y, and turning powers and moves the wheels accordingly to reach that desired motion.
     * The instructions will only go through if the given priority value is greater than the set priority for the wheels.
     * This allows you to have one driver control the wheels while others have their instructions ignored.
     * The default priority is 0.
     * @param x Amount of force to move rightwards
     * @param y Amount of force to move forwards
     * @param turning Amount of force to turn clockwise
     * @param priority Given priority value to compare to the set priority
     */
    public void relativeVectorToMotion(double x, double y, double turning, int priority) {
        if (priority >= this.priority) {
            turning *= 0.7;
            motorBR.setPower(y + x - turning);
            motorBL.setPower(y - x + turning);
            motorFR.setPower(y - x - turning);
            motorFL.setPower(y + x + turning);
        }
    }

    /**
     * Takes in a combination of x, y, and turning powers and moves the wheels accordingly to reach that desired motion.
     * Uses a default priority of 0, so any call of set priority will overpower this function.
     * @param x Amount of force to move rightwards
     * @param y Amount of force to move forwards
     * @param turning Amount of force to turn clockwise
     */
    public void relativeVectorToMotion(double x, double y, double turning) {
        relativeVectorToMotion(x, y, turning, 0);
    }

    /**
     * Takes in a combination of x, y, and turning powers and moves the wheels accordingly to reach that desired motion.
     * This function differs from the relativeVectorToMotion function by having forward and rightwards be relative to the given yaw instead of to the robot.
     * Uses a default priority of 0, so any call of set priority will overpower this function.
     * @param x Amount of force to move rightwards (relative to yaw)
     * @param y Amount of force to move rightwards (relative to yaw)
     * @param turning Amount of force to turn clockwise
     * @param yaw The yaw to account for. 0 will be relative to the robot
     */
    public void absoluteVectorToMotion(double x, double y, double turning, double yaw) {
        absoluteVectorToMotion(x, y, turning, yaw, 0);
    }

    /**
     * Takes in a combination of x, y, and turning powers and moves the wheels accordingly to reach that desired motion.
     * This function differs from the relativeVectorToMotion function by having forward and rightwards be relative to the given yaw instead of to the robot.
     * The instructions will only go through if the given priority value is greater than the set priority for the wheels.
     * This allows you to have one driver control the wheels while others have their instructions ignored.
     * The default priority is 0.
     * @param x Amount of force to move rightwards (relative to yaw)
     * @param y Amount of force to move rightwards (relative to yaw)
     * @param turning Amount of force to turn clockwise
     * @param priority Given priority value to compare to the set priority
     */
    public void absoluteVectorToMotion(double x, double y, double turning, double yaw, int priority) {
        double angle = (yaw * Math.PI) / 180;
        double x2 = (Math.cos(angle) * x) - (Math.sin(angle) * y);
        double y2 = (Math.sin(angle) * x) + (Math.cos(angle) * y);

        relativeVectorToMotion(x2, y2, turning, priority);
    }

    /**
     * Sets the priority to compare when using one of the functions that use priority.
     * @param priority Given priority value to set priority to
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Returns the set priority that is compared to when using a vector to motion function
     * @return The set priority
     */
    public int getPriority() {
        return priority;
    }
}
