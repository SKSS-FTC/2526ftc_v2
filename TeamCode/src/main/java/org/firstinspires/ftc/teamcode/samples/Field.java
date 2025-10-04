package org.firstinspires.ftc.teamcode.samples;
/**
 * Represents the playing field for an FTC (FIRST Tech Challenge) game.
 * <p>
 * This class defines the physical dimensions of the field, such as its total size and the
 * size of individual tiles. It provides utility methods to translate real-world coordinates
 * (in inches) into a grid-based tile system. This is useful for determining the robot's
 * position on the field in terms of tiles.
 * <p>
 * The field is modeled as a square grid of tiles, with the origin (0,0) at the bottom-left corner.
 *
 * @see #getTileForCoordinate(double, double)
 */
public class Field {
    private final double fieldSizeFeet = 12.0;
    private final double tileSizeFeet = 2.0;
    private final double feetToInches = 12.0;
    private final int numTilesPerSide = (int)(fieldSizeFeet / tileSizeFeet);

    /**
     * Given a coordinate in inches from the bottom left, returns the tile (row, col).
     * Row 0 is the bottom, Col 0 is the left.
     * @param xInches x coordinate in inches from bottom left
     * @param yInches y coordinate in inches from bottom left
     * @return int[]{row, col} tile indices
     */
    public int[] getTileForCoordinate(double xInches, double yInches) {
        double xFeet = xInches / feetToInches;
        double yFeet = yInches / feetToInches;
        int col = (int)(xFeet / tileSizeFeet);
        int row = (int)(yFeet / tileSizeFeet);
        // Clamp to field bounds
        if (col < 0) col = 0;
        if (col >= numTilesPerSide) col = numTilesPerSide - 1;
        if (row < 0) row = 0;
        if (row >= numTilesPerSide) row = numTilesPerSide - 1;
        return new int[]{row, col};
    }

    private boolean isShootingTile(double xInches, double yInches) {
        return false;
    }

    private int getNumTilesPerSide() {
        return numTilesPerSide;
    }

    public static void main(String[] args) {
        Field field = new Field();
        double xInches = 36; // 3 feet
        double yInches = 24; // 2 feet
        int[] tile = field.getTileForCoordinate(xInches, yInches);
        System.out.println("Tile: (row=" + tile[0] + ", col=" + tile[1] + ")");
    }
}
