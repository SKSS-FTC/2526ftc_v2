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

@Autonomous(name = "Main Auto", group = ".Competition Modes")
public class MainAuto extends OpMode {
	
	private Timer pathTimer, opmodeTimer;
	private int pathState;
	private MatchConfigurationWizard wizard;
	private MechanismManager mechanisms;
	private MatchSettings matchSettings;
	
	// PathChain declarations with more descriptive names.
	// Far paths
	private PathChain farPreset1Prep;
	private PathChain farPreset1End;
	private PathChain farLaunch1;
	private PathChain farPreset2Prep;
	private PathChain farPreset2End;
	private PathChain farLaunch2;
	private PathChain farPreset3Prep;
	private PathChain farPreset3End;
	private PathChain farLaunch3;
	private PathChain farPark;
	
	// Close paths
	private PathChain closePreset1Prep;
	private PathChain closePreset1End;
	private PathChain closeLaunch1;
	private PathChain closePreset2Prep;
	private PathChain closePreset2End;
	private PathChain closeLaunch2;
	private PathChain closePreset3Prep;
	private PathChain closePreset3End;
	private PathChain closeLaunch3;
	private PathChain closePark;
	
	
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
				mechanisms.follower.setStartingPose(Settings.Autonomous.RedFar.START);
			} else { // BLUE
				buildBlueFarPaths();
				mechanisms.follower.setStartingPose(Settings.Autonomous.BlueFar.START);
			}
		} else { // CLOSE
			if (alliance == MatchSettings.AllianceColor.RED) {
				buildRedClosePaths();
				mechanisms.follower.setStartingPose(Settings.Autonomous.RedClose.START);
			} else { // BLUE
				buildBlueClosePaths();
				mechanisms.follower.setStartingPose(Settings.Autonomous.BlueClose.START);
			}
		}
	}
	
	// NOTE: Far paths are retained from the original code but assigned to new variables for clarity.
	public void buildRedFarPaths() {
		farPreset1Prep = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.START, Settings.Autonomous.RedFar.PRESET_1_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.START.getHeading(), Settings.Autonomous.RedFar.PRESET_1_PREP.getHeading())
				.build();
		
		farPreset1End = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.PRESET_1_PREP, Settings.Autonomous.RedFar.PRESET_1_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRESET_1_PREP.getHeading(), Settings.Autonomous.RedFar.PRESET_1_END.getHeading())
				.build();
		
		farLaunch1 = mechanisms.follower.pathBuilder()
				.addPath(Settings.Autonomous.RedFar.BEZIER_LAUNCH_1)
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRESET_1_END.getHeading(), Settings.Autonomous.RedFar.ENDING_LAUNCH_1.getHeading())
				.build();
		
		farPreset2Prep = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.ENDING_LAUNCH_1, Settings.Autonomous.RedFar.PRESET_2_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.ENDING_LAUNCH_1.getHeading(), Settings.Autonomous.RedFar.PRESET_2_PREP.getHeading())
				.build();
		
		farPreset2End = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.PRESET_2_PREP, Settings.Autonomous.RedFar.PRESET_2_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRESET_2_PREP
						.getHeading(), Settings.Autonomous.RedFar.PRESET_2_END.getHeading())
				.build();
		
		farLaunch2 = mechanisms.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedFar.PRESET_2_END, Settings.Autonomous.RedFar.LAUNCH_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRESET_2_END.getHeading(), Settings.Autonomous.RedFar.LAUNCH_2.getHeading())
				.build();
		
		farPreset3Prep = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.LAUNCH_2, Settings.Autonomous.RedFar.PRESET_3_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.LAUNCH_2.getHeading(), Settings.Autonomous.RedFar.PRESET_3_END.getHeading())
				.build();
		
		farLaunch3 = mechanisms.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedFar.PRESET_3_END, Settings.Autonomous.RedFar.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PRESET_3_END.getHeading(), Settings.Autonomous.RedFar.PARK.getHeading())
				.build();
		
		farPark = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedFar.PARK, Settings.Autonomous.RedFar.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedFar.PARK.getHeading(), Settings.Autonomous.RedFar.PARK.getHeading())
				.build();
	}
	
	public void buildBlueFarPaths() {
		farPreset1Prep = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.START, Settings.Autonomous.BlueFar.PRESET_1_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.START.getHeading(), Settings.Autonomous.BlueFar.PRESET_1_PREP.getHeading())
				.build();
		
		farPreset1End = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.PRESET_1_PREP, Settings.Autonomous.BlueFar.PRESET_1_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRESET_1_PREP.getHeading(), Settings.Autonomous.BlueFar.PRESET_1_END.getHeading())
				.build();
		
		farLaunch1 = mechanisms.follower.pathBuilder()
				.addPath(Settings.Autonomous.BlueFar.BEZIER_LAUNCH_1)
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRESET_1_END.getHeading(), Settings.Autonomous.BlueFar.ENDING_LAUNCH_1.getHeading())
				.build();
		
		farPreset2Prep = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.ENDING_LAUNCH_1, Settings.Autonomous.BlueFar.PRESET_2_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.ENDING_LAUNCH_1.getHeading(), Settings.Autonomous.BlueFar.PRESET_2_PREP.getHeading())
				.build();
		
		farPreset2End = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.PRESET_2_PREP, Settings.Autonomous.BlueFar.PRESET_2_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRESET_2_PREP
						.getHeading(), Settings.Autonomous.BlueFar.PRESET_2_END.getHeading())
				.build();
		
		farLaunch2 = mechanisms.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.BlueFar.PRESET_2_END, Settings.Autonomous.BlueFar.LAUNCH_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRESET_2_END.getHeading(), Settings.Autonomous.BlueFar.LAUNCH_2.getHeading())
				.build();
		
		farPreset3Prep = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.LAUNCH_2, Settings.Autonomous.BlueFar.PRESET_3_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.LAUNCH_2.getHeading(), Settings.Autonomous.BlueFar.PRESET_3_END.getHeading())
				.build();
		
		farLaunch3 = mechanisms.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.BlueFar.PRESET_3_END, Settings.Autonomous.BlueFar.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PRESET_3_END.getHeading(), Settings.Autonomous.BlueFar.PARK.getHeading())
				.build();
		
		farPark = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueFar.PARK, Settings.Autonomous.BlueFar.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueFar.PARK.getHeading(), Settings.Autonomous.BlueFar.PARK.getHeading())
				.build();
	}
	
	/**
	 * OVERHAULED: Builds the paths for the RED CLOSE starting position.
	 */
	public void buildRedClosePaths() {
		closePreset1Prep = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.START, Settings.Autonomous.RedClose.PRESET_1_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.START.getHeading(), Settings.Autonomous.RedClose.PRESET_1_PREP.getHeading())
				.build();
		
		closePreset1End = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.PRESET_1_PREP, Settings.Autonomous.RedClose.PRESET_1_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_1_PREP.getHeading(), Settings.Autonomous.RedClose.PRESET_1_END.getHeading())
				.build();
		
		closeLaunch1 = mechanisms.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedClose.PRESET_1_END, Settings.Autonomous.RedClose.LAUNCH_1, Settings.Autonomous.RedClose.LAUNCH_1))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_1_END.getHeading(), Settings.Autonomous.RedClose.LAUNCH_1.getHeading())
				.build();
		
		closePreset2Prep = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.LAUNCH_1, Settings.Autonomous.RedClose.PRESET_2_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.LAUNCH_1.getHeading(), Settings.Autonomous.RedClose.PRESET_2_PREP.getHeading())
				.build();
		
		closePreset2End = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.PRESET_2_PREP, Settings.Autonomous.RedClose.PRESET_2_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_2_PREP
						.getHeading(), Settings.Autonomous.RedClose.PRESET_2_END.getHeading())
				.build();
		
		closeLaunch2 = mechanisms.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedClose.PRESET_2_END, Settings.Autonomous.RedClose.LAUNCH_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_2_END.getHeading(), Settings.Autonomous.RedClose.LAUNCH_2.getHeading())
				.build();
		
		closePreset3Prep = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(mechanisms.follower::getPose, Settings.Autonomous.RedClose.PRESET_3_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.LAUNCH_2.getHeading(), Settings.Autonomous.RedClose.PRESET_3_PREP.getHeading())
				.build();
		
		closePreset3End = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.PRESET_3_PREP, Settings.Autonomous.RedClose.PRESET_3_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_3_PREP.getHeading(), Settings.Autonomous.RedClose.PRESET_3_END.getHeading())
				.build();
		
		closeLaunch3 = mechanisms.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.RedClose.PRESET_3_END, Settings.Autonomous.RedClose.LAUNCH_3))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.PRESET_3_END.getHeading(), Settings.Autonomous.RedClose.LAUNCH_3.getHeading())
				.build();
		
		closePark = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.RedClose.LAUNCH_3, Settings.Autonomous.RedClose.PARK))
				.setLinearHeadingInterpolation(Settings.Autonomous.RedClose.LAUNCH_3.getHeading(), Settings.Autonomous.RedClose.PARK.getHeading())
				.build();
	}
	
	/**
	 * OVERHAULED: Builds the paths for the BLUE CLOSE starting position using mirrored values.
	 */
	public void buildBlueClosePaths() {
		closePreset1Prep = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.START, Settings.Autonomous.BlueClose.PRESET_1_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.START.getHeading(), Settings.Autonomous.BlueClose.PRESET_1_PREP.getHeading())
				.build();
		
		closePreset1End = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.PRESET_1_PREP, Settings.Autonomous.BlueClose.PRESET_1_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_1_PREP.getHeading(), Settings.Autonomous.BlueClose.PRESET_1_END.getHeading())
				.build();
		
		closeLaunch1 = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.PRESET_1_END, Settings.Autonomous.BlueClose.LAUNCH_1))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_1_END.getHeading(), Settings.Autonomous.BlueClose.LAUNCH_1.getHeading())
				.build();
		
		closePreset2Prep = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.LAUNCH_1, Settings.Autonomous.BlueClose.PRESET_2_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.LAUNCH_1.getHeading(), Settings.Autonomous.BlueClose.PRESET_2_PREP.getHeading())
				.build();
		
		closePreset2End = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.PRESET_2_PREP, Settings.Autonomous.BlueClose.PRESET_2_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_2_PREP
						.getHeading(), Settings.Autonomous.BlueClose.PRESET_2_END.getHeading())
				.build();
		
		closeLaunch2 = mechanisms.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.BlueClose.PRESET_2_END, Settings.Autonomous.BlueClose.LAUNCH_2))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_2_END.getHeading(), Settings.Autonomous.BlueClose.LAUNCH_2.getHeading())
				.build();
		
		closePreset3Prep = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.LAUNCH_2, Settings.Autonomous.BlueClose.PRESET_3_PREP))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.LAUNCH_2.getHeading(), Settings.Autonomous.BlueClose.PRESET_3_PREP.getHeading())
				.build();
		
		closePreset3End = mechanisms.follower.pathBuilder()
				.addPath(new BezierLine(Settings.Autonomous.BlueClose.PRESET_3_PREP, Settings.Autonomous.BlueClose.PRESET_3_END))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_3_PREP.getHeading(), Settings.Autonomous.BlueClose.PRESET_3_END.getHeading())
				.build();
		
		closeLaunch3 = mechanisms.follower.pathBuilder()
				.addPath(new BezierCurve(Settings.Autonomous.BlueClose.PRESET_3_END, Settings.Autonomous.BlueClose.LAUNCH_3))
				.setLinearHeadingInterpolation(Settings.Autonomous.BlueClose.PRESET_3_END.getHeading(), Settings.Autonomous.BlueClose.LAUNCH_3.getHeading())
				.build();
		
		closePark = mechanisms.follower.pathBuilder()
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
				ifMechanismValid(mechanisms.get(Launcher.class), l -> {
					if (l.okayToLaunch()) l.launch();
				});
				ifMechanismValid(mechanisms.get(Spindex.class), s -> {
					if (s.isEmpty()) {
						mechanisms.follower.followPath(closePreset1Prep);
						setPathState(1);
					}
				});
				break;
			case 1:
				if (!mechanisms.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::in);
					mechanisms.follower.followPath(closePreset1End);
					setPathState(2);
				}
				break;
			case 2:
				if (!mechanisms.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::stop);
					mechanisms.follower.followPath(closeLaunch1);
					setPathState(3);
				}
				break;
			case 3:
				if (!mechanisms.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Launcher.class), l -> {
						if (l.okayToLaunch()) l.launch();
					});
					ifMechanismValid(mechanisms.get(Spindex.class), s -> {
						if (s.isEmpty()) {
							mechanisms.follower.followPath(closePreset2Prep);
							setPathState(4);
						}
					});
				}
				break;
			case 4:
				if (!mechanisms.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::in);
					mechanisms.follower.followPath(closePreset2End);
					setPathState(5);
				}
				break;
			case 5:
				if (!mechanisms.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::stop);
					mechanisms.follower.followPath(closeLaunch2);
					setPathState(6);
				}
				break;
			case 6:
				ifMechanismValid(mechanisms.get(Launcher.class), l -> {
					if (l.okayToLaunch()) l.launch();
				});
				ifMechanismValid(mechanisms.get(Spindex.class), s -> {
					if (s.isEmpty()) mechanisms.follower.followPath(closePreset3Prep);
					setPathState(7);
				});
				break;
			case 7:
				if (!mechanisms.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::in);
					mechanisms.follower.followPath(closePreset3End);
					setPathState(8);
				}
				break;
			case 8:
				if (!mechanisms.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::stop);
					mechanisms.follower.followPath(closeLaunch3);
					setPathState(9);
				}
				break;
			case 9:
				ifMechanismValid(mechanisms.get(Launcher.class), l -> {
					if (l.okayToLaunch()) l.launch();
				});
				ifMechanismValid(mechanisms.get(Spindex.class), s -> {
					if (s.isEmpty()) mechanisms.follower.followPath(closePark);
					setPathState(10);
				});
				break;
			case 10:
				break;
		}
	}
	
	private void updateFarPath() {
		switch (pathState) {
			case 0:
				ifMechanismValid(mechanisms.get(Launcher.class), l -> {
					if (l.okayToLaunch()) l.launch();
				});
				ifMechanismValid(mechanisms.get(Spindex.class), s -> {
					if (s.isEmpty()) mechanisms.follower.followPath(farPreset1Prep);
					setPathState(1);
				});
				break;
			case 1:
				if (!mechanisms.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::in);
					mechanisms.follower.followPath(farPreset1End);
					setPathState(2);
				}
				break;
			case 2:
				if (!mechanisms.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::stop);
					mechanisms.follower.followPath(farLaunch1);
					setPathState(3);
				}
				break;
			case 3:
				ifMechanismValid(mechanisms.get(Launcher.class), l -> {
					if (l.okayToLaunch()) l.launch();
				});
				ifMechanismValid(mechanisms.get(Spindex.class), s -> {
					if (s.isEmpty()) mechanisms.follower.followPath(farPreset2Prep);
					setPathState(4);
				});
				break;
			case 4:
				if (!mechanisms.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::in);
					mechanisms.follower.followPath(farPreset2End);
					setPathState(5);
				}
				break;
			case 5:
				if (!mechanisms.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::stop);
					mechanisms.follower.followPath(farLaunch2);
					setPathState(6);
				}
				break;
			case 6:
				ifMechanismValid(mechanisms.get(Launcher.class), l -> {
					if (l.okayToLaunch()) l.launch();
				});
				ifMechanismValid(mechanisms.get(Spindex.class), s -> {
					if (s.isEmpty()) mechanisms.follower.followPath(farPreset3Prep);
					setPathState(7);
				});
				break;
			case 7:
				if (!mechanisms.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::in);
					mechanisms.follower.followPath(farPreset3End);
					setPathState(8);
				}
				break;
			case 8:
				if (!mechanisms.follower.isBusy()) {
					ifMechanismValid(mechanisms.get(Intake.class), Intake::stop);
					mechanisms.follower.followPath(farLaunch3);
					setPathState(9);
				}
				break;
			case 9:
				ifMechanismValid(mechanisms.get(Launcher.class), l -> {
					if (l.okayToLaunch()) l.launch();
				});
				ifMechanismValid(mechanisms.get(Spindex.class), s -> {
					if (s.isEmpty()) mechanisms.follower.followPath(farPark);
					setPathState(10);
				});
				break;
			case 10:
				break;
		}
	}
	
	public void setPathState(int pState) {
		pathState = pState;
		pathTimer.resetTimer();
	}
	
	@Override
	public void loop() {
		mechanisms.follower.update();
		autonomousPathUpdate();
		
		Drawing.drawDebug(mechanisms.follower);
		telemetry.addData("Path State", pathState);
		telemetry.addData("X", mechanisms.follower.getPose().getX());
		telemetry.addData("Y", mechanisms.follower.getPose().getY());
		telemetry.addData("Heading", Math.toDegrees(mechanisms.follower.getPose().getHeading()));
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
		mechanisms.follower.setStartingPose(getStartingPose());
	}
	
	@Override
	public void init_loop() {
		// Allow driver to select match settings using the wizard
		wizard.refresh();
		Drawing.drawRobot(mechanisms.follower.getPose());
		Drawing.sendPacket();
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
		mechanisms.stop();
	}
	
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
	
	
	private <T> void ifMechanismValid(T mechanism, java.util.function.Consumer<T> action) {
		if (mechanism != null) action.accept(mechanism);
	}
}
