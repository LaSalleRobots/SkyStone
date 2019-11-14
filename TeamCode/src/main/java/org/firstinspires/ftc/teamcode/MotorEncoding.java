package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class MotorEncoding extends LinearOpMode {
    public DcMotor aMotor = null;


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        aMotor = hardwareMap.get(DcMotor.class, "motor");
        aMotor.setDirection(DcMotor.Direction.FORWARD);

        double cpm = 1440;
        double gearRatio = 1;
        double diameter = 3.972;
        int cpcm = (int)((cpm * gearRatio) / (diameter * Math.PI));

        waitForStart();
        aMotor.setTargetPosition(cpcm * 10);
    }
}
