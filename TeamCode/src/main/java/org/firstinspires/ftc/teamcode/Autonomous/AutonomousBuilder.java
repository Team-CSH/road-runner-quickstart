package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.Autonomous.Pipelines.RingPipeline;
import org.firstinspires.ftc.teamcode.Autonomous.SafePaths.ISafePath;
import org.firstinspires.ftc.teamcode.Autonomous.SafePaths.Path1;
import org.firstinspires.ftc.teamcode.Systems.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.RobotConfiguration;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import lombok.Getter;

public class AutonomousBuilder {

    @Getter
    private AllianceColor color;
    @Getter
    private AlliancePosition position;
    @Getter
    private SafePath safePath;

    @Getter
    private Rings rings;

    public boolean SHOULD_THROW_AT_POWER_SHOT = false;
    public boolean SHOULD_TRY_TO_COLLECT_PS_RINGS = false;
    public boolean SHOULD_COLLECT_START_RINGS = false;

    @Getter
    private SampleMecanumDrive drive;

    public AutonomousBuilder(SampleMecanumDrive drive, AllianceColor color, AlliancePosition position) {
        this.drive = drive;

        OpenCvPipeline pipeline = RobotConfiguration.Pipelines.RING_DETECTION.getPipeline();

        RobotConfiguration.getCamera().setPipeline(pipeline);
        RobotConfiguration.getCamera().startStreaming(RobotConfiguration.RESOLUTION.getWidth(), RobotConfiguration.RESOLUTION.getHeight(), OpenCvCameraRotation.UPRIGHT);

        this.color = color;
        this.position = position;
    }

    public AutonomousBuilder(SampleMecanumDrive drive) {
        this.drive = drive;
    }

    public AutonomousBuilder throwAtPowerShot() {
        SHOULD_THROW_AT_POWER_SHOT = true;
        return this;
    }

    public AutonomousBuilder tryCollectPowerShotRings() {
        SHOULD_TRY_TO_COLLECT_PS_RINGS = true;
        return this;
    }

    public AutonomousBuilder collectStartRings() {
        SHOULD_COLLECT_START_RINGS = true;
        return this;
    }

    public TrajectorySequence build() {
        this.rings = Rings.getFor(((RingPipeline) RobotConfiguration.Pipelines.RING_DETECTION.getPipeline()).getRing1(), ((RingPipeline) RobotConfiguration.Pipelines.RING_DETECTION.getPipeline()).getRing4());
        return safePath.getPath().build(this);
    }

    public AutonomousBuilder usePath(SafePath safePath) {
        this.safePath = safePath;
        return this;
    }

    public AutonomousBuilder useAutoConfig() {
        SHOULD_THROW_AT_POWER_SHOT = AutonomousConfigurator.SHOULD_THROW_AT_POWER_SHOT;
        SHOULD_TRY_TO_COLLECT_PS_RINGS = AutonomousConfigurator.SHOULD_TRY_TO_COLLECT_PS_RINGS;
        SHOULD_COLLECT_START_RINGS = AutonomousConfigurator.SHOULD_COLLECT_START_RINGS;

        color = AutonomousConfigurator.COLOR;
        position = AutonomousConfigurator.POSITION;
        safePath = AutonomousConfigurator.SAFE_PATH;

        return this;
    }

    public enum Rings {

        // 255 255
        // 0 255
        // 0 0
        NONE(255, 255), ONE(0, 255), FOUR(0, 0);

        public static Rings getFor(int ring1, int ring2) {
            for (Rings rings : Rings.values()) {
                if (rings.getRing1() == ring1 && rings.getRing2() == ring2) {
                    return rings;
                }
            }
            return null;
        }

        @Getter
        private int ring1, ring2;

        Rings(int ring1, int ring2) {
            this.ring1 = ring1;
            this.ring2 = ring2;
        }
    }

    public enum SafePath {
        PATH1("Path 1", new Path1());

        @Getter
        private ISafePath path;
        @Getter
        private String name;

        SafePath(String name, ISafePath path) {
            this.name = name;
            this.path = path;
        }
    }

    public enum Constants {
        POWER_SHOT_1(72, 12),
        POWER_SHOT_2(72, 16),
        POWER_SHOT_3(72, 20),
        TOWER(72, 36),
        RINGS(-24, 36),
        A_SQUARE(16, 45),
        B_SQUARE(36, 36),
        C_SQUARE(65, 60);

        @Getter
        private double x, y;

        private int multiplier = -1;
        private double correction = 0;

        public double getHeading(AutonomousBuilder builder, Pose2d position) {
            if (builder.getColor().equals(AllianceColor.BLUE)) {
                correction = Math.PI * 2;
                multiplier = 1;
            }
            double tetha = new Vector2d(x, y * multiplier).minus(position.vec()).angle();
            return tetha - correction;
        }

        public Vector2d toVector(AutonomousBuilder builder) {
            if (builder.getColor().equals(AllianceColor.BLUE))
                return new Vector2d(x, y);
            return new Vector2d(x, -y);
        }

        Constants(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public enum AllianceColor {
        RED, BLUE;
    }

    public enum AlliancePosition {
        CLOSE, FAR;
    }

    public enum Position {
        RED_CLOSE(AllianceColor.RED, AlliancePosition.CLOSE, -63, -48, 0),
        RED_FAR(AllianceColor.RED, AlliancePosition.FAR, -63, -24, 0),
        BLUE_CLOSE(AllianceColor.BLUE, AlliancePosition.CLOSE, -63, 48, 0),
        BLUE_FAR(AllianceColor.BLUE, AlliancePosition.FAR, -63, 24, 0);

        @Getter
        private AllianceColor allianceColor;
        @Getter
        private AlliancePosition alliancePosition;
        @Getter
        private double x, y, heading;

        private Pose2d getPosition() {
            return new Pose2d(x, y, heading);
        }

        Position(AllianceColor allianceColor, AlliancePosition alliancePosition, double x, double y, double heading) {
            this.allianceColor = allianceColor;
            this.alliancePosition = alliancePosition;
            this.x = x;
            this.y = y;
            this.heading = heading;
        }
    }

}
