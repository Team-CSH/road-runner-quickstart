package org.firstinspires.ftc.teamcode.util;

public class RobotConfiguration {

    public static final String FRONT_LEFT_WHEEL = "FLW";
    public static final String FRONT_RIGHT_WHEEL = "FRW";
    public static final String REAR_LEFT_WHEEL = "RLW";
    public static final String REAR_RIGHT_WHEEL = "RRW";

    public static final String ENCODER_TEST = "encoder";

    public static final String EXTERNAL_CAMERA = "webcam";

    public static final Resolution RESOLUTION = Resolution.R960x720;
    public static final int HEIGHT_OFFSET = 100;
    public static final int HEIGHT = 300;

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

        private int width, height;

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

}