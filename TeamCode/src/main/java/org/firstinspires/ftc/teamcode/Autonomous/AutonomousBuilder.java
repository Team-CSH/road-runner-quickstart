package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import lombok.Getter;

public class AutonomousBuilder {

    private AllianceColor color;
    private AlliancePosition position;

    public boolean SHOULD_THROW_AT_POWER_SHOT = true;
    public boolean SHOULD_TRY_TO_COLLECT_PS_RINGS = false;
    public boolean SHOULD_COLLECT_START_RINGS = true;

    public AutonomousBuilder() {

    }

    public AutonomousBuilder throwAtPowerShot(){
        SHOULD_THROW_AT_POWER_SHOT = true;
        return this;
    }

    public enum Rings {
        NONE, ONE, FOUR;
    }

    public enum Constants {
        POWER_SHOT_1(72, 12),
        POWER_SHOT_2(72, 16),
        POWER_SHOT_3(72, 20),
        TOWER(72, 36),
        RINGS(-24, 36);

        @Getter
        private double x, y;

        private int multiplier = -1;
        private double correction = 0;

        public double getHeading(Pose2d position, Position alliance) {
            if (alliance.equals(Position.BLUE_CLOSE) || alliance.equals(Position.BLUE_FAR)) {
                correction = Math.PI * 2;
                multiplier = 1;
            }
            double tetha = new Vector2d(x, y * multiplier).minus(position.vec()).angle();
            return tetha - correction;
        }

        public Vector2d toVector(Position alliance) {
            if (alliance.equals(Position.BLUE_CLOSE) || alliance.equals(Position.BLUE_FAR))
                return new Vector2d(x, y);
            return new Vector2d(x, y * -1);
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