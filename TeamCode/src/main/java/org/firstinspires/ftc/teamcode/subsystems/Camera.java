package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.*;
import org.opencv.imgproc.*;
import org.openftc.easyopencv.*;

public class Camera {
    private final OpenCvCamera camera;

    // private result; - To be determined

    // Used for camera streaming - make sure this matches the camera's resolution
    private final int CAMERA_WIDTH = 320;
    private final int CAMERA_HEIGHT = 240;

    public Camera(HardwareMap hardwareMap, Telemetry telemetry) {
        synchronized (this) {
            WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
            OpenCvPipeline pipeline = new AprilTagPipeline(telemetry);
            camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName);
            camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
                @Override
                public void onOpened() {
                    camera.startStreaming(CAMERA_WIDTH, CAMERA_HEIGHT, OpenCvCameraRotation.UPRIGHT);
                }

                @Override
                public void onError(int errorCode) {
                    telemetry.addData("error code: ", errorCode);
                    telemetry.update();
                }
            });
            camera.setPipeline(pipeline);
        }

    }

    // OpenCV image processing
    class AprilTagPipeline extends OpenCvPipeline {
        private final Mat hsv = new Mat();
        private final Mat blurredImage = new Mat();
        private final Mat blurredImage2 = new Mat();
        private final Mat mask = new Mat();
        private final Mat hierarchy = new Mat();
        private final MatOfPoint2f approx = new MatOfPoint2f();
        private final MatOfPoint2f contour2f = new MatOfPoint2f();
        private final Telemetry telemetry;

        public AprilTagPipeline(Telemetry telemetry) {
            this.telemetry = telemetry;
        }

        @Override
        public Mat processFrame(Mat input) {
            synchronized (Camera.this) {
                // Convert to HSV
                Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

                // Reduce noise
                int GAUSSIAN_BLUR = 5;
                Imgproc.GaussianBlur(hsv, blurredImage, new Size(GAUSSIAN_BLUR, GAUSSIAN_BLUR), 0);

                blurredImage.convertTo(blurredImage2, -1, 0.9, 0);
                return input;
            }
        }

        public OpenCvCamera getCamera() {
            return camera;
        }

        // method to be determined
        public void getResult() {
            // Important data to used for bot's yaw and flywheel power
        }
    }
}
