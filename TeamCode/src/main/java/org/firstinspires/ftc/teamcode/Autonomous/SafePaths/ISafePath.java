package org.firstinspires.ftc.teamcode.Autonomous.SafePaths;

import org.firstinspires.ftc.teamcode.Autonomous.AutonomousBuilder;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

public interface ISafePath {

    TrajectorySequence build(AutonomousBuilder builder);

}
