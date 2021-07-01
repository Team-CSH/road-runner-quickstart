package org.firstinspires.ftc.teamcode.util;

import android.os.Build;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.function.Consumer;

import lombok.Getter;

public class RobotConfiguration {

    // Camera Configuration
    public static final Resolution RESOLUTION = Resolution.R960x720;
    public static final int HEIGHT_OFFSET = 100;
    public static final int HEIGHT = 300;

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
            camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

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
}