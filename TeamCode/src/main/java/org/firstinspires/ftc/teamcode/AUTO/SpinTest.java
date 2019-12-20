package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Disabled
public class SpinTest extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();
        takeCurrentAngle();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        spinRight(10, 0.5, 0.15);
        delay(0.5);

        turnLeftToAngle(13, 0.25, 0.15);
        takeCurrentAngle();
        delay(2.0);

        turnLeftToAngle(60, 0.25, 0.15);
        takeCurrentAngle();
        delay(2.0);



    }
    public void spinRight(double turnAngle, double maxSpeed, double minSpeed) {
        double currentAngle = angles.firstAngle;
        double startScaling = 0.01;
        double startingAngle = currentAngle;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;

        while (opModeIsActive() && currentAngle > (startingAngle - turnAngle)) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentAngle = angles.firstAngle;
            double percentComplete = (currentAngle - startingAngle) / ((startingAngle - turnAngle) - startingAngle);

            if (percentComplete > startScaling) {
                currentSpeed = (minSpeed + deltaSpeed * (1 - (percentComplete - startScaling) / (1.0 - startScaling)));
            } else {
                currentSpeed = maxSpeed;
            }
            turnRight(currentSpeed);

        }currentAngle = angles.firstAngle;
        stopMotors();
    }
    public void spinLeft (double turnAngle, double maxSpeed, double minSpeed) {
        double currentAngle = angles.firstAngle;
        double startScaling = 0.01;
        double startingAngle = currentAngle;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;


        while (opModeIsActive() && currentAngle < (startingAngle + turnAngle)) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            double percentComplete = (currentAngle - startingAngle) / ((startingAngle + turnAngle) - startingAngle);

            if (percentComplete > startScaling) {
                currentSpeed = (minSpeed + deltaSpeed * (1 - (percentComplete - startScaling) / (1.0 - startScaling)));
            } else {
                currentSpeed = maxSpeed;
            }
            takeCurrentAngle();
            turnLeft(currentSpeed);

        }
        stopMotors();
    }
    public void turnRightToAngle (double targetAngle, double maxSpeed, double minSpeed) {
        double currentAngle = getNormCurrentAngle();
        boolean crossingZero = (currentAngle < targetAngle);
        if (crossingZero) {
            while (opModeIsActive() && (currentAngle < 358)) {
                currentAngle = getNormCurrentAngle();
                turnRight(maxSpeed);
            }
        }
        currentAngle = getNormCurrentAngle();
        double startingAngle = currentAngle;
        double startScaling = 0.01;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;

        while (opModeIsActive() && (currentAngle > targetAngle)) {
            currentAngle = getNormCurrentAngle();
            double percentComplete = (currentAngle - startingAngle) / (targetAngle - startingAngle);

            if (percentComplete > startScaling) {
                currentSpeed = (minSpeed + deltaSpeed * (1 - (percentComplete - startScaling) / (1.0 - startScaling)));

            } else {
                currentSpeed = maxSpeed;

            }
            turnRight(currentSpeed);
            takeCurrentAngle();

        }
        stopMotors();
    }
    public void turnLeftToAngle (double targetAngle, double maxSpeed, double minSpeed) {
        double currentAngle = getNormCurrentAngle();
        boolean crossingZero = (currentAngle > targetAngle);
        if (crossingZero) {
            while (opModeIsActive() && currentAngle > 2) {
                currentAngle = getNormCurrentAngle();
                turnLeft(maxSpeed);
            }
        }
        double startScaling = 0.01;
        double startingAngle = currentAngle;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;


        while (opModeIsActive() && currentAngle < targetAngle) {
            currentAngle = getNormCurrentAngle();
            double percentComplete = (currentAngle - startingAngle) / (targetAngle - startingAngle);

            if (percentComplete > startScaling) {
                currentSpeed = (minSpeed + deltaSpeed * (1 - (percentComplete - startScaling) / (1.0 - startScaling)));
                takeCurrentAngle();
            } else {
                currentSpeed = maxSpeed;
                takeCurrentAngle();
            }
            turnLeft(currentSpeed);
            takeCurrentAngle();

        }
        stopMotors();
    }
    public void takeCurrentAngle(){
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", normalizeAngle(angles.firstAngle));
        telemetry.update();
    }
    public double getNormCurrentAngle(){
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", normalizeAngle(angles.firstAngle));
        telemetry.update();
        return normalizeAngle(angles.firstAngle);
    }
}