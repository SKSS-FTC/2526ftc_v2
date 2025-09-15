package org.firstinspires.ftc.teamcode.configuration;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * MatchConfigurationWizard provides an interface for configuring match settings
 * during the init_loop phase of autonomous programs.
 * <p>
 * Uses gamepad1 d-pad up/down to toggle between RED and BLUE alliance colors.
 */
public class MatchConfigurationWizard {
	private final MatchSettings matchSettings;
	private final Gamepad gamepad1;
	private final Telemetry telemetry;
	
	// Track previous button states for edge detection
	private boolean lastDpadUp = false;
	private boolean lastDpadDown = false;
	
	/**
	 * Creates a new MatchConfigurationWizard
	 *
	 * @param matchSettings The MatchSettings instance to modify
	 * @param gamepad1      The primary gamepad for input
	 * @param telemetry     Telemetry instance for displaying current settings
	 */
	public MatchConfigurationWizard(MatchSettings matchSettings, Gamepad gamepad1, Telemetry telemetry) {
		this.matchSettings = matchSettings;
		this.gamepad1 = gamepad1;
		this.telemetry = telemetry;
	}
	
	/**
	 * Call this method repeatedly in init_loop to process input and update display
	 */
	public void refresh() {
		// Detect rising edge of dpad_up (just pressed)
		if (gamepad1.dpad_up && !lastDpadUp) {
			matchSettings.setAllianceColor(MatchSettings.AllianceColor.BLUE);
		}
		
		// Detect rising edge of dpad_down (just pressed)
		if (gamepad1.dpad_down && !lastDpadDown) {
			matchSettings.setAllianceColor(MatchSettings.AllianceColor.RED);
		}
		
		// Update telemetry display
		updateTelemetry();
		
		// Save current button states for next iteration
		lastDpadUp = gamepad1.dpad_up;
		lastDpadDown = gamepad1.dpad_down;
	}
	
	/**
	 * Updates telemetry with current configuration and instructions
	 */
	private void updateTelemetry() {
		MatchSettings.AllianceColor currentColor = matchSettings.getAllianceColor();
		
		telemetry.addLine("=== MATCH CONFIGURATION ===");
		telemetry.addLine("");
		telemetry.addData("Alliance Color", currentColor.toString());
		telemetry.addLine("");
		telemetry.addLine("Controls:");
		telemetry.addLine("  D-Pad UP   → BLUE Alliance");
		telemetry.addLine("  D-Pad DOWN → RED Alliance");
		telemetry.addLine("");
		
		// Visual indicator
		if (currentColor == MatchSettings.AllianceColor.BLUE) {
			telemetry.addLine("🔵 BLUE Alliance Selected");
		} else {
			telemetry.addLine("🔴 RED Alliance Selected");
		}
		
		telemetry.update();
	}
}
