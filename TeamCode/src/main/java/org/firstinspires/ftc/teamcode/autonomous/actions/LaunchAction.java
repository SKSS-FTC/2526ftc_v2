package org.firstinspires.ftc.teamcode.autonomous.actions;

import org.firstinspires.ftc.teamcode.autonomous.AutonomousAction;
import org.firstinspires.ftc.teamcode.hardware.Launcher;
import org.firstinspires.ftc.teamcode.hardware.MechanismManager;
import org.firstinspires.ftc.teamcode.hardware.Spindex;

/**
 * Action that launches samples using the launcher mechanism.
 * Waits for the spindex to empty before completing.
 */
public class LaunchAction implements AutonomousAction {
	private boolean hasSpindex;
	
	@Override
	public void initialize(MechanismManager mechanisms) {
		Launcher launcher = mechanisms.get(Launcher.class);
		if (launcher != null) {
			launcher.launch();
		}
		
		// Check if spindex exists so we know whether to wait for it
		hasSpindex = mechanisms.get(Spindex.class) != null;
	}
	
	@Override
	public boolean execute(MechanismManager mechanisms) {
		if (!hasSpindex) {
			// If no spindex, complete immediately after starting launch
			return true;
		}
		
		// Wait for spindex to be empty
		Spindex spindex = mechanisms.get(Spindex.class);
		return spindex != null && spindex.isEmpty();
	}
	
	@Override
	public void end(MechanismManager mechanisms, boolean interrupted) {
		// Launcher continues running - will be stopped by next action if needed
	}
	
	@Override
	public String getName() {
		return "Launch";
	}
}
