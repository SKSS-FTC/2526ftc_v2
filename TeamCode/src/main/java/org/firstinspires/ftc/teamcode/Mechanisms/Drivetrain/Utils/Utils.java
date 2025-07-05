package org.firstinspires.ftc.teamcode.Mechanisms.Drivetrain.Utils;

import org.ejml.simple.SimpleMatrix;
public class Utils {

    public static SimpleMatrix R(double th) {
        SimpleMatrix rotation = new SimpleMatrix(
                new double[][]{
                        new double[]{Math.cos(th), -1*Math.sin(th), 0},
                        new double[]{Math.sin(th), Math.cos(th), 0},
                        new double[]{0, 0, 1},
                }
        );
        return rotation;
    }
     //Inverse kinematics Matrix
    public static SimpleMatrix rotateBodyToGlobal(SimpleMatrix vectorBody, double th) {
        return R(th).mult(vectorBody);
    }

    public static SimpleMatrix rotateGlobalToBody(SimpleMatrix vectorGlobal, double th) {
        return R(th).invert().mult(vectorGlobal);
    }

    public static double angleWrap(double radians) {

        while (radians > Math.PI) {
            radians -= 2 * Math.PI;
        }
        while (radians < -Math.PI) {
            radians += 2 * Math.PI;
        }

        // keep in mind that the result is in radians
        return radians;
    }
    public static double calculateDistance(double x1, double y1,double x2,double y2){
        return Math.sqrt(Math.pow((x2-x1),2)+Math.pow((y2-y1),2));
    }
    public static SimpleMatrix makePoseVector(double posX, double posY, double posTheta){
        return new SimpleMatrix(
            new double[][]{
                new double[]{posX},
                new double[]{posY},
                new double[]{Math.toRadians(posTheta)}
            }
        );
    }
}
