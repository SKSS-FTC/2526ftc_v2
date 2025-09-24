package org.firstinspires.ftc.teamcode.Utility;

import java.util.Scanner;

/**
 * A standalone Java utility to perform quadratic regression on launch data.
 * This program takes distance and velocity data points and calculates the
 * coefficients for a best-fit quadratic function in the form:
 * v = a * d^2 + b * d + c
 *
 * This utility helps transform discrete calibration data into a continuous
 * mathematical model for improved launch accuracy.
 */
public class LauncherDataAnalysis {

  public static void main(String[] args) {
      new Scanner(System.in);

      // --- DATA COLLECTION ---
    // Enter your collected data here. Distances are in inches, velocities in ticks/sec.
    // You can uncomment and modify these pre-populated examples.
    double[] distances = {30.0, 40.0, 50.0, 60.0, 70.0, 80.0};
    double[] velocities = {1350.0, 1500.0, 1620.0, 1750.0, 1880.0, 2050.0};

    // For a more dynamic input, you can prompt the user:
    // System.out.print("Enter number of data points: ");
    // int n = scanner.nextInt();
    // distances = new double[n];
    // velocities = new double[n];
    // for (int i = 0; i < n; i++) {
    //     System.out.printf("Enter data point #%d (distance velocity): ", i + 1);
    //     distances[i] = scanner.nextDouble();
    //     velocities[i] = scanner.nextDouble();
    // }
    // scanner.close();

    // --- PERFORM QUADRATIC REGRESSION ---
    if (distances.length != velocities.length || distances.length < 3) {
      System.err.println("Error: Must have at least 3 data points and equal number of distances and velocities.");
      return;
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
    // The coefficients (a, b, c) are found by solving this 3x3 system:
    // C1*a + C2*b + C3*c = C4
    // C2*a + C5*b + C1*c = C6
    // C3*a + C1*b + n*c = C7

    // where the C's are our pre-calculated sums
    double C1 = sumD2;
    double C2 = sumD3;
    double C3 = sumD;
    double C4 = sumD2V;
    double C5 = sumD4;
    double C6 = sumDV;
    double C7 = sumV;

    double determinant = C1 * (C5*n - C1*C1) - C2 * (C2*n - C1*C3) + C3 * (C2*C1 - C3*C5);

    double a = (C4 * (C5*n - C1*C1) - C2 * (C6*n - C1*C7) + C3 * (C6*C1 - C7*C5)) / determinant;
    double b = (C1 * (C6*n - C1*C7) - C2 * (C4*n - C3*C7) + C3 * (C4*C1 - C3*C6)) / determinant;
    double c = (C1 * (C5*C7 - C1*C6) - C2 * (C2*C7 - C3*C6) + C3 * (C2*C6 - C3*C5)) / determinant;

    // --- OUTPUT RESULTS ---
    System.out.println("---------------------------------------------");
    System.out.println("Quadratic Regression Results");
    System.out.println("---------------------------------------------");
    System.out.print("Coefficients for v = a * d^2 + b * d + c\n\n");
    System.out.printf("  a = %.8f\n", a);
    System.out.printf("  b = %.8f\n", b);
    System.out.printf("  c = %.8f\n", c);
    System.out.println("---------------------------------------------");
    System.out.println("Use the following function in your OpMode:");
    System.out.print("public double getLaunchVelocity(double distance) {\n");
    System.out.printf("    return (%.8f * distance * distance) + (%.8f * distance) + %.8f;\n", a, b, c);
    System.out.println("}");
    System.out.println("---------------------------------------------");
  }
}
