package org.firstinspires.ftc.teamcode.autonomous.actions;

import org.firstinspires.ftc.teamcode.autonomous.AutonomousAction;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.MechanismManager;

/**
 * Action that runs the intake mechanism.
 * Can be configured to run for a specific duration or indefinitely.
 */
public class IntakeAction implements AutonomousAction {
	private final boolean stopOnComplete;
	
	public IntakeAction(boolean stopOnComplete) {
		this.stopOnComplete = stopOnComplete;
	}
	
	public IntakeAction() {
		this(false);
	}
	
	@Override
	public void initialize(MechanismManager mechanisms) {
		Intake intake = mechanisms.get(Intake.class);
		if (intake != null) {
			intake.in();
		}
	}
	
	@Override
	public boolean execute(MechanismManager mechanisms) {
		// This action completes immediately - it just starts the intake
		// The intake will continue running until stopped by another action
		return true;
	}
	
	@Override
	public void end(MechanismManager mechanisms, boolean interrupted) {
		if (stopOnComplete) {
			Intake intake = mechanisms.get(Intake.class);
			if (intake != null) {
				intake.stop();
			}
		}
	}
	
	@Override
	public String getName() {
		return "Intake" + (stopOnComplete ? "AndStop" : "Start");
	}
}
