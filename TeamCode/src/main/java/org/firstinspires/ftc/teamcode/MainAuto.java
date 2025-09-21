package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.configuration.MatchConfigurationWizard;
import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.configuration.Settings;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Main Auto", group = ".Competition Modes")
public class MainAuto extends OpMode {
	
	private Follower follower;
	private Timer pathTimer, opmodeTimer;
	private int pathState;
	private MatchConfigurationWizard wizard;
	private MatchSettings matchSettings;
	
	// PathChain declarations with more descriptive names.
	// Far paths
	private PathChain far_StartToSpike;
	private PathChain far_SpikeToScore;
	private PathChain far_ScoreToStack;
	private PathChain far_StackToScore;
	private PathChain far_ScoreToStack2;
	private PathChain far_StackToScore2;
	private PathChain far_Park;
	
	// Close paths
	private PathChain close_StartToSpike;
	private PathChain close_SpikeToScore;
	private PathChain close_ScoreToStack;
	private PathChain close_StackToScore;
	private PathChain close_Park;
	
	
	/**
	 * This method is the dispatcher. It calls the correct path-building method
	 * based on the settings from the MatchConfigurationWizard.
	 */
	public void buildPaths() {
		MatchSettings.AutoStartingPosition startPos = matchSettings.getAutoStartingPosition();
		MatchSettings.AllianceColor alliance = matchSettings.getAllianceColor();
		
		if (startPos == MatchSettings.AutoStartingPosition.FAR) {
			if (alliance == MatchSettings.AllianceColor.RED) {
				buildRedFarPaths();
				follower.setStartingPose(Settings.Autonomous.RedFar.START);
			} else { // BLUE
				buildBlueFarPaths();
				follower.setStartingPose(Settings.Autonomous.BlueFar.START);
			}
		} else { // CLOSE
			if (alliance == MatchSettings.AllianceColor.RED) {
				buildRedClosePaths();
				follower.setStartingPose(Settings.Autonomous.RedClose.START);
			} else { // BLUE
				buildBlueClosePaths();
				follower.setStartingPose(Settings.Autonomous.BlueClose.START);
			}
		}
	}
	
	// NOTE: Far paths are retained from the original code but assigned to new variables for clarity.
	public void buildRedFarPaths() {
		far_StartToSpike = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.START, Settings.Autonomous.RedFar.WAYPOINT_1))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.START.getHeading(), Settings.Autonomous.RedFar.PRELOAD_1_PICKUP.getHeading())
				.addPath(new BezierLine(Settings.Autonomous.RedFar.WAYPOINT_1, Settings.Autonomous.RedFar.PRELOAD_1_PICKUP))
				.setTangentHeadingInterpolation()
				.build();
		
		far_SpikeToScore = follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedFar.PRELOAD_1_PICKUP, Settings.Autonomous.RedFar.SCORE_1_APPROACH, Settings.Autonomous.RedFar.SCORE_1))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRELOAD_1_PICKUP.getHeading(), Settings.Autonomous.RedFar.SCORE_1.getHeading())
				.build();
		
		far_ScoreToStack = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.SCORE_1, Settings.Autonomous.RedFar.WAYPOINT_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.SCORE_1.getHeading(), Settings.Autonomous.RedFar.PRELOAD_2_PICKUP.getHeading())
				.addPath(new BezierLine(Settings.Autonomous.RedFar.WAYPOINT_2, Settings.Autonomous.RedFar.PRELOAD_2_PICKUP))
				.setTangentHeadingInterpolation()
				.build();
		
		far_StackToScore = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.PRELOAD_2_PICKUP, Settings.Autonomous.RedFar.SCORE_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRELOAD_2_PICKUP.getHeading(), Settings.Autonomous.RedFar.SCORE_2.getHeading())
				.build();
		
		far_ScoreToStack2 = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.SCORE_2, Settings.Autonomous.RedFar.PRELOAD_3_PICKUP))
				.setConstantHeadingInterpolation(Settings.Autonomous.RedFar.PRELOAD_3_PICKUP.getHeading())
				.build();
		
		far_StackToScore2 = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.PRELOAD_3_PICKUP, Settings.Autonomous.RedFar.SCORE_3))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRELOAD_3_PICKUP.getHeading(), Settings.Autonomous.RedFar.SCORE_3.getHeading())
				.build();
		
		// Add a parking path for FAR if needed, or build an empty one.
		far_Park = follower.pathBuilder().build();
	}
	
	public void buildBlueFarPaths() {
		far_StartToSpike = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.START, Settings.Autonomous.BlueFar.WAYPOINT_1))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.START.getHeading(), Settings.Autonomous.BlueFar.PRELOAD_1_PICKUP.getHeading())
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.WAYPOINT_1, Settings.Autonomous.BlueFar.PRELOAD_1_PICKUP))
				.setTangentHeadingInterpolation()
				.build();
		
		far_SpikeToScore = follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.BlueFar.PRELOAD_1_PICKUP, Settings.Autonomous.BlueFar.SCORE_1_APPROACH, Settings.Autonomous.BlueFar.SCORE_1))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRELOAD_1_PICKUP.getHeading(), Settings.Autonomous.BlueFar.SCORE_1.getHeading())
				.build();
		
		far_ScoreToStack = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.SCORE_1, Settings.Autonomous.BlueFar.WAYPOINT_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.SCORE_1.getHeading(), Settings.Autonomous.BlueFar.PRELOAD_2_PICKUP.getHeading())
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.WAYPOINT_2, Settings.Autonomous.BlueFar.PRELOAD_2_PICKUP))
				.setTangentHeadingInterpolation()
				.build();
		
		far_StackToScore = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.PRELOAD_2_PICKUP, Settings.Autonomous.BlueFar.SCORE_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRELOAD_2_PICKUP.getHeading(), Settings.Autonomous.BlueFar.SCORE_2.getHeading())
				.build();
		
		far_ScoreToStack2 = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.SCORE_2, Settings.Autonomous.BlueFar.PRELOAD_3_PICKUP))
				.setConstantHeadingInterpolation(Settings.Autonomous.BlueFar.PRELOAD_3_PICKUP.getHeading())
				.build();
		
		far_StackToScore2 = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.PRELOAD_3_PICKUP, Settings.Autonomous.BlueFar.SCORE_3))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRELOAD_3_PICKUP.getHeading(), Settings.Autonomous.BlueFar.SCORE_3.getHeading())
				.build();
		
		far_Park = follower.pathBuilder().build();
	}
	
	/**
	 * OVERHAULED: Builds the paths for the RED CLOSE starting position.
	 */
	public void buildRedClosePaths() {
		close_StartToSpike = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.START, Settings.Autonomous.RedClose.SPIKE_MARK_CENTER))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.START.getHeading(), Settings.Autonomous.RedClose.SPIKE_MARK_CENTER.getHeading())
				.build();
		
		close_SpikeToScore = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.SPIKE_MARK_CENTER, Settings.Autonomous.RedClose.BACKDROP_SCORE))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.SPIKE_MARK_CENTER.getHeading(), Settings.Autonomous.RedClose.BACKDROP_SCORE.getHeading())
				.build();
		
		close_ScoreToStack = follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedClose.BACKDROP_SCORE, Settings.Autonomous.RedClose.WAYPOINT_CENTER, Settings.Autonomous.RedClose.STACK_PICKUP))
				.setTangentHeadingInterpolation()
				.build();
		
		close_StackToScore = follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedClose.STACK_PICKUP, Settings.Autonomous.RedClose.WAYPOINT_CENTER, Settings.Autonomous.RedClose.BACKDROP_SCORE))
				.setTangentHeadingInterpolation()
				.build();
		
		close_Park = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.BACKDROP_SCORE, Settings.Autonomous.RedClose.PARK_CORNER))
				.setConstantHeadingInterpolation(Settings.Autonomous.RedClose.PARK_CORNER.getHeading())
				.build();
	}
	
	/**
	 * OVERHAULED: Builds the paths for the BLUE CLOSE starting position using mirrored values.
	 */
	public void buildBlueClosePaths() {
		close_StartToSpike = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.START, Settings.Autonomous.BlueClose.SPIKE_MARK_CENTER))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.START.getHeading(), Settings.Autonomous.BlueClose.SPIKE_MARK_CENTER.getHeading())
				.build();
		
		close_SpikeToScore = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.SPIKE_MARK_CENTER, Settings.Autonomous.BlueClose.BACKDROP_SCORE))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.SPIKE_MARK_CENTER.getHeading(), Settings.Autonomous.BlueClose.BACKDROP_SCORE.getHeading())
				.build();
		
		close_ScoreToStack = follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.BlueClose.BACKDROP_SCORE, Settings.Autonomous.BlueClose.WAYPOINT_CENTER, Settings.Autonomous.BlueClose.STACK_PICKUP))
				.setTangentHeadingInterpolation()
				.build();
		
		close_StackToScore = follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.BlueClose.STACK_PICKUP, Settings.Autonomous.BlueClose.WAYPOINT_CENTER, Settings.Autonomous.BlueClose.BACKDROP_SCORE))
				.setTangentHeadingInterpolation()
				.build();
		
		close_Park = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.BACKDROP_SCORE, Settings.Autonomous.BlueClose.PARK_CORNER))
				.setConstantHeadingInterpolation(Settings.Autonomous.BlueClose.PARK_CORNER.getHeading())
				.build();
	}
	
	/**
	 * OVERHAULED: This is the state machine for the autonomous path.
	 * It executes the correct sequence based on the starting position.
	 */
	public void autonomousPathUpdate() {
		if (matchSettings.getAutoStartingPosition() == MatchSettings.AutoStartingPosition.CLOSE) {
			updateClosePath();
		} else {
			updateFarPath();
		}
	}
	
	/**
	 * State machine for CLOSE paths.
	 */
	private void updateClosePath() {
		switch (pathState) {
			case 0:
				follower.followPath(close_StartToSpike);
				setPathState(1);
				break;
			case 1:
				if (!follower.isBusy()) {
					// TODO: Add action to drop pixel on spike mark
					follower.followPath(close_SpikeToScore);
					setPathState(2);
				}
				break;
			case 2:
				if (!follower.isBusy()) {
					// TODO: Add action to score pixel on backdrop
					// Decide if you want to go for the stack or park
					// For now, let's go to park for a simple 2-pixel auto
					follower.followPath(close_Park);
					setPathState(10); // End state
				}
				break;
			// Example of a 4-pixel cycle
            /*
            case 3:
                if (!follower.isBusy()) {
                    // TODO: Add action to prepare for stack pickup
                    follower.followPath(close_ScoreToStack);
                    setPathState(4);
                }
                break;
            case 4:
                if (!follower.isBusy()) {
                    // TODO: Add action to pick up from stack
                    follower.followPath(close_StackToScore);
                    setPathState(5);
                }
                break;
            case 5:
                if (!follower.isBusy()) {
                    // TODO: Add action to score stack pixels
                    follower.followPath(close_Park);
                    setPathState(10); // End state
                }
                break;
            */
			case 10: // Path is finished
				break;
		}
	}
	
	/**
	 * State machine for FAR paths.
	 */
	private void updateFarPath() {
		switch (pathState) {
			case 0:
				follower.followPath(far_StartToSpike);
				setPathState(1);
				break;
			case 1:
				if (!follower.isBusy()) {
					follower.followPath(far_SpikeToScore);
					setPathState(2);
				}
				break;
			case 2:
				if (!follower.isBusy()) {
					follower.followPath(far_ScoreToStack);
					setPathState(3);
				}
				break;
			case 3:
				if (!follower.isBusy()) {
					follower.followPath(far_StackToScore);
					setPathState(4);
				}
				break;
			case 4:
				if (!follower.isBusy()) {
					follower.followPath(far_ScoreToStack2);
					setPathState(5);
				}
				break;
			case 5:
				if (!follower.isBusy()) {
					follower.followPath(far_StackToScore2);
					setPathState(6);
				}
				break;
			case 6:
				if (!follower.isBusy()) {
					// TODO: Add any final actions, then park or end
					setPathState(10); // End state
				}
				break;
			case 10: // Path is finished
				break;
		}
	}
	
	
	public void setPathState(int pState) {
		pathState = pState;
		pathTimer.resetTimer();
		opmodeTimer.resetTimer(); // Reset timer at each state for timeout checks
	}
	
	@Override
	public void loop() {
		follower.update();
		autonomousPathUpdate();
		
		telemetry.addData("Path State", pathState);
		telemetry.addData("X", follower.getPose().getX());
		telemetry.addData("Y", follower.getPose().getY());
		telemetry.addData("Heading", Math.toDegrees(follower.getPose().getHeading()));
		telemetry.addData("Opmode Timer", opmodeTimer.getElapsedTimeSeconds());
		telemetry.update();
	}
	
	@Override
	public void init() {
		pathTimer = new Timer();
		opmodeTimer = new Timer();
		
		// These settings will be configured by the driver during the init_loop
		matchSettings = new MatchSettings(blackboard);
		wizard = new MatchConfigurationWizard(matchSettings, gamepad1, telemetry);
		
		follower = Constants.createFollower(hardwareMap);
	}
	
	@Override
	public void init_loop() {
		// Allow driver to select match settings using the wizard
		wizard.refresh();
	}
	
	@Override
	public void start() {
		opmodeTimer.resetTimer();
		// Now that settings are finalized, build the correct paths
		buildPaths();
		// Start the autonomous sequence
		setPathState(0);
	}
	
	@Override
	public void stop() {
	}
	
}
