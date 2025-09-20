package org.firstinspires.ftc.teamcode.software;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Drivetrain class refactored to use the PedroPathing V2 library.
 * It abstracts away direct motor control in favor of the Follower API for
 * both manual (tele-op) and autonomous movement.
 */
public class Drivetrain {
	
	public final Follower follower;
	// Define field-centric poses for autonomous targets.
	// TODO: Tune these coordinates for your actual field and alliance.
	private final Map<Position, Pose> positionPoses = new HashMap<>();
	private State state;
	
	/**
	 * Initializes the Drivetrain and the PedroPathing Follower.
	 *
	 * @param hardwareMap The robot's hardware map.
	 */
	public Drivetrain(HardwareMap hardwareMap) {
		// The Constants class now holds all hardware and tuning configurations.
		follower = Constants.createFollower(hardwareMap);
		follower.setStartingPose(new Pose()); // Set a default starting pose at (0,0,0)
		switchToManual(); // Start in manual control mode.
		
		// Initialize the poses for each predefined position
		positionPoses.put(Position.CLOSE_SHOOT, new Pose(24, 48, Math.toRadians(45)));
		positionPoses.put(Position.FAR_SHOOT, new Pose(72, 48, Math.toRadians(135)));
		positionPoses.put(Position.HUMAN_PLAYER, new Pose(0, 72, Math.toRadians(90)));
		positionPoses.put(Position.SECRET_TUNNEL, new Pose(120, 24, Math.toRadians(0)));
	}
	
	/**
	 * This method MUST be called in the main loop of your OpMode to keep the
	 * follower's internal state and localization updated.
	 */
	public void update() {
		follower.update();
		
		// When an automated movement (GOTO or AIMING) is finished,
		// automatically switch back to manual control.
		if ((state == State.GOTO || state == State.AIMING) && !follower.isBusy()) {
			switchToManual();
		}
	}
	
	/**
	 * Implements mecanum drive using the PedroPathing Follower.
	 *
	 * @param drivePower   Forward/backward power (-1.0 to 1.0).
	 * @param strafePower  Left/right strafe power (-1.0 to 1.0).
	 * @param rotation     Rotational power (-1.0 to 1.0).
	 * @param robotCentric True for robot-centric, false for field-centric.
	 */
	public void mecanumDrive(double drivePower, double strafePower, double rotation, boolean robotCentric) {
		if (state != State.MANUAL) {
			return; // Automation is handling driving, so ignore manual input.
		}
		// Gamepad inputs are typically inverted (up on stick is negative).
		// The Follower expects a standard coordinate system (forward is positive).
		follower.setTeleOpDrive(-drivePower, -strafePower, -rotation, robotCentric);
	}
	
	/**
	 * Overloaded mecanumDrive for robot-centric control by default.
	 */
	public void mecanumDrive(double drivePower, double strafePower, double rotation) {
		mecanumDrive(drivePower, strafePower, rotation, true);
	}
	
	/**
	 * Moves the robot to correct for a given offset from a target (e.g., from an AprilTag).
	 * This calculates a field-centric target pose based on the robot's current pose and
	 * the robot-centric offsets, then creates and follows a path to it.
	 *
	 * @param offsetX       The robot's lateral offset from the target. Positive is to the right.
	 * @param offsetY       The robot's forward offset from the target. Positive is in front.
	 * @param offsetHeading The robot's heading offset from the target. Positive is clockwise.
	 */
	public void interpolateToOffset(double offsetX, double offsetY, double offsetHeading) {
		this.state = State.AIMING;
		Pose currentPose = follower.getPose();
		double currentHeading = currentPose.getHeading();
		
		// We want to move by (-offsetX, -offsetY) in the robot's reference frame.
		// Convert this robot-centric displacement into the field-centric frame.
		double robotFrameDx = -offsetX;
		double robotFrameDy = -offsetY;
		
		double fieldFrameDx = robotFrameDx * Math.cos(currentHeading) - robotFrameDy * Math.sin(currentHeading);
		double fieldFrameDy = robotFrameDx * Math.sin(currentHeading) + robotFrameDy * Math.cos(currentHeading);
		
		// Calculate the absolute target pose in the field frame.
		Pose targetPose = new Pose(
				currentPose.getX() + fieldFrameDx,
				currentPose.getY() + fieldFrameDy,
				currentHeading - offsetHeading
		);
		
		goTo(targetPose);
	}
	
	/**
	 * Commands the robot to follow a path to a predefined position.
	 *
	 * @param position The target position from the Position enum.
	 */
	public void goTo(Position position) {
		Pose targetPose = positionPoses.get(position);
		
		if (targetPose == follower.getCurrentPath().endPose()) {
			switchToManual();
		}
		if (targetPose != null) {
			goTo(targetPose);
		}
	}
	
	/**
	 * Commands the robot to follow a path to a specific field-centric pose.
	 *
	 * @param targetPose The absolute target pose.
	 */
	public void goTo(Pose targetPose) {
		this.state = State.GOTO;
		PathChain path = follower.pathBuilder()
				.addPath(new Path(new BezierLine(follower::getPose, targetPose)))
				.build();
		follower.followPath(path);
	}
	
	/**
	 * Switches the drivetrain to manual (tele-op) control mode.
	 * This will stop any active path following.
	 */
	public void switchToManual() {
		this.state = State.MANUAL;
		follower.startTeleopDrive();
	}
	
	/**
	 * @return The current state of the drivetrain (MANUAL, AIMING, GOTO).
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * @return true if the follower is busy following a path.
	 */
	public boolean isBusy() {
		return follower.isBusy();
	}
	
	/**
	 * @return The robot's current estimated pose (x, y, heading) on the field.
	 */
	public Pose getPose() {
		return follower.getPose();
	}
	
	public enum Position {
		CLOSE_SHOOT,
		FAR_SHOOT,
		HUMAN_PLAYER,
		SECRET_TUNNEL,
	}
	
	public enum State {
		MANUAL,
		AIMING,
		GOTO,
	}
}
