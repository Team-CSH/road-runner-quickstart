package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.util.RobotConfiguration;

import java.util.Arrays;
import java.util.List;

@TeleOp(name="EncoderTestButton", group="Tests")
public class EncoderTestButton extends LinearOpMode {
    private DcMotorEx leftFront, leftRear, rightRear, rightFront;
    private List<DcMotorEx> motors;

    public void setTicksForMotors(int ticks) {
        leftFront.setTargetPosition(ticks);
        leftRear.setTargetPosition(ticks);
        rightRear.setTargetPosition(ticks);
        rightFront.setTargetPosition(ticks);

        for(DcMotorEx m : motors) {
            m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    public void stopMotors() {
        for(DcMotorEx m : motors) {
            m.setPower(0);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        leftFront = hardwareMap.get(DcMotorEx.class, RobotConfiguration.FRONT_LEFT_WHEEL);
        leftRear = hardwareMap.get(DcMotorEx.class, RobotConfiguration.REAR_LEFT_WHEEL);
        rightRear = hardwareMap.get(DcMotorEx.class, RobotConfiguration.REAR_RIGHT_WHEEL);
        rightFront = hardwareMap.get(DcMotorEx.class, RobotConfiguration.FRONT_RIGHT_WHEEL);

        //NU STIU DACA SUNT NECESARE
//        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
//        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);

        motors = Arrays.asList(leftFront, leftRear, rightRear, rightFront);

        for(DcMotorEx m : motors) {
            m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        telemetry.addData("Status:", "waiting");
        telemetry.update();

        waitForStart();

        telemetry.addData("Status:", "running");
        telemetry.update();

        for(DcMotorEx m : motors) {
            m.setPower(0.25);
        }

        while(opModeIsActive()) {
            if(gamepad1.x) {
                setTicksForMotors(5000);
                telemetry.addData("EncoderLF:", leftFront.getCurrentPosition());
                telemetry.addData("EncoderLR:", leftRear.getCurrentPosition());
                telemetry.addData("EncoderRR:", rightRear.getCurrentPosition());
                telemetry.addData("EncoderRL:", rightFront.getCurrentPosition());
                telemetry.update();
            }
        }
    }
}
