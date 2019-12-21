package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="RoboHelperFuncTester")
public class RoboHelperFuncTester extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private String commands[] = {
            "moveForwards",
            "moveBackwards",
            "moveLeft",
            "moveRight",
            "moveBackwardsLeft",
            "moveBackwardsRight",
            "moveForwardsLeft",
            "moveForwardsRight",
            "rotateLeft",
            "rotateRight",
            "toggleFunc"
    };
    private double runTime = 0;
    private int index = 0;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        telemetry.addData("Status", "Started");
        telemetry.update();

        RoboHelper robot = new RoboHelper(hardwareMap, runtime);

        while (opModeIsActive()) {
            if (index > commands.length-1) {index = 0;}
            else if (index < 0) {index = commands.length - 1;}


            if (gamepad1.dpad_down) {
                index--;
                robot.sleep(0.25);
            } else if (gamepad1.dpad_up) {
                index++;
                robot.sleep(0.25);
            } else if (gamepad1.dpad_left) {
                runTime--;
                robot.sleep(0.25);
            } else if (gamepad1.dpad_right) {
                runTime++;
                robot.sleep(0.25);
            } else if (gamepad1.a) {
                String command = commands[index];
                if (command.equals("moveForwards")) {robot.moveForwards();}
                else if (command.equals("moveBackwards")) {robot.moveBackwards();}
                else if (command.equals("moveLeft")) {robot.moveLeft();}
                else if (command.equals("moveRight")) {robot.moveRight();}
                else if (command.equals("moveBackwardsLeft")) {robot.moveBackwardsLeft();}
                else if (command.equals("moveBackwardsRight")) {robot.moveBackwardsRight();}
                else if (command.equals("moveForwardsLeft")) {robot.moveForwardsLeft();}
                else if (command.equals("moveForwardsRight")) {robot.moveForwardsRight();}
                else if (command.equals("rotateLeft")) {robot.rotateLeft();}
                else if (command.equals("rotateRight")) {robot.rotateRight();}
                else if (command.equals("toggleFunc")) {robot.togglePlateGrabber();}
                robot.runFor(runTime);
            }

            telemetry.addData("Command", commands[index]);
            telemetry.addData("Index", index);
            telemetry.addData("Time", runTime);
            telemetry.update();
        }


    }
}