package org.firstinspires.ftc.teamcode.AUTO;


import java.util.logging.Level;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "BasicAutoTest", group = "Linear Opmode")
//@Disabled
public class DriveStraightTest extends LinearOpMode {
    AutoTabasco tabasco = new AutoTabasco();

    private ElapsedTime buttonTime = new ElapsedTime();
    private ElapsedTime actionTime = new ElapsedTime();

    double slowSpeed = 0.15;
    int rampDownDistance = 1000;
    double currentSpeed = 0.5;
    double speedStepSize = 0.05;
    int encoderUnitsToTravel = 270;
    int encoderUnitStepSize = 5;
    int distanceTraveled = 0;
    double buttonTimeOut = 0.3; // seconds between button clicks
    double actionTimeTaken = 0.0; // seconds it took for action to complete

    @Override
    public void runOpMode() throws InterruptedException {

        tabasco.initializeRobot(hardwareMap);
        tabasco.angles = tabasco.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", tabasco.angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        while (opModeIsActive()) {

            // allow user to press up/down on dpad to increase/decrease speed
            if (buttonTime.seconds() > buttonTimeOut) {
                if (gamepad1.dpad_up) {
                    currentSpeed += speedStepSize;
                    buttonTime.reset();
                } else if (gamepad1.dpad_down) {
                    currentSpeed -= speedStepSize;
                    buttonTime.reset();
                }
            }

            if (currentSpeed > 1.0) {
                currentSpeed = 0.5;
            } else if (currentSpeed <= 0) {
                currentSpeed = 0.05;
            }

            // allow user to press left/right on dpad to increase/decrease distance
            if (buttonTime.seconds() > buttonTimeOut) {
                if (gamepad1.dpad_right) {
                    encoderUnitsToTravel += encoderUnitStepSize;
                    buttonTime.reset();
                } else if (gamepad1.dpad_left) {
                    encoderUnitsToTravel -= encoderUnitStepSize;
                    buttonTime.reset();
                }
            }

            if (encoderUnitsToTravel <= encoderUnitStepSize) {
                encoderUnitsToTravel = encoderUnitStepSize;
            }


            if (gamepad1.x) {
                actionTime.reset();
                tabasco.horizontalSlideIn(0.25);
                actionTimeTaken = actionTime.seconds();
            }

            if (gamepad1.y) {
                actionTime.reset();
                tabasco.horizontalSlideOut(0.25);
                actionTimeTaken = actionTime.seconds();
            }

            if (gamepad1.a) {
                actionTime.reset();
                //tabasco.verticalSlide(-1.0, 0.25);
                actionTimeTaken = actionTime.seconds();
            }

            if (gamepad1.b) {
                actionTime.reset();
                //tabasco.verticalSlide(1.0, 0.25);
                actionTimeTaken = actionTime.seconds();
            }
            tabasco.angles = tabasco.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            //telemetry.addData("Position", motorFrontRight.getCurrentPosition());
            //telemetry.addData("Angle", angles.firstAngle);
            //telemetry.addData("FLState", sensorFoundationLeft.getState);
            //telemetry.addData("FRState", sensorFoundationRight.getState);
            telemetry.addData("Speed", currentSpeed);
            telemetry.addData("Angle", encoderUnitsToTravel);
            telemetry.addData("Encoders", tabasco.motorBackLeft.getCurrentPosition() + " " + tabasco.motorBackRight.getCurrentPosition());
            telemetry.addData("Distance", distanceTraveled);
            telemetry.addData("At Bottom", tabasco.isLiftAtBottom());
            telemetry.update();
        }
        tabasco.stopMotors();
        //shutdownRobot();
    }
}