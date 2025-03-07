package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.List;


public class Echolocation {
    /* How many telemetry lines the Echolocate visualization takes up. */
    public static final int VERTICAL_RESOLUTION = 10;
    /* How wide telemetry lines are. */
    public static final int HORIZONTAL_RESOLUTION = 8;

    /**
     * The phonate method allows the user to interactively define a list of positions
     * using gamepad inputs and visualize them on the telemetry screen.
     */
    public static List<Position> phonate(Telemetry telemetry, Gamepad gamepad, PositionType positionType) {
        List<Echo> echoes = new ArrayList<>();
        Echo nextEcho = new Echo(0, 0, 0);
        boolean crossDebounceActive = false;
        boolean dpadDebounceActive = false;
        boolean bumperDebounceActive = false;

        while (!gamepad.options) {
            if (gamepad.cross) {
                if (!crossDebounceActive) {
                    echoes.add(nextEcho);
                    nextEcho = new Echo(0, 0, 0);
                }
                crossDebounceActive = true;
            }
            if (!gamepad.cross) {
                crossDebounceActive = false;
            }

            if (!dpadDebounceActive) {
                // dpad moves echo by 1 count in the respective direction
                if (gamepad.dpad_up) {
                    nextEcho.y += 1;
                    dpadDebounceActive = true;
                }
                if (gamepad.dpad_down) {
                    nextEcho.y -= 1;
                    dpadDebounceActive = true;
                }
                if (gamepad.dpad_left) {
                    nextEcho.x -= 1;
                    dpadDebounceActive = true;
                }
                if (gamepad.dpad_right) {
                    nextEcho.x += 1;
                    dpadDebounceActive = true;
                }
                // bound echo to the screen resolution
                nextEcho.x = Math.max(-HORIZONTAL_RESOLUTION, Math.min(HORIZONTAL_RESOLUTION, nextEcho.x));
                nextEcho.y = Math.max(-VERTICAL_RESOLUTION, Math.min(VERTICAL_RESOLUTION, nextEcho.y));
            }
            if (!gamepad.dpad_down && !gamepad.dpad_up && !gamepad.dpad_left && !gamepad.dpad_right) {
                dpadDebounceActive = false;
            }

            if (!bumperDebounceActive) {
                // right and left bumper change rotation by 1/4 PI (45 degrees aka one emoji arrow)
                if (gamepad.right_bumper) {
                    nextEcho.rotation += Math.toRadians(45);
                    bumperDebounceActive = true;
                } else if (gamepad.left_bumper) {
                    nextEcho.rotation -= Math.toRadians(45);
                    bumperDebounceActive = true;
                }
            }

            if (!gamepad.right_bumper && !gamepad.left_bumper) {
                bumperDebounceActive = false;
            }

            telemetry.addLine("==== ECHOLOCATION ====");
            for (double y = VERTICAL_RESOLUTION / 2.0; y >= -VERTICAL_RESOLUTION / 2.0; y -= 1) { // Iterate across the vertical resolution
                StringBuilder row = new StringBuilder();
                for (double x = -HORIZONTAL_RESOLUTION / 2.0; x <= HORIZONTAL_RESOLUTION / 2.0; x += 1) { // Iterate across the horizontal resolution
                    String tile = ((int) x + (int) y) % 2 == 0 ? "⬛" : "⬜";

                    // Check if the nextEcho is at this position
                    if (Math.abs(nextEcho.x - x) < 0.25 && Math.abs(nextEcho.y - y) < 0.25) {
                        row.append(rotationToArrowEmoji(nextEcho.rotation));
                    } else {
                        row.append(tile);
                    }
                }
                telemetry.addLine(row.toString());
            }

            telemetry.addData("(x, y)", "(" + nextEcho.x + ", " + nextEcho.y + ")");
            telemetry.addData("rotation", nextEcho.rotation);
            telemetry.addData("previous confirmed positions", echoes.size());
            telemetry.addLine("(Options to confirm)");
            telemetry.update();
        }


        List<Position> results = new ArrayList<>();
        switch (positionType) {
            case PEDRO:
                for (Echo echo : echoes) {
                    results.add(new PedroPosition(0, 0, 0).fromEcho(echo));
                }
                break;
            case ROADRUNNER:
                for (Echo echo : echoes) {
                    results.add(new RoadRunnerPosition(0, 0, 0).fromEcho(echo));
                }
                break;
        }
        return results;
    }

    /**
     * Converts a rotation angle (in radians) to a corresponding arrow emoji.
     * This method divides the full circle (2π radians) into 8 equal segments,
     * each represented by a directional arrow emoji. The input rotation is
     * normalized to be within the range of 0 to 2π, and then mapped to one of
     * the 8 arrow directions.
     *
     * @param rotation The rotation angle in radians.
     * @return The corresponding arrow emoji representing the direction.
     */
    public static String rotationToArrowEmoji(double rotation) {
        String[] arrows = {"⬆️", "↖️", "⬅️", "↙️", "⬇️", "↘️", "➡️", "↗️"};

        double mod = ((rotation % (2 * Math.PI)) + (2 * Math.PI)) % (2 * Math.PI);
        int index = (int) Math.round((mod / (2 * Math.PI)) * 8) % 8;

        return arrows[index];
    }


    public enum PositionType {
        PEDRO,
        ROADRUNNER
    }

    public static class Echo {
        public double x, y, rotation;

        public Echo(double x, double y, double rotation) {
            this.x = x;
            this.y = y;
            this.rotation = rotation;
        }
    }

    public static class Position {
        public double x, y, rotation;

        public Position(double x, double y, double rotation) {
            this.x = x;
            this.y = y;
            this.rotation = rotation;
        }
    }

    public static class PedroPosition extends Position {
        private final Vector2D pedroCenter = new Vector2D(72, 72);

        public PedroPosition(double x, double y, double rotation) {
            super(x, y, rotation);
        }

        public PedroPosition fromEcho(Echo echo) {
            /* scale the echo x and y based on ratio of resolution to pedro
             * pedro has a chamber resolution of 40y by 28x, but we are limited by telemetry
             */
            echo.x *= 28.0 / HORIZONTAL_RESOLUTION;
            echo.y *= 40.0 / VERTICAL_RESOLUTION;
            // because the driver is facing the chamber from the side,
            // echo x and y are inverted from pedro, so we need to flip them
            double pedroX = pedroCenter.getX() - echo.y;
            double pedroY = pedroCenter.getY() - echo.x;
            // rotation is already handled because echolocate uses 0 by default for straight up
            double pedroRotation = echo.rotation;
            return new PedroPosition(pedroX, pedroY, pedroRotation);
        }

    }

    public static class RoadRunnerPosition extends Position {
        private final Vector2D roadRunnerCenter = new Vector2D(0, 0);

        public RoadRunnerPosition(double x, double y, double rotation) {
            super(x, y, rotation);
        }

        public RoadRunnerPosition fromEcho(Echo echo) {
            double rrX = roadRunnerCenter.getX() + echo.x;
            double rrY = roadRunnerCenter.getY() + echo.y;
            double rrRotation = echo.rotation;
            return new RoadRunnerPosition(rrX, rrY, rrRotation);
        }

    }
}
