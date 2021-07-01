package com.example.meepmeeptrajectories;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.roadrunner.AddTrajectorySequenceCallback;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;


public class MyClass {



    /*
     * Align TeleOp
     * Init:
     *   Init robot (DRIVE, ALIGN_SENSOR)
     *   Input Safe Path
     *
     * Start:
     *   Go to the left until it finds the color
     *
     * BigBrain Autonomous - General
     * Init:
     *   Init robot (ALL)
     *   Turn the camera towards the rings. (Checking the position of the robot) Throws error if rings not found (run manually)
     *
     *
     * BigBrain CR (Close Red)
     * BigBrain FR (Far Red)
     * BigBrain CB (Close Blue)
     * BigBrain FB (Far Blue)
     *
     * Start:
     *   Detect the rings
     *
     *
     *
     * Chestii optionale:
Colectat Discuri (PowerShot)
Colectat Discuri Start

Chestii sigure:
Wobble
Aruncam PowerShot/Tower

1. Inainteaza putin
2. Power Shot / Tower
3. Collect rings if allowed and if any then throw them.
4. Leave Wobble
5. Collect PS Rings if allowed then go back and throw them.
6. Park

     *
     * */
    // Align Robot to starting position

    public static final boolean SHOULD_THROW_AT_POWER_SHOT = true;
    public static final boolean SHOULD_TRY_TO_COLLECT_PS_RINGS = false;
    public static final boolean SHOULD_COLLECT_START_RINGS = false;

    public static void main(String[] args) {
        MeepMeep mm = new MeepMeep(700)
                .setBackground(MeepMeep.Background.FIELD_ULTIMATE_GOAL_DARK)
                .setTheme(new ColorSchemeBlueDark())
                .setBackgroundAlpha(1f)
                .setConstraints(55, 100, Math.toRadians(270), Math.toRadians(180), 14.15)
                .setBotDimensions(18, 18);

        Alliance alliance = Alliance.RED_FAR;
        Rings rings = Rings.NONE;
        mm.followTrajectorySequence(drive ->
                drive.trajectorySequenceBuilder(alliance.getPosition())
                        .lineToLinearHeading(new Pose2d(alliance.getX() + 15, alliance.getY(), SHOULD_THROW_AT_POWER_SHOT ? Constants.POWER_SHOT_1.getHeading(new Pose2d(alliance.getX() + 15, alliance.getY()), alliance) : Constants.TOWER.getHeading(new Pose2d(alliance.getX() + 15, alliance.getY()), alliance)))
                        .build()
        );
        if (SHOULD_THROW_AT_POWER_SHOT) {
            mm.followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(alliance.getPosition())
                    .turn(Constants.POWER_SHOT_2.getHeading(new Pose2d(alliance.getX() + 15, alliance.getY()), alliance))
                    .turn(Constants.POWER_SHOT_3.getHeading(new Pose2d(alliance.getX() + 15, alliance.getY()), alliance))
                    .build()
            );
        }

        switch (alliance) {

            case RED_CLOSE:
                break;
            case RED_FAR:

                break;
            case BLUE_FAR:

                break;
            case BLUE_CLOSE:

                break;
        }

        mm.start();

    }

    public enum Rings {
        NONE, ONE, FOUR;
    }

    public enum Constants {
        POWER_SHOT_1(72, 12),
        POWER_SHOT_2(72, 16),
        POWER_SHOT_3(72, 20),
        TOWER(72, 36);

        private double x, y;

        private int multiplier = -1;
        private double correction = 0;

        public double getHeading(Pose2d position, Alliance alliance) {
            if (alliance.equals(Alliance.BLUE_CLOSE) || alliance.equals(Alliance.BLUE_FAR)) {
                correction = Math.PI * 2;
                multiplier = 1;
            }
            double tetha = new Vector2d(x, y * multiplier).minus(position.vec()).angle();
            System.out.println(tetha);
            return tetha - correction;
        }

        Constants(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public enum Alliance {
        RED_CLOSE(-63, -48, 0),
        RED_FAR(-63, -24, 0),
        BLUE_CLOSE(-63, 48, 0),
        BLUE_FAR(-63, 24, 0);

        @Getter
        private double x, y, heading;

        private Pose2d getPosition() {
            return new Pose2d(x, y, heading);
        }

        Alliance(double x, double y, double heading) {
            this.x = x;
            this.y = y;
            this.heading = heading;
        }
    }
}