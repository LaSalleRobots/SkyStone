/*  SCANN-R (theoretical Robot)
 *
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

/**
 * This 2019-2020 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Skystone game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous(name = "Distance From Phone", group = "Concept")
//@Disabled
public class DistanceFromStone extends LinearOpMode {
  private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
  private static final String LABEL_FIRST_ELEMENT = "Stone";
  private static final String LABEL_SECOND_ELEMENT = "Skystone";

  private int sensorWidth = 3264;
  private double focal = (507 * 60.96) / 20.32; // Precalibrated focal length

  /*
   * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
   * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
   * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
   * web site at https://developer.vuforia.com/license-manager.
   *
   * Vuforia license keys are always 380 characters long, and look as if they contain mostly
   * random data. As an example, here is a example of a fragment of a valid key:
   *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
   * Once you've obtained a license key, copy the string from the Vuforia web site
   * and paste it in to your code on the next line, between the double quotes.
   */
  private static final String VUFORIA_KEY =
    "ATEunI3/////AAABmTcVGyhDpk/vjQi7sL+G3bkNZLIZNtw/qLByk8N+aFHflTAH7VSKoR4Cmzmgq62zIwg4ijmn/bzp1aCFu3u3S2aGfK0fBzUmddZV8n1+vO2sA4RRALRfeGnv/5UWQHXcqPg1Jz3yysRBhu4ur0g7FadQJq0sTfcoWWsELgQYAeFwsZSl+ktrswOc+SUyhrlJUDJijBL4y2kH4/3aeGYsQhQRVW/0EvSmbPuwMa+6Yo7u1f13PKEOdPWbJYybeGPjybwKrptzgTyNLhSoFqIFiUA8Ft1UD9IalVhGlEsHy6KBcWkvCxGJZBvANancvdTJ0O2Ux2pYsAllxQ+h2oR925ND/oeK5Mgno13LfBKkrm2B";

  /**
   * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
   * localization engine.
   */
  private VuforiaLocalizer vuforia;

  /**
   * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
   * Detection engine.
   */
  private TFObjectDetector tfod;

  @Override
  public void runOpMode() {
    // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
    // first.
    initVuforia();

    if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
      initTfod();
    } else {
      telemetry.addData("Sorry!", "This device is not compatible with TFOD");
    }

    /**
     * Activate TensorFlow Object Detection before we wait for the start command.
     * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
     **/
    if (tfod != null) {
      tfod.activate();
    }

    /** Wait for the game to begin */
    telemetry.addData(">", "Press Play to start op mode");
    telemetry.update();
    waitForStart();

    if (opModeIsActive()) {
      while (opModeIsActive()) {
        if (tfod != null) {
          // getUpdatedRecognitions() will return null if no new information is available since
          // the last time that call was made.
          List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
          if (updatedRecognitions != null) {
            telemetry.addData("# Object Detected", updatedRecognitions.size());

            // step through the list of recognitions and display boundary info.
            int i = 0;
            int iter = 0;

            double[] importances = new double[updatedRecognitions.size()]; // the smaller the importance the more important it is
            for (Recognition recognition : updatedRecognitions) {
              // calculate the distance from the camera to the recognized object
              double distance =
                (((60.69 * focal) / recognition.getWidth()) * 0.27377245509);

              // Calculate the importance score
              importances[iter] = distance * 0.1;
              if (recognition.getLabel().equals("Skystone")) {
                importances[iter] -= 1;
              } else {
                importances[iter] -= 0.5;
              }
              if (recognition.getLeft() < sensorWidth * 2.0 / 8) {
                importances[iter] += 0.5;
              } else if (
                recognition.getLeft() > (sensorWidth * 2.0 / 8) &&
                recognition.getLeft() <= (sensorWidth * 4.0 / 8)
              ) {
                importances[iter] -= 0.5;
              } else if (recognition.getLeft() > (sensorWidth * (4.0 / 8))) {
                importances[iter] += 0.5;
              }

              //send object information to logging

              telemetry.addData(
                String.format("label (%d)", i),
                recognition.getLabel()
              );
              telemetry.addData("  Distance:", distance);
              telemetry.addData("  Importance Score: ", importances[iter]);
              telemetry.addData(
                String.format("  left,top (%d)", i),
                "%.03f , %.03f",
                recognition.getLeft(),
                recognition.getTop()
              );
              telemetry.addData(
                String.format("  right,bottom (%d)", i),
                "%.03f , %.03f",
                recognition.getRight(),
                recognition.getBottom()
              );
              iter++;
            }
            telemetry.update();
          }
        }
      }
    }

    if (tfod != null) {
      tfod.shutdown();
    }
  }

  /**
   * Initialize the Vuforia localization engine.
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

  /**
   * Initialize the TensorFlow Object Detection engine.
   */
  private void initTfod() {
    int tfodMonitorViewId = hardwareMap
      .appContext.getResources()
      .getIdentifier(
        "tfodMonitorViewId",
        "id",
        hardwareMap.appContext.getPackageName()
      );
    TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(
      tfodMonitorViewId
    );
    tfodParameters.minimumConfidence = 0.8;
    tfod =
      ClassFactory
        .getInstance()
        .createTFObjectDetector(tfodParameters, vuforia);
    tfod.loadModelFromAsset(
      TFOD_MODEL_ASSET,
      LABEL_FIRST_ELEMENT,
      LABEL_SECOND_ELEMENT
    );
  }
}
