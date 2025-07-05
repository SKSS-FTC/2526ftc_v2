package org.firstinspires.ftc.teamcode.Mechanisms.Drivetrain.Models;

import org.ejml.simple.SimpleMatrix;
public class MecanumKinematicModel {
    public static double r = 2.16535; //in
    public static double l = 5.7; //in
    public static double w = 5.31496; //in
    public static SimpleMatrix inverseKinematics(SimpleMatrix twist) {
        SimpleMatrix wheelSpeeds = H_inv.scale(1 / r).mult(twist);
        return wheelSpeeds;
    }
}
