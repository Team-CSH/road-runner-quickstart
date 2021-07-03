package org.firstinspires.ftc.teamcode.Autonomous.Pipelines;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import lombok.Getter;

public class RingPipeline extends OpenCvPipeline {


    // Working Mat variables
    Mat YCrCb = new Mat(); // This will store the whole YCrCb channel
    Mat Cb = new Mat(); // This will store the Cb Channel (part from YCrCb)
    Mat tholdMat = new Mat(); // This will store the threshold

    // Drawing variables
    Scalar GRAY = new Scalar(220, 220, 220); // RGB values for gray.
    Scalar GREEN = new Scalar(0, 255, 0); // RGB values for green.

    // Variables that will store the results of our pipeline
    @Getter
    private int ring1;
    @Getter
    private int ring4;

    // Space which we will annalise data
    private Point BigSquare1 /*= new Point(X_LEFT, Y_UP)*/;
    private Point BigSquare2 /*= new Point(X_RIGHT, Y_DOWN)*/;

    private Point SmallSquare1 /*= new Point(X_LEFT, Y_MIDDLE)*/;
    private Point SmallSquare2 /*= new Point(X_RIGHT, Y_DOWN)*/;

    @Override
    public Mat processFrame(Mat input) {

        // Img processing
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_BGR2YCrCb);
        Core.extractChannel(YCrCb, Cb, 2);
        Imgproc.threshold(Cb, tholdMat, 150, 255, Imgproc.THRESH_BINARY_INV);

        // Drawing Points
        int BigSquarePointX = (int) ((BigSquare1.x + BigSquare2.x) / 2);
        int BigSquarePointY = (int) ((BigSquare1.y + SmallSquare1.y) / 2);

        int SmallSquarePointX = (int) ((SmallSquare1.x + SmallSquare2.x) / 2);
        int SmallSquarePointY = (int) ((SmallSquare1.y + SmallSquare2.y) / 2);

        // Point BigSquarePoint = new Point((int)((BigSquare1.x + BigSqare2.x) / 2),(int)((BigSquare1.y + SmallSquare1.y) / 2));
        // Point SmallSquarePoint = new Point((int)((SmallSquare1.x + SmallSquare2.x) / 2),(int)((SmallSquare1.y + SmallSquare2.y) / 2));

        double[] bigSquarePointValues = tholdMat.get(BigSquarePointY, BigSquarePointX);
        double[] smallSquarePointValues = tholdMat.get(SmallSquarePointY, SmallSquarePointX);

        ring4 = (int) bigSquarePointValues[0];
        ring1 = (int) smallSquarePointValues[0];

        // Big Square
        Imgproc.rectangle(
                input,
                BigSquare1,
                BigSquare2,
                GRAY,
                1
        );

        // Small Square
        Imgproc.rectangle(
                input,
                SmallSquare1,
                SmallSquare2,
                GRAY,
                1
        );

        // Big Square Point
        Imgproc.circle(
                input,
                new Point(BigSquarePointX, BigSquarePointY),
                2,
                GRAY,
                1
        );

        // Small Square Point
        Imgproc.circle(
                input,
                new Point(SmallSquarePointX, SmallSquarePointY),
                2,
                GRAY,
                1
        );

        // Change colors if the pipeline detected something

        if (ring1 == 0 && ring4 == 0) {
            Imgproc.rectangle(
                    input,
                    BigSquare1,
                    BigSquare2,
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
        if (ring1 == 0) {
            Imgproc.rectangle(
                    input,
                    SmallSquare1,
                    SmallSquare2,
                    GREEN,
                    1
            );
            Imgproc.circle(
                    input,
                    new Point(SmallSquarePointX, SmallSquarePointY),
                    2,
                    GREEN,
                    1
            );
        }

        return input;
    }
}
