package org.firstinspires.ftc.teamcode.AUTO;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.logging.Level;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "CorrectAngleTest", group = "Linear Opmode")
public class CorrectAngleTest extends aPaprikaAutoBase {

    private ElapsedTime buttonTime = new ElapsedTime(); // to keep code from thinking you pushed a button twice
    private ElapsedTime actionTime = new ElapsedTime(); // for measuring time it takes to complete action

    int margin = 2;
    double targetAngle = 0;
    double currentSpeed = 0.5;
    double slowSpeed = 0.17;

    int marginStepSize = 1;
    double angleStepSize = 10.0;
    double speedStepSize = 0.05;

    double buttonTimeOut = 0.3; // seconds between button clicks
    double actionTimeTaken = 0.0; // seconds it took to complete action

    String crossingZero = "no";
    String turningToZero = "no";

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
            //allow user to adjust turn speed
            if (buttonTime.seconds() > buttonTimeOut) {
                if (gamepad1.dpad_up) {
                    currentSpeed += speedStepSize;
                    buttonTime.reset();
                }
                else if (gamepad1.dpad_down) {
                    currentSpeed -= speedStepSize;
                    buttonTime.reset();
                }
            }
            if (currentSpeed > 1.0) {
                currentSpeed = 1.0;
            }
            if (currentSpeed <= 0) {
                currentSpeed = 0.05;
            }
            //allow user to adjust turn angle
            if (buttonTime.seconds() > buttonTimeOut) {
                if (gamepad1.dpad_right) {
                    targetAngle += angleStepSize;
                    buttonTime.reset();
                }
                if (gamepad1.dpad_left) {
                    targetAngle -= angleStepSize;
                    buttonTime.reset();
                }
            }
            if (targetAngle >= 370) {
                targetAngle = 0;
            }
            if (targetAngle <= -10) {
                targetAngle = 360;
            }
            //allows user to adjust margin for error
            if (buttonTime.seconds() > buttonTimeOut) {
                if (gamepad1.right_bumper) {
                    margin += marginStepSize;
                    buttonTime.reset();
                }
                if (gamepad1.left_bumper) {
                    margin -= marginStepSize;
                    buttonTime.reset();
                }
            }
            if (margin > 10) {
                margin = 10;
            }
            if (margin < 0) {
                margin = 0;
            }
            //actions
            if (gamepad1.x) {
                actionTime.reset();
                spinLeft(30, 0.22, 0.17);
                actionTimeTaken = actionTime.seconds();
            }
            if (gamepad1.y) {
                actionTime.reset();
                spinRight(30, 0.22, 0.17);
                actionTimeTaken = actionTime.seconds();
            }
            if (gamepad1.a) {
                actionTime.reset();
                correctAngle(margin, targetAngle, currentSpeed, slowSpeed);
                actionTimeTaken = actionTime.seconds();
            }
            if (gamepad1.b) {
                actionTime.reset();
                correctAngle(0, 0, 0.3, 0.19);
                actionTimeTaken = actionTime.seconds();
            }
        }
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("margin for error", margin);
        telemetry.addData("currentAngle", normalizeAngle(angles.firstAngle));
        telemetry.addData("Speed", currentSpeed);
        telemetry.addData("targetAngle", targetAngle);
        telemetry.addData("Time Taken", actionTimeTaken);
        telemetry.update();

        shutdownRobot();
    }

    public void correctAngle(int margin, double targetAngle, double maxSpeed, double minSpeed) {
        double currentAngle = getNormCurrentAngle();
        boolean crossingZeroFromLeft = (91 > currentAngle && 269 < targetAngle);

        boolean crossingZeroFromRight = (269 < currentAngle && 91 > targetAngle);

        telemetry.update();
        if (crossingZeroFromLeft){
            while (opModeIsActive() && (91 > currentAngle)) {
                crossingZero = "From Left";
                telemetry.update();
                currentAngle = getNormCurrentAngle();
                turnRight(maxSpeed);
            }
            stopMotors();
        }
        else if (crossingZeroFromRight) {
            while (opModeIsActive() && (269 < currentAngle)) {
                crossingZero = "From Right";
                telemetry.update();
                currentAngle = getNormCurrentAngle();
                turnLeft(maxSpeed);
            }
            stopMotors();
        }
        else if (targetAngle == 0) {
            //if its on right turn left
            //if on left turn right
            if (currentAngle < 180) {
                turningToZero = "From Left";
                delay(1.0);

                turnRightToAngle(targetAngle, maxSpeed, minSpeed);
            }
            else if (currentAngle > 180) {
                turningToZero = "From Right";
                delay(1.0);

                turnLeftToAngle(targetAngle, maxSpeed, minSpeed);
            }
        }
        else if (currentAngle < (targetAngle - margin)) {
            getNormCurrentAngle();
            telemetry.addData("turningLeft", "turningLeft");
            telemetry.update();

            turnLeftToAngle(targetAngle, maxSpeed, minSpeed);
        }
        else if (currentAngle > (targetAngle + margin)) {
            getNormCurrentAngle();
            telemetry.addData("turningRight", "turningRight");
            telemetry.update();

            turnRightToAngle(targetAngle, maxSpeed, minSpeed);

        }
    }
}