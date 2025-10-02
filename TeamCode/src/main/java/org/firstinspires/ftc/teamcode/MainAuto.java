package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.configuration.MatchConfigurationWizard;
import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.configuration.Settings;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Launcher;
import org.firstinspires.ftc.teamcode.hardware.MechanismManager;
import org.firstinspires.ftc.teamcode.hardware.Spindex;
import org.firstinspires.ftc.teamcode.pedroPathing.Drawing;

/**
 * The main Autonomous script that makes the robot move by itself during the Auto period of a match.
 * Handles all possible variance in starting position and team color using a {@link MatchConfigurationWizard}.
 */
@Autonomous(name = "Main Auto", group = ".Competition Modes")
public class MainAuto extends OpMode {
	
	private Timer pathTimer, opmodeTimer;
	private int pathState;
	private MatchConfigurationWizard wizard;
	private MechanismManager mechanisms;
	private MatchSettings matchSettings;
	
	// Declare each segment of path with a descriptive name. These are then constructed at runtime.
	private PathChain farPreset1Prep, farPreset1End, farLaunch1;
	private PathChain farPreset2Prep, farPreset2End, farLaunch2;
	private PathChain farPreset3Prep, farPreset3End, farLaunch3;
	private PathChain farPark;
	
	// Close-starting-position paths
	private PathChain closePreset1Prep, closePreset1End, closeLaunch1;
	private PathChain closePreset2Prep, closePreset2End, closeLaunch2;
	private PathChain closePreset3Prep, closePreset3End, closeLaunch3;
	private PathChain closePark;
	
	
	/**
	 * This method creates all the needed paths for the robot to run the given Autonomous.
	 * It calls the correct path-building method
	 * based on the settings from the {@link MatchConfigurationWizard}.
	 * Note that, as an efficient byproduct of this method, the variables not used by the given path
	 * will never be constructed. If we run a far path, the closePark variable will be null, for example.
	 */
	public void buildPaths() {
		MatchSettings.AutoStartingPosition startPos = matchSettings.getAutoStartingPosition();
		MatchSettings.AllianceColor alliance = matchSettings.getAllianceColor();
		
		if (startPos == MatchSettings.AutoStartingPosition.FAR) {
			if (alliance == MatchSettings.AllianceColor.RED) {
				buildRedFarPaths();
				mechanisms.drivetrain.follower.setStartingPose(Settings.Autonomous.RedFar.START);
			} else {
				buildBlueFarPaths();
				mechanisms.drivetrain.follower.setStartingPose(Settings.Autonomous.BlueFar.START);
			}
		} else {
			if (alliance == MatchSettings.AllianceColor.RED) {
				buildRedClosePaths();
				mechanisms.drivetrain.follower.setStartingPose(Settings.Autonomous.RedClose.START);
			} else {
				buildBlueClosePaths();
				mechanisms.drivetrain.follower.setStartingPose(Settings.Autonomous.BlueClose.START);
			}
		}
	}
	
	/**
	 * buildABPaths() methods construct each PathChain for the given alliance color and position.
	 * Only one of these is called per Autonomous run.
	 * Note that the Far and Close pathbuilders use the same variables.
	 * For each part of the path, the functions use points on the field declared in Settings and do linear or curved interpolation
	 * to travel between them. Each path is then built so they are ready to be used by the mapping algorithm.
	 * Nearly all changes to pathing should be done through the Settings. Changes to these functions
	 * should only be made to change the actual structure of the methods the robot uses to go between places.
	 */
	public void buildRedFarPaths() {
		farPreset1Prep = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.START, Settings.Autonomous.RedFar.PRESET_1_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.START.getHeading(), Settings.Autonomous.RedFar.PRESET_1_PREP.getHeading())
				.build();
		
		farPreset1End = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.PRESET_1_PREP, Settings.Autonomous.RedFar.PRESET_1_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRESET_1_PREP.getHeading(), Settings.Autonomous.RedFar.PRESET_1_END.getHeading())
				.build();
		
		farLaunch1 = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(Settings.Autonomous.RedFar.BEZIER_LAUNCH_1)
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRESET_1_END.getHeading(), Settings.Autonomous.RedFar.ENDING_LAUNCH_1.getHeading())
				.build();
		
		farPreset2Prep = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.ENDING_LAUNCH_1, Settings.Autonomous.RedFar.PRESET_2_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.ENDING_LAUNCH_1.getHeading(), Settings.Autonomous.RedFar.PRESET_2_PREP.getHeading())
				.build();
		
		farPreset2End = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.PRESET_2_PREP, Settings.Autonomous.RedFar.PRESET_2_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRESET_2_PREP
						.getHeading(), Settings.Autonomous.RedFar.PRESET_2_END.getHeading())
				.build();
		
		farLaunch2 = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedFar.PRESET_2_END, Settings.Autonomous.RedFar.LAUNCH_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRESET_2_END.getHeading(), Settings.Autonomous.RedFar.LAUNCH_2.getHeading())
				.build();
		
		farPreset3Prep = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.LAUNCH_2, Settings.Autonomous.RedFar.PRESET_3_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.LAUNCH_2.getHeading(), Settings.Autonomous.RedFar.PRESET_3_END.getHeading())
				.build();
		
		farLaunch3 = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedFar.PRESET_3_END, Settings.Autonomous.RedFar.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRESET_3_END.getHeading(), Settings.Autonomous.RedFar.PARK.getHeading())
				.build();
		
		farPark = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.PARK, Settings.Autonomous.RedFar.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PARK.getHeading(), Settings.Autonomous.RedFar.PARK.getHeading())
				.build();
	}
	
	/**
	 * See {@link MainAuto#buildRedFarPaths()} javadoc for more details.
	 */
	public void buildBlueFarPaths() {
		farPreset1Prep = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.START, Settings.Autonomous.BlueFar.PRESET_1_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.START.getHeading(), Settings.Autonomous.BlueFar.PRESET_1_PREP.getHeading())
				.build();
		
		farPreset1End = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.PRESET_1_PREP, Settings.Autonomous.BlueFar.PRESET_1_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRESET_1_PREP.getHeading(), Settings.Autonomous.BlueFar.PRESET_1_END.getHeading())
				.build();
		
		farLaunch1 = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(Settings.Autonomous.BlueFar.BEZIER_LAUNCH_1)
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRESET_1_END.getHeading(), Settings.Autonomous.BlueFar.ENDING_LAUNCH_1.getHeading())
				.build();
		
		farPreset2Prep = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.ENDING_LAUNCH_1, Settings.Autonomous.BlueFar.PRESET_2_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.ENDING_LAUNCH_1.getHeading(), Settings.Autonomous.BlueFar.PRESET_2_PREP.getHeading())
				.build();
		
		farPreset2End = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.PRESET_2_PREP, Settings.Autonomous.BlueFar.PRESET_2_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRESET_2_PREP
						.getHeading(), Settings.Autonomous.BlueFar.PRESET_2_END.getHeading())
				.build();
		
		farLaunch2 = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.BlueFar.PRESET_2_END, Settings.Autonomous.BlueFar.LAUNCH_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRESET_2_END.getHeading(), Settings.Autonomous.BlueFar.LAUNCH_2.getHeading())
				.build();
		
		farPreset3Prep = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.LAUNCH_2, Settings.Autonomous.BlueFar.PRESET_3_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.LAUNCH_2.getHeading(), Settings.Autonomous.BlueFar.PRESET_3_END.getHeading())
				.build();
		
		farLaunch3 = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.BlueFar.PRESET_3_END, Settings.Autonomous.BlueFar.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRESET_3_END.getHeading(), Settings.Autonomous.BlueFar.PARK.getHeading())
				.build();
		
		farPark = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.PARK, Settings.Autonomous.BlueFar.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PARK.getHeading(), Settings.Autonomous.BlueFar.PARK.getHeading())
				.build();
	}
	
	/**
	 * See the {@link MainAuto#buildRedFarPaths()} javadoc for more details.
	 */
	public void buildRedClosePaths() {
		closePreset1Prep = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.START, Settings.Autonomous.RedClose.PRESET_1_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.START.getHeading(), Settings.Autonomous.RedClose.PRESET_1_PREP.getHeading())
				.build();
		
		closePreset1End = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.PRESET_1_PREP, Settings.Autonomous.RedClose.PRESET_1_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_1_PREP.getHeading(), Settings.Autonomous.RedClose.PRESET_1_END.getHeading())
				.build();
		
		closeLaunch1 = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedClose.PRESET_1_END, Settings.Autonomous.RedClose.LAUNCH_1, Settings.Autonomous.RedClose.LAUNCH_1))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_1_END.getHeading(), Settings.Autonomous.RedClose.LAUNCH_1.getHeading())
				.build();
		
		closePreset2Prep = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.LAUNCH_1, Settings.Autonomous.RedClose.PRESET_2_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.LAUNCH_1.getHeading(), Settings.Autonomous.RedClose.PRESET_2_PREP.getHeading())
				.build();
		
		closePreset2End = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.PRESET_2_PREP, Settings.Autonomous.RedClose.PRESET_2_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_2_PREP
						.getHeading(), Settings.Autonomous.RedClose.PRESET_2_END.getHeading())
				.build();
		
		closeLaunch2 = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedClose.PRESET_2_END, Settings.Autonomous.RedClose.LAUNCH_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_2_END.getHeading(), Settings.Autonomous.RedClose.LAUNCH_2.getHeading())
				.build();
		
		closePreset3Prep = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(mechanisms.drivetrain.follower::getPose, Settings.Autonomous.RedClose.PRESET_3_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.LAUNCH_2.getHeading(), Settings.Autonomous.RedClose.PRESET_3_PREP.getHeading())
				.build();
		
		closePreset3End = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.PRESET_3_PREP, Settings.Autonomous.RedClose.PRESET_3_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_3_PREP.getHeading(), Settings.Autonomous.RedClose.PRESET_3_END.getHeading())
				.build();
		
		closeLaunch3 = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedClose.PRESET_3_END, Settings.Autonomous.RedClose.LAUNCH_3))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_3_END.getHeading(), Settings.Autonomous.RedClose.LAUNCH_3.getHeading())
				.build();
		
		closePark = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.LAUNCH_3, Settings.Autonomous.RedClose.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.LAUNCH_3.getHeading(), Settings.Autonomous.RedClose.PARK.getHeading())
				.build();
	}
	
	/**
	 * See the {@link MainAuto#buildRedFarPaths()} javadoc for more details.
	 */
	public void buildBlueClosePaths() {
		closePreset1Prep = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.START, Settings.Autonomous.BlueClose.PRESET_1_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.START.getHeading(), Settings.Autonomous.BlueClose.PRESET_1_PREP.getHeading())
				.build();
		
		closePreset1End = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.PRESET_1_PREP, Settings.Autonomous.BlueClose.PRESET_1_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_1_PREP.getHeading(), Settings.Autonomous.BlueClose.PRESET_1_END.getHeading())
				.build();
		
		closeLaunch1 = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.PRESET_1_END, Settings.Autonomous.BlueClose.LAUNCH_1))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_1_END.getHeading(), Settings.Autonomous.BlueClose.LAUNCH_1.getHeading())
				.build();
		
		closePreset2Prep = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.LAUNCH_1, Settings.Autonomous.BlueClose.PRESET_2_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.LAUNCH_1.getHeading(), Settings.Autonomous.BlueClose.PRESET_2_PREP.getHeading())
				.build();
		
		closePreset2End = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.PRESET_2_PREP, Settings.Autonomous.BlueClose.PRESET_2_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_2_PREP
						.getHeading(), Settings.Autonomous.BlueClose.PRESET_2_END.getHeading())
				.build();
		
		closeLaunch2 = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.BlueClose.PRESET_2_END, Settings.Autonomous.BlueClose.LAUNCH_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_2_END.getHeading(), Settings.Autonomous.BlueClose.LAUNCH_2.getHeading())
				.build();
		
		closePreset3Prep = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.LAUNCH_2, Settings.Autonomous.BlueClose.PRESET_3_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.LAUNCH_2.getHeading(), Settings.Autonomous.BlueClose.PRESET_3_PREP.getHeading())
				.build();
		
		closePreset3End = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.PRESET_3_PREP, Settings.Autonomous.BlueClose.PRESET_3_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_3_PREP.getHeading(), Settings.Autonomous.BlueClose.PRESET_3_END.getHeading())
				.build();
		
		closeLaunch3 = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.BlueClose.PRESET_3_END, Settings.Autonomous.BlueClose.LAUNCH_3))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_3_END.getHeading(), Settings.Autonomous.BlueClose.LAUNCH_3.getHeading())
				.build();
		
		closePark = mechanisms.drivetrain.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.LAUNCH_3, Settings.Autonomous.BlueClose.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.LAUNCH_3.getHeading(), Settings.Autonomous.BlueClose.PARK.getHeading())
				.build();
	}
	
	/**
	 * This is the state machine manager for the autonomous path.
	 * It executes the correct update based on the starting position.
	 */
	public void autonomousPathUpdate() {
		if (matchSettings.getAutoStartingPosition() == MatchSettings.AutoStartingPosition.CLOSE) {
			updateClosePath();
		} else {
			updateFarPath();
		}
	}
	
	/**
	 * State machine for Close paths.
	 * pathState is a variable that tells us what path we are currently following, so we change
	 * what the robot does based on that. Most of the states simply wait until the robot is done with
	 * the last movement (follower.isBusy) and then does something like a mechanisms execution before
	 * moving on.
	 */
	private void updateClosePath() {
		switch (pathState) {
			case 0:
				ifMechanismValid(mechanisms.get(Launcher.class), Launcher::launch);
				if (ifMechanismValid(mechanisms.get(Spindex.class), s -> {
					if (s.isEmpty()) {
						mechanisms.drivetrain.follower.followPath(closePreset1Prep);
						setPathState(1);
					}
				})) {
				} else {
					setPathState(1);
				}
				
				break;
			case 1:
				if (!mechanisms.drivetrain.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::in);
					mechanisms.drivetrain.follower.followPath(closePreset1End);
					setPathState(2);
				}
				break;
			case 2:
				if (!mechanisms.drivetrain.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::stop);
					mechanisms.drivetrain.follower.followPath(closeLaunch1);
					setPathState(3);
				}
				break;
			case 3:
				if (!mechanisms.drivetrain.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Launcher.class), Launcher::launch);
					ifMechanismValid(mechanisms.get(Spindex.class), s -> {
						if (s.isEmpty()) {
							mechanisms.drivetrain.follower.followPath(closePreset2Prep);
							setPathState(4);
						}
					});
				}
				break;
			case 4:
				if (!mechanisms.drivetrain.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::in);
					mechanisms.drivetrain.follower.followPath(closePreset2End);
					setPathState(5);
				}
				break;
			case 5:
				if (!mechanisms.drivetrain.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::stop);
					mechanisms.drivetrain.follower.followPath(closeLaunch2);
					setPathState(6);
				}
				break;
			case 6:
				ifMechanismValid(mechanisms.get(Launcher.class), Launcher::launch);
				ifMechanismValid(mechanisms.get(Spindex.class), s -> {
					if (s.isEmpty())
						mechanisms.drivetrain.follower.followPath(closePreset3Prep);
					setPathState(7);
				});
				break;
			case 7:
				if (!mechanisms.drivetrain.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::in);
					mechanisms.drivetrain.follower.followPath(closePreset3End);
					setPathState(8);
				}
				break;
			case 8:
				if (!mechanisms.drivetrain.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::stop);
					mechanisms.drivetrain.follower.followPath(closeLaunch3);
					setPathState(9);
				}
				break;
			case 9:
				ifMechanismValid(mechanisms.get(Launcher.class), Launcher::launch);
				ifMechanismValid(mechanisms.get(Spindex.class), s -> {
					if (s.isEmpty())
						mechanisms.drivetrain.follower.followPath(closePark);
					setPathState(10);
				});
				break;
			case 10:
				break;
		}
	}
	
	/**
	 * State machine for Far paths. See the {@link MainAuto#updateClosePath()} javadoc for more details.
	 */
	private void updateFarPath() {
		switch (pathState) {
			case 0:
				ifMechanismValid(mechanisms.get(Launcher.class), Launcher::launch);
				if (ifMechanismValid(mechanisms.get(Spindex.class), s -> {
					if (s.isEmpty())
						mechanisms.drivetrain.follower.followPath(farPreset1Prep);
					setPathState(1);
				})) {
				} else {
					setPathState(1);
				}
				break;
			case 1:
				if (!mechanisms.drivetrain.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::in);
					mechanisms.drivetrain.follower.followPath(farPreset1End);
					setPathState(2);
				}
				break;
			case 2:
				if (!mechanisms.drivetrain.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::stop);
					mechanisms.drivetrain.follower.followPath(farLaunch1);
					setPathState(3);
				}
				break;
			case 3:
				ifMechanismValid(mechanisms.get(Launcher.class), Launcher::launch);
				ifMechanismValid(mechanisms.get(Spindex.class), s -> {
					if (s.isEmpty())
						mechanisms.drivetrain.follower.followPath(farPreset2Prep);
					setPathState(4);
				});
				break;
			case 4:
				if (!mechanisms.drivetrain.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::in);
					mechanisms.drivetrain.follower.followPath(farPreset2End);
					setPathState(5);
				}
				break;
			case 5:
				if (!mechanisms.drivetrain.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::stop);
					mechanisms.drivetrain.follower.followPath(farLaunch2);
					setPathState(6);
				}
				break;
			case 6:
				ifMechanismValid(mechanisms.get(Launcher.class), Launcher::launch);
				ifMechanismValid(mechanisms.get(Spindex.class), s -> {
					if (s.isEmpty())
						mechanisms.drivetrain.follower.followPath(farPreset3Prep);
					setPathState(7);
				});
				break;
			case 7:
				if (!mechanisms.drivetrain.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::in);
					mechanisms.drivetrain.follower.followPath(farPreset3End);
					setPathState(8);
				}
				break;
			case 8:
				if (!mechanisms.drivetrain.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::stop);
					mechanisms.drivetrain.follower.followPath(farLaunch3);
					setPathState(9);
				}
				break;
			case 9:
				ifMechanismValid(mechanisms.get(Launcher.class), Launcher::launch);
				ifMechanismValid(mechanisms.get(Spindex.class), s -> {
					if (s.isEmpty()) mechanisms.drivetrain.follower.followPath(farPark);
					setPathState(10);
				});
				break;
			case 10:
				break;
		}
	}
	
	/**
	 * A simple state machine manager for the path. Resets the pathTimer so that we know how long
	 * has passed since the last state change.
	 *
	 * @param pState The new state of the path
	 */
	public void setPathState(int pState) {
		pathState = pState;
		pathTimer.resetTimer();
	}
	
	/**
	 * Runs repeatedly during the OpMode.
	 */
	@Override
	public void loop() {
		// Update the mechanisms, and then execute movement based on the path and robot state
		mechanisms.update();
		autonomousPathUpdate();
		
		// Log everything
		Drawing.drawDebug(mechanisms.drivetrain.follower);
		telemetry.addData("Path State", pathState);
		telemetry.addData("X", mechanisms.drivetrain.follower.getPose().getX());
		telemetry.addData("Y", mechanisms.drivetrain.follower.getPose().getY());
		telemetry.addData("Heading", Math.toDegrees(mechanisms.drivetrain.follower.getPose().getHeading()));
		telemetry.addData("Opmode Timer", opmodeTimer.getElapsedTimeSeconds());
		telemetry.addData("Path", pathState);
		telemetry.update();
	}
	
	/**
	 * Runs when INIT is pressed on the driver station.
	 */
	@Override
	public void init() {
		// Create fresh timers
		pathTimer = new Timer();
		opmodeTimer = new Timer();
		
		// Match settings, like alliance color, will be configured by the driver during the init_loop.
		matchSettings = new MatchSettings(blackboard);
		wizard = new MatchConfigurationWizard(matchSettings, gamepad1, telemetry);
		mechanisms = new MechanismManager(hardwareMap, matchSettings);
	}
	
	/**
	 * Runs repeatedly after INIT is pressed and before START is pressed.
	 */
	@Override
	public void init_loop() {
		// Allow driver to select match settings using the wizard. Refresh the screen using refresh()
		// To allow the wizard to take configuration input and display an updated configuration.
		wizard.refresh();
		
		// Draw the initial pose of the robot
		Drawing.drawRobot(mechanisms.drivetrain.follower.getPose());
		Drawing.sendPacket();
	}
	
	/**
	 * Runs once, when the driver presses PLAY after having pressed INIT and configured the robot.
	 */
	@Override
	public void start() {
		// Set up
		mechanisms.init();
		mechanisms.drivetrain.follower.setStartingPose(getStartingPose());
		
		// Create the paths based on the configuration/starting pose/alliance color.
		// This must be done here and not during init, because the match config is hitherto unknown.
		buildPaths();
		
		// The opmode is beginning, so start the timer
		opmodeTimer.resetTimer();
		
		// Start the autonomous sequence
		setPathState(0);
	}
	
	/**
	 * Runs when "stop" is pressed on the Driver Station.
	 * Cleanup and shutdown should occur instantaneously and be non-blocking.
	 */
	@Override
	public void stop() {
		mechanisms.stop();
	}
	
	/**
	 * Gives the starting pose for the robot based on the match settings.
	 *
	 * @return The starting pose for the robot.
	 */
	private Pose getStartingPose() {
		switch (matchSettings.getAllianceColor()) {
			case RED:
				return matchSettings.getAutoStartingPosition() == MatchSettings.AutoStartingPosition.CLOSE
						? Settings.Autonomous.RedClose.PARK
						: Settings.Autonomous.RedFar.PARK;
			case BLUE:
				return matchSettings.getAutoStartingPosition() == MatchSettings.AutoStartingPosition.CLOSE
						? Settings.Autonomous.BlueClose.PARK
						: Settings.Autonomous.BlueFar.PARK;
			default:
				return new Pose(); // fallback
		}
	}
	
	/**
	 * This allows us to run commands only if the related mechanism works.
	 * For example I could run "if launcher exists, shoot it" using this.
	 */
	private <T> boolean ifMechanismValid(T mechanism, java.util.function.Consumer<T> action) {
		if (mechanism != null) {
			action.accept(mechanism);
			return true;
		} else {
			return false;
		}
	}
}
