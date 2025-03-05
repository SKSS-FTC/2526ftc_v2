package org.firstinspires.ftc.teamcode;

import com.pedropathing.localization.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.input.CombinedControllerProfile;
import org.firstinspires.ftc.teamcode.input.InputManager;
import org.firstinspires.ftc.teamcode.input.MovementInputEvent;
import org.firstinspires.ftc.teamcode.input.ProfileManager;
import org.firstinspires.ftc.teamcode.mechanisms.MechanismManager;
import org.firstinspires.ftc.teamcode.mechanisms.submechanisms.LimelightManager;
import org.firstinspires.ftc.teamcode.mechanisms.submechanisms.Shoulder;
import org.firstinspires.ftc.teamcode.mechanisms.submechanisms.ViperSlide;
import org.firstinspires.ftc.teamcode.mechanisms.submechanisms.Wrist;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Main TeleOp class for driver-controlled period.
 * Handles controller profile selection and robot operation during matches.
 * 
 * @noinspection HardcodedLineSeparator, CodeBlock2Expr
 */
@TeleOp(name = "MainOp", group = ".Competition Modes")
public class MainOp extends LinearOpMode {

    private MechanismManager mechanisms;
    private InputManager inputManager;
    private Drivetrain drivetrain;
    private GoBildaPinpointDriver manualPinpoint;
    private boolean chassisDisabled = false;
    private double flip = 1.0;

    private boolean prevGamepadTriangle;
    private LimelightManager.LimelightPipeline pipeline = LimelightManager.LimelightPipeline.BLUE;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private double storedTx;

    /**
     * Main execution flow:
     * 1. Displays controller profile selection menu
     * 2. Initializes robot with selected profiles
     * 3. Runs main control loop for driver operation
     * 4. Handles shutdown when OpMode ends
     */
    @Override
    public void runOpMode() {
        // Show profile selection menu for both controllers
        CombinedControllerProfile mainProfile = selectControllerProfiles();
        if (mainProfile == null) {
            // If we didn't complete profile selection (op mode was stopped), exit
            return;
        }

        // Initialize robot systems
        mechanisms = new MechanismManager(hardwareMap);
        inputManager = new InputManager(gamepad1, gamepad2, mainProfile);
        drivetrain = new Drivetrain(hardwareMap);

        // Configure input handlers
        setupInputHandlers();

        // Wait for start
        waitForStart();

        // Initialize mechanisms
        mechanisms.init();
        manualPinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        mechanisms.intake.limelight.setCurrentPipeline(pipeline);

        // Main loop
        while (opModeIsActive()) {
            // Update pinpoint localization
            manualPinpoint.update();

            // Process all inputs
            inputManager.update();

            // Check automation and assistance conditions
            checkAutomationConditions();
            checkAssistanceConditions();

            // Update mechanisms
            mechanisms.outtake.verticalSlide.checkMotors();
            mechanisms.intake.colorSensor.update();

            // Update telemetry
            updateTelemetry();
        }
    }

    /**
     * Displays the controller profile selection menu and returns the selected
     * profile
     */
    private CombinedControllerProfile selectControllerProfiles() {
        List<CombinedControllerProfile> mainProfiles = ProfileManager.getMainProfiles();
        List<CombinedControllerProfile> subProfiles = ProfileManager.getSubProfiles();

        AtomicReference<CombinedControllerProfile> mainProfile = new AtomicReference<>(mainProfiles.get(0));
        AtomicReference<CombinedControllerProfile> subProfile = new AtomicReference<>(subProfiles.get(0));

        AtomicInteger mainSelection = new AtomicInteger(0);
        AtomicInteger subSelection = new AtomicInteger(0);

        AtomicBoolean mainConfirmed = new AtomicBoolean(false);
        AtomicBoolean subConfirmed = new AtomicBoolean(false);

        boolean menuConfirmed = false;

        while (!isStarted() && !isStopRequested() && !menuConfirmed) {
            // Display menu and handle selection
            telemetry.addLine("\nSelected Profiles:");
            telemetry.addData("Main Controller", mainProfile.get().name + (mainConfirmed.get() ? " (Confirmed)" : ""));
            telemetry.addData("Sub Controller", subProfile.get().name + (subConfirmed.get() ? " (Confirmed)" : ""));
            telemetry.addLine("Limelight Color: "
                    + (pipeline == LimelightManager.LimelightPipeline.BLUE ? "BLUE" : "RED") + ". Press ▲ to switch.");

            // Build options array for main profiles
            String[] mainOptions = new String[mainProfiles.size() + 1];
            for (int i = 0; i < mainProfiles.size(); i++) {
                mainOptions[i] = mainProfiles.get(i).name;
            }
            mainOptions[mainOptions.length - 1] = "Confirm";

            // Build options array for sub profiles
            String[] subOptions = new String[subProfiles.size() + 1];
            for (int i = 0; i < subProfiles.size(); i++) {
                subOptions[i] = subProfiles.get(i).name;
            }
            subOptions[subOptions.length - 1] = "Confirm";

            // Display menu header
            telemetry.addLine("=== Controller Profile Selection ===");

            // Main Controller Menu
            if (!mainConfirmed.get()) {
                telemetry.addLine("\nMain Controller (Gamepad 1):");
                MenuHelper.displayMenuOptions(telemetry, mainOptions, mainSelection.get());
            }

            // Sub Controller Menu
            if (!subConfirmed.get()) {
                telemetry.addLine("\nSub Controller (Gamepad 2):");
                MenuHelper.displayMenuOptions(telemetry, subOptions, subSelection.get());
            }

            // Handle controller inputs with debounce
            MenuHelper.handleControllerInput(this, gamepad1, !mainConfirmed.get(), () -> {
                if (gamepad1.dpad_up) {
                    mainSelection.set((mainSelection.get() - 1 + mainOptions.length) % mainOptions.length);
                } else if (gamepad1.dpad_down) {
                    mainSelection.set((mainSelection.get() + 1) % mainOptions.length);
                } else if (gamepad1.cross) {
                    if (mainSelection.get() < mainProfiles.size()) {
                        mainProfile.set(mainProfiles.get(mainSelection.get()));
                    } else {
                        mainConfirmed.set(true);
                        gamepad1.rumble(200);
                    }
                }
            });

            MenuHelper.handleControllerInput(this, gamepad2, !subConfirmed.get(), () -> {
                if (gamepad2.dpad_up) {
                    subSelection.set((subSelection.get() - 1 + subOptions.length) % subOptions.length);
                } else if (gamepad2.dpad_down) {
                    subSelection.set((subSelection.get() + 1) % subOptions.length);
                } else if (gamepad2.cross) {
                    if (subSelection.get() < subProfiles.size()) {
                        subProfile.set(subProfiles.get(subSelection.get()));
                    } else {
                        subConfirmed.set(true);
                        gamepad2.rumble(200);
                    }
                }
            });

            // Handle pipeline selection
            if (gamepad1.triangle) {
                if (!prevGamepadTriangle) {
                    pipeline = pipeline == LimelightManager.LimelightPipeline.BLUE
                            ? LimelightManager.LimelightPipeline.RED
                            : LimelightManager.LimelightPipeline.BLUE;
                    gamepad1.rumble(50);
                    gamepad2.rumble(50);
                }
                prevGamepadTriangle = true;
            } else {
                prevGamepadTriangle = false;
            }

            // Set game controller colors based on pipeline
            if (pipeline == LimelightManager.LimelightPipeline.BLUE) {
                gamepad1.setLedColor(0, 0, 255, 1000);
                gamepad2.setLedColor(0, 0, 255, 1000);
            } else {
                gamepad1.setLedColor(255, 0, 0, 1000);
                gamepad2.setLedColor(255, 0, 0, 1000);
            }

            // Check for menu completion
            menuConfirmed = mainConfirmed.get() && subConfirmed.get();

            telemetry.update();
            sleep(50); // Small delay to prevent CPU hogging
        }

        // If we were stopped during selection, return null
        if (isStopRequested()) {
            return null;
        }

        // Create a combined profile with the main gamepad settings and sub gamepad
        // settings
        return new CombinedControllerProfile(
                mainProfile.get().name + " + " + subProfile.get().name,
                mainProfile.get().mainGamepadSettings,
                subProfile.get().subGamepadSettings);
    }

    /**
     * Sets up all input handlers for the robot
     */
    private void setupInputHandlers() {
        // Movement handler
        inputManager.on("movement", event -> {
            if (!chassisDisabled) {
                MovementInputEvent movementEvent = (MovementInputEvent) event;
                drivetrain.mecanumDrive(
                        movementEvent.forward,
                        movementEvent.strafe,
                        movementEvent.rotation,
                        flip);
            }
        });

        // Flip movement handler
        inputManager.on("main.GUIDE.pressed", event -> {
            flip *= -1;
            gamepad1.rumble(100);
        });

        // === INTAKE CONTROLS ===

        // Intake claw controls
        inputManager.on("sub.LEFT_TRIGGER.pressed", event -> {
            mechanisms.intake.intakeClaw.close();
            scheduleTask(() -> mechanisms.intake.wrist.setPosition(Wrist.Position.VERTICAL), 200);
            mechanisms.outtake.outtakeClaw.open();
        });

        inputManager.on("sub.RIGHT_TRIGGER.held", event -> {
            mechanisms.intake.intakeClaw.open();
        });

        // Wrist controls
        inputManager.on("sub.DPAD_LEFT.pressed", event -> {
            if (mechanisms.intake.horizontalSlide.currentPosition.getValue() > 30 &&
                    mechanisms.intake.intakeClaw.opened) {
                mechanisms.intake.wrist.setPosition(Wrist.Position.READY);
            } else {
                mechanisms.intake.wrist.setPosition(Wrist.Position.VERTICAL);
            }
        });

        inputManager.on("sub.DPAD_RIGHT.held", event -> {
            mechanisms.intake.wrist.setPosition(Wrist.Position.HORIZONTAL);
        });

        // Horizontal slide controls - incremental mode
        inputManager.on("sub.CIRCLE.held", event -> {
            if (inputManager.profile.subGamepadSettings.incrementalHorizontal) {
                mechanisms.intake.horizontalSlide.increment();
            }
        });

        inputManager.on("sub.SQUARE.held", event -> {
            if (inputManager.profile.subGamepadSettings.incrementalHorizontal) {
                mechanisms.intake.horizontalSlide.decrement();
            }
        });

        // Horizontal slide controls - direct mode
        inputManager.on("sub.CIRCLE.pressed", event -> {
            if (!inputManager.profile.subGamepadSettings.incrementalHorizontal) {
                mechanisms.intake.horizontalSlide.extend();
            }
        });

        inputManager.on("sub.SQUARE.pressed", event -> {
            if (!inputManager.profile.subGamepadSettings.incrementalHorizontal) {
                mechanisms.intake.horizontalSlide.retract();
                if (mechanisms.intake.horizontalSlide.currentPosition == ViperSlide.HorizontalPosition.COLLAPSED) {
                    mechanisms.outtake.outtakeClaw.open();
                    mechanisms.intake.wrist.setPosition(Wrist.Position.VERTICAL);
                }
            }
        });

        // Rotator control
        inputManager.on("sub.RIGHT_STICK_X", event -> {
            double normalizedValue = (event.value + 1) / 2; // Convert from -1..1 to 0..1
            mechanisms.intake.rotator.setPosition(normalizedValue);
        });

        // === OUTTAKE CONTROLS ===

        // Vertical slide controls - incremental mode
        inputManager.on("main.RIGHT_BUMPER.held", event -> {
            if (inputManager.profile.mainGamepadSettings.incrementalVertical) {
                mechanisms.outtake.verticalSlide.increment();
            }
        });

        inputManager.on("main.LEFT_BUMPER.held", event -> {
            if (inputManager.profile.mainGamepadSettings.incrementalVertical) {
                mechanisms.outtake.verticalSlide.decrement();
            }
        });

        // Vertical slide controls - direct mode
        inputManager.on("main.RIGHT_BUMPER.pressed", event -> {
            if (!inputManager.profile.mainGamepadSettings.incrementalVertical) {
                mechanisms.outtake.verticalSlide.extend();
            }
        });

        inputManager.on("main.LEFT_BUMPER.pressed", event -> {
            if (!inputManager.profile.mainGamepadSettings.incrementalVertical) {
                mechanisms.outtake.verticalSlide.retract();
            }
        });

        // Vertical slide position presets
        inputManager.on("main.TRIANGLE.pressed", event -> {
            mechanisms.outtake.verticalSlide.setPosition(ViperSlide.VerticalPosition.HIGH_BASKET);
        });

        inputManager.on("main.CROSS.pressed", event -> {
            mechanisms.outtake.verticalSlide.setPosition(ViperSlide.VerticalPosition.HIGH_RUNG);
        });

        inputManager.on("main.SQUARE.pressed", event -> {
            mechanisms.outtake.verticalSlide.setPosition(ViperSlide.VerticalPosition.PREP_HIGH_RUNG);
        });

        inputManager.on("main.CIRCLE.pressed", event -> {
            mechanisms.outtake.verticalSlide.setPosition(ViperSlide.VerticalPosition.TRANSFER);
        });

        // Outtake claw control
        inputManager.on("sub.RIGHT_STICK_BUTTON.pressed", event -> {
            if (mechanisms.outtake.outtakeClaw.opened) {
                mechanisms.outtake.outtakeClaw.close();
                scheduleTask(() -> mechanisms.intake.intakeClaw.open(), 400);
            } else {
                mechanisms.outtake.outtakeClaw.open();
            }
        });

        // Shoulder control
        inputManager.on("sub.LEFT_STICK_BUTTON.pressed", event -> {
            mechanisms.outtake.shoulder.cyclePosition();
        });

        // === LINEAR ACTUATOR CONTROLS ===

        inputManager.on("sub.TRIANGLE.held", event -> {
            mechanisms.linearActuator.extend();
        });

        inputManager.on("sub.CROSS.held", event -> {
            mechanisms.linearActuator.retract();
        });

        // === ASSISTANCE CONTROLS ===

        inputManager.on("main.TOUCHPAD.pressed", event -> {
            chassisDisabled = true;
        });

        inputManager.on("main.TOUCHPAD.released", event -> {
            chassisDisabled = false;
        });
    }

    /**
     * Checks for conditions that trigger automated actions
     */
    private void checkAutomationConditions() {
        // Skip if automation is disabled
        if (!Settings.Teleop.automationEnabled) {
            return;
        }

        // Automatically transfer when everything is collapsed
        boolean horizontalCollapsed = mechanisms.intake.horizontalSlide.currentPosition
                .getValue() <= ViperSlide.HorizontalPosition.COLLAPSED.getValue() + 10;

        boolean verticalCollapsed = mechanisms.outtake.verticalSlide.verticalMotorRight
                .getCurrentPosition() <= ViperSlide.VerticalPosition.TRANSFER.getValue() + 10;

        boolean intakeHasPixel = !mechanisms.intake.intakeClaw.opened;
        boolean outtakeClawClosed = mechanisms.outtake.outtakeClaw.clawServo.getPosition() > 0.8;
        boolean shoulderInBackPosition = mechanisms.outtake.shoulder.position() == Shoulder.Position.PLACE_BACKWARD;

        if (horizontalCollapsed && verticalCollapsed && intakeHasPixel &&
                outtakeClawClosed && shoulderInBackPosition) {
            mechanisms.intake.intakeClaw.open();
            scheduleTask(() -> mechanisms.outtake.moveShoulderToBack(), 200);
        }
    }

    /**
     * Checks for conditions that trigger driver assistance
     */
    private void checkAssistanceConditions() {
        boolean specimenDetected = mechanisms.intake.limelight.specimenDetected();
        boolean headingAligned = Math.abs(manualPinpoint.getHeading()) < 0.3;

        if (specimenDetected && headingAligned) {
            if (gamepad1.touchpad) {
                gamepad1.setLedColor(0, 0, 255, 1000);
                drivetrain.interpolateToOffset(
                        mechanisms.intake.limelight.limelight.getLatestResult().getTx(),
                        Settings.Assistance.approachSpeed,
                        wrappedHeading());
                storedTx = mechanisms.intake.limelight.limelight.getLatestResult().getTx();
                chassisDisabled = true;
            } else {
                gamepad1.rumble(50);
                gamepad1.setLedColor(0, 255, 0, 1000);
                chassisDisabled = false;
            }
        } else if (chassisDisabled && gamepad1.touchpad && storedTx != 0) {
            gamepad1.setLedColor(255, 0, 255, 1000);
            drivetrain.interpolateToOffset(
                    mechanisms.intake.limelight.limelight.getLatestResult().getTx(),
                    0.35,
                    wrappedHeading());
        } else {
            storedTx = 0;
            if (gamepad1.touchpad) {
                gamepad1.setLedColor(0, 255, 255, 1000);
                drivetrain.interpolateToOffset(0, 0, wrappedHeading());
                chassisDisabled = true;
            } else {
                gamepad1.setLedColor(255, 0, 0, 1000);
                chassisDisabled = false;
            }
        }
    }

    /**
     * Updates telemetry with current robot state
     */
    private void updateTelemetry() {
        telemetry.addData("limelight tx", mechanisms.intake.limelight.limelight.getLatestResult().getTx());
        telemetry.addData("limelight ty", mechanisms.intake.limelight.limelight.getLatestResult().getTy());
        telemetry.addData("limelight detects specimen?", mechanisms.intake.limelight.specimenDetected());
        telemetry.addData("heading", wrappedHeading());
        telemetry.update();
    }

    /**
     * Normalizes heading to range -π to π
     */
    private double wrappedHeading() {
        return (manualPinpoint.getHeading() + Math.PI) % (2 * Math.PI) - Math.PI;
    }

    /**
     * Schedules a task to run after a delay
     */
    private void scheduleTask(Runnable task, long delayMillis) {
        scheduler.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
    }
}