package org.firstinspires.ftc.teamcode.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.RobotConfiguration;

@TeleOp
public class AutonomousConfigurator extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {

        // Safe Path: Path 1
        // > Collect Disc PS: false
        // Collect Disc Start

        waitForStart();

        //Colectat Discuri (PowerShot)
        //Colectat Discuri Start

        while (opModeIsActive() && !isStopRequested()) {

            // Y to toggle (boolean)
            // DPAD_UP, DPAD_DOWN (increase/decrease)
            // X to continue

            //
        }
    }
}
