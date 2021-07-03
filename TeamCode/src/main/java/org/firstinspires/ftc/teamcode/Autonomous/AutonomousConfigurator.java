package org.firstinspires.ftc.teamcode.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class AutonomousConfigurator extends LinearOpMode {

    public static boolean SHOULD_THROW_AT_POWER_SHOT = false;
    public static boolean SHOULD_TRY_TO_COLLECT_PS_RINGS = false;
    public static boolean SHOULD_COLLECT_START_RINGS = false;

    public static AutonomousBuilder.AllianceColor COLOR = AutonomousBuilder.AllianceColor.RED;
    public static AutonomousBuilder.AlliancePosition POSITION = AutonomousBuilder.AlliancePosition.CLOSE;
    public static AutonomousBuilder.SafePath SAFE_PATH = AutonomousBuilder.SafePath.PATH1;

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
