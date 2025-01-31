package org.nknsd.teamcode.drivers;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.autoSteps.AutoStepExtendSpecAndOrientBackDist;
import org.nknsd.teamcode.components.handlers.RotationHandler;
import org.nknsd.teamcode.components.handlers.SpecimenClawHandler;
import org.nknsd.teamcode.components.handlers.SpecimenExtensionHandler;
import org.nknsd.teamcode.components.handlers.SpecimenRotationHandler;
import org.nknsd.teamcode.components.handlers.WheelHandler;
import org.nknsd.teamcode.components.utility.GamePadHandler;
import org.nknsd.teamcode.controlSchemes.abstracts.SpecimenControlScheme;
import org.nknsd.teamcode.controlSchemes.abstracts.WheelControlScheme;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.helperClasses.AutoSkeleton;

public class SpecimenFancyDepositDriver implements NKNComponent {
    private GamePadHandler gamePadHandler;
    private SpecimenExtensionHandler specimenExtensionHandler;
    private SpecimenRotationHandler specimenRotationHandler;
    private SpecimenClawHandler specimenClawHandler;
    private WheelControlScheme controlScheme;
    private WheelHandler wheelHandler;
    private AutoSkeleton autoSkeleton;
    private AutoStepExtendSpecAndOrientBackDist fancyDepositStep = new AutoStepExtendSpecAndOrientBackDist(SpecimenExtensionHandler.SpecimenExtensionPositions.SPECIMEN_CLIP, 3.5, .07, 0.5);;

    private boolean isFancyDepositing = false;


    Runnable specFancyDeposit = new Runnable() {
        @Override
        public void run() {
            fancyDepositStep.begin();
            isFancyDepositing = true;
            wheelHandler.setPriority(0);
            autoSkeleton.setPriority(0);
        }
    };

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {
        gamePadHandler.addListener(controlScheme.specFancyDeposit(), specFancyDeposit, "Spec Fancy Deposit");
    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "SpecimenFancyDepositDriver";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        if (isFancyDepositing) {
            fancyDepositStep.run(telemetry, runtime);

            if (fancyDepositStep.isDone(runtime)) {
                wheelHandler.setPriority(0);
                autoSkeleton.setPriority(0);
                isFancyDepositing = false;
            }
        }
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        telemetry.addData("Specimen FANCY Controls", controlScheme.getName());
        telemetry.addData("Currently Trying", isFancyDepositing);
    }

    public void link (SpecimenExtensionHandler specimenExtensionHandler, GamePadHandler gamepadHandler, WheelControlScheme controlScheme, WheelHandler wheelHandler, AutoSkeleton autoSkeleton){
        this.specimenExtensionHandler = specimenExtensionHandler;
        this.gamePadHandler = gamepadHandler;
        this.controlScheme = controlScheme;
        this.wheelHandler = wheelHandler;
        this.autoSkeleton = autoSkeleton;

        fancyDepositStep.link(autoSkeleton);
    }
}
