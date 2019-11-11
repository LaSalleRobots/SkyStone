package org.firstinspires.ftc.teamcode;

import android.content.Context;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Hardware;

public class Robot {

    private HardwareMap hardwareMap;
    private ElapsedTime runtime;
    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;
    private double power = 0.5;


    //setup class initalizer
    public Robot (HardwareMap hardwareMap, ElapsedTime runtime) {
        this.hardwareMap = hardwareMap;
        this.runtime = runtime;

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


    public void rotateLeft () {
        double time = runtime.time();
        double initTime = time;

        while (time <= initTime+0.45) {
            rightBack.setPower(power);
            rightFront.setPower(power);
            leftBack.setPower(-power);
            leftFront.setPower(-power);

            time = runtime.time();
        }

        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);
    }


    public void rotateRight () {
        double time = runtime.time();
        double initTime = time;

        while (time <= initTime+0.45) {
            leftBack.setPower(power);
            leftFront.setPower(power);
            rightBack.setPower(-power);
            rightFront.setPower(-power);

            time = runtime.time();
        }

        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);
    }

    public void moveForward () {
        double time = runtime.time();
        double initTime = time;

        while (time <= initTime+0.45) {
            leftFront.setPower(power);
            leftBack.setPower(power);
            rightBack.setPower(power);
            rightFront.setPower(power);
            time = runtime.time();
        }

        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);
    }

    public void moveBackward () {
        double time = runtime.time();
        double initTime = time;

        while (time <= initTime+0.45) {
            leftFront.setPower(-power);
            leftBack.setPower(-power);
            rightBack.setPower(-power);
            rightFront.setPower(-power);
            time = runtime.time();
        }

        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);
    }

    public void moveRight() {
        double time = runtime.time();
        double initTime = time;

        while (time <= initTime+0.45) {
            leftFront.setPower(power);
            leftBack.setPower(-power);
            rightFront.setPower(-power);
            rightBack.setPower(0.5);
            time = runtime.time();

        }

        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);
    }

    public void moveLeft() {
        double time = runtime.time();
        double initTime = time;

        while (time <= initTime+0.45) {
            leftFront.setPower(-power);
            leftBack.setPower(power);
            rightFront.setPower(power);
            rightBack.setPower(-power);
            time = runtime.time();

        }

        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);
    }


    public void setPower(double power) {
        this.power = power;
    }


    public void sleep(double sleepTime) {
        double time = runtime.time();
        double initTime = time;

        while (time <= initTime+sleepTime) {
            time = runtime.time();
        }
    }

}
