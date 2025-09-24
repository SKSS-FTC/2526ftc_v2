package org.firstinspires.ftc.teamcode.Mechanisms.Utils.Filters;

/**
 * A reusable Low-Pass Filter for smoothing noisy signals. Example: motor velocity, IMU data,
 * distance sensors, etc.
 */
public class LowPassFilter {
    private double smoothingFactor;         // smoothing factor
    private double prevFilteredValue; // last output

    /**
     * Constructor
     *
     * @param smoothingFactor smoothing factor (0 < alpha < 1)
     */
    public LowPassFilter(double smoothingFactor) {
        this.smoothingFactor = smoothingFactor;
        this.prevFilteredValue = 0.0;
    }

    /**
     * Update filter with a new input value.
     *
     * @param rawValue the latest measurement
     *
     * @return filtered output
     */
    public double update(double rawValue) {
        prevFilteredValue = smoothingFactor * rawValue + (1 - smoothingFactor) * prevFilteredValue;
        return prevFilteredValue;
    }


    /**
     * Reset filter to a specific value.
     */
    public void reset(double value) {
        this.prevFilteredValue = value;
    }

    /**
     * Change smoothing factor dynamically.
     */
    public void setSmoothingFactor(double smoothingFactor) {
        this.smoothingFactor = smoothingFactor;
    }
}

