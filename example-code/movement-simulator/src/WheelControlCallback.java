public interface WheelControlCallback {
    void onSteeringChanged(double steeringAngle);  // Adjust steering
    void onSpeedChanged(double speed);  // Adjust speed
}
