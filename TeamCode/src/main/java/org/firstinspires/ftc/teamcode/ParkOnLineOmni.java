package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "Park On Line Omni", group = "Autonomous")
public class ParkOnLineOmni extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    //Setup Drive Motor variables
    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;

    //Setup Arm Motor variables
    private DcMotor armLeft = null;
    private DcMotor armRight = null;

    //Setup claw servos variables
    private Servo clawLeft = null;
    private Servo clawRight = null;
    private Servo clawRotate = null;

    //Main Function
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initalized");
        telemetry.update();

        //setup motors
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        //Set Directions
        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);



        armLeft = hardwareMap.get(DcMotor.class, "armLeft");
        armRight = hardwareMap.get(DcMotor.class, "armRight");


        //setup servos
        clawLeft = hardwareMap.get(Servo.class, "clawLeft");
        clawRight = hardwareMap.get(Servo.class, "clawRight");
        clawRotate = hardwareMap.get(Servo.class, "clawRotate");

        //setup power
        double leftFrontPower = 0.5;
        double rightFrontPower = 0.5;
        double leftBackPower = 0.5;
        double rightBackPower = 0.5;

        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {

            boolean turning = false;
            boolean moveRight = false;
            boolean moveForward = false;

            if (runtime.time() < 0.45) {
                moveForward = true;
            }
            else if (runtime.time() < 2) {
                moveForward = false;
                turning = true;
                moveRight = true;
            }




            if (turning) {
                if (moveRight) {
                    rightFrontPower = 1;
                    rightBackPower = -1;

                    leftFrontPower = 1;
                    leftBackPower = -1;

                } else {

                    rightFrontPower = -1;
                    rightBackPower = 1;

                    leftFrontPower = -1;
                    leftBackPower = -1;
                }
            }
            if (moveForward) {
                leftFrontPower = 1;
                rightFrontPower = 1;

                leftBackPower = 1;
                rightBackPower = 1;
            } else {
                leftFrontPower = -1;
                rightFrontPower = -1;
                leftBackPower = -1;
                rightBackPower = -1;

            }
            leftFront.setPower(leftFrontPower);
            rightFront.setPower(rightFrontPower);
            leftBack.setPower(leftBackPower);
            rightBack.setPower(rightBackPower);
        }


    }
}
