package org.nknsd.teamcode.feedbackcontroller;

public interface ControlLoop {

    double findOutput(double error, double errorDelta, double vel, double interval) ;


}
