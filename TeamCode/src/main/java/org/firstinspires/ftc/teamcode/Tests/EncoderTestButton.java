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
    private List<DcMotorEx> motors;

    public void setTicksForMotors(int ticks) {
        RobotConfiguration.getFLM().setTargetPosition(ticks);
        RobotConfiguration.getRLM().setTargetPosition(ticks);
        RobotConfiguration.getRRM().setTargetPosition(ticks);
        RobotConfiguration.getFRM().setTargetPosition(ticks);

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
        RobotConfiguration.init(hardwareMap, RobotConfiguration.Init.DRIVE);

        //NU STIU DACA SUNT NECESARE
        RobotConfiguration.getFLM().setDirection(DcMotorEx.Direction.REVERSE);
        RobotConfiguration.getRLM().setDirection(DcMotorEx.Direction.REVERSE);

        motors = Arrays.asList(RobotConfiguration.getFLM(), RobotConfiguration.getRLM(), RobotConfiguration.getRRM(), RobotConfiguration.getFRM());


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
                telemetry.addData("EncoderLF:", RobotConfiguration.getFLM().getCurrentPosition());
                telemetry.addData("EncoderLR:", RobotConfiguration.getRLM().getCurrentPosition());
                telemetry.addData("EncoderRR:", RobotConfiguration.getRRM().getCurrentPosition());
                telemetry.addData("EncoderRL:", RobotConfiguration.getFRM().getCurrentPosition());
                telemetry.update();
            }
        }
    }
}
