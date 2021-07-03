package com.example.meepmeeptrajectories;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequenceBuilder;

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
    public static final boolean SHOULD_COLLECT_START_RINGS = true;

    public static void main(String[] args) {
        MeepMeep mm = new MeepMeep(600)
                .setBackground(MeepMeep.Background.FIELD_ULTIMATE_GOAL_DARK)
                .setTheme(new ColorSchemeBlueDark())
                .setBackgroundAlpha(1f)
                .setConstraints(55, 100, Math.toRadians(270), Math.toRadians(180), 14.15)
                .setBotDimensions(18, 18);

        Alliance alliance = Alliance.RED_FAR;
        Rings rings = Rings.ONE;
        mm.followTrajectorySequence(drive -> {
                    TrajectorySequenceBuilder trajectorySequenceBuilder = drive.trajectorySequenceBuilder(alliance.getPosition());
                    trajectorySequenceBuilder
                            .lineToLinearHeading(new Pose2d(alliance.getX() + 15, alliance.getY(), SHOULD_THROW_AT_POWER_SHOT ? Constants.POWER_SHOT_1.getHeading(new Pose2d(alliance.getX() + 15, alliance.getY()), alliance) : Constants.TOWER_RED.getHeading(new Pose2d(alliance.getX() + 15, alliance.getY()), alliance)));


                    if (SHOULD_THROW_AT_POWER_SHOT) {
                        trajectorySequenceBuilder
                                .waitSeconds(1)
                                .turn(Constants.POWER_SHOT_2.getHeading(new Pose2d(alliance.getX() + 15, alliance.getY()), alliance))
                                .waitSeconds(1)
                                .turn(Constants.POWER_SHOT_3.getHeading(new Pose2d(alliance.getX() + 15, alliance.getY()), alliance));
                        // TODO: 7/2/2021 SHOT RINGS
                    } else {
                        trajectorySequenceBuilder
                                .waitSeconds(1)
                                .turn(Constants.TOWER_RED.getHeading(new Pose2d(alliance.getX() + 15, alliance.getY()), alliance));
                        // TODO: 7/2/2021 SHOT RINGS
                    }

                    if (rings.equals(Rings.ONE) & SHOULD_COLLECT_START_RINGS) {
                        //Ia primul disc de pe teren si il arunca in tower
                        trajectorySequenceBuilder
                                .waitSeconds(1)
                                .lineToLinearHeading(new Pose2d(Constants.RINGS_RED.getX(), Constants.RINGS_RED.getY(), Constants.TOWER_RED.getHeading(new Pose2d(alliance.getX() + 15, alliance.getY()), alliance)))
                                .waitSeconds(0.3);
                    }
                    if (rings.equals(Rings.FOUR) && SHOULD_COLLECT_START_RINGS) {
                        trajectorySequenceBuilder
                                .waitSeconds(1)
//                                .turn(Constants.RINGS_RED.getHeading(new Pose2d(alliance.getX() + 15, alliance.getY()), alliance))
//                                .turn(Constants.TOWER.getHeading(new Pose2d(Constants.RINGS.getX(), Constants.RINGS.getY()), alliance))
//                                .lineTo(Constants.RINGS_RED.toVector(alliance));
                                .lineToLinearHeading(new Pose2d(Constants.RINGS_RED.getX(), Constants.RINGS_RED.getY(), Constants.TOWER_RED.getHeading(new Pose2d(alliance.getX() + 15, alliance.getY()), alliance)))
                                .waitSeconds(0.2) //pushing the disks

                                .lineToLinearHeading(new Pose2d(Constants.RINGS_RED.getX() + 3, Constants.RINGS_RED.getY(), Constants.TOWER_RED.getHeading(new Pose2d(alliance.getX() + 15, alliance.getY()), alliance)))
                                .waitSeconds(0.2)
                                .lineToLinearHeading(new Pose2d(Constants.RINGS_RED.getX() + 5, Constants.RINGS_RED.getY(), Constants.TOWER_RED.getHeading(new Pose2d(alliance.getX() + 15, alliance.getY()), alliance)))
                                .waitSeconds(0.2)
                                .lineToLinearHeading(new Pose2d(Constants.RINGS_RED.getX() + 7, Constants.RINGS_RED.getY(), Constants.TOWER_RED.getHeading(new Pose2d(alliance.getX() + 15, alliance.getY()), alliance)))
                                .waitSeconds(0.7) //shooting the three disks
                                .lineToLinearHeading(new Pose2d(Constants.RINGS_RED.getX() + 10, Constants.RINGS_RED.getY(), Constants.TOWER_RED.getHeading(new Pose2d(alliance.getX() + 15, alliance.getY()), alliance)))
                                .waitSeconds(0.2); //shooting the last one
                    }
                    return trajectorySequenceBuilder.waitSeconds(10).build();
                }
        );
        mm.start();
    }

    public enum Rings {
        NONE, ONE, FOUR;
    }

    public enum Constants {
        POWER_SHOT_1(72, 12),
        POWER_SHOT_2(72, 16),
        POWER_SHOT_3(72, 20),
        TOWER_RED(72, 36),
        RINGS_RED(-24, -36),
        RINGS_BLUE(-24, 36),
        A_RED_SQUARE(16, -45),
        B_RED_SQAURE(36, -36),
        C_RED_SQUARE(65, -60);

        @Getter
        private double x, y;

        private int multiplier = -1;
        private double correction = 0;

        Constants(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getHeading(Pose2d position, Alliance alliance) {
            if (alliance.equals(Alliance.BLUE_CLOSE) || alliance.equals(Alliance.BLUE_FAR)) {
                correction = Math.PI * 2;
                multiplier = 1;
            }
            double tetha = new Vector2d(x, y * multiplier).minus(position.vec()).angle();
            return tetha - correction;
        }

        public Vector2d toVector(Alliance alliance) {
            if (alliance.equals(Alliance.BLUE_CLOSE) || alliance.equals(Alliance.BLUE_FAR))
                return new Vector2d(x, y);
            return new Vector2d(x, y * -1);
        }
    }

    public enum Alliance {
        RED_CLOSE(-63, -48, 0),
        RED_FAR(-63, -24, 0),
        BLUE_CLOSE(-63, 48, 0),
        BLUE_FAR(-63, 24, 0);

        @Getter
        private double x, y, heading;

        Alliance(double x, double y, double heading) {
            this.x = x;
            this.y = y;
            this.heading = heading;
        }

        private Pose2d getPosition() {
            return new Pose2d(x, y, heading);
        }
    }
}