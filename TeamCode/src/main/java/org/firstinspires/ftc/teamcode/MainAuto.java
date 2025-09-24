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
import org.firstinspires.ftc.teamcode.hardware.MechanismManager;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Main Auto", group = ".Competition Modes")
public class MainAuto extends OpMode {
	
	private Follower follower;
	private Timer pathTimer, opmodeTimer;
	private int pathState;
	private MatchConfigurationWizard wizard;
	private MechanismManager mechanisms;
	private MatchSettings matchSettings;
	
	// PathChain declarations with more descriptive names.
	// Far paths
	private PathChain far_preset_1_prep;
	private PathChain far_preset_1_end;
	private PathChain far_launch_1;
	private PathChain far_preset_2_prep;
	private PathChain far_preset_2_end;
	private PathChain far_launch_2;
	private PathChain far_preset_3_prep;
	private PathChain far_preset_3_end;
	private PathChain far_launch_3;
	private PathChain far_Park;
	
	// Close paths
	private PathChain close_preset_1_prep;
	private PathChain close_preset_1_end;
	private PathChain close_launch_1;
	private PathChain close_preset_2_prep;
	private PathChain close_preset_2_end;
	private PathChain close_launch_2;
	private PathChain close_preset_3_prep;
	private PathChain close_preset_3_end;
	private PathChain close_launch_3;
	private PathChain close_park;
	
	
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
		close_preset_1_prep = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.START, Settings.Autonomous.RedFar.PRESET_1_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.START.getHeading(), Settings.Autonomous.RedFar.PRESET_1_PREP.getHeading())
				.build();
		
		close_preset_1_end = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.PRESET_1_PREP, Settings.Autonomous.RedFar.PRESET_1_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRESET_1_PREP.getHeading(), Settings.Autonomous.RedFar.PRESET_1_END.getHeading())
				.build();
		
		close_launch_1 = follower.pathBuilder()
				.addPath(Settings.Autonomous.RedFar.BEZIER_LAUNCH_1)
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRESET_1_END.getHeading(), Settings.Autonomous.RedFar.ENDING_LAUNCH_1.getHeading())
				.build();
		
		close_preset_2_prep = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.ENDING_LAUNCH_1, Settings.Autonomous.RedFar.PRESET_2_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.ENDING_LAUNCH_1.getHeading(), Settings.Autonomous.RedFar.PRESET_2_PREP.getHeading())
				.build();
		
		close_preset_2_end = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.PRESET_2_PREP, Settings.Autonomous.RedFar.PRESET_2_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRESET_2_PREP
						.getHeading(), Settings.Autonomous.RedFar.PRESET_2_END.getHeading())
				.build();
		
		close_launch_2 = follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedFar.PRESET_2_END, Settings.Autonomous.RedFar.LAUNCH_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRESET_2_END.getHeading(), Settings.Autonomous.RedFar.LAUNCH_2.getHeading())
				.build();
		
		close_preset_3_prep = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.LAUNCH_2, Settings.Autonomous.RedFar.PRESET_3_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.LAUNCH_2.getHeading(), Settings.Autonomous.RedFar.PRESET_3_END.getHeading())
				.build();
		
		close_launch_3 = follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedFar.PRESET_3_END, Settings.Autonomous.RedFar.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRESET_3_END.getHeading(), Settings.Autonomous.RedFar.PARK.getHeading())
				.build();
		
		close_park = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.PARK, Settings.Autonomous.RedFar.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PARK.getHeading(), Settings.Autonomous.RedFar.PARK.getHeading())
				.build();
	}
	
	public void buildBlueFarPaths() {
		close_preset_1_prep = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.START, Settings.Autonomous.BlueFar.PRESET_1_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.START.getHeading(), Settings.Autonomous.BlueFar.PRESET_1_PREP.getHeading())
				.build();
		
		close_preset_1_end = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.PRESET_1_PREP, Settings.Autonomous.BlueFar.PRESET_1_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRESET_1_PREP.getHeading(), Settings.Autonomous.BlueFar.PRESET_1_END.getHeading())
				.build();
		
		close_launch_1 = follower.pathBuilder()
				.addPath(Settings.Autonomous.BlueFar.BEZIER_LAUNCH_1)
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRESET_1_END.getHeading(), Settings.Autonomous.BlueFar.ENDING_LAUNCH_1.getHeading())
				.build();
		
		close_preset_2_prep = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.ENDING_LAUNCH_1, Settings.Autonomous.BlueFar.PRESET_2_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.ENDING_LAUNCH_1.getHeading(), Settings.Autonomous.BlueFar.PRESET_2_PREP.getHeading())
				.build();
		
		close_preset_2_end = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.PRESET_2_PREP, Settings.Autonomous.BlueFar.PRESET_2_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRESET_2_PREP
						.getHeading(), Settings.Autonomous.BlueFar.PRESET_2_END.getHeading())
				.build();
		
		close_launch_2 = follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.BlueFar.PRESET_2_END, Settings.Autonomous.BlueFar.LAUNCH_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRESET_2_END.getHeading(), Settings.Autonomous.BlueFar.LAUNCH_2.getHeading())
				.build();
		
		close_preset_3_prep = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.LAUNCH_2, Settings.Autonomous.BlueFar.PRESET_3_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.LAUNCH_2.getHeading(), Settings.Autonomous.BlueFar.PRESET_3_END.getHeading())
				.build();
		
		close_launch_3 = follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.BlueFar.PRESET_3_END, Settings.Autonomous.BlueFar.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRESET_3_END.getHeading(), Settings.Autonomous.BlueFar.PARK.getHeading())
				.build();
		
		close_park = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.PARK, Settings.Autonomous.BlueFar.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PARK.getHeading(), Settings.Autonomous.BlueFar.PARK.getHeading())
				.build();
	}
	
	/**
	 * OVERHAULED: Builds the paths for the RED CLOSE starting position.
	 */
	public void buildRedClosePaths() {
		close_preset_1_prep = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.START, Settings.Autonomous.RedClose.PRESET_1_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.START.getHeading(), Settings.Autonomous.RedClose.PRESET_1_PREP.getHeading())
				.build();
		
		close_preset_1_end = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.PRESET_1_PREP, Settings.Autonomous.RedClose.PRESET_1_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_1_PREP.getHeading(), Settings.Autonomous.RedClose.PRESET_1_END.getHeading())
				.build();
		
		close_launch_1 = follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedClose.PRESET_1_END, Settings.Autonomous.RedClose.LAUNCH_1, Settings.Autonomous.RedClose.LAUNCH_1))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_1_END.getHeading(), Settings.Autonomous.RedClose.LAUNCH_1.getHeading())
				.build();
		
		close_preset_2_prep = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.LAUNCH_1, Settings.Autonomous.RedClose.PRESET_2_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.LAUNCH_1.getHeading(), Settings.Autonomous.RedClose.PRESET_2_PREP.getHeading())
				.build();
		
		close_preset_2_end = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.PRESET_2_PREP, Settings.Autonomous.RedClose.PRESET_2_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_2_PREP
						.getHeading(), Settings.Autonomous.RedClose.PRESET_2_END.getHeading())
				.build();
		
		close_launch_2 = follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedClose.PRESET_2_END, Settings.Autonomous.RedClose.LAUNCH_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_2_END.getHeading(), Settings.Autonomous.RedClose.LAUNCH_2.getHeading())
				.build();
		
		close_preset_3_prep = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.LAUNCH_2, Settings.Autonomous.RedClose.PRESET_3_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.LAUNCH_2.getHeading(), Settings.Autonomous.RedClose.PRESET_3_PREP.getHeading())
				.build();
		
		close_preset_3_end = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.PRESET_3_PREP, Settings.Autonomous.RedClose.PRESET_3_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_3_PREP.getHeading(), Settings.Autonomous.RedClose.PRESET_3_END.getHeading())
				.build();
		
		close_launch_3 = follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedClose.PRESET_3_END, Settings.Autonomous.RedClose.LAUNCH_3))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_3_END.getHeading(), Settings.Autonomous.RedClose.LAUNCH_3.getHeading())
				.build();
		
		close_park = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.LAUNCH_3, Settings.Autonomous.RedClose.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.LAUNCH_3.getHeading(), Settings.Autonomous.RedClose.PARK.getHeading())
				.build();
	}
	
	/**
	 * OVERHAULED: Builds the paths for the BLUE CLOSE starting position using mirrored values.
	 */
	public void buildBlueClosePaths() {
		close_preset_1_prep = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.START, Settings.Autonomous.BlueClose.PRESET_1_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.START.getHeading(), Settings.Autonomous.BlueClose.PRESET_1_PREP.getHeading())
				.build();
		
		close_preset_1_end = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.PRESET_1_PREP, Settings.Autonomous.BlueClose.PRESET_1_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_1_PREP.getHeading(), Settings.Autonomous.BlueClose.PRESET_1_END.getHeading())
				.build();
		
		close_launch_1 = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.PRESET_1_END, Settings.Autonomous.BlueClose.LAUNCH_1))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_1_END.getHeading(), Settings.Autonomous.BlueClose.LAUNCH_1.getHeading())
				.build();
		
		close_preset_2_prep = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.LAUNCH_1, Settings.Autonomous.BlueClose.PRESET_2_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.LAUNCH_1.getHeading(), Settings.Autonomous.BlueClose.PRESET_2_PREP.getHeading())
				.build();
		
		close_preset_2_end = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.PRESET_2_PREP, Settings.Autonomous.BlueClose.PRESET_2_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_2_PREP
						.getHeading(), Settings.Autonomous.BlueClose.PRESET_2_END.getHeading())
				.build();
		
		close_launch_2 = follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.BlueClose.PRESET_2_END, Settings.Autonomous.BlueClose.LAUNCH_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_2_END.getHeading(), Settings.Autonomous.BlueClose.LAUNCH_2.getHeading())
				.build();
		
		close_preset_3_prep = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.LAUNCH_2, Settings.Autonomous.BlueClose.PRESET_3_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.LAUNCH_2.getHeading(), Settings.Autonomous.BlueClose.PRESET_3_PREP.getHeading())
				.build();
		
		close_preset_3_end = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.PRESET_3_PREP, Settings.Autonomous.BlueClose.PRESET_3_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_3_PREP.getHeading(), Settings.Autonomous.BlueClose.PRESET_3_END.getHeading())
				.build();
		
		close_launch_3 = follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.BlueClose.PRESET_3_END, Settings.Autonomous.BlueClose.LAUNCH_3))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_3_END.getHeading(), Settings.Autonomous.BlueClose.LAUNCH_3.getHeading())
				.build();
		
		close_park = follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.LAUNCH_3, Settings.Autonomous.BlueClose.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.LAUNCH_3.getHeading(), Settings.Autonomous.BlueClose.PARK.getHeading())
				.build();
	}
	
	/**
	 * This is the state machine for the autonomous path.
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
				if (mechanisms.launcher.okayToLaunch()) {
					mechanisms.launcher.launch();
				}
				
				if (mechanisms.spindex.isEmpty()) {
					follower.followPath(close_preset_1_prep);
					setPathState(1);
				}
				break;
			case 1:
				if (!follower.isBusy()) {
					mechanisms.intake.in();
					follower.followPath(close_preset_1_end);
					setPathState(2);
				}
				break;
			case 2:
				if (!follower.isBusy()) {
					mechanisms.intake.stop();
					follower.followPath(close_launch_1);
					setPathState(3); // End state
				}
				break;
			case 3:
				if (mechanisms.launcher.okayToLaunch()) {
					mechanisms.launcher.launch();
				}
				
				if (mechanisms.spindex.isEmpty()) {
					follower.followPath(close_preset_2_prep);
					setPathState(4);
				}
			case 4:
				if (!follower.isBusy()) {
					mechanisms.intake.in();
					follower.followPath(close_preset_2_end);
					setPathState(5);
				}
				break;
			case 5:
				if (!follower.isBusy()) {
					mechanisms.intake.stop();
					follower.followPath(close_launch_2);
					setPathState(6); // End state
				}
				break;
			case 6:
				if (mechanisms.launcher.okayToLaunch()) {
					mechanisms.launcher.launch();
				}
				
				if (mechanisms.spindex.isEmpty()) {
					follower.followPath(close_preset_3_prep);
					setPathState(7);
				}
				break;
			
			case 7:
				if (!follower.isBusy()) {
					mechanisms.intake.in();
					follower.followPath(close_preset_3_end);
					setPathState(8);
				}
				break;
			
			case 8:
				if (!follower.isBusy()) {
					mechanisms.intake.stop();
					follower.followPath(close_launch_3);
					setPathState(9); // End state
				}
				break;
			
			case 9:
				if (mechanisms.launcher.okayToLaunch()) {
					mechanisms.launcher.launch();
				}
				
				if (mechanisms.spindex.isEmpty()) {
					follower.followPath(close_park);
					setPathState(10); // End state
				}
				break;
			
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
				if (mechanisms.launcher.okayToLaunch()) {
					mechanisms.launcher.launch();
				}
				
				if (mechanisms.spindex.isEmpty()) {
					follower.followPath(close_preset_1_prep);
					setPathState(1);
				}
				break;
			case 1:
				if (!follower.isBusy()) {
					mechanisms.intake.in();
					follower.followPath(close_preset_1_end);
					setPathState(2);
				}
				break;
			case 2:
				if (!follower.isBusy()) {
					mechanisms.intake.stop();
					follower.followPath(close_launch_1);
					setPathState(3); // End state
				}
				break;
			case 3:
				if (mechanisms.launcher.okayToLaunch()) {
					mechanisms.launcher.launch();
				}
				
				if (mechanisms.spindex.isEmpty()) {
					follower.followPath(close_preset_2_prep);
					setPathState(4);
				}
			case 4:
				if (!follower.isBusy()) {
					mechanisms.intake.in();
					follower.followPath(close_preset_2_end);
					setPathState(5);
				}
				break;
			case 5:
				if (!follower.isBusy()) {
					mechanisms.intake.stop();
					follower.followPath(close_launch_2);
					setPathState(6);
				}
				break;
			case 6:
				if (mechanisms.launcher.okayToLaunch()) {
					mechanisms.launcher.launch();
				}
				
				if (mechanisms.spindex.isEmpty()) {
					follower.followPath(close_preset_3_prep);
					setPathState(7);
				}
				break;
			
			case 7:
				if (!follower.isBusy()) {
					mechanisms.intake.in();
					follower.followPath(close_preset_3_end);
					setPathState(8);
				}
				break;
			
			case 8:
				if (!follower.isBusy()) {
					mechanisms.intake.stop();
					follower.followPath(close_launch_3);
					setPathState(9); // End state
				}
				break;
			
			case 9:
				if (mechanisms.launcher.okayToLaunch()) {
					mechanisms.launcher.launch();
				}
				
				if (mechanisms.spindex.isEmpty()) {
					follower.followPath(close_park);
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
		mechanisms = new MechanismManager(hardwareMap, matchSettings);
		
		follower = Constants.createFollower(hardwareMap);
	}
	
	@Override
	public void init_loop() {
		// Allow driver to select match settings using the wizard
		wizard.refresh();
	}
	
	@Override
	public void start() {
		mechanisms.init();
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
