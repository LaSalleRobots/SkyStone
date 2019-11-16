package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="Auto Plate Test", group = "Autonomous")
public class AutoPlateTest extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor armLeft = null;
    private DcMotor armRight = null;
    private Servo clawLeft = null;
    private Servo clawRight = null;


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        leftDrive = hardwareMap.get(DcMotor.class, "left");
        rightDrive = hardwareMap.get(DcMotor.class, "right");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            double leftPower = 0;
            double rightPower = 0;

            if (runtime.time() < 2) {
                leftPower = 0.5;
                rightPower = 0.5;
            }
            else if (runtime.time() < 3) {
                leftPower = -0.5;
                rightPower = 0.5;
            }
            else if (runtime.time() < 5) {
                armLeft.setPower(1);
                armRight.setPower(-1);
            }
            else if (runtime.time() < 6) {
                leftPower = -0.5;
                rightPower = 0.5;
            }
            else if (runtime.time() < 9) {
                leftPower = 1;
                rightPower = 1;
            }


            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);



        }
    }
}

