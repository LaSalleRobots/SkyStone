package org.firstinspires.ftc.teamcode;

import android.content.Context;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Hardware;

public class Robot {

    private HardwareMap hardwareMap;
    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;


    //setup class initalizer
    public Robot (HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

        //setup motors
        this.leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        this.rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        this.leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        this.rightBack = hardwareMap.get(DcMotor.class, "rightBack");


        this.leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        this.rightBack.setDirection(DcMotorSimple.Direction.FORWARD);
        this.leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        this.rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

    }


    public void rotateLeft (ElapsedTime runtime) {
        double time = runtime.time();
        double initTime = time;

        while (time <= initTime+0.45) {
            rightBack.setPower(0.5);
            rightFront.setPower(0.5);
            leftBack.setPower(-0.5);
            leftFront.setPower(-0.5);

            time = runtime.time();
        }
    }


    public void rotateRight (ElapsedTime runtime) {
        double time = runtime.time();
        double initTime = time;

        while (time <= initTime+0.45) {
            leftBack.setPower(0.5);
            leftFront.setPower(0.5);
            rightBack.setPower(-0.5);
            rightFront.setPower(-0.5);

            time = runtime.time();
        }
    }
    public void sleep(ElapsedTime runtime, double sleepTime) {
        double time = runtime.time();
        double initTime = time;

        while (time <= initTime+sleepTime) {
            time = runtime.time();
        }
    }
}
