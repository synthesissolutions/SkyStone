
package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.logging.Level;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "Red3Auto", group = "Linear Opmode")
public class Red3Auto extends aPaprikaAutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        Red3Auto();

        shutdownRobot();
    }

    public void Red3Auto() {
        SkystonePosition skystonePosition = findSkystone("Red");

        if (skystonePosition == SkystonePosition.Wall)
        {
            //Stone on left
            prepRobot();
            telemetry.addData("levelRest", levelRest);
            telemetry.addData("levelCap", levelCap);
            telemetry.update();

            strafeLeft(0.6, 400);
            delay(0.1);

            captureFirstSkyStone();

            intakeFirstSkyStone();
            //LAUNCH

            driveStraightForwardRampDown(0.75, 0.12, 3000, 600);
            turnRightToAngle(186, 0.36, 0.18);
            //ready to move foundation

            driveStraightBack(0.6, 100);
            DownFieldAuto();
            driveStraightForwardRampDown(0.5, 0.12, 1600, 500);

            /*
            driveStraightForward(1.0, 1630);
            motorHorizontalSlide.setPower(0.0);
            takeCurrentAngle();
            driveStraightForward(0.4, 700);
            takeCurrentAngle();
            delay(0.2);
            sSRight(0.8, 720);
            correctAngle(3, 93, 0.22, 0.19);

            intakeSecondSkyStone();

            sSLeft(0.8, 920);
            takeCurrentAngle();
            delay(0.5);
            correctAngle(2, 93, 0.22, 0.19);
            takeCurrentAngle();

            driveStraightBack(1.0, 2000);
            driveStraightBack(0.4, 500);
            horizontalSlide(1.0, 0.1);
            motorVerticalSlide.setTargetPosition(levelRest - 600);
            delay(0.5);
            motorHorizontalSlide.setPower(-1.0);
            timedDriveBackward(0.35, 1.0);

            releaseStone();
            delay(0.2);
            motorHorizontalSlide.setPower(1.0);
            driveStraightForward(0.4, 100);
            delay(0.3);
            stonePosition();

            correctAngle(3, 90, 0.22, 0.19);
            motorHorizontalSlide.setPower(0.0);
            driveStraightForward(1.0, 600);
            driveStraightForward(0.35, 250);*/
        }
        else if (skystonePosition == SkystonePosition.Center)
        {
            //Stone in center
            prepRobot();

            strafeLeft(0.6, 90);
            delay(0.1);

            captureFirstSkyStone();

            intakeFirstSkyStone();
            //LAUNCH

            driveStraightForwardRampDown(0.75, 0.12, 2800, 1000);
            turnRightToAngle(180, 0.34, 0.18);

            driveStraightBack(0.6, 150);
            DownFieldAuto();

            driveStraightForwardRampDown(0.5, 0.12, 1600, 500);

            /*
            //---------
            driveStraightForward(1.0, 1550);
            motorHorizontalSlide.setPower(0.0);
            driveStraightForward(0.4, 600);
            takeCurrentAngle();
            delay(0.2);
            sSRight(0.8, 700);

            intakeSecondSkyStone();

            sSLeft(0.8, 750);
            takeCurrentAngle();
            delay(0.5);
            correctAngle(2, 94, 0.24, 0.21);
            takeCurrentAngle();

            driveStraightBack(1.0, 1800);
            driveStraightBack(0.4, 500);
            horizontalSlide(1.0, 0.1);
            motorVerticalSlide.setTargetPosition(levelRest - 600);
            delay(0.5);
            motorHorizontalSlide.setPower(-1.0);
            timedDriveBackward(0.35, 1.0);

            releaseStone();
            delay(0.2);
            motorHorizontalSlide.setPower(1.0);
            driveStraightForward(0.4, 100);
            delay(0.3);
            stonePosition();

            correctAngle(3, 90, 0.22, 0.19);
            motorHorizontalSlide.setPower(0.0);
            driveStraightForward(1.0, 600);
            driveStraightForward(0.35, 250);*/
        }
        else
        {
            //Stone on right
            prepRobot();

            strafeRight(0.6, 110);
            strafeRight(0.3, 110);
            delay(0.1);

            captureFirstSkyStone();

            intakeFirstSkyStone();
            //LAUNCH

            driveStraightForwardRampDown(0.75, 0.12, 2600, 1000);
            turnRightToAngle(181, 0.36, 0.18);

            driveStraightBack(0.35, 200);
            DownFieldAuto();

            driveStraightForwardRampDown(0.5, 0.12, 1600, 500);
            /*
            driveStraightForward(1.0, 1100);
            motorHorizontalSlide.setPower(0.0);
            // first four attempts at 600
            // 14.06 650 - missed second stone
            // 13.87 600
            driveStraightForward(0.4, 600);
            delay(0.2);
            sSRight(0.8, 700);

            intakeSecondSkyStone();
            // 14.36 got second stone
            // 14.01 got second stone
            // 13.74 missed second stone, straffed farther because we were closer to the center
            // 13.47 got second stone, barely needed to go slightly farther forward, was centered in proper square

            sSLeft(0.8, 750);
            takeCurrentAngle();
            delay(1.0);
            correctAngle(2, 94, 0.24, 0.21);
            takeCurrentAngle();
            delay(1.0);

            driveStraightBack(1.0, 1500);
            driveStraightBack(0.4, 300);
            horizontalSlide(1.0, 0.1);
            motorVerticalSlide.setTargetPosition(levelRest - 600);
            delay(0.5);
            motorHorizontalSlide.setPower(-1.0);
            timedDriveBackward(0.35, 1.0);

            releaseStone();
            delay(0.2);
            motorHorizontalSlide.setPower(1.0);
            driveStraightForward(0.4, 100);
            delay(0.3);
            stonePosition();

            correctAngle(3, 90, 0.22, 0.19);
            motorHorizontalSlide.setPower(0.0);
            driveStraightForward(1.0, 600);
            driveStraightForward(0.35, 200);

            // 14.36 ended in the middle
            // 14.01 ended in the proper square
            // 13.76 hit center post
            */
        }
    }

    public void captureFirstSkyStone() {
        driveStraightForwardRampDown(0.5, 0.12, 900, 300);
        delay(0.2);
        lowerSpat();
        delay(0.17);

        driveStraightBackRampDown(0.5, 0.12, 650, 200);
        driveStraightForward(0.25, 50);
        raiseSpat();
        delay(0.1);

    }
    public void prepRobot() {
        motorVerticalSlide.setTargetPosition(-500);
        delay(0.1);
        extendRestArm();
        delay(0.5);
        stonePosition();
    }
    public void intakeFirstSkyStone() {
        intakeIn();
        spinRight(30, 0.45, 0.15);
        driveStraightForwardRampDown(0.4, 0.12, 400, 200);
        turnRightToAngle(271, 0.33, 0.17);
        gateClose();
        delay(0.2);
        motorVerticalSlide.setTargetPosition(levelRest + 10);
        grabStone();
        intakeOff();
        sSRight(0.6, 200);
    }
    public void intakeSecondSkyStone() {
        gateOpen();
        intakeIn();
        delay(0.5);
        driveStraightForwardRampDown(0.4, 0.12, 200, 100);
        delay(0.5);
        grabStone();
        intakeOut();
        delay(0.2);
        intakeOff();
    }
    private void DownFieldAuto() {
        driveStraightBack(0.35, 170);
        intakeOut();
        bumpLeftB(0.3, 150);
        grabFoundation();
        delay(0.45);
        //foundation captured

        intakeOff();
        strafeRight(0.5, 300);
        driveStraightForward(0.5, 500);
        hardCurveLeftB(0.6, 1300);
        //vertical lift is high enough to just drop stone and come back.. hopefully
        motorVerticalSlide.setTargetPosition(levelRest - 600);
        //(-1.0) power is out, (1.0) power is in
        motorHorizontalSlide.setPower(-1.0);
        timedDriveBackward(0.5, 1.0);
        releaseStone();
        delay(0.1);
        releaseFoundation();
        getNormCurrentAngle();
        delay (0.45);
        if (angles.firstAngle > 110 || angles.firstAngle < 70) {
            shutdownRobot();
            stopMotors();
        }
        else {
            sSLeft(0.6, 300);
            stonePosition();

            timedDriveBackward(0.5, 0.5); // speed, time

        /*
        takeCurrentAngle();
        delay(0.5);
        correctAngle(1, 89, 0.24, 0.21);
        takeCurrentAngle();
        delay(0.5);
        */
            motorHorizontalSlide.setPower(1.0);
        }
    }
}