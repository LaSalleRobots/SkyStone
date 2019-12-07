package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Testing Movements", group="Linear Opmode")
public class TimeDistTest extends LinearOpMode {
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initalized");
        telemetry.update();



        waitForStart();
        runtime.reset();
        RoboHelper robot = new RoboHelper(hardwareMap, runtime);
        robot.moveForwards();
        robot.runFor(1);


    }
}
