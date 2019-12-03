/*  GRAB-E (v3 Robot)
 *
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Controller Op Mode", group="Linear Opmode")

public class ControllerOpMode extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor armLeft = null;
    private DcMotor armRight = null;
    private Servo servoRotation = null;
    private Servo servoElevation = null;
    private Servo clawLeft = null;
    private Servo clawRight = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        //servoRotation = hardwareMap.get(Servo.class, "rotation");
        //servoElevation = hardwareMap.get(Servo.class, "elevation");
        clawRight = hardwareMap.get(Servo.class, "clawright");
        clawLeft = hardwareMap.get(Servo.class, "clawleft");
        leftDrive  = hardwareMap.get(DcMotor.class, "left");
        rightDrive = hardwareMap.get(DcMotor.class, "right");
        armRight = hardwareMap.get(DcMotor.class, "armright");
        armLeft = hardwareMap.get(DcMotor.class, "armleft");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
     
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;
            
            double maxSpeed = 1;
            
            if (gamepad1.left_bumper){
                maxSpeed = 2;
            }
            if (gamepad1.left_stick_button){
                maxSpeed = 4;
            }
            
            
            double drive = -gamepad1.left_stick_x;
            double turn  = gamepad1.left_stick_y;
            double armPower = gamepad1.right_stick_y;
            armPower = Range.clip(armPower, -0.35, 0.5)/maxSpeed;
            leftPower    = Range.clip(drive + turn, -1.0, 1.0)/maxSpeed ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0)/maxSpeed ;
         
            if (gamepad1.a) {
                clawLeft.setPosition(1);
                clawRight.setPosition(0);

            }
            if (gamepad1.y) {
                clawLeft.setPosition(0);
                clawRight.setPosition(1);
            }

            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);
            
            armLeft.setPower(armPower);
            armRight.setPower(-armPower);
        
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
         
            telemetry.update();
        }
    }
}
