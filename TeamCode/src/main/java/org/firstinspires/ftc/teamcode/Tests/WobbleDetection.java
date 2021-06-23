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
    OpenCvCamera phoneCam;
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
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();

        // Loading pipeline
        RingPipeline visionPipeline = new RingPipeline();
        phoneCam.setPipeline(visionPipeline);

        // Start streaming the pipeline
        phoneCam.startStreaming(320,240,OpenCvCameraRotation.UPRIGHT);

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
        private Scalar GREEN = new Scalar(0, 255, 0);

        private Point mLowerBoundPoint = new Point(X_LEFT, Y_UP);
        private Point mUpperBoundPoint = new Point(X_RIGHT, Y_DOWN);

        private double mMinContourArea = 0.1;
        private Scalar mColorRadius = new Scalar(25,50,50,0);
        private Scalar GRAY = new Scalar(220, 220, 220);
        private List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
        private int Wobble;

        Mat mPyrDownMat = new Mat();
        Mat mHsvMat = new Mat();
        Mat mMask = new Mat();
        Mat mDilatedMask = new Mat();
        Mat mHierarchy = new Mat();
        Mat tholdMat = new Mat();
        Mat Cb = new Mat();


        @Override
        public Mat processFrame(Mat input) {
            Imgproc.pyrDown(input, mPyrDownMat);
            Imgproc.pyrDown(mPyrDownMat, mPyrDownMat);
            Imgproc.cvtColor(mPyrDownMat, mHsvMat, Imgproc.COLOR_RGB2HSV_FULL);

            Core.inRange(mHsvMat, mLowerBound, mUpperBound, mMask);
            Imgproc.dilate(mMask, mDilatedMask, new Mat());

            List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
            Imgproc.findContours(mDilatedMask, contours, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
            Imgproc.threshold(Cb, tholdMat, 150, 255, Imgproc.THRESH_BINARY_INV);

            int BigSquarePointX = (int) ((mLowerBoundPoint.x + mLowerBoundPoint.y) / 2);
            int BigSquarePointY = (int) ((mLowerBoundPoint.y + mLowerBoundPoint.y) / 2);

            double[] bigSquarePointValues = tholdMat.get(BigSquarePointY, BigSquarePointX);
            Wobble = (int) bigSquarePointValues[0];


            Imgproc.rectangle(
                    input,
                    mUpperBoundPoint,
                    mLowerBoundPoint,
                    GRAY,
                    1
            );

            Imgproc.circle(
                    input,
                    new Point(BigSquarePointX, BigSquarePointY),
                    2,
                    GRAY,
                    1
            );

            if (Wobble == 0) {
                Imgproc.rectangle(
                        input,
                        mUpperBoundPoint,
                        mLowerBoundPoint,
                        GREEN,
                        1
                );
                Imgproc.circle(
                        input,
                        new Point(BigSquarePointX, BigSquarePointY),
                        2,
                        GREEN,
                        1
                );
            }


            return input;
        }
    }


}