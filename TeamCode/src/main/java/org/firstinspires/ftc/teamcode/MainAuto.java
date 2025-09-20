package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Main Auto", group = ".Competition Modes")
public class MainAuto extends OpMode {
	
	// Define the starting pose of the robot. This is the beginning of the first path.
	private final Pose startPose = new Pose(65.533, 12.244, Math.toRadians(115));
	
	// Declare all the PathChain objects for our autonomous routine.
	private PathChain startToPreload1;
	private PathChain preload1ToScore;
	private PathChain scoreToPreload2;
	private PathChain preload2ToScore;
	private PathChain scoreToPreload3;
	private PathChain preload3ToScore;
	
	private Follower follower;
	private Timer pathTimer, opmodeTimer;
	private int pathState;
	
	/**
	 * This method is where we define all of our paths.
	 * It takes the generated path segments and combines them into logical sequences.
	 */
	public void buildPaths() {
		/*
		 * Path: Start to Preload 1
		 * Description: Moves from the starting position in the far launch zone
		 * to the first preload pickup area.
		 */
		startToPreload1 = follower.pathBuilder()
				.addPath(new BezierLine(new Pose(65.533, 12.244), new Pose(35.526, 28.455)))
				.setLinearHeadingInterpolation(Math.toRadians(115), Math.toRadians(180))
				.addPath(new BezierLine(new Pose(35.526, 28.455), new Pose(18.453, 28.628)))
				.setTangentHeadingInterpolation()
				.build();
		
		/*
		 * Path: Preload 1 to Score
		 * Description: Moves from the first preload pickup area to the scoring area
		 * in the close launch zone. Uses a curve for a smooth approach.
		 */
		preload1ToScore = follower.pathBuilder()
				.addPath(new BezierCurve(new Pose(18.453, 28.628), new Pose(64.671, 44.493), new Pose(63.808, 69.499)))
				.setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(130))
				.build();
		
		/*
		 * Path: Score to Preload 2
		 * Description: Moves from the scoring area back to the second preload pickup area.
		 */
		scoreToPreload2 = follower.pathBuilder()
				.addPath(new BezierLine(new Pose(63.808, 69.499), new Pose(38.802, 54.668)))
				.setLinearHeadingInterpolation(Math.toRadians(130), Math.toRadians(180))
				.addPath(new BezierLine(new Pose(38.802, 54.668), new Pose(18.970, 54.496)))
				.setTangentHeadingInterpolation()
				.build();
		
		/*
		 * Path: Preload 2 to Score
		 * Description: Moves from the second preload pickup area to the scoring area.
		 */
		preload2ToScore = follower.pathBuilder()
				.addPath(new BezierLine(new Pose(18.970, 54.496), new Pose(52.081, 80.364)))
				.setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))
				.build();
		
		/*
		 * Path: Score to Preload 3
		 * Description: Moves from the scoring area to the third and final preload pickup area.
		 */
		scoreToPreload3 = follower.pathBuilder()
				.addPath(new BezierLine(new Pose(52.081, 80.364), new Pose(19.143, 80.019)))
				.setConstantHeadingInterpolation(Math.toRadians(180))
				.build();
		
		/*
		 * Path: Preload 3 to Score
		 * Description: Moves from the third preload pickup area to the scoring area for the final time.
		 */
		preload3ToScore = follower.pathBuilder()
				.addPath(new BezierLine(new Pose(19.143, 80.019), new Pose(40.354, 92.091)))
				.setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(126))
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
					// Now, move to the scoring location.
					follower.followPath(preload1ToScore);
					setPathState(2);
				}
				break;
			case 2:
				// When the robot is done moving, it's at the scoring location.
				if (!follower.isBusy()) {
					// TODO: Add your action to score the first element.
					// Now, move to the second preload location.
					follower.followPath(scoreToPreload2);
					setPathState(3);
				}
				break;
			case 3:
				// At the second preload location.
				if (!follower.isBusy()) {
					// TODO: Add your action to pick up the second preload element.
					// Move to the scoring location.
					follower.followPath(preload2ToScore);
					setPathState(4);
				}
				break;
			case 4:
				// At the scoring location again.
				if (!follower.isBusy()) {
					// TODO: Add your action to score the second element.
					// Move to the third preload location.
					follower.followPath(scoreToPreload3);
					setPathState(5);
				}
				break;
			case 5:
				// At the third preload location.
				if (!follower.isBusy()) {
					// TODO: Add your action to pick up the third preload element.
					// Move to the scoring location for the last time.
					follower.followPath(preload3ToScore);
					setPathState(6);
				}
				break;
			case 6:
				// At the final scoring location.
				if (!follower.isBusy()) {
					// TODO: Add your action to score the final element.
					// Set state to -1 to stop the state machine.
					setPathState(-1);
				}
				break;
		}
	}
	
	/**
	 * Changes the state of the path and resets the path timer.
	 **/
	public void setPathState(int pState) {
		pathState = pState;
		pathTimer.resetTimer();
	}
	
	/**
	 * This is the main loop of the OpMode, it will run repeatedly after clicking "Play".
	 **/
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
	 **/
	@Override
	public void init() {
		pathTimer = new Timer();
		opmodeTimer = new Timer();
		opmodeTimer.resetTimer();
		
		// Initialize the follower with hardware map and constants.
		follower = Constants.createFollower(hardwareMap);
		// Build all the paths for the autonomous routine.
		buildPaths();
		// Set the robot's starting pose.
		follower.setStartingPose(startPose);
	}
	
	/**
	 * This method is called continuously after Init while waiting for "play".
	 **/
	@Override
	public void init_loop() {
	}
	
	/**
	 * This method is called once at the start of the OpMode.
	 **/
	@Override
	public void start() {
		opmodeTimer.resetTimer();
		// Set the initial path state to start the autonomous sequence.
		setPathState(0);
	}
	
	/**
	 * This method is called once when the OpMode is stopped.
	 **/
	@Override
	public void stop() {
	}
}
