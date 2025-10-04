package org.firstinspires.ftc.teamcode.samples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code BallPatternMatcher} class is designed to determine the correct placement of colored balls
 * based on a predefined pattern. This is often useful in robotics competitions where objects
 * (like balls) need to be sorted into specific goals.
 *
 * <p>The class is initialized with a three-character string representing the desired pattern,
 * for example, "PPG" for two purple balls and one green ball. The three positions are implicitly
 * mapped to physical locations such as "Left", "Straight", and "Right".</p>
 *
 * <p>The primary method, {@link #matchBalls(List)}, takes a list of incoming balls (represented by
 * characters) and assigns each one to a position that matches the pattern. If a ball does not
 * fit the pattern or the required slot is already filled, it is marked for disposal.</p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * // The pattern requires two 'P' balls and one 'G' ball.
 * BallPatternMatcher matcher = new BallPatternMatcher("PPG");
 *
 * // A sequence of balls is detected.
 * List<Character> detectedBalls = Arrays.asList('G', 'P', 'P', 'G');
 *
 * // Determine the action for each ball.
 * List<String> actions = matcher.matchBalls(detectedBalls);
 *
 * // The output will be: [Right, Left, Straight, Dispose]
 * // - The first 'G' ball matches the third pattern slot ("Right").
 * // - The first 'P' ball matches the first available 'P' slot ("Left").
 * // - The second 'P' ball matches the second 'P' slot ("Straight").
 * // - The final 'G' ball has no available slot and is marked for "Dispose".
 * System.out.println(actions);
 * }</pre>
 *
 */
public class BallPatternMatcher {
    private final char[] pattern; // e.g., {'P', 'P', 'G'}
    private final boolean[] filled; // tracks if a slot is filled

    /**
     * @param patternString A string of length 3, e.g., "PPG" or "PGP"
     */
    public BallPatternMatcher(String patternString) {
        if (patternString == null || patternString.length() != 3) {
            throw new IllegalArgumentException("Pattern must be a string of length 3");
        }
        this.pattern = patternString.toCharArray();
        this.filled = new boolean[3];
    }

    /**
     * Given up to 4 balls, returns the position for each: Left, Straight, Right, or Dispose.
     * @param balls List of balls, each 'P' or 'G', up to 4
     * @return List of positions: "Left", "Straight", "Right", or "Dispose"
     */
    public List<String> matchBalls(List<Character> balls) {
        Arrays.fill(filled, false); // Reset for each call
        List<String> result = new ArrayList<>();
        String[] positions = {"Left", "Straight", "Right"};
        for (char ball : balls) {
            boolean placed = false;
            for (int i = 0; i < 3; i++) {
                if (!filled[i] && pattern[i] == ball) {
                    filled[i] = true;
                    result.add(positions[i]);
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                result.add("Dispose");
            }
        }
        return result;
    }

    public static void main(String[] args) {
        BallPatternMatcher matcher = new BallPatternMatcher("PPG");
        List<Character> balls = Arrays.asList('G', 'P', 'P', 'G');
        List<String> actions = matcher.matchBalls(balls);
        System.out.println(actions); // Example output: [Right, Left, Straight, Dispose]
    }
}