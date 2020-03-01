package org.firstinspires.ftc.teamcode.AUTO;


import java.util.logging.Level;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "BasicAutoTest", group = "Linear Opmode")
//@Disabled
public class DriveStraightTest extends aTabascoAutoBase {

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

        initializeRobot();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

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
                horizontalSlideIn(0.25);
                actionTimeTaken = actionTime.seconds();
            }

            if (gamepad1.y) {
                actionTime.reset();
                horizontalSlideOut(0.25);
                actionTimeTaken = actionTime.seconds();
            }

            if (gamepad1.a) {
                actionTime.reset();
                verticalSlide(-1.0, 0.25);
                actionTimeTaken = actionTime.seconds();
            }

            if (gamepad1.b) {
                actionTime.reset();
                verticalSlide(1.0, 0.25);
                actionTimeTaken = actionTime.seconds();
            }
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            //telemetry.addData("Position", motorFrontRight.getCurrentPosition());
            //telemetry.addData("Angle", angles.firstAngle);
            //telemetry.addData("FLState", sensorFoundationLeft.getState);
            //telemetry.addData("FRState", sensorFoundationRight.getState);
            telemetry.addData("Speed", currentSpeed);
            telemetry.addData("Angle", encoderUnitsToTravel);
            telemetry.addData("Encoders", motorBackLeft.getCurrentPosition() + " " + motorBackRight.getCurrentPosition());
            telemetry.addData("Distance", distanceTraveled);
            telemetry.addData("At Bottom", isLiftAtBottom());
            telemetry.update();
        }
        stopMotors();
        //shutdownRobot();
    }

    public int driveStraightForward2(double speed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        int currentPosition = startPosition;
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double startAngle = angles.firstAngle;

        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(speed);
        motorBackRight.setPower(speed);
        motorBackLeft.setPower(speed);

        while (opModeIsActive() && currentPosition < (startPosition + distance)) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles.firstAngle > (startAngle + 0.5)) {
                motorFrontRight.setPower(speed * 0.9);
                motorFrontLeft.setPower(speed);
                motorBackRight.setPower(speed * 0.9);
                motorBackLeft.setPower(speed);
            }
            else if (angles.firstAngle < (startAngle - 0.5)) {
                motorFrontRight.setPower(speed);
                motorFrontLeft.setPower(speed * 0.9);
                motorBackRight.setPower(speed);
                motorBackLeft.setPower(speed * 0.9);
            }
            else {
                motorFrontRight.setPower(speed);
                motorFrontLeft.setPower(speed);
                motorBackRight.setPower(speed);
                motorBackLeft.setPower(speed);
            }

            currentPosition = motorFrontLeft.getCurrentPosition();
        }
        stopMotors();

        return currentPosition;
    }
    public int driveStraightBack2(double speed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        int currentPosition = startPosition;
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double startAngle = angles.firstAngle;

        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(-speed);

        while (opModeIsActive() && currentPosition > (startPosition + distance)) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles.firstAngle > (startAngle + 0.5)) {
                motorFrontRight.setPower(-speed);
                motorFrontLeft.setPower(-speed * 0.9);
                motorBackRight.setPower(-speed);
                motorBackLeft.setPower(-speed * 0.9);
            }
            else if (angles.firstAngle < (startAngle - 0.5)) {
                motorFrontRight.setPower(-speed * 0.9);
                motorFrontLeft.setPower(-speed);
                motorBackRight.setPower(-speed * 0.9);
                motorBackLeft.setPower(-speed);
            }
            else {
                motorFrontRight.setPower(-speed);
                motorFrontLeft.setPower(-speed);
                motorBackRight.setPower(-speed);
                motorBackLeft.setPower(-speed);
            }

            currentPosition = motorFrontLeft.getCurrentPosition();
        }
        stopMotors();

        return currentPosition;
    }
    public int driveForward2(double speed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        int currentPosition = startPosition;

        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(speed);
        motorBackRight.setPower(speed);
        motorBackLeft.setPower(speed);

        while (opModeIsActive() && currentPosition < (startPosition + distance)) {
            currentPosition = motorFrontLeft.getCurrentPosition();
        }
        stopMotors();

        return currentPosition;
    }
    public int driveBack2(double speed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        int currentPosition = startPosition;

        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(-speed);

        while (opModeIsActive() && currentPosition > (startPosition - distance)) {
            currentPosition = motorFrontLeft.getCurrentPosition();
        }
        stopMotors();

        return currentPosition;
    }
}