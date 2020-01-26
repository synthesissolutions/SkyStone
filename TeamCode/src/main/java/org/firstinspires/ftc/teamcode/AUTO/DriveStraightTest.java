package org.firstinspires.ftc.teamcode.AUTO;


import java.util.logging.Level;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "Drive Straight Test", group = "Linear Opmode")
public class DriveStraightTest extends AutoBase {

    private ElapsedTime buttonTime = new ElapsedTime();
    private ElapsedTime actionTime = new ElapsedTime();

    double slowSpeed = 0.1;
    int rampDownDistance = 1500;
    double currentSpeed = 0.5;
    double speedStepSize = 0.05;
    int encoderUnitsToTravel = 3000;
    int encoderUnitStepSize = 100;
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
                currentSpeed = 1.0;
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
                distanceTraveled = driveStraightForward2(currentSpeed, encoderUnitsToTravel);
                actionTimeTaken = actionTime.seconds();
            }

            if (gamepad1.y) {
                actionTime.reset();
                distanceTraveled = driveStraightBack2(currentSpeed, encoderUnitsToTravel);
                actionTimeTaken = actionTime.seconds();
            }

            if (gamepad1.a) {
                actionTime.reset();
                distanceTraveled = driveForward2(currentSpeed, encoderUnitsToTravel);
                actionTimeTaken = actionTime.seconds();
            }

            if (gamepad1.b) {
                actionTime.reset();
                distanceTraveled = driveStraightBackRampDown(currentSpeed, slowSpeed, encoderUnitsToTravel);
                actionTimeTaken = actionTime.seconds();
            }
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            telemetry.addData("Angle", angles.firstAngle);
            telemetry.addData("Speed", currentSpeed);
            telemetry.addData("Distance", encoderUnitsToTravel);
            telemetry.addData("Encoders", motorFrontLeft.getCurrentPosition() + " " + motorFrontRight.getCurrentPosition());
            telemetry.addData("Distance", distanceTraveled);
            telemetry.addData("Time Taken", actionTimeTaken);
            telemetry.update();
        }

        shutdownRobot();
    }

    public int driveStraightForward2(double speed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        int currentPosition = startPosition;
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double startAngle = angles.firstAngle;

        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(-speed);

        while (opModeIsActive() && currentPosition > (startPosition - distance)) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles.firstAngle > (startAngle + 0.5)) {
                motorFrontRight.setPower(-speed * 0.9);
                motorFrontLeft.setPower(-speed);
                motorBackRight.setPower(-speed * 0.9);
                motorBackLeft.setPower(-speed);
            }
            else if (angles.firstAngle < (startAngle - 0.5)) {
                motorFrontRight.setPower(-speed);
                motorFrontLeft.setPower(-speed * 0.9);
                motorBackRight.setPower(-speed);
                motorBackLeft.setPower(-speed * 0.9);
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
    public int driveStraightBack2(double speed, int distance) {
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
                motorFrontRight.setPower(speed);
                motorFrontLeft.setPower(speed * 0.9);
                motorBackRight.setPower(speed);
                motorBackLeft.setPower(speed * 0.9);
            }
            else if (angles.firstAngle < (startAngle - 0.5)) {
                motorFrontRight.setPower(speed * 0.9);
                motorFrontLeft.setPower(speed);
                motorBackRight.setPower(speed * 0.9);
                motorBackLeft.setPower(speed);
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

    public int driveStraightBackRampDown(double startSpeed, double endSpeed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        int currentPosition = startPosition;
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double startAngle = angles.firstAngle;
        double percentComplete = 0.0;
        int endEncoderDistance = startPosition + distance;
        int startRampDownEncoderDistance = endEncoderDistance - rampDownDistance; // start ramping down with 500 encoder units left

        double speed = startSpeed;

        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(speed);
        motorBackRight.setPower(speed);
        motorBackLeft.setPower(speed);

        while (opModeIsActive() && currentPosition < endEncoderDistance) {
            int distanceRemaining = endEncoderDistance - currentPosition;
            if (distanceRemaining < rampDownDistance) {
                double rampPercent = distanceRemaining / rampDownDistance;
                speed = endSpeed + (rampPercent * (startSpeed - endSpeed));
            }
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles.firstAngle > (startAngle + 0.5)) {
                motorFrontRight.setPower(speed);
                motorFrontLeft.setPower(speed * 0.9);
                motorBackRight.setPower(speed);
                motorBackLeft.setPower(speed * 0.9);
            }
            else if (angles.firstAngle < (startAngle - 0.5)) {
                motorFrontRight.setPower(speed * 0.9);
                motorFrontLeft.setPower(speed);
                motorBackRight.setPower(speed * 0.9);
                motorBackLeft.setPower(speed);
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

    public int driveForward2(double speed, int distance) {
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
    public int driveBack2(double speed, int distance) {
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
}