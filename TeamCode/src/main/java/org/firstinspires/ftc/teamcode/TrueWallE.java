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
    private int safeZone = 300; // the safezone for getting the bricks centered



    private ElapsedTime runtime = new ElapsedTime();

    //Setup claw servos variables
    private Servo plateGrabber = null;
    private Servo plateGrabber2 = null;
    private Servo capstoneHolder = null;


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
        capstoneHolder = hardwareMap.get(Servo.class, "teamMarker");


        RoboHelper robot = new RoboHelper(hardwareMap,runtime);
        waitForStart();
        capstoneHolder.setPosition(0);

        closedMover = true;
        runtime.reset();

        if (opModeIsActive()) {

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
                            double boxX = recognition.getWidth()/2; // The mid-position for the recognized bounding box width
                            double centerX = recognition.getImageWidth()/2; // the mid-position for the frame box width
                            double centerY = recognition.getImageHeight()/2; // the mid-position for the frame box height
                            double boxMid = recognition.getLeft() + boxX; // Center point Horizontally

                            //Action and logic
                            if (recognition.getLabel().equals("Skystone")) {
                                //Make sure to use tolerances for comparison


                                //Are we not in the safe zone?
                                if (!(boxMid + safeZone <= centerX && boxMid - safeZone >= centerX)) {
                                    // we are not in the safe zone of whatever var saveZone px
                                    if (boxMid < centerX) {

                                        double distancePX = centerX - boxMid;
                                        // the box is to our right so we need to move left
                                        robot.moveLeft();
                                        robot.runFor(distancePX/1000);

                                    } else if (boxMid > centerX) {
                                        double distancePX = boxMid - centerX;
                                        // the box is to our left so we need to move right
                                        robot.moveRight();
                                        robot.runFor(distancePX/1000);
                                    }
                                } else {
                                    // we are in the safe zone
                                    robot.moveForwards();
                                    robot.runFor(distance/51);
                                }

                            }
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
            plateGrabber.setPosition(0.8);
            plateGrabber2.setPosition(0.2);
            closedMover = false;
        } else  {
            plateGrabber.setPosition(0.2);
            plateGrabber2.setPosition(0.8);
            closedMover = true;
        }
    }

}
