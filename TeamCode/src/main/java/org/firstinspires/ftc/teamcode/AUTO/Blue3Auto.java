
package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.logging.Level;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "Blue3Auto", group = "Linear Opmode")
public class Blue3Auto extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        turnRightToAngle(182, 0.32, 0.18);

        shutdownRobot();
    }

    public void Blue3Auto() {
        SkystonePosition skystonePosition = findSkystone("Blue");

        if (skystonePosition == SkystonePosition.Wall)
        {
            //Stone on right
            prepRobot();
            telemetry.addData("levelRest", levelRest);
            telemetry.addData("levelCap", levelCap);
            telemetry.update();

            strafeRight(0.8, 1100);
            strafeRight(0.3, 325);
            bumpRightF(0.3, 70);
            delay(0.1);

            captureFirstSkyStone();

            strafeLeft(0.5, 200);
            intakeIn();
            spinRight(45, 0.45, 0.15);
            driveStraightForward(0.4, 400);
            gateClose();
            turnRightToAngle(272, 0.33, 0.17);
            motorVerticalSlide.setTargetPosition(-80);
            grabStone();
            intakeOff();
            takeCurrentAngle();
            sSRight(0.6, 70);
            //LAUNCH

            driveStraightBack(1.0, 3400);
            turnRightToAngle(180, 0.36, 0.18);
            //ready to move foundation

            DownFieldAuto();
        }
        else if (skystonePosition == SkystonePosition.Center)
        {
            //Stone in center
            prepRobot();

            strafeRight(0.6, 900);
            bumpRightF(0.3, 50);
            delay(0.1);

            captureFirstSkyStone();

            intakeIn();
            spinRight(30, 0.45, 0.15);
            driveStraightForward(0.4, 400);
            gateClose();
            turnRightToAngle(272, 0.33, 0.17);
            motorVerticalSlide.setTargetPosition(levelRest + 10);
            grabStone();
            intakeOff();
            takeCurrentAngle();
            sSRight(0.6, 200);
            //LAUNCH

            driveStraightBack(1.0, 3200);
            turnRightToAngle(182, 0.32, 0.18);

            driveStraightBack(0.35, 200);
            DownFieldAuto();
        }
        else
        {
            //Stone on left
            prepRobot();

            strafeRight(0.6, 450);
            bumpRightF(0.3, 50);
            delay(0.1);

            intakeIn();
            captureFirstSkyStone();
            spinRight(45, 0.45, 0.15);
            driveStraightForward(0.4, 400);
            gateClose();
            turnRightToAngle(272, 0.33, 0.17);
            motorVerticalSlide.setTargetPosition(levelRest + 10);
            grabStone();
            intakeOff();
            takeCurrentAngle();
            sSRight(0.6, 200);
            //LAUNCH

            driveStraightBack(1.0, 3000);
            turnRightToAngle(182, 0.36, 0.18);

            driveStraightBack(0.35, 200);
            DownFieldAuto();
        }
    }

    public void captureFirstSkyStone() {
        driveStraightForward(0.5, 585);
        driveStraightForward(0.25, 230);
        delay(0.2);
        lowerSpat();
        delay(0.17);

        driveStraightBack(0.5, 700);
        driveStraightForward(0.25, 50);
        raiseSpat();
        delay(0.1);

    }
    public void prepRobot() {
        motorVerticalSlide.setTargetPosition(-500);
        extendRestArm();
        delay(0.5);
        stonePosition();
    }
    private void DownFieldAuto() {
        driveStraightBack(0.35, 170);
        intakeOut();
        bumpRightB(0.3, 150);
        grabFoundation();
        delay(0.45);
        //foundation captured

        intakeOff();
        strafeLeft(0.5, 300);
        driveStraightForward(0.5, 800);
        hardCurveRightB(0.6, 1300);
        //vertical lift is high enough to just drop stone and come back.. hopefully
        motorVerticalSlide.setTargetPosition(levelRest - 600);
        //(-1.0) power is out, (1.0) power is in
        motorHorizontalSlide.setPower(-1.0);
        timedDriveBackward(0.5, 1.0);
        releaseStone();
        delay(0.1);
        releaseFoundation();
        delay (0.45);

        driveStraightForward(0.3, 100);
        sSRight(0.6, 200);
        stonePosition();
        motorHorizontalSlide.setPower(1.0);
        driveStraightForward(1.0, 700);
        driveStraightForward(0.25, 100);
    }
}