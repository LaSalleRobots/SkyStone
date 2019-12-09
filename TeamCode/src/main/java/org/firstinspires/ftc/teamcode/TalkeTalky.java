package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;

@Autonomous(name="TalkeTalky", group="Concepts")
public class TalkeTalky extends LinearOpMode {

    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        AndroidTextToSpeech speaker = new AndroidTextToSpeech();
        speaker.initialize();
        speaker.speak("Initialized");
        waitForStart();
        speaker.speak("Started");

    }
}
