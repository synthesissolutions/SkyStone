package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "Move Foundation Test", group = "Linear Opmode")
public class MoveFoundationTest extends AutoBase {

    double currentPullSpeed = 0.25;
    double pullSpeedStepSize = 0.05;
    int currentEncoderDistance = 500;
    int encoderDistanceStepSize = 50;

    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime buttonTime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            telemetry.addData("Pull Speed", "" + currentPullSpeed);
            telemetry.addData("Encoder Distance", "" + currentEncoderDistance);
            telemetry.update();

            if (buttonTime.seconds() > 0.5) {
                if (gamepad1.dpad_up) {
                    currentPullSpeed += pullSpeedStepSize;
                    buttonTime.reset();
                } else if (gamepad1.dpad_down) {
                    currentPullSpeed -= pullSpeedStepSize;
                    buttonTime.reset();
                } else if (gamepad1.dpad_left) {
                    currentEncoderDistance -= encoderDistanceStepSize;
                    buttonTime.reset();
                } else if (gamepad1.dpad_right) {
                    currentEncoderDistance += encoderDistanceStepSize;
                    buttonTime.reset();
                }
            }

            if (currentPullSpeed > 1.0) {
                currentPullSpeed = 1.0;
            } else if (currentPullSpeed <= 0) {
                currentPullSpeed = 0.05;
            }

            if (currentEncoderDistance <= 0) {
                currentEncoderDistance = 50;
            }

            if (gamepad1.a) {
                grabFoundation();
                delay(1.0);
                driveStraightForward(currentPullSpeed, currentEncoderDistance);
            } else if (gamepad1.b) {
                grabFoundation();
                delay(1.0);
                driveStraightBack(currentPullSpeed, currentEncoderDistance);
            }
        }
    }
}
