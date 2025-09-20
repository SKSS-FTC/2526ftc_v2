package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.configuration.Settings;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Main Auto", group = ".Competition Modes")
public class MainAuto extends OpMode {
	
	private Follower follower;
	private Timer pathTimer, opmodeTimer;
	private int pathState;
	
	// Declare all the PathChain objects for our autonomous routine.
	private PathChain startToPreload1;
	private PathChain preload1ToScore;
	private PathChain scoreToPreload2;
	private PathChain preload2ToScore;
	private PathChain scoreToPreload3;
	private PathChain preload3ToScore;
	
	/**
	 * This method is where we define all of our paths using constants from the Settings class.
	 * It takes the generated path segments and combines them into logical sequences.
	 */
	public void buildPaths() {
		// Path constants are now stored in Settings.Autonomous for easy tuning.
		// This keeps the logic here clean and focused on the sequence of actions.
		
		startToPreload1 = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.P_START, Settings.Autonomous.P_WAYPOINT_1))
				.setLinearHeadingInterpolation(Settings.Autonomous.H_START, Settings.Autonomous.H_PRELOAD_1)
				.addPath(new BezierLine(Settings.Autonomous.P_WAYPOINT_1, Settings.Autonomous.P_PRELOAD_1_PICKUP))
				.setTangentHeadingInterpolation()
				.build();
		
		preload1ToScore = follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.P_PRELOAD_1_PICKUP, Settings.Autonomous.P_SCORE_1_APPROACH, Settings.Autonomous.P_SCORE_1))
				.setLinearHeadingInterpolation(Settings.Autonomous.H_PRELOAD_1, Settings.Autonomous.H_SCORE_1)
				.build();
		
		scoreToPreload2 = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.P_SCORE_1, Settings.Autonomous.P_WAYPOINT_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.H_SCORE_1, Settings.Autonomous.H_PRELOAD_2)
				.addPath(new BezierLine(Settings.Autonomous.P_WAYPOINT_2, Settings.Autonomous.P_PRELOAD_2_PICKUP))
				.setTangentHeadingInterpolation()
				.build();
		
		preload2ToScore = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.P_PRELOAD_2_PICKUP, Settings.Autonomous.P_SCORE_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.H_PRELOAD_2, Settings.Autonomous.H_SCORE_2)
				.build();
		
		scoreToPreload3 = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.P_SCORE_2, Settings.Autonomous.P_PRELOAD_3_PICKUP))
				.setConstantHeadingInterpolation(Settings.Autonomous.H_PRELOAD_3)
				.build();
		
		preload3ToScore = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.P_PRELOAD_3_PICKUP, Settings.Autonomous.P_SCORE_3))
				.setLinearHeadingInterpolation(Settings.Autonomous.H_PRELOAD_3, Settings.Autonomous.H_SCORE_3)
				.build();
	}
	
	/**
	 * This is the state machine for the autonomous path. It executes each path in sequence.
	 */
	public void autonomousPathUpdate() {
		switch (pathState) {
			case 0:
				// Start following the first path from the start to preload 1.
				follower.followPath(startToPreload1);
				setPathState(1);
				break;
			case 1:
				// When the robot is done moving, it's at the first preload location.
				if (!follower.isBusy()) {
					// TODO: Add your action to pick up the first preload element.
					follower.followPath(preload1ToScore);
					setPathState(2);
				}
				break;
			case 2:
				// When the robot is done moving, it's at the scoring location.
				if (!follower.isBusy()) {
					// TODO: Add your action to score the first element.
					follower.followPath(scoreToPreload2);
					setPathState(3);
				}
				break;
			case 3:
				// At the second preload location.
				if (!follower.isBusy()) {
					// TODO: Add your action to pick up the second preload element.
					follower.followPath(preload2ToScore);
					setPathState(4);
				}
				break;
			case 4:
				// At the scoring location again.
				if (!follower.isBusy()) {
					// TODO: Add your action to score the second element.
					follower.followPath(scoreToPreload3);
					setPathState(5);
				}
				break;
			case 5:
				// At the third preload location.
				if (!follower.isBusy()) {
					// TODO: Add your action to pick up the third preload element.
					follower.followPath(preload3ToScore);
					setPathState(6);
				}
				break;
			case 6:
				// At the final scoring location.
				if (!follower.isBusy()) {
					// TODO: Add your action to score the final element.
					setPathState(-1);
				}
				break;
		}
	}
	
	/**
	 * Changes the state of the path and resets the path timer.
	 */
	public void setPathState(int pState) {
		pathState = pState;
		pathTimer.resetTimer();
	}
	
	/**
	 * This is the main loop of the OpMode, it will run repeatedly after clicking "Play".
	 */
	@Override
	public void loop() {
		// These methods must be called continuously to update the robot's position and path following.
		follower.update();
		autonomousPathUpdate();
		
		// Provide feedback to the Driver Hub for debugging.
		telemetry.addData("Path State", pathState);
		telemetry.addData("X", follower.getPose().getX());
		telemetry.addData("Y", follower.getPose().getY());
		telemetry.addData("Heading", Math.toDegrees(follower.getPose().getHeading()));
		telemetry.update();
	}
	
	/**
	 * This method is called once when the OpMode is initialized.
	 */
	@Override
	public void init() {
		pathTimer = new Timer();
		opmodeTimer = new Timer();
		opmodeTimer.resetTimer();
		
		// Initialize the follower with hardware map and constants.
		follower = Constants.createFollower(hardwareMap);
		// Build all the paths for the autonomous routine.
		buildPaths();
		// Set the robot's starting pose from our settings file.
		follower.setStartingPose(Settings.Autonomous.START_POSE);
	}
	
	@Override
	public void init_loop() {
	}
	
	@Override
	public void start() {
		opmodeTimer.resetTimer();
		// Set the initial path state to start the autonomous sequence.
		setPathState(0);
	}
	
	@Override
	public void stop() {
	}
}
