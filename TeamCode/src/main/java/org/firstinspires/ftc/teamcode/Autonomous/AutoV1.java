package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Systems.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;


@Autonomous(name = "Autonomous Test 1", group = "tests")
public class AutoV1 extends LinearOpMode {

    private SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

    @Override
    public void runOpMode() throws InterruptedException {
        TrajectorySequence trajectorySequence = drive.trajectorySequenceBuilder(new Pose2d(0, 0)).build();
        drive.followTrajectorySequence(trajectorySequence);

    }
}