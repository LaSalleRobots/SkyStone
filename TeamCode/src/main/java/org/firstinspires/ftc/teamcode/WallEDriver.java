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

@TeleOp(name="Wall-E Driver", group="Linear Opmode")
public class WallEDriver extends LinearOpMode {
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
    private Servo plateGrabber2 = null;
    private Servo clawLeft = null;
    private Servo clawRight = null;
    private Servo clawRotate = null;
    private Servo capstoneHolder = null;

    double leftFrontPower = 0.5;
    double rightFrontPower = 0.5;
    double leftBackPower = 0.5;
    double rightBackPower = 0.5;

    boolean closedMover = false;


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
        capstoneHolder = hardwareMap.get(Servo.class, "teamMarker");

        //Set Directions
        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);


        waitForStart();
        plateGrabber.setPosition(0);
        plateGrabber2.setPosition(1);
        capstoneHolder.setPosition(0);
        closedMover = true;
        runtime.reset();
        while (opModeIsActive()) {
            if (gamepad1.dpad_up) {moveForwards();}
            else if (gamepad1.dpad_down) {moveBackwards();}
            else if (gamepad1.dpad_left) {moveLeft();}
            else if (gamepad1.dpad_right) {moveRight();}
            else if (gamepad1.a) {rotateLeft();}
            else if (gamepad1.b) {rotateRight();}
            else if (gamepad1.left_bumper) {toggleClaw();}
            else {zeroMove();}

            if (gamepad1.a) {dropMarker();}

            leftFront.setPower(leftFrontPower);
            rightFront.setPower(rightFrontPower);
            leftBack.setPower(leftBackPower);
            rightBack.setPower(rightBackPower);

            telemetry.addData("grabber1:", plateGrabber.getPosition());
            telemetry.addData("grabber2:", plateGrabber.getPosition());

            telemetry.update();
        }

    }

    public void toggleClaw() {
        if (closedMover) {
            plateGrabber.setPosition(1);
            plateGrabber2.setPosition(0);
            closedMover = false;
        } else {
            plateGrabber.setPosition(0);
            plateGrabber2.setPosition(1);
            closedMover = true;
        }
    }

    public void dropMarker() {
        capstoneHolder.setPosition(0.7);
    }

    public void moveForwards() {
        rightFrontPower = -1;
        rightBackPower = -1;

        leftFrontPower = -1;
        leftBackPower = -1;
    }
    public void moveBackwards() {
        rightFrontPower = 1;
        rightBackPower = 1;

        leftFrontPower = 1;
        leftBackPower = 1;
    }
    public void moveLeft() {
        rightFrontPower = 1;
        rightBackPower = -1;

        leftFrontPower = -1;
        leftBackPower = 1;
    }
    public void moveRight() {
        rightFrontPower = -1;
        rightBackPower = 1;

        leftFrontPower = 1;
        leftBackPower = -1;
    }
    public void zeroMove() {
        rightFrontPower = 0;
        rightBackPower = 0;

        leftFrontPower = 0;
        leftBackPower = 0;
    }
    public void rotateLeft() {

        leftFrontPower = 1;
        rightFrontPower = -1;
        leftBackPower = 1;
        rightBackPower = 1;
    }
    public void rotateRight() {
        leftFrontPower = 1;
        rightFrontPower = 1;
        leftBackPower = -1;
        rightBackPower = 1;
    }
}
