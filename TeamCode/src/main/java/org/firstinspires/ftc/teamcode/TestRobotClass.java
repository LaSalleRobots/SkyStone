package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Test Robot Obj", group = "Autonomous")
public class TestRobotClass extends LinearOpMode {
  private ElapsedTime runtime = new ElapsedTime();

  @Override
  public void runOpMode() {
    telemetry.addData("Status", "Initialized");
    telemetry.update();

    waitForStart();
    runtime.reset();

    telemetry.addData("Status", "Started");
    telemetry.update();

    Robot robot = new Robot(hardwareMap, runtime);
    robot.setPower(1);

    if (opModeIsActive()) {
      robot.moveForward(10);
      robot.moveBackward(10);
      robot.sleep(15);
    }
  }

  public void sleep(ElapsedTime runtime, double sleepTime) {
    double time = runtime.time();
    double initTime = time;

    while (time <= initTime + sleepTime) {
      telemetry.addData("Time", time);
      telemetry.update();
      time = runtime.time();
    }
  }
}
