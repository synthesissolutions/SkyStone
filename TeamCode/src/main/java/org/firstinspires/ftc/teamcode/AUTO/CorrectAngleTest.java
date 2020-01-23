package org.firstinspires.ftc.teamcode.AUTO;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.logging.Level;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "CorrectAngleTest", group = "Linear Opmode")
public class CorrectAngleTest extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();


        correctAngle(3, 353, 0.22, 0.19);
        takeCurrentAngle();
        delay(2.0);
        correctAngle(3, 7, 0.22, 0.19);
        takeCurrentAngle();
        delay(2.0);
      /*
        correctAngle(3, 0, 0.22, 0.19);
        takeCurrentAngle();
        delay(0.5);
        turnLeftToAngle(10, 0.5, 0.18);
        takeCurrentAngle();
        delay(0.5);
        correctAngle(3, 0, 0.22, 0.19);
        takeCurrentAngle();
        delay(1.0);

        spinRight(91, 0.5, 0.18);
        takeCurrentAngle();
        delay(0.5);
        correctAngle(3, 180, 0.22, 0.19);
        takeCurrentAngle();
        delay(0.5);
        spinRight(80, 0.5, 0.18);
        takeCurrentAngle();
        delay(0.5);
        correctAngle(3, 180, 0.22, 0.19);
        takeCurrentAngle();
        delay(0.5);
        spinLeft(90, 0.5, 0.18);
        takeCurrentAngle();
        delay(0.5);
        correctAngle(3, 7, 0.22, 0.19);
        takeCurrentAngle();
        delay(1.0);
        correctAngle(3, 353, 0.22, 0.19);
        takeCurrentAngle();
        delay(1.0);
        turnRightToAngle(180, 0.5, 0.18);
        takeCurrentAngle();
        delay(1.0);
        correctAngle(3, 190, 0.22, 0.19);
        takeCurrentAngle();
        delay(1.0);
        correctAngle(3, 170, 0.22, 0.19);
        takeCurrentAngle();
        delay(1.0);
        spinRight(360, 1.0, 0.2);
        delay(5.0);
        */
        shutdownRobot();
    }

    public void correctAngle(int margin, double targetAngle, double maxSpeed, double minSpeed) {
        double currentAngle = getNormCurrentAngle();
        boolean crossingZeroFromLeft = ((91 > currentAngle) && ((targetAngle > 269)));
        telemetry.addData("crossingZeroFromLeft", crossingZeroFromLeft);

        boolean crossingZeroFromRight = ((269 < currentAngle) && ((targetAngle < 91)));
        telemetry.addData("crossingZeroFromRight", crossingZeroFromRight);

        telemetry.update();
        if (crossingZeroFromLeft){
            while (opModeIsActive() && (91 > currentAngle)) {
                telemetry.addData("crossingZeroFromLeft", crossingZeroFromLeft);
                telemetry.update();
                currentAngle = getNormCurrentAngle();
                turnRight(maxSpeed);
            }
            stopMotors();
        }
        else if (crossingZeroFromRight) {
            while (opModeIsActive() && (269 < currentAngle)) {
                telemetry.addData("crossingZeroFromRight", crossingZeroFromRight);
                telemetry.update();
                currentAngle = getNormCurrentAngle();
                turnLeft(maxSpeed);
            }
            stopMotors();
        }
        else if (targetAngle == 0) {
            //if its on right turn left
            //if on left turn right
            telemetry.addData("turning to zero", "turning to zero");
            telemetry.update();
            if (currentAngle < 180) {
                telemetry.addData("turning from left side", "turning from left side");
                telemetry.update();
                delay(1.0);

                turnRightToAngle(0, 0.22, 0.19);
            }
            else if (currentAngle > 180) {
                telemetry.addData("turning from right side", "turning from right side");
                telemetry.update();
                delay(1.0);

                turnLeftToAngle(0, 0.22, 0.19);
            }
        }
        else if (currentAngle < (targetAngle - margin)) {
            getNormCurrentAngle();
            telemetry.addData("runningLeft", "runningLeft");
            telemetry.update();

            turnLeftToAngle(targetAngle, maxSpeed, minSpeed);
        }
        else if (currentAngle > (targetAngle + margin)) {
            getNormCurrentAngle();
            telemetry.addData("runningRight", "runningRight");
            telemetry.update();

            turnRightToAngle(targetAngle, maxSpeed, minSpeed);

        }
    }
}