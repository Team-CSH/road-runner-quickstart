package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.util.Encoder;
import org.firstinspires.ftc.teamcode.util.RobotConfiguration;

public class DigitalEncoderTest extends LinearOpMode {
    DcMotorEx leftRear;
    Encoder leftRearEncoder;
    @Override
    public void runOpMode() throws InterruptedException {
        leftRear = hardwareMap.get(DcMotorEx.class, RobotConfiguration.REAR_LEFT_WHEEL);
        leftRearEncoder = new Encoder()
    }
}
