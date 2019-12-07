package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name="AI Wall-E", group = "AI")
public class TrueWallE extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
    private static final String VUFORIA_KEY =
            "ATEunI3/////AAABmTcVGyhDpk/vjQi7sL+G3bkNZLIZNtw/qLByk8N+aFHflTAH7VSKoR4Cmzmgq62zIwg4ijmn/bzp1aCFu3u3S2aGfK0fBzUmddZV8n1+vO2sA4RRALRfeGnv/5UWQHXcqPg1Jz3yysRBhu4ur0g7FadQJq0sTfcoWWsELgQYAeFwsZSl+ktrswOc+SUyhrlJUDJijBL4y2kH4/3aeGYsQhQRVW/0EvSmbPuwMa+6Yo7u1f13PKEOdPWbJYybeGPjybwKrptzgTyNLhSoFqIFiUA8Ft1UD9IalVhGlEsHy6KBcWkvCxGJZBvANancvdTJ0O2Ux2pYsAllxQ+h2oR925ND/oeK5Mgno13LfBKkrm2B";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;


    private int sensorWidth = 3264;
    private double focal = (507 * 60.96) / 20.32; // Precalibrated focal length


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



        RoboHelper robot = new RoboHelper(hardwareMap,runtime);
        waitForStart();

        runtime.reset();

        if (opModeIsActive()) {

            while (opModeIsActive()) {
                if (tfod != null) {
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        int i = 0;
                        for (Recognition recognition : updatedRecognitions) {
                            double distance = (((60.69 * focal) / recognition.getWidth()) * 0.27377245509); // Distance from position
                            double boxX = recognition.getWidth() / 2; // The mid-position for the recognized bounding box width
                            double centerX = recognition.getImageWidth() / 2; // the mid-position for the frame box width
                            double centerY = recognition.getImageHeight() / 2; // the mid-position for the frame box height
                            double boxMid = recognition.getLeft() + boxX; // Center point Horizontally
                            double distancePX = 0;
                            //Action and logic
                            if (recognition.getLabel().equals("Skystone")) {

                                if (boxMid >= 512 && boxMid <= 768) {
                                    //inside middle safezone
                                    robot.moveForwards();
                                    robot.runFor(distance/51);
                                } else {
                                    //outside safezone
                                    if (boxMid < 512) {
                                        robot.moveLeft();
                                        robot.runFor(0.05);
                                    } else if (boxMid > 768) {
                                        robot.moveRight();
                                        robot.runFor(0.05);
                                    }
                                }

                            }
                            telemetry.addData("FullW", recognition.getImageWidth());
                            telemetry.addData("FullH", recognition.getImageHeight());
                            telemetry.addData("CameraX: ", centerX);
                            telemetry.addData("CameraY: ", centerY);
                            telemetry.addData("BoxMid :", boxMid);
                            telemetry.addData("Distance(px) :", distancePX);
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData("Distance:", distance);
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
}
