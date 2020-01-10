package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;

import java.util.List;

@Autonomous(name="Wall-E Full Auto", group="AI")
public class FullAiAuto extends OpMode {


    //Ai/CV Variables
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
    private static final String VUFORIA_KEY =
            "ATEunI3/////AAABmTcVGyhDpk/vjQi7sL+G3bkNZLIZNtw/qLByk8N+aFHflTAH7VSKoR4Cmzmgq62zIwg4ijmn/bzp1aCFu3u3S2aGfK0fBzUmddZV8n1+vO2sA4RRALRfeGnv/5UWQHXcqPg1Jz3yysRBhu4ur0g7FadQJq0sTfcoWWsELgQYAeFwsZSl+ktrswOc+SUyhrlJUDJijBL4y2kH4/3aeGYsQhQRVW/0EvSmbPuwMa+6Yo7u1f13PKEOdPWbJYybeGPjybwKrptzgTyNLhSoFqIFiUA8Ft1UD9IalVhGlEsHy6KBcWkvCxGJZBvANancvdTJ0O2Ux2pYsAllxQ+h2oR925ND/oeK5Mgno13LfBKkrm2B";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    private int sensorWidth = 3264; //Camera Sensor width
    private double focal = (507 * 60.96) / 20.32; // Precalibrated focal length
    private int safeZone = 100; // the safezone for getting the bricks centered



    //Class Variables
    private ElapsedTime runtime = new ElapsedTime();
    private RoboHelper robot;
    private RecordPlayer recordPlayer;
    private AndroidTextToSpeech speaker;

    private Recognition skystone;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        recordPlayer = new RecordPlayer(hardwareMap.appContext);
        speaker = new AndroidTextToSpeech();
        speaker.initialize();
        this.robot = new RoboHelper(hardwareMap, runtime); //Robot object
        initVuforia();
        initTfod();
        tfod.activate();
        telemetry.addData("Status", "Initialized");
    }



    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {

    }
    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();

        robot.moveForwards();
        robot.runFor(1);
        while (skystone == null && runtime.time() < 3) {
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size()); // How many things do we see on screen
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getLabel().equals("Skystone")) {
                        skystone = recognition;
                    }
                }
            }
        }
        if (skystone != null) {
            double distance = (((60.69 * focal) / skystone.getWidth()) * 0.27377245509); // Distance from position
            double boxX = skystone.getWidth() / 2; // The mid-position for the recognized bounding box width
            double boxMid = skystone.getLeft() + boxX; // Center point Horizontally

            if (boxMid >= 512 && boxMid <= 768) {
                speaker.speak("All ready setup!");
            } else {
                if (boxMid < (skystone.getImageWidth() / 2)) {
                    robot.moveLeft();
                    robot.runFor(0.35);
                } else {
                    robot.moveRight();
                    robot.runFor(0.35);
                }
            }
            robot.moveForwards();
            robot.runFor(3);
            robot.rotateRight();
            robot.runFor(2);
            robot.moveForwards();
            robot.runFor(3);
        } else {
            robot.moveBackwards();
            robot.runFor(2);
            robot.moveLeft();
            robot.runFor(5);
        }
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }





    /*
     * Tensorflow Object Detection api methods
     */
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
