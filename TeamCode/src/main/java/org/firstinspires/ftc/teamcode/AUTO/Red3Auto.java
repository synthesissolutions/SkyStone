
package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import java.util.logging.Level;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Disabled
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
            if (DownFieldAuto()) {

                //(1600, 500) for bridge, (4000, 2000) for 2nd skystone
                driveStraightForwardRampDown(1.0, 0.2, 4000, 2000);

                takeCurrentAngle();
                sSRight(0.8, 720);
                //all 92s-94s should be 90s but our field is tilted
                correctAngle(3, 93, 0.22, 0.19);

                intakeSecondSkyStone();

                sSLeft(0.8, 920);
                takeCurrentAngle();
                delay(0.2);
                correctAngle(2, 92, 0.22, 0.19);

                driveStraightBackRampDown(1.0, 0.35, 3000, 1000);
                motorVerticalSlide.setTargetPosition(levelRest - 600);
                delay(0.5);
                horizontalSlide(1.0);
                timedDriveBackward(0.35, 1.0);

                releaseStone();
                delay(0.2);
                horizontalSlide(-1.0);
                driveStraightForward(0.4, 100);
                delay(0.3);
                stonePosition();

                correctAngle(3, 94, 0.22, 0.19);
                horizontalSlide(0.0);
                driveStraightForwardRampDown(0.75, 0.12, 1300, 500);
            }
        }
        else if (skystonePosition == SkystonePosition.Center)
        {
            //Stone in center
            strafeLeft(0.6, 90);
            delay(0.1);

            captureFirstSkyStone();

            intakeFirstSkyStone();
            //LAUNCH

            driveStraightForwardRampDown(1.0, 0.12, 2800, 1000);
            turnRightToAngle(180, 0.34, 0.18);

            driveStraightBack(0.6, 150);
            if (DownFieldAuto()){

                //(1600, 500) for bridge 3600, 1800 for 2nd stone
                driveStraightForwardRampDown(1.0, 0.12, 3600, 1800);

                //---------center
                takeCurrentAngle();
                sSRight(0.8, 700);
                correctAngle(2, 94, 0.22, 0.19);

                intakeSecondSkyStone();

                sSLeft(0.8, 800);
                takeCurrentAngle();
                delay(0.5);
                correctAngle(2, 94, 0.22, 0.19);
                takeCurrentAngle();

                driveStraightBackRampDown(1.0, 0.12, 2300, 800);
                motorVerticalSlide.setTargetPosition(levelRest - 600);
                delay(0.5);
                horizontalSlide(1.0);
                timedDriveBackward(0.35, 1.0);

                releaseStone();
                delay(0.2);
                horizontalSlide(-1.0);
                driveStraightForward(0.4, 100);
                delay(0.3);
                stonePosition();

                correctAngle(3, 94, 0.22, 0.19);
                horizontalSlide(0.0);
                driveStraightForwardRampDown(0.75, 0.12, 1300, 500);

            }
        }
        else {

            //Stone on right
            strafeRight(0.6, 110);
            strafeRight(0.3, 130);
            delay(0.1);

            captureFirstSkyStone();

            intakeFirstSkyStone();
            //LAUNCH

            driveStraightForwardRampDown(0.75, 0.12, 2600, 1000);
            turnRightToAngle(181, 0.36, 0.18);

            driveStraightBack(0.35, 200);
            DownFieldAuto();

            //(1600, 500) for bridge, (3200, 1500) for 2nd stone);
            driveStraightForwardRampDown(1.0, 0.12, 3200, 1500);

            //-------------------
            sSRight(0.8, 720);
            correctAngle(2, 93, 0.22, 0.19);

            intakeSecondSkyStone();

            sSLeft(0.8, 850);
            takeCurrentAngle();
            delay(0.2);
            correctAngle(2, 94, 0.22, 0.19);

            driveStraightBackRampDown(1.0, 0.12, 2500, 700);
            motorVerticalSlide.setTargetPosition(levelRest - 600);
            delay(0.35);
            horizontalSlide(1.0);
            timedDriveBackward(0.35, 1.0);

            releaseStone();
            delay(0.2);
            horizontalSlide(-1.0);
            driveStraightForward(0.4, 100);
            delay(0.3);
            stonePosition();

            correctAngle(3, 94, 0.22, 0.19);
            horizontalSlide(0.0);
            driveStraightForwardRampDown(0.75, 0.12, 1300, 500);
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
        getNormCurrentAngle();
        //if (angles.firstAngle > 290 || angles.firstAngle < 250) {
        //  return false;
        //}

        ///return true;
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
    public boolean DownFieldAuto() {
        driveStraightBack(0.35, 170);
        intakeOut();
        bumpLeftB(0.3, 150);
        grabFoundation();
        delay(0.45);
        //foundation captured

        intakeOff();
        strafeRight(0.5, 300);
        takeCurrentAngle();
        getNormCurrentAngle();
        if (angles.firstAngle > 190 || angles.firstAngle < 150) {
            return false;
        }
        //vertical lift is high enough to just drop stone and come back.. hopefully
        motorVerticalSlide.setTargetPosition(levelRest - 600);
        //(-1.0) power is out, (1.0) power is in
        horizontalSlide(1.0);
        driveStraightForward(0.5, 600);
        horizontalSlide(0.0);
        releaseStone();
        hardCurveLeftB(0.6, 1300);
        horizontalSlide(-1.0);
        releaseFoundation();
        getNormCurrentAngle();
        if (angles.firstAngle > 110 || angles.firstAngle < 70) {
            return false;
        }
        timedDriveBackward(0.5, 0.5); // speed, time
        sSLeft(0.6, 300);
        horizontalSlide(0.0);
        stonePosition();
        timedDriveBackward(0.5, 0.4);

        /*
        takeCurrentAngle();
        delay(0.5);
        correctAngle(1, 89, 0.24, 0.21);
        takeCurrentAngle();
        delay(0.5);
        */
        return true;
    }
}