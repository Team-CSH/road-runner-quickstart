package org.firstinspires.ftc.teamcode.Tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.util.RobotConfiguration;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@TeleOp (name = "WobbleDetector", group = "Tests")
public class WobbleDetection extends LinearOpMode {

    OpenCvCamera Webcam;
    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry dashboardTelemetry = dashboard.getTelemetry();
    // CONSTANTS

    final int X_LEFT = 0;
    final int X_RIGHT = RobotConfiguration.RESOLUTION.getWidth();
    final int Y_UP = RobotConfiguration.HEIGHT_OFFSET;
    final int Y_DOWN = RobotConfiguration.HEIGHT_OFFSET + RobotConfiguration.HEIGHT;


    @Override
    public void runOpMode()
    {
        // Camera Init
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        Webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorViewId);
        Webcam.openCameraDevice();

        // Loading pipeline
        RingPipeline visionPipeline = new RingPipeline();
        Webcam.setPipeline(visionPipeline);

        // Start streaming the pipeline
        Webcam.startStreaming(1280,720,OpenCvCameraRotation.UPRIGHT);

        waitForStart();

        while (opModeIsActive())
        {
            // Get data from the pipeline and output it to the telemetry. This are the variables you are going to work with.
            dashboardTelemetry.addData("Wobble: ", visionPipeline.Wobble);
            dashboardTelemetry.update();
        }
    }

    // Pipeline class
    class RingPipeline extends OpenCvPipeline {
        private Scalar mLowerBound = new Scalar(X_LEFT, Y_UP);
        private Scalar mUpperBound = new Scalar(X_RIGHT, Y_DOWN);
        private double mMinContourArea = 0.1;
        private Scalar mColorRadius = new Scalar(25,50,50,0);
        Scalar GRAY = new Scalar(220, 220, 220);
        private Mat mSpectrum = new Mat();
        private List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
        private int Wobble;

        Mat mPyrDownMat = new Mat();
        Mat mHsvMat = new Mat();
        Mat mMask = new Mat();
        Mat mDilatedMask = new Mat();
        Mat mHierarchy = new Mat();


        @Override
        public Mat processFrame(Mat input) {
            Imgproc.pyrDown(input, mPyrDownMat);
            Imgproc.pyrDown(mPyrDownMat, mPyrDownMat);

            Imgproc.cvtColor(mPyrDownMat, mHsvMat, Imgproc.COLOR_RGB2HSV_FULL);

            Core.inRange(mHsvMat, mLowerBound, mUpperBound, mMask);
            Imgproc.dilate(mMask, mDilatedMask, new Mat());

            List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

            Imgproc.findContours(mDilatedMask, contours, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            // Find max contour area
            double maxArea = 0;
            Iterator<MatOfPoint> each = contours.iterator();
            while (each.hasNext()) {
                MatOfPoint wrapper = each.next();
                double area = Imgproc.contourArea(wrapper);
                if (area > maxArea)
                    maxArea = area;
            }

            int BigSquarePointX = (int) ((X_LEFT + Y_UP) / 2);
            int BigSquarePointY = (int) ((X_RIGHT + Y_DOWN) / 2);

            // Filter contours by area and resize to fit the original image size
            mContours.clear();
            each = contours.iterator();
            while (each.hasNext()) {
                MatOfPoint contour = each.next();
                if (Imgproc.contourArea(contour) > mMinContourArea * maxArea) {
                    Core.multiply(contour, new Scalar(4,4), contour);
                    mContours.add(contour);
                }
            }

            Imgproc.rectangle(
                    input,
                    mUpperBound,
                    mLowerBound,
                    GRAY,
                    1
            );
            return input;
        }
    }


}