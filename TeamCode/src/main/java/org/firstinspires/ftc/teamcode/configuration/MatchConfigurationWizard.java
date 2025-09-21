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
	
	public boolean confirmed = false;
	
	
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
		if (gamepad1.dpadUpWasPressed()) {
			matchSettings.setAllianceColor(MatchSettings.AllianceColor.BLUE);
		}
		
		// Detect rising edge of dpad_down (just pressed)
		if (gamepad1.dpadDownWasPressed()) {
			matchSettings.setAllianceColor(MatchSettings.AllianceColor.RED);
		}
		
		if (gamepad1.dpadLeftWasPressed()) {
			matchSettings.setAutoStartingPosition(MatchSettings.AutoStartingPosition.CLOSE);
		}
		
		if (gamepad1.dpadRightWasPressed()) {
			matchSettings.setAutoStartingPosition(MatchSettings.AutoStartingPosition.FAR);
		}
		
		if (gamepad1.crossWasPressed()) {
			confirmed = !confirmed;
		}
		
		// Update telemetry display
		updateTelemetry();
	}
	
	/**
	 * Updates telemetry with current configuration and instructions
	 */
	private void updateTelemetry() {
		MatchSettings.AllianceColor currentColor = matchSettings.getAllianceColor();
		MatchSettings.AutoStartingPosition autoStartingPosition = matchSettings.getAutoStartingPosition();
		
		if (!confirmed) {
			telemetry.addLine("=== MATCH CONFIGURATION ===");
			telemetry.addLine("  D-Pad UP    → BLUE Alliance");
			telemetry.addLine("  D-Pad DOWN  → RED Alliance");
			telemetry.addLine("  D-Pad LEFT  → Close Starting Position");
			telemetry.addLine("  D-Pad RIGHT → Far Starting Position");
			telemetry.addLine("  CROSS       → Confirm Configuration");
		} else {
			telemetry.addLine("=== CONFIGURATION CONFIRMED ===");
			telemetry.addLine("❎ Press cross to cancel");
		}
		
		telemetry.addLine("");
		
		if (currentColor == MatchSettings.AllianceColor.BLUE) {
			telemetry.addLine("\uD83D\uDD35 BLUE Alliance Selected");
		} else {
			telemetry.addLine("\uD83D\uDD34 RED Alliance Selected");
		}
		
		if (autoStartingPosition == MatchSettings.AutoStartingPosition.CLOSE) {
			telemetry.addLine("\uD83D\uDD0D Close Starting Position Selected");
		} else {
			telemetry.addLine("\uD83D\uDD2D Far Starting Position Selected");
		}
		
		telemetry.update();
	}
}
