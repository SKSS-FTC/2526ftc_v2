package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.configuration.Settings;

import java.util.HashMap;

/**
 * @noinspection CyclicClassDependency, DataFlowIssue
 */
public class Controller extends Gamepad {
	private final Gamepad gamepad;
	private final HashMap<Control, Double> previousControlState;
	private final Follower follower;
	
	private final MatchSettings matchSettings;
	
	public Controller(Gamepad gamepad, Follower follower, MatchSettings matchSettings) {
		this.gamepad = gamepad;
		this.follower = follower;
		this.matchSettings = matchSettings;
		this.previousControlState = new HashMap<>();
		// Populate the initial state in the constructor
		saveLastState();
	}
	
	public final void saveLastState() {
		for (Control control : Control.values()) {
			previousControlState.put(control, getRawValue(control));
		}
	}
	
	public final boolean wasJustPressed(Control control) {
		return getRawValue(control) != 0.0 && previousControlState.getOrDefault(control, 0.0) == 0;
	}
	
	public final boolean wasJustPressed(Action action) {
		return wasJustPressed(getControlForAction(action));
	}
	
	public final Control getControlForAction(Action action) {
		return Settings.Controls.actionControlMap.getOrDefault(action, Control.UNKNOWN);
	}
	
	public final double getProcessedValue(Control control) {
		// add value modifiers here
		double val = getRawValue(control);
		
		if (control == Control.LEFT_STICK_X) {
			val = -val;
		}
		// add more here
		
		return val;
	}
	
	public final double getProcessedValue(Action action) {
		return getProcessedValue(getControlForAction(action));
	}
	
	/**
	 * Compute robot-centric DPad contribution.
	 * Robot Y = forward/back, Robot X = strafe
	 * TODO this is still very buggy
	 */
	private double[] getRobotCentricDpad() {
		double headingRadians = follower.getHeading();
		
		double dpadNorth = getProcessedValue(Action.ABS_NORTH);
		double dpadSouth = getProcessedValue(Action.ABS_SOUTH);
		double dpadEast = getProcessedValue(Action.ABS_EAST);
		double dpadWest = getProcessedValue(Action.ABS_WEST);
		
		double fieldY = dpadNorth - dpadSouth; // North positive
		double fieldX = dpadEast - dpadWest;   // East positive
		
		double cos = Math.cos(headingRadians);
		double sin = Math.sin(headingRadians);
		
		double robotY = fieldY * cos + fieldX * sin;
		double robotX = fieldX * cos - fieldY * sin;
		
		return new double[]{robotX, robotY};
	}
	
	public final double getProcessedDrive() {
		double[] robotVec = getRobotCentricDpad();
		double drive = getProcessedValue(Action.MOVE_Y) + robotVec[1];
		return Math.max(-1, Math.min(1, drive));
	}
	
	public final double getProcessedStrafe() {
		double[] robotVec = getRobotCentricDpad();
		return getProcessedValue(Action.MOVE_X) + robotVec[0];
	}
	
	
	public final double getProcessedRotation() {
		double rotationValue = getProcessedValue(Action.ROTATE_AXIS) - getProcessedValue(Action.ROTATE_LEFT) + getProcessedValue(Action.ROTATE_RIGHT);
		// clamp wont work for me on this sdk version lol
		if (rotationValue < -1) return -1;
		if (rotationValue > 1) return 1;
		return rotationValue;
	}
	
	
	/**
	 * @noinspection OverlyComplexMethod, OverlyLongMethod - Never edit this
	 */
	private double getRawValue(Control control) {
		switch (control) {
			case LEFT_TRIGGER:
				return gamepad.left_trigger;
			case RIGHT_TRIGGER:
				return gamepad.right_trigger;
			case LEFT_STICK_X:
				return gamepad.left_stick_x;
			case LEFT_STICK_Y:
				return gamepad.left_stick_y;
			case RIGHT_STICK_X:
				return gamepad.right_stick_x;
			case RIGHT_STICK_Y:
				return gamepad.right_stick_y;
			case BACK:
				return gamepad.back ? 1 : 0;
			case START:
				return gamepad.start ? 1 : 0;
			case CIRCLE:
				return gamepad.circle ? 1 : 0;
			case CROSS:
				return gamepad.cross ? 1 : 0;
			case SQUARE:
				return gamepad.square ? 1 : 0;
			case TRIANGLE:
				return gamepad.triangle ? 1 : 0;
			case LEFT_BUMPER:
				return gamepad.left_bumper ? 1 : 0;
			case RIGHT_BUMPER:
				return gamepad.right_bumper ? 1 : 0;
			case DPAD_UP:
				return gamepad.dpad_up ? 1 : 0;
			case DPAD_DOWN:
				return gamepad.dpad_down ? 1 : 0;
			case DPAD_LEFT:
				return gamepad.dpad_left ? 1 : 0;
			case DPAD_RIGHT:
				return gamepad.dpad_right ? 1 : 0;
			case LEFT_STICK_BUTTON:
				return gamepad.left_stick_button ? 1 : 0;
			case RIGHT_STICK_BUTTON:
				return gamepad.right_stick_button ? 1 : 0;
			case GUIDE:
				return gamepad.guide ? 1 : 0;
			case OPTIONS:
				return gamepad.options ? 1 : 0;
			case TOUCHPAD:
				return gamepad.touchpad ? 1 : 0;
			case TOUCHPAD_X:
				return gamepad.touchpad_finger_1_x;
			case TOUCHPAD_Y:
				return gamepad.touchpad_finger_1_y;
			default:
				return 0;
		}
	}
	
	
	// Define your Action enum
	public enum Action {
		MOVE_Y,
		MOVE_X,
		ROTATE_LEFT,
		ROTATE_RIGHT,
		ROTATE_AXIS,
		ABS_NORTH,
		ABS_EAST,
		ABS_WEST,
		ABS_SOUTH,
		GOTO_CLOSE_SHOOT,
		GOTO_FAR_SHOOT,
		GOTO_HUMAN_PLAYER,
		GOTO_SECRET_TUNNEL,
		CANCEL_ASSISTED_DRIVING,
		INTAKE,
		RELEASE_EXTRAS,
		RELEASE_PURPLE,
		RELEASE_GREEN,
		AIM,
		LAUNCH,
		LAUNCHER_STEEPNESS_AXIS,
		LAUNCHER_ROTATION_AXIS,
		PARK_EXTEND,
		INCREMENT_CLASSIFIER_STATE,
		EMPTY_CLASSIFIER_STATE,
		UNSET,
	}
	
	public enum Control {
		TRIANGLE, CIRCLE, CROSS, SQUARE,
		DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT,
		LEFT_BUMPER, RIGHT_BUMPER,
		START, BACK, GUIDE, OPTIONS,
		LEFT_STICK_BUTTON, RIGHT_STICK_BUTTON,
		TOUCHPAD, TOUCHPAD_X, TOUCHPAD_Y,
		
		LEFT_TRIGGER, RIGHT_TRIGGER,
		LEFT_STICK_X, LEFT_STICK_Y,
		RIGHT_STICK_X, RIGHT_STICK_Y,
		
		UNKNOWN
	}
}