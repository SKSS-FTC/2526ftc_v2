package org.firstinspires.ftc.teamcode.teleop;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Actuator;
import org.firstinspires.ftc.teamcode.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;

/**
 * Represents the Teleop OpMode
 */
@TeleOp(name = "Proto", group = "AA_main")
public class Prototyping extends LinearOpMode {
    //private Intake intake;
    //private Indexer indexer;
    //private Actuator actuator;
    private Outtake outtake;
    /**
     * Runs the OpMode.
     */
    @Override
    public void runOpMode() throws InterruptedException {
        GamepadEx gamepadEx1 = new GamepadEx(gamepad1);
        GamepadEx gamepadEx2 = new GamepadEx(gamepad2);

        //intake = new Intake(hardwareMap);
        //indexer = new Indexer(hardwareMap);
        //actuator = new Actuator(hardwareMap);
        outtake = new Outtake(hardwareMap);

        waitForStart();
        //indexer.moveTo(Indexer.IndexerState.one);
        //actuator.down();
        while (opModeIsActive()) {
            gamepadEx1.readButtons();
            gamepadEx2.readButtons();
            teleopTick(gamepadEx2, telemetry);
            telemetry.update();
        }
    }

    public void teleopTick(GamepadEx two, Telemetry telem)
    {
        telem.addData("Outtake Power: ",outtake.getPower());
        if(two.wasJustPressed(GamepadKeys.Button.A))
        {
            //intake.run(!intake.isRunning());
        }
        if(two.wasJustPressed(GamepadKeys.Button.B))
        {
            //indexer.moveTo(indexer.nextState());
        }
        if(two.wasJustPressed(GamepadKeys.Button.X))
        {
            //actuator.set(!actuator.isActivated());
        }
        if(two.wasJustPressed(GamepadKeys.Button.Y))
        {
            //indexer.quickSpin();
        }
        if(two.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)>0.01){
            outtake.run();
        }
        else {
            outtake.stop();
        }
        if(two.isDown(GamepadKeys.Button.DPAD_UP))
        {
            outtake.setPower(outtake.getPower()+0.0005);
        }
        else if(two.isDown(GamepadKeys.Button.DPAD_DOWN))
        {
            outtake.setPower(outtake.getPower()-0.0005);
        }
    }
}