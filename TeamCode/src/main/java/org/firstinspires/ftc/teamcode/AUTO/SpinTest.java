package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "SpinTest", group = "Linear Opmode")
public class SpinTest extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        spinRight(45, 0.3, 0.15);
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        delay(1.0);
        spinRight(45, 0.3, 0.15);
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        delay(1.0);
    }
    public void spinRight(double targetAngle, double maxSpeed, double minSpeed) {
        double currentAngle = angles.firstAngle;
        double startScaling = 0.01;
        double startingAngle = currentAngle;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;

        while (opModeIsActive() && currentAngle > (startingAngle - targetAngle)) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentAngle = angles.firstAngle;
            double percentComplete = (currentAngle - startingAngle) / ((startingAngle - targetAngle) - startingAngle);

            if (percentComplete > startScaling) {
                currentSpeed = (minSpeed + deltaSpeed * (1 - (percentComplete - startScaling) / (1.0 - startScaling)));
            } else {
                currentSpeed = maxSpeed;
            }
            turnRight(currentSpeed);

        }
        stopMotors();
    }
    public void turnRightToAngle (double targetAngle, double maxSpeed, double minSpeed) {
        double currentAngle = normalizeAngle(angles.firstAngle);
        double startScaling = 0.01;
        double startingAngle = currentAngle;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;


        while (opModeIsActive() && currentAngle > targetAngle) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentAngle = normalizeAngle(angles.firstAngle);
            double percentComplete = (currentAngle - startingAngle) / (targetAngle - startingAngle);

            if (percentComplete > startScaling) {
                currentSpeed = (minSpeed + deltaSpeed * (1 - (percentComplete - startScaling) / (1.0 - startScaling)));
            } else {
                currentSpeed = maxSpeed;
            }
            turnRight(currentSpeed);

        }
        stopMotors();
    }
}