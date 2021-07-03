package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Systems.SampleMecanumDrive;


@Autonomous(name = "Autonomous Test 1", group = "tests")
public class AutoV1 extends LinearOpMode {

    private SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

    @Override
    public void runOpMode() throws InterruptedException {

        AutonomousBuilder builder = new AutonomousBuilder(drive)
                .useAutoConfig();

        waitForStart();

        if (isStopRequested()) return;

        drive.followTrajectorySequence(builder.build());
    }
}
