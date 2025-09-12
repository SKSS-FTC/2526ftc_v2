package org.firstinspires.ftc.teamcode.software;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.configuration.MatchSettings;
import org.firstinspires.ftc.teamcode.configuration.Settings;
import org.firstinspires.ftc.teamcode.hardware.submechanisms.LimelightManager;

public class TrajectoryEngine {

    private final GoBildaPinpointDriver pinpoint;
    private final MatchSettings matchSettings;
    private final LimelightManager limelightManager;

    public TrajectoryEngine(LimelightManager limelightManager, GoBildaPinpointDriver pinpoint, MatchSettings matchSettings) {
        this.limelightManager = limelightManager;
        this.pinpoint = pinpoint;
        this.matchSettings = matchSettings;
    }

    /**
     * Computes turret yaw (relative to robot heading) and pitch to aim at target.
     *
     * @param robotPose robot Pose2d (x,y,heading radians)
     * @param cornerX   target right-angle corner x
     * @param cornerY   target right-angle corner y
     * @param depth     depth of target (hypotenuse length component)
     * @return double[]{yawRadians, pitchRadians} pitch null if no solution
     */
    public static double[] aimTurret(Pose2D robotPose,
                                     double cornerX, double cornerY,
                                     double depth) {
        // Compute midpoint of hypotenuse (assuming legs along +x,+y from corner)
        double targetX = cornerX + depth / 2.0;
        double targetY = cornerY + depth / 2.0;

        // horizontal vector
        double dx = targetX - robotPose.getX(DistanceUnit.CM);
        double dy = targetY - robotPose.getY(DistanceUnit.CM);
        double d = Math.hypot(dx, dy);

        double yawWorld = Math.atan2(dy, dx);
        double yawRel = normalizeAngle(yawWorld - robotPose.getHeading(AngleUnit.RADIANS));

        // vertical difference
        double h = Settings.Aiming.goalHeight - Settings.Aiming.muzzleHeight;
        double v = Settings.Aiming.muzzleSpeed;
        double g = Settings.Aiming.gravity;

        double disc = v * v * v * v - g * (g * d * d + 2 * h * v * v);
        Double pitch = null;
        if (disc >= 0 && d > 1e-6) {
            double sqrtDisc = Math.sqrt(disc);
            double t1 = Math.atan((v * v + sqrtDisc) / (g * d));
            double t2 = Math.atan((v * v - sqrtDisc) / (g * d));
            // choose lower angle
            pitch = Math.min(t1, t2);
        }

        return new double[]{yawRel, pitch != null ? pitch : Double.NaN};
    }

    public static boolean okayToLaunch(Pose2D robotPose,
                                       double cornerX, double cornerY,
                                       double depth) {
        double[] aim = aimTurret(robotPose, cornerX, cornerY, depth);
        double yaw = aim[0];
        double pitch = aim[1];

        if (Double.isNaN(pitch)) return false; // no ballistic solution

        // check angular tolerances
        if (Math.abs(yaw) > Settings.Aiming.maxYawError) return false;
        if (Math.abs(pitch - aim[1]) > Settings.Aiming.maxPitchError)
            return false; // always 0, can skip

        // check if vertical impact is within goal tolerance
        double dx = (cornerX + depth / 2.0) - robotPose.getX(DistanceUnit.CM);
        double dy = (cornerY + depth / 2.0) - robotPose.getY(DistanceUnit.CM);
        double d = Math.hypot(dx, dy);
        double predictedHeight = Settings.Aiming.muzzleHeight +
                d * Math.tan(pitch) -
                (Settings.Aiming.gravity * d * d) /
                        (2 * Settings.Aiming.muzzleSpeed * Settings.Aiming.muzzleSpeed * Math.cos(pitch) * Math.cos(pitch));

        return !(Math.abs(predictedHeight - Settings.Aiming.goalHeight) > Settings.Aiming.goalTolerance);
    }

    private static double normalizeAngle(double a) {
        while (a > Math.PI) a -= 2 * Math.PI;
        while (a < -Math.PI) a += 2 * Math.PI;
        return a;
    }
}
