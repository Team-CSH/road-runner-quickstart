package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.util.RobotConfiguration;

import java.util.Arrays;
import java.util.List;

@Autonomous(name="EncoderTest", group="Tests")
public class EncoderTest extends LinearOpMode {
    private DcMotorEx leftFront, leftRear, rightRear, rightFront;
    private List<DcMotorEx> motors;

    //JUST IN CASE PRIMA VARIANTA NU MERGE
    public void setTicksForMotors(int ticks) {
        leftFront.setTargetPosition(ticks);
        leftRear.setTargetPosition(ticks);
        rightRear.setTargetPosition(ticks);
        rightFront.setTargetPosition(ticks);
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
            m.setTargetPosition(5000);
            m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        telemetry.addData("Status:", "waiting");
        telemetry.update();

        waitForStart();

        telemetry.addData("Status:", "running");
        telemetry.update();

        for(DcMotorEx m : motors) {
            m.setPower(0.25);
        }

        while(opModeIsActive() && rightFront.isBusy()) {
            telemetry.addData("EncoderLF:", leftFront.getCurrentPosition());
            telemetry.addData("EncoderLR:", leftRear.getCurrentPosition());
            telemetry.addData("EncoderRR:", rightRear.getCurrentPosition());
            telemetry.addData("EncoderRL:", rightFront.getCurrentPosition());
            telemetry.update();
            idle();
        }

        for(DcMotorEx m : motors) {
            m.setPower(0.0);
        }

        resetStartTime();

        while(opModeIsActive() && getRuntime() < 5) {
            telemetry.addData("EncoderLF:", leftFront.getCurrentPosition());
            telemetry.addData("EncoderLR:", leftRear.getCurrentPosition());
            telemetry.addData("EncoderRR:", rightRear.getCurrentPosition());
            telemetry.addData("EncoderRL:", rightFront.getCurrentPosition());
            telemetry.update();
            idle();
        }

        for(DcMotorEx m : motors) {
            m.setTargetPosition(0);
            m.setPower(-0.25);
        }

        while(opModeIsActive() && rightFront.isBusy()) {
            telemetry.addData("EncoderLF:", leftFront.getCurrentPosition());
            telemetry.addData("EncoderLR:", leftRear.getCurrentPosition());
            telemetry.addData("EncoderRR:", rightRear.getCurrentPosition());
            telemetry.addData("EncoderRL:", rightFront.getCurrentPosition());
            telemetry.update();
            idle();
        }

        for(DcMotorEx m : motors) {
            m.setPower(0.0);
        }

        resetStartTime();

        while (opModeIsActive() && getRuntime() < 5)
        {
            telemetry.addData("EncoderLF:", leftFront.getCurrentPosition());
            telemetry.addData("EncoderLR:", leftRear.getCurrentPosition());
            telemetry.addData("EncoderRR:", rightRear.getCurrentPosition());
            telemetry.addData("EncoderRL:", rightFront.getCurrentPosition());
            telemetry.update();
            idle();
        }


    }
}
