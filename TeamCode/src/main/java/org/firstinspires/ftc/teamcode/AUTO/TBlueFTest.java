package org.firstinspires.ftc.teamcode.AUTO;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous(name = "TBlue Foundation Test OBJ", group = "Linear Opmode")
public class TBlueFTest extends aTabascoAutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        grabFoundation();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        double leftSpeed = 0.0;
        double rightSpeed = 0.6;
        while (motorFrontRight.getCurrentPosition() < 2500) {
            motorFrontRight.setPower(rightSpeed);
            motorFrontLeft.setPower(leftSpeed);
            motorBackRight.setPower(rightSpeed);
            motorBackLeft.setPower(leftSpeed);
            telemetry.addData("Left Encoder", motorFrontLeft.getCurrentPosition());
            telemetry.addData("Right Encoder", motorFrontRight.getCurrentPosition());
            telemetry.update();
        }

        stopMotors();
        delay(3.0);

        runtime.reset();
        while (runtime.seconds() < 1.5) {
            motorFrontRight.setPower(-rightSpeed);
            motorFrontLeft.setPower(-rightSpeed);
            motorBackRight.setPower(-rightSpeed);
            motorBackLeft.setPower(-rightSpeed);
        }
        stopMotors();
        delay(5.0);
        //shutdownRobot();
    }
}