/*  WALL-E (v4 Robot)
 *
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Controller Omni", group="Linear Opmode")
public class ControllerOmni extends LinearOpMode {
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
    private Servo plateGrabber = null;
    private Servo thisServo = null;
    private Servo plateGrabber2 = null;
    private Servo clawLeft = null;
    private Servo clawRight = null;
    private Servo clawRotate = null;

    double leftFrontPower = 0.5;
    double rightFrontPower = 0.5;
    double leftBackPower = 0.5;
    double rightBackPower = 0.5;

    boolean closedMover = false;
    boolean closedMover2 = false;


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initalized");
        telemetry.update();

        //setup motors
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        plateGrabber = hardwareMap.get(Servo.class, "plateGrabber");
        plateGrabber2 = hardwareMap.get(Servo.class, "plateGrabber2");
        thisServo = hardwareMap.get(Servo.class, "extraServo");

        //Set Directions
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);


        waitForStart();
        plateGrabber.setPosition(0);
        plateGrabber2.setPosition(1);
        closedMover = true;
        runtime.reset();
        while (opModeIsActive()) {
            double y = gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double x2 = gamepad1.right_stick_x;
            if (x2 > 0) {rotateRight();}
            else if (x2 < 0) {rotateLeft();}
            else if (x > 0 && y < 0.25 && y > -0.25){moveRight();}
            else if (x < 0 && y < 0.25 && y > -0.25){moveLeft();}
            else if (y > 0 && x < 0.25 && x > -0.25){moveForwards();}
            else if (y < 0 && x < 0.25 && x > -0.25){moveBackwards();}

            else if (y > 0.25 && x < -0.25){moveForwardsLeft();}
            else if (y > 0.25 && x > 0.25){moveForwardsRight();}
            else if (y < -0.25 && x < -0.25){moveBackwardsLeft();}
            else if (y < -0.25 && x > 0.25){moveBackwardsRight();}
            else {zeroMove();}

            if (gamepad1.right_bumper){
                toggleClaw();
                sleeP(0.25);

            }
            if (gamepad1.a){
                toggleRandomServo();
                sleeP(0.25);

            }

            leftFront.setPower(leftFrontPower);
            rightFront.setPower(rightFrontPower);
            leftBack.setPower(leftBackPower);
            rightBack.setPower(rightBackPower);
        }

    }

    public void sleeP(double sleepTime) {
        double time = runtime.time();
        double initTime = time;
        while (time <= initTime+sleepTime) {
            time = runtime.time();
        }

    }


    public void toggleClaw() {
        if (closedMover) {
            plateGrabber.setPosition(0.8);
            plateGrabber2.setPosition(0.2);
            closedMover = false;
        } else  {
            plateGrabber.setPosition(0.2);
            plateGrabber2.setPosition(0.8);
            closedMover = true;
        }
    }

    public void toggleRandomServo() {
        if (closedMover2) {
            thisServo.setPosition(0.8);
            thisServo.setPosition(0.2);
            closedMover = false;
        } else  {
            thisServo.setPosition(0.2);
            thisServo.setPosition(0.8);
            closedMover = true;
        }
    }

    public void moveForwards() {
        rightFrontPower = 1;
        rightBackPower = -1;

        leftFrontPower = -1;
        leftBackPower = 1;
    }
    public void moveBackwards() {
        rightFrontPower = -1;
        rightBackPower = 1;

        leftFrontPower = 1;
        leftBackPower = -1;
    }
    public void moveLeft() {
        rightFrontPower = -1;
        rightBackPower = -1;

        leftFrontPower = -1;
        leftBackPower = -1;
    }
    public void moveRight() {
        rightFrontPower = 1;
        rightBackPower = 1;

        leftFrontPower = 1;
        leftBackPower = 1;
    }
    public void moveBackwardsLeft() {
        rightFrontPower = -1;
        rightBackPower = 0;

        leftFrontPower = 0;
        leftBackPower = -1;
    }
    public void moveBackwardsRight() {
        rightFrontPower = 0;
        rightBackPower = 1;

        leftFrontPower = 1;
        leftBackPower = 0;
    }
    public void moveForwardsLeft() {
        rightFrontPower = 0;
        rightBackPower = -1;

        leftFrontPower = -1;
        leftBackPower = 0;
    }
    public void moveForwardsRight() {
        rightFrontPower = 1;
        rightBackPower = 0;

        leftFrontPower = 0;
        leftBackPower = 1;
    }
    public void zeroMove() {
        rightFrontPower = 0;
        rightBackPower = 0;

        leftFrontPower = 0;
        leftBackPower = 0;
    }
    public void rotateLeft() {

        leftFrontPower = -1;
        rightFrontPower = -1;
        leftBackPower = 1;
        rightBackPower = 1;
    }
    public void rotateRight() {
        leftFrontPower = 1;
        rightFrontPower = 1;
        leftBackPower = -1;
        rightBackPower = -1;
    }
}
