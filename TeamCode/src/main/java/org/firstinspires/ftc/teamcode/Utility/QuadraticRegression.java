/*
 * MIT License
 *
 * Copyright (c) 2024 ParkCircus Productions; All Rights Reserved
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.Utility;

import java.util.Objects;

/**
 * A dedicated utility class to perform quadratic regression.
 * The core mathematical logic has been extracted from the main
 * program to make it reusable and, most importantly, testable.
 * This class does not perform any console I/O.
 */
public class QuadraticRegression {

  /**
   * Represents the coefficients of the quadratic equation v = a * d^2 + b * d + c.
   */
  public static class Coefficients {
    public final double a;
    public final double b;
    public final double c;

    public Coefficients(double a, double b, double c) {
      this.a = a;
      this.b = b;
      this.c = c;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Coefficients that = (Coefficients) o;
      return Double.compare(that.a, a) == 0 &&
              Double.compare(that.b, b) == 0 &&
              Double.compare(that.c, c) == 0;
    }

    @Override
    public int hashCode() {
      return Objects.hash(a, b, c);
    }

    @Override
    public String toString() {
      return String.format("v = %.8f * d^2 + %.8f * d + %.8f", a, b, c);
    }
  }

  /**
   * Calculates the coefficients for a best-fit quadratic function
   * from a set of data points.
   *
   * @param distances The distance data points.
   * @param velocities The velocity data points.
   * @return A Coefficients object containing the calculated a, b, and c values.
   * @throws IllegalArgumentException if the number of data points is invalid.
   */
  public static Coefficients calculateCoefficients(double[] distances, double[] velocities) {
    if (distances.length != velocities.length || distances.length < 3) {
      throw new IllegalArgumentException("Must have at least 3 data points and equal number of distances and velocities.");
    }

    int n = distances.length;
    double sumD = 0, sumV = 0, sumD2 = 0, sumD3 = 0, sumD4 = 0, sumDV = 0, sumD2V = 0;

    for (int i = 0; i < n; i++) {
      double d = distances[i];
      double v = velocities[i];
      double d2 = d * d;
      double d3 = d * d2;
      double d4 = d * d3;

      sumD += d;
      sumV += v;
      sumD2 += d2;
      sumD3 += d3;
      sumD4 += d4;
      sumDV += d * v;
      sumD2V += d2 * v;
    }

    // --- SOLVE THE SYSTEM OF LINEAR EQUATIONS ---
    double C1 = sumD2;
    double C2 = sumD3;
    double C3 = sumD;
    double C4 = sumD2V;
    double C5 = sumD4;
    double C6 = sumDV;
    double C7 = sumV;

    double determinant = C1 * (C5 * n - C1 * C1) - C2 * (C2 * n - C1 * C3) + C3 * (C2 * C1 - C3 * C5);

    double a = (C4 * (C5 * n - C1 * C1) - C2 * (C6 * n - C1 * C7) + C3 * (C6 * C1 - C7 * C5)) / determinant;
    double b = (C1 * (C6 * n - C1 * C7) - C2 * (C4 * n - C3 * C7) + C3 * (C4 * C1 - C3 * C6)) / determinant;
    double c = (C1 * (C5 * C7 - C1 * C6) - C2 * (C2 * C7 - C3 * C6) + C3 * (C2 * C6 - C3 * C5)) / determinant;

    return new Coefficients(a, b, c);
  }
}

