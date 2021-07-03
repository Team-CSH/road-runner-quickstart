package org.firstinspires.ftc.teamcode.Autonomous.SafePaths;

import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;

import org.firstinspires.ftc.teamcode.Autonomous.AutonomousBuilder;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;

public class Path1 implements ISafePath {
    @Override
    public TrajectorySequence build(AutonomousBuilder builder) {

        TrajectorySequenceBuilder trajectoryBuilder = builder.getDrive().trajectorySequenceBuilder(null);


        return trajectoryBuilder.build();
    }
}
