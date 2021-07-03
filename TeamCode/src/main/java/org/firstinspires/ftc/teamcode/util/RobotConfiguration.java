package org.firstinspires.ftc.teamcode.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.nio.channels.Pipe;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.Getter;

public class RobotConfiguration {

    // Camera Configuration Wobble
    public static final Resolution RESOLUTION = Resolution.R960x720;
    public static final int HEIGHT_OFFSET = 100;
    public static final int HEIGHT = 300;

    public static final int X = RESOLUTION.getWidth() / 2;
    public static final int Y = RESOLUTION.getHeight() / 2;
    public static final int ALL_RINGS_HEIGHT = 50;
    public static final int RINGS_HEIGHT = 30;

    // Robot Configuration Name
    protected static final String FRONT_LEFT_WHEEL = "FLW";
    protected static final String FRONT_RIGHT_WHEEL = "FRW";
    protected static final String REAR_LEFT_WHEEL = "RLW";
    protected static final String REAR_RIGHT_WHEEL = "RRW";

    protected static final String FLY_WHEEL_1 = "FW1";
    protected static final String FLY_WHEEL_2 = "FW2";

    protected static final String EXTERNAL_CAMERA = "webcam";
    // Hardware
    @Getter
    private static DcMotorEx FLM, FRM, RLM, RRM;
    @Getter
    private static OpenCvCamera camera;
    @Getter
    private static DcMotorEx FW1, FW2;

    // Initialize Method
    public static void init(HardwareMap hardwareMap, Init... initialize) {
        if (initialize == null) {
            Init.ALL.init(hardwareMap);
        } else {
            for (Init init : initialize) {
                init.init(hardwareMap);
            }
        }
    }

    public enum Resolution {
        R640x480(640, 480),
        R160x120(160, 120),
        R176x144(176, 144),
        R320x176(320, 176),
        R320x240(320, 240),
        R352x288(352, 288),
        R432x240(432, 240),
        R544x288(544, 288),
        R640x360(640, 360),
        R752x416(752, 416),
        R800x448(800, 448),
        R800x600(800, 600),
        R864x480(864, 480),
        R960x544(960, 544),
        R960x720(960, 720),
        R1024x576(1024, 576),
        R1184x656(1184, 656),
        R1280x720(1280, 720),
        R1280x960(1280, 960);

        private final int width;
        private final int height;

        Resolution(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    public enum Init {
        ALL(hardwareMap -> {
            // DRIVE
            FLM = hardwareMap.get(DcMotorEx.class, FRONT_LEFT_WHEEL);
            FRM = hardwareMap.get(DcMotorEx.class, FRONT_RIGHT_WHEEL);
            RLM = hardwareMap.get(DcMotorEx.class, REAR_LEFT_WHEEL);
            RRM = hardwareMap.get(DcMotorEx.class, REAR_RIGHT_WHEEL);

            // CAMARE
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, EXTERNAL_CAMERA), cameraMonitorViewId);

            // FLY_WHEEL
            FW1 = hardwareMap.get(DcMotorEx.class, RobotConfiguration.FLY_WHEEL_1);
            FW2 = hardwareMap.get(DcMotorEx.class, RobotConfiguration.FLY_WHEEL_2);
        }),
        DRIVE(hardwareMap -> {
            FLM = hardwareMap.get(DcMotorEx.class, FRONT_LEFT_WHEEL);
            FRM = hardwareMap.get(DcMotorEx.class, FRONT_RIGHT_WHEEL);
            RLM = hardwareMap.get(DcMotorEx.class, REAR_LEFT_WHEEL);
            RRM = hardwareMap.get(DcMotorEx.class, REAR_RIGHT_WHEEL);
        }),
        PHONE_CAM(hardwareMap -> {
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        }),
        EXTERNAL_CAM(hardwareMap -> {
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, EXTERNAL_CAMERA), cameraMonitorViewId);
        }),
        FLY_WHEEL(hardwareMap -> {
            FW1 = hardwareMap.get(DcMotorEx.class, RobotConfiguration.FLY_WHEEL_1);
            FW2 = hardwareMap.get(DcMotorEx.class, RobotConfiguration.FLY_WHEEL_2);
        });

        private final Consumer<HardwareMap>[] initializers;

        Init(Consumer<HardwareMap>... initializers) {
            this.initializers = initializers;
        }

        private void init(HardwareMap map) {
            for (Consumer<HardwareMap> initializer : initializers) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    initializer.accept(map);
                }
            }
        }
    }

    public enum Pipelines {
        RING_DETECTION(() -> {
            return new OpenCvPipeline() {
                // Working Mat variables
                Mat YCrCb = new Mat(); // This will store the whole YCrCb channel
                Mat Cb = new Mat(); // This will store the Cb Channel (part from YCrCb)
                Mat tholdMat = new Mat(); // This will store the threshold

                // Drawing variables
                Scalar GRAY = new Scalar(220, 220, 220); // RGB values for gray.
                Scalar GREEN = new Scalar(0, 255, 0); // RGB values for green.

                // Variables that will store the results of our pipeline
                public int ring1;
                public int ring4;

                // Space which we will annalise data
                public Point BigSquare1 /*= new Point(X_LEFT, Y_UP)*/;
                public Point BigSquare2 /*= new Point(X_RIGHT, Y_DOWN)*/;

                public Point SmallSquare1 /*= new Point(X_LEFT, Y_MIDDLE)*/;
                public Point SmallSquare2 /*= new Point(X_RIGHT, Y_DOWN)*/;

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
            };
        });

        private final Supplier<OpenCvPipeline> pipeline;

        public OpenCvPipeline getPipeline() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return pipeline.get();
            }
            return null;
        }

        Pipelines(Supplier<OpenCvPipeline> pipeline) {
            this.pipeline = pipeline;
        }
    }
}