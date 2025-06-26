package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/** Configuration File
 * Control Hub:
 * Servo Port 00: servoOne
 */

//@Disabled
@TeleOp(group = "Primary")

public class ServoOneTest extends LinearOpMode {

    private Servo servoOne;
    private double servoOneInitPosition = 0.5;
    private double servoOnePosisitionOne = 0.0;
    private double servoOnePositionTwo = 1.0;
    private int servoOneDelay = 100;


    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        while(!isStarted()){
            servoTelemetry();
        }
        waitForStart();
        while(opModeIsActive()) {
            teleOpControls();
            servoTelemetry();
        }
    }

    public void initHardware() {
        initServoOne();
    }

    public void initServoOne(){
        servoOne = hardwareMap.get(Servo.class, "servoOne");
        servoOne.setDirection(Servo.Direction.FORWARD);
        servoOne.setPosition(servoOneInitPosition);
    }

    private void servoOneSlower(double startPosition, double endPosition, int delay){
        double range = ((endPosition - startPosition) * 100);
        for (int i = 0; i <= range; i++){
            servoOne.setPosition(startPosition);
            sleep(delay);
            startPosition = startPosition + .01;
        }
    }

    public void teleOpControls(){
        if (gamepad1.a){
            servoOne.setPosition(servoOnePosisitionOne);
        }
        if (gamepad1.b){
            servoOne.setPosition(servoOnePositionTwo);
        }
        if (gamepad2.right_bumper){
            servoOneSlower(servoOnePosisitionOne, servoOnePositionTwo, servoOneDelay);
        }
    }

    public void servoTelemetry(){
        telemetry.log().clear();
        telemetry.addData("Position", servoOne.getPosition());
        telemetry.addData("Direction", servoOne.getDirection());
        telemetry.addData("Controller", servoOne.getController());
        telemetry.addData("Port Number", servoOne.getPortNumber());
        telemetry.addData("Connection Info", servoOne.getConnectionInfo());
        telemetry.addData("Device Name", servoOne.getDeviceName());
        telemetry.addData("Manufacturer", servoOne.getManufacturer());
        telemetry.addData("Version", servoOne.getVersion());
        telemetry.addData("Version", servoOne.getClass());
        telemetry.update();
    }
}