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

@Autonomous(name="Wall-E", group="AI")
public class TrueWallE extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
    private static final String VUFORIA_KEY =
            "ATEunI3/////AAABmTcVGyhDpk/vjQi7sL+G3bkNZLIZNtw/qLByk8N+aFHflTAH7VSKoR4Cmzmgq62zIwg4ijmn/bzp1aCFu3u3S2aGfK0fBzUmddZV8n1+vO2sA4RRALRfeGnv/5UWQHXcqPg1Jz3yysRBhu4ur0g7FadQJq0sTfcoWWsELgQYAeFwsZSl+ktrswOc+SUyhrlJUDJijBL4y2kH4/3aeGYsQhQRVW/0EvSmbPuwMa+6Yo7u1f13PKEOdPWbJYybeGPjybwKrptzgTyNLhSoFqIFiUA8Ft1UD9IalVhGlEsHy6KBcWkvCxGJZBvANancvdTJ0O2Ux2pYsAllxQ+h2oR925ND/oeK5Mgno13LfBKkrm2B";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;


    private int sensorWidth = 3264;
    private double focal = (507 * 60.96) / 20.32; // Precalibrated focal length
    private int safeZone = 50; // the safezone for getting the bricks centered


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

    double leftFrontPower = 0.5;
    double rightFrontPower = 0.5;
    double leftBackPower = 0.5;
    double rightBackPower = 0.5;

    boolean closedMover = false;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initalized");
        telemetry.update();
        initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
        if (tfod != null) {
            tfod.activate();
        }

        //setup motors
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        plateGrabber = hardwareMap.get(Servo.class, "plateGrabber");
        plateGrabber2 = hardwareMap.get(Servo.class, "plateGrabber2");

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

        if (opModeIsActive()) {

            //insert move plate between
            //begin
            while (runtime.time() < 14){
                if (runtime.time() < 1) {
                    rightFrontPower = -1;
                    rightBackPower = -1;

                    leftFrontPower = -1;
                    leftBackPower = -1;
                }

                if (runtime.time() < 3) {
                    rightFrontPower = 1;
                    rightBackPower = -1;

                    leftFrontPower = -1;
                    leftBackPower = 1;
                }
                else if (runtime.time() < 5) {
                    plateGrabber.setPosition(1);
                    plateGrabber2.setPosition(0);

                    rightFrontPower = 0;
                    rightBackPower = 0;

                    leftFrontPower = 0;
                    leftBackPower = 0;
                }
                else if (runtime.time() < 7) {
                    rightFrontPower = -1;
                    rightBackPower = 1;

                    leftFrontPower = 1;
                    leftBackPower = -1;
                }
                else if (runtime.time() < 9) {
                    plateGrabber.setPosition(0);
                    plateGrabber2.setPosition(1);

                    rightFrontPower = 0;
                    rightBackPower = 0;

                    leftFrontPower = 0;
                    leftBackPower = 0;
                }
                else {
                    rightFrontPower = -1;
                    rightBackPower = -1;

                    leftFrontPower = -1;
                    leftBackPower = -1;
                }
                applyPower();
            }

            powerOff();



            //end

            while (opModeIsActive()) {
                if (tfod != null) {
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        int i = 0;
                        int iter = 0;
                        double[] importances = new double[updatedRecognitions.size()];
                        for (Recognition recognition : updatedRecognitions) {
                            double distance = (((60.69 * focal) / recognition.getWidth()) * 0.27377245509); // Distance from position
                            double boxX = recognition.getWidth()/2; // The mid-position for the recignized bounding box width
                            double centerX = recognition.getImageWidth()/2; // the mid-position for the frame box width
                            double centerY = recognition.getImageHeight()/2; // the mid-position for the frame box height
                            double boxMid = recognition.getLeft() + boxX; // Center point Horizontally

                            //Action and logic
                            if (recognition.getLabel().equals("Skystone")) {
                                //Make sure to use tolerances for comparison


                                //Are we not in the safe zone?
                                if (!(boxMid+safeZone <= centerX && boxMid-safeZone >= centerX)) {
                                    // we are not in the safe zone of 50px
                                    if (boxMid < centerX ) {
                                        // the box is to our right so we need to move left
                                        moveLeft();
                                    }
                                    else if (boxMid > centerX) {
                                        // the box is to our left so we need to move right
                                        moveRight();
                                    }
                                }
                                else { // we are in the safe zone
                                    moveForwards();
                                }

                            }
                            applyPower();
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.update();
                        }
                    }
                }
            }
        }




    }
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
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

    public void powerOff() {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }
    public void applyPower() {
        leftFront.setPower(leftFrontPower);
        rightFront.setPower(rightFrontPower);
        leftBack.setPower(leftBackPower);
        rightBack.setPower(rightBackPower);
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
