package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name="Wall-E Auto Right", group="AI")
public class WallEAutoRight extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
    private static final String VUFORIA_KEY =
            "ATEunI3/////AAABmTcVGyhDpk/vjQi7sL+G3bkNZLIZNtw/qLByk8N+aFHflTAH7VSKoR4Cmzmgq62zIwg4ijmn/bzp1aCFu3u3S2aGfK0fBzUmddZV8n1+vO2sA4RRALRfeGnv/5UWQHXcqPg1Jz3yysRBhu4ur0g7FadQJq0sTfcoWWsELgQYAeFwsZSl+ktrswOc+SUyhrlJUDJijBL4y2kH4/3aeGYsQhQRVW/0EvSmbPuwMa+6Yo7u1f13PKEOdPWbJYybeGPjybwKrptzgTyNLhSoFqIFiUA8Ft1UD9IalVhGlEsHy6KBcWkvCxGJZBvANancvdTJ0O2Ux2pYsAllxQ+h2oR925ND/oeK5Mgno13LfBKkrm2B";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;


    private int sensorWidth = 3264;
    private double focal = (507 * 60.96) / 20.32; // Precalibrated focal length
    private int safeZone = 100; // the safezone for getting the bricks centered


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
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);


        waitForStart();
        plateGrabber.setPosition(0);
        plateGrabber2.setPosition(1);
        capstoneHolder.setPosition(0);

        closedMover = true;
        runtime.reset();
        RoboHelper robot = new RoboHelper(hardwareMap, runtime);

        if (opModeIsActive()) {

            robot.moveLeft();
            robot.runFor(0.5);
            robot.moveBackwards();
            robot.runFor(2.5);
            robot.powerOff();
            telemetry.addData("Status", "stoping");
            telemetry.update();
            robot.togglePlateGrabber();
            robot.runFor(2);
            robot.moveForwards();
            robot.runFor(4);
            robot.togglePlateGrabber();
            robot.moveRight();
            robot.runFor(4);



        }




    }



}
