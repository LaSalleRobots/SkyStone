package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;

@Disabled
@Autonomous(name = "HELLO JPY", group = "JPY")
public class Test extends LinearOpMode {

  @Override
  public void runOpMode() {
    telemetry.addData("status", "inited from jpy");
    AndroidTextToSpeech speaker = new AndroidTextToSpeech();
    speaker.initialize();
    waitForStart();
    speaker.speak("Hello World from j p y ");
    telemetry.addData("Status", "hello World!");
  }
}
