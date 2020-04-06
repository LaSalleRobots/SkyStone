package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
@Autonomous(name = "MotorEncoding", group = "Linear OpMode")
public class MotorEncoding extends LinearOpMode {
  public DcMotor aMotor = null;

  @Override
  public void runOpMode() {
    telemetry.addData("Status", "Initialized");
    telemetry.update();

    aMotor = hardwareMap.get(DcMotor.class, "motor");
    aMotor.setDirection(DcMotor.Direction.FORWARD);

    double cpm = 1440;
    double gearRatio = 60;
    double diameter = 8;
    int cpcm = (int) ((cpm * gearRatio) / (diameter * Math.PI));

    waitForStart();
    aMotor.setPower(1);
    aMotor.setTargetPosition(cpcm * 100);
  }
}
