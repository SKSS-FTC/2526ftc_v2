package org.nknsd.teamcode.programs.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.nknsd.teamcode.autoSteps.AutoStepAbsoluteControl;
import org.nknsd.teamcode.autoSteps.AutoStepMove;
import org.nknsd.teamcode.autoSteps.magentaSteps.AutoStepMoveNRotate;
import org.nknsd.teamcode.components.handlers.JointedArmHandler;
import org.nknsd.teamcode.components.handlers.ShaiHuludHandler;
import org.nknsd.teamcode.components.handlers.WheelHandler;
import org.nknsd.teamcode.components.sensors.FlowSensor;
import org.nknsd.teamcode.components.sensors.IMUSensor;
import org.nknsd.teamcode.components.sensors.TouchSens;
import org.nknsd.teamcode.components.utility.AutoHeart;
import org.nknsd.teamcode.frameworks.NKNAutoStep;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.frameworks.NKNProgramTrue;
import org.nknsd.teamcode.helperClasses.AutoSkeleton;

import java.util.LinkedList;
import java.util.List;

@Autonomous(name = "COPY TEST Observation Zone Auto")
public class ObservationZoneCopyAuto extends NKNProgramTrue {
    @Override
    public void createComponents(List<NKNComponent> components, List<NKNComponent> telemetryEnabled) {
        // Step List
        List<NKNAutoStep> stepList = new LinkedList<NKNAutoStep>();


        // Core mover
        AutoSkeleton autoSkeleton = new AutoSkeleton(0.7, 0.3, 0.3, 1.5);

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

        TouchSens touchSens = new TouchSens("jaLimit");
        components.add(touchSens);


        // Wheel Handler
        WheelHandler wheelHandler = new WheelHandler();
        components.add(wheelHandler);


        // Shai Hulud to hold its position
        ShaiHuludHandler shaiHuludHandler = new ShaiHuludHandler();
        components.add(shaiHuludHandler);

        JointedArmHandler jointedArmHandler = new JointedArmHandler();
        components.add(jointedArmHandler);


        // Linking
        jointedArmHandler.link(touchSens);
        autoSkeleton.link(wheelHandler, flowSensor, imuSensor);
        assembleList(stepList, autoHeart, autoSkeleton);
    }

    private void assembleList(List<NKNAutoStep> stepList, AutoHeart autoHeart, AutoSkeleton autoSkeleton) {
        // Declare steps
        AutoStepAbsoluteControl moveToB2 = new AutoStepAbsoluteControl(1.3, 1, 0);
        AutoStepMove moveUp = new AutoStepMove(0, 1);
        AutoStepMoveNRotate turnToLeft = new AutoStepMoveNRotate(0, 0, -90);
        AutoStepAbsoluteControl verticalAlignWith1stSample = new AutoStepAbsoluteControl(1.3, 2.4861, -90);
        AutoStepAbsoluteControl moveTo1stSample = new AutoStepAbsoluteControl(1.6, 2.4861, -90);
        AutoStepMove depositSample = new AutoStepMove(0, -1.951);
        AutoStepAbsoluteControl moveTo2ndSample = new AutoStepAbsoluteControl(2.15, 2.4861, -90);
        AutoStepAbsoluteControl moveTo3rdSample = new AutoStepAbsoluteControl(2.375, 2.4861, -90);

        //Move forward a little
        AutoStepMove step0 = new AutoStepMove(0, 0.5);
        stepList.add(step0);

        // Push 1st blue
        stepList.add(moveToB2);
        stepList.add(moveUp);
        stepList.add(turnToLeft);
        stepList.add(verticalAlignWith1stSample);
        stepList.add(moveTo1stSample);
        stepList.add(depositSample);

        stepList.add(moveTo1stSample);
        stepList.add(moveTo2ndSample);
        stepList.add(depositSample);

        stepList.add(moveTo2ndSample);
        stepList.add(moveTo3rdSample);
        stepList.add(depositSample);

        // We'll end in the obs zone!

        autoHeart.linkSteps(stepList, autoSkeleton);
    }
}
