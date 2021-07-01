package org.firstinspires.ftc.teamcode.Tests;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.util.RobotConfiguration;

@TeleOp(name = "FlyWheeltest", group = "Tests")
public class FlyWheelTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        RobotConfiguration.init(hardwareMap, RobotConfiguration.Init.ALL);

        waitForStart();

        while (opModeIsActive() && !isStopRequested()){
            RobotConfiguration.getFW1().setPower(gamepad1.left_stick_y);
            RobotConfiguration.getFW2().setPower(gamepad1.left_stick_y);
        }
    }
}
