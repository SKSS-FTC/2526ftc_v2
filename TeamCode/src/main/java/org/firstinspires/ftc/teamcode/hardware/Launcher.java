package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.configuration.Settings;
import org.firstinspires.ftc.teamcode.software.TrajectoryEngine;

public class Launcher {
    // TODO
    private final TrajectoryEngine trajectoryEngine;

    public Servo horizontalServo;
    public Servo verticalServo;
    public Sorter sorter;
    public SyncBelt belt;

    public Launcher(Sorter sorter, DcMotor beltRight, DcMotor beltLeft, Servo horizontalServo, Servo verticalServo, TrajectoryEngine trajectoryEngine) {
        this.sorter = sorter;
        this.trajectoryEngine = trajectoryEngine;
        this.horizontalServo = horizontalServo;
        this.verticalServo = verticalServo;
        this.belt = new SyncBelt(beltRight, beltLeft);
    }

    public void launch() {
        if (okayToLaunch()) {
            // FIRE IN THE HOLE!
            sorter.ejectBallAtExit();
        }
    }

    public void ready() {
        // ready the launcher to fire
        belt.spinUp();
        sorter.rotateNextArtifactToExit();
    }

    public void stop() {
        belt.spinDown();
    }

    public boolean okayToLaunch() {
        return trajectoryEngine.isOkayToLaunch() && belt.atSpeed() && sorter.isNextArtifactAtExit();
    }

    public class SyncBelt {
        public DcMotor beltRight;
        public DcMotor beltLeft;

        public long spinupTimestamp = 0;
        public boolean active = false;

        public SyncBelt(DcMotor turretLauncherRight, DcMotor turretLauncherLeft) {
            this.beltRight = turretLauncherRight;
            this.beltLeft = turretLauncherLeft;
        }

        public void spinUp() {
            if (active) {
                return;
            }
            active = true;
            spinupTimestamp = System.currentTimeMillis();
            beltRight.setPower(Settings.Launcher.BELT_MOTOR_SPEED);
            beltLeft.setPower(Settings.Launcher.BELT_MOTOR_SPEED);
        }

        public void spinDown() {
            active = false;
            spinupTimestamp = 0;
            beltRight.setPower(0);
            beltLeft.setPower(0);
        }

        /**
         * Returns true if the belt has been active for longer than the configured spinup time.
         *
         * @return true if the belt is at speed, false otherwise.
         */
        public boolean atSpeed() {
            return active && System.currentTimeMillis() - spinupTimestamp > Settings.Launcher.BELT_SPINUP_TIME_MS;
        }

    }

}
