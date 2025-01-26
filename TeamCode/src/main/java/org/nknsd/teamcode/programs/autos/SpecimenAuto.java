package org.nknsd.teamcode.programs.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.nknsd.teamcode.autoSteps.AutoStepAbsoluteControl;
import org.nknsd.teamcode.autoSteps.AutoStepChangeMaxSpeed;
import org.nknsd.teamcode.autoSteps.AutoStepExtendSpecimenArm;
import org.nknsd.teamcode.autoSteps.AutoStepExtendSpecimenArmSynced;
import org.nknsd.teamcode.autoSteps.AutoStepMoveBackwardWithSensor;
import org.nknsd.teamcode.autoSteps.AutoStepMoveForwardWithSensor;
import org.nknsd.teamcode.autoSteps.AutoStepMoveForwardWithSensorSmart;
import org.nknsd.teamcode.autoSteps.AutoStepMoveNRotate;
import org.nknsd.teamcode.autoSteps.AutoStepRelativeMove;
import org.nknsd.teamcode.autoSteps.AutoStepSpecimenClaw;
import org.nknsd.teamcode.autoSteps.AutoStepSpecimenRotate;
import org.nknsd.teamcode.components.handlers.SpecimenClawHandler;
import org.nknsd.teamcode.components.handlers.SpecimenExtensionHandler;
import org.nknsd.teamcode.components.handlers.SpecimenRotationHandler;
import org.nknsd.teamcode.components.sensors.DistSensor;
import org.nknsd.teamcode.frameworks.NKNAutoStep;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.autoSteps.AutoStepMove;
import org.nknsd.teamcode.autoSteps.AutoStepSleep;
import org.nknsd.teamcode.components.handlers.ExtensionHandler;
import org.nknsd.teamcode.components.sensors.FlowSensor;
import org.nknsd.teamcode.components.sensors.IMUSensor;
import org.nknsd.teamcode.components.handlers.IntakeSpinnerHandler;
import org.nknsd.teamcode.components.sensors.PotentiometerSensor;
import org.nknsd.teamcode.components.handlers.RotationHandler;
import org.nknsd.teamcode.components.handlers.WheelHandler;
import org.nknsd.teamcode.components.utility.AutoHeart;
import org.nknsd.teamcode.frameworks.NKNProgramTrue;
import org.nknsd.teamcode.helperClasses.AutoSkeleton;

import java.util.LinkedList;
import java.util.List;

@Autonomous(name = "Score Specimen on Bar (IN DEV)")
public class SpecimenAuto extends NKNProgramTrue {
    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        // Step List
        List<NKNAutoStep> stepList = new LinkedList<NKNAutoStep>();


        // Core mover
        AutoSkeleton autoSkeleton = new AutoSkeleton(0.3, 0.8, 1);

        AutoHeart autoHeart = new AutoHeart(stepList);
        components.add(autoHeart);
        telemetryEnabled.add(autoHeart);


        // Sensors
        FlowSensor flowSensor = new FlowSensor();
        components.add(flowSensor);
        telemetryEnabled.add(flowSensor);

        IMUSensor imuSensor = new IMUSensor();
        components.add(imuSensor);
        //telemetryEnabled.add(imuComponent);

        PotentiometerSensor potentiometerSensor = new PotentiometerSensor();
        components.add(potentiometerSensor);

        DistSensor sensorForDist = new DistSensor("sensorForDist");
        components.add(sensorForDist);

        DistSensor sensorBackDist = new DistSensor("sensorBackDist");
        components.add(sensorBackDist);


        // Wheel Handler
        WheelHandler wheelHandler = new WheelHandler();
        components.add(wheelHandler);


        // Arm Stuff
        RotationHandler rotationHandler = new RotationHandler ();
        components.add(rotationHandler);

        ExtensionHandler extensionHandler = new ExtensionHandler();
        components.add(extensionHandler);

        IntakeSpinnerHandler intakeSpinnerHandler = new IntakeSpinnerHandler();
        components.add(intakeSpinnerHandler);


        // Specimen Stuff
        SpecimenRotationHandler specimenRotationHandler = new SpecimenRotationHandler();
        components.add(specimenRotationHandler);

        SpecimenExtensionHandler specimenExtensionHandler = new SpecimenExtensionHandler();
        components.add(specimenExtensionHandler);

        SpecimenClawHandler specimenClawHandler = new SpecimenClawHandler();
        components.add(specimenClawHandler);
        telemetryEnabled.add(specimenClawHandler);


        // Linking
        rotationHandler.link(potentiometerSensor, extensionHandler);
        extensionHandler.link(rotationHandler);

        autoSkeleton.link(wheelHandler, rotationHandler, extensionHandler, intakeSpinnerHandler, flowSensor, imuSensor);
        autoSkeleton.specimenLink(specimenExtensionHandler, specimenRotationHandler, specimenClawHandler);
        autoSkeleton.distSensorLink(sensorForDist, sensorBackDist);
        autoSkeleton.setOffset(new double[]{0.0, 0.0}, 180);
        specimenClawHandler.link(specimenRotationHandler);
        specimenExtensionHandler.link(specimenClawHandler, specimenRotationHandler);
        assembleList(stepList, autoHeart, autoSkeleton);
    }

    private void assembleList(List<NKNAutoStep> stepList, AutoHeart autoHeart, AutoSkeleton autoSkeleton) {
        // Declare steps
        AutoStepSleep sleep = new AutoStepSleep(300);

        AutoStepAbsoluteControl moveToBar = new AutoStepAbsoluteControl(-0.4332, 1.15, 0);
        AutoStepMoveBackwardWithSensor approachBar = new AutoStepMoveBackwardWithSensor(6, .1, 0.7);
        AutoStepRelativeMove clipApproach = new AutoStepRelativeMove(0,-0.24,100);
        AutoStepAbsoluteControl moveToB2 = new AutoStepAbsoluteControl(1.3, 1, 0);
        AutoStepMove moveUp = new AutoStepMove(0, 1);
        AutoStepAbsoluteControl moveTo1stSample = new AutoStepAbsoluteControl(1.6, 2.4861, 0);
        AutoStepMove depositSample = new AutoStepMove(0, -1.801);
        AutoStepAbsoluteControl moveTo2ndSample = new AutoStepAbsoluteControl(2.15, 2.4861, 0);
        //AutoStepAbsoluteControl moveTo3rdSample = new AutoStepAbsoluteControl(2.4, 2.4861, 0);
        AutoStepAbsoluteControl prepareFor1stPickup = new AutoStepAbsoluteControl(1.6008, 0.5233, 0);
        AutoStepMoveForwardWithSensorSmart approachPickup = new AutoStepMoveForwardWithSensorSmart(17, 0.2, .4);
        AutoStepRelativeMove alignSpecimen = new AutoStepRelativeMove(-0.3, 0, 400);
        AutoStepMoveNRotate rotateToEnd = new AutoStepMoveNRotate(0, 0, -90);
        AutoStepMove slightEndAdjust = new AutoStepMove(-.2, 0);

        AutoStepSpecimenRotate rotateToDeposit = new AutoStepSpecimenRotate(SpecimenRotationHandler.SpecimenRotationPositions.BACK);
        AutoStepSpecimenRotate rotateToCollect = new AutoStepSpecimenRotate(SpecimenRotationHandler.SpecimenRotationPositions.FORWARD);

        AutoStepExtendSpecimenArm extendToReady = new AutoStepExtendSpecimenArm(SpecimenExtensionHandler.SpecimenExtensionPositions.SPECIMEN_READY);
        AutoStepExtendSpecimenArmSynced extendToClip = new AutoStepExtendSpecimenArmSynced(SpecimenExtensionHandler.SpecimenExtensionPositions.SPECIMEN_CLIP);
        AutoStepExtendSpecimenArm extendToRest = new AutoStepExtendSpecimenArm(SpecimenExtensionHandler.SpecimenExtensionPositions.RESTING);

        AutoStepSpecimenClaw grip = new AutoStepSpecimenClaw(SpecimenClawHandler.ClawPositions.GRIP);
        AutoStepSpecimenClaw release = new AutoStepSpecimenClaw(SpecimenClawHandler.ClawPositions.RELEASE);

        AutoStepChangeMaxSpeed slowSpeed = new AutoStepChangeMaxSpeed(0.3);
        AutoStepChangeMaxSpeed normalSpeed = new AutoStepChangeMaxSpeed(0.6);


        // Create path
        // Approach bar and align arm
        stepList.add(grip);
        stepList.add(moveToBar);

        // Deposit 1st specimen
        stepList.add(extendToReady);
        stepList.add(approachBar);
        stepList.add(grip);
        stepList.add(extendToClip);
        stepList.add(sleep);
        stepList.add(sleep);
        stepList.add(clipApproach);
        stepList.add(sleep);
        stepList.add(release);

        // Transition
        stepList.add(rotateToCollect);
        stepList.add(extendToRest);

        // Push 1st blue
        stepList.add(normalSpeed);
        stepList.add(moveToB2);
        stepList.add(moveUp);
        stepList.add(moveTo1stSample);
        stepList.add(depositSample);

        // Push 2nd blue
        stepList.add(moveTo1stSample);
        stepList.add(moveTo2ndSample);
        stepList.add(depositSample);

        // Grab 2nd specimen
        stepList.add(prepareFor1stPickup);
        stepList.add(slowSpeed);
        stepList.add(approachPickup);
        //temporary sleep added below by karsten to observe potential issues
        stepList.add(sleep);
        stepList.add(alignSpecimen);
        stepList.add(sleep);
        stepList.add(grip);
        stepList.add(sleep);
        stepList.add(sleep);

        // Deposit 2nd specimen
        stepList.add(rotateToDeposit);
        stepList.add(normalSpeed);
        stepList.add(moveToBar);
        stepList.add(slightEndAdjust);
        //actual deposit
        stepList.add(extendToReady);
        stepList.add(approachBar);
        stepList.add(grip);

        stepList.add(extendToClip);
        stepList.add(sleep);
        stepList.add(sleep);
        stepList.add(clipApproach);
        stepList.add(sleep);
        stepList.add(release);

        stepList.add(rotateToEnd);
        stepList.add(extendToRest);
        stepList.add(sleep);



        autoHeart.linkSteps(stepList, autoSkeleton);
    }
}
