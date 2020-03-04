
package org.firstinspires.ftc.teamcode.AUTO;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.logging.Level;

@Autonomous(name = "TRed3Auto", group = "Linear Opmode")
//@Disabled
public class TRed3Auto extends aTabascoAutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        prepRobot();

        Red3Auto();

        shutdownRobot();
    }

    public void Red3Auto() {
        SkystonePosition skystonePosition = findSkystone("Red");
        skystonePosition = SkystonePosition.Wall;

        //int SkyStandIn = 2;
        if (skystonePosition == SkystonePosition.Wall)
        {
            //Stone by wall
            strafeRight(0.6, 230);
            correctAngle(1, 0, 0.22, 0.19);

            captureFirstSkyStone(false);

            intakeFirstSkyStone(false);

            sSLeft(0.6, 200);
            correctAngle(1, 89, 0.3, 0.2);

            //LAUNCH

            driveStraightBackRampDown(1.0, 0.12, 4200, 1600);
            turnLeftToAngle(180, 0.6, 0.25);
            //ready to move foundation

            if (DownFieldAuto()) {
                park();
            /*
            driveStraightForward(1.0, 1800);
            driveStraightForward(0.35, 500);
            motorHorizontalSlide.setPower(0.0);
            sSLeft(0.7, 770);

            gateOpen();
            intakeIn();
            delay(0.5);
            driveStraightForward(0.4, 200);
            delay(0.5);
            grabStone();
            intakeOut();
            delay(0.2);
            intakeOff();


            strafeRight(0.7, 800);
            correctAngle(2, 270, 0.22, 0.19);
            gateClose();


            driveStraightBack(1.0, 1200);
            driveStraightBack(0.5, 800);
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

            correctAngle(3, 270, 0.22, 0.19);
            motorHorizontalSlide.setPower(0.0);
            driveStraightForward(1.0, 500);
            driveStraightForward(0.35, 250);
            */
            }
        }
        else if (skystonePosition == SkystonePosition.Center)
        {
            //Stone in center
            sSLeft(0.6, 40);
            correctAngle(1, 0, 0.22, 0.19);

            captureFirstSkyStone(true);

            intakeFirstSkyStone(true);

            sSLeft(0.6, 300);
            correctAngle(1, 89, 0.3, 0.2);

            //LAUNCH

            driveStraightBackRampDown(1.0, 0.12, 4000, 1600);
            turnLeftToAngle(180, 0.6, 0.25);

            if (DownFieldAuto()) {
                park();
            /*driveStraightForward(1.0, 100);

            driveStraightForward(1.0, 600);
            driveStraightForward(0.35, 400);
            motorHorizontalSlide.setPower(0.0);
            sSLeft(0.7, 550);

            gateOpen();
            intakeIn();
            delay(0.5);
            driveStraightForward(0.4, 200);
            delay(0.5);
            grabStone();
            intakeOut();
            delay(0.2);
            intakeOff();


            strafeRight(0.7, 600);
            correctAngle(2, 270, 0.22, 0.19);
            gateClose();

            driveStraightBack(1.0, 1000);
            driveStraightBack(0.5, 700);
            motorVerticalSlide.setTargetPosition(levelRest - 800);
            delay(0.5);
            motorHorizontalSlide.setPower(-1.0);
            timedDriveBackward(0.35, 1.0);

            releaseStone();
            delay(0.2);
            motorHorizontalSlide.setPower(1.0);
            driveStraightForward(0.4, 100);
            delay(0.3);
            stonePosition();

            correctAngle(2, 270, 0.22, 0.19);
            driveStraightForward(1.0, 700);
            driveStraightForward(0.35, 100);
            */
            }
        }
        else
        {
            //Stone on left
            strafeLeft(0.6, 150);
            correctAngle(5, 0, 0.22, 0.19);

            captureFirstSkyStone(false);

            intakeFirstSkyStone(false);

            sSLeft(0.4, 300);
            //takeCurrentAngle();
            correctAngle(1, 89, 0.22, 0.19);

            //LAUNCH

            driveStraightBackRampDown(1.0, 0.12, 4300, 1600);
            turnLeftToAngle(179, 0.6, 0.25);


            driveStraightBack(0.35, 100);
            if (DownFieldAuto()) {
                park();
            /*
            driveStraightForward(1.0, 900);
            driveStraightForward(0.5, 900);
            spinLeft(29, 0.33, 0.18);
            motorHorizontalSlide.setPower(0.0);

            releaseStone();
            gateOpen();
            intakeIn();
            delay(0.3);
            driveStraightForward(0.4, 600);
            delay(0.5);
            grabStone();
            intakeOut();
            delay(0.2);
            intakeOff();

            turnRightToAngle(270, 0.3, 0.18);
            strafeRight(0.7, 900);
            correctAngle(2, 268, 0.22, 0.19);
            gateClose();

            driveStraightBack(1.0, 1000);
            driveStraightBack(0.5, 500);

            motorVerticalSlide.setTargetPosition(levelRest - 800);
            delay(0.5);
            intakeOut();
            delay(0.1);
            intakeOff();
            motorHorizontalSlide.setPower(-1.0);
            timedDriveBackward(0.35, 1.0);

            releaseStone();
            delay(0.2);
            motorHorizontalSlide.setPower(1.0);
            driveStraightForward(0.4, 100);
            delay(0.3);
            stonePosition();

            correctAngle(2, 268, 0.22, 0.19);
            driveStraightForward(1.0, 600);
            motorHorizontalSlide.setPower(0.0);
            driveStraightForward(0.35, 100);
            */
            }
        }
    }

    public void captureFirstSkyStone(boolean onRight) {
        if (onRight == true) {
            driveStraightForwardRampDown(0.7, 0.12, 1100, 500);
            lowerSpatR();
            delay(0.4);

            driveStraightBackRampDown(0.7, 0.15, 600, 300);
            driveStraightForward(0.25, 50);
            ricePattyR();
            delay(0.1);
        }
        else {
            driveStraightForwardRampDown(0.7, 0.12, 1050, 500);
            lowerSpatL();
            delay(0.4);

            driveStraightBackRampDown(0.7, 0.15, 600, 400);
            driveStraightForward(0.3, 50);
            ricePattyL();
            delay(0.1);
        }

    }
    public void intakeFirstSkyStone(boolean onRight) {
        releaseStone();
        intakeIn();
        if (onRight == true) {
            spinRight(20, 0.5, 0.2);
            driveStraightForwardRampDown(0.4, 0.12, 400, 200);
            // For now we need to double-tap the block to get it to intake properly
            // otherwise it just gets jammed on the first attempt.
            gateClose();
            turnLeftToAngle(90, 0.5, 0.2);
        }
        else {
            spinLeft(20, 0.5, 0.2);
            driveStraightForwardRampDown(0.4, 0.12, 400, 200);
            gateClose();
            turnLeftToAngle(90, 0.4, 0.18);
        }
        gateOpen();
        delay(0.15);
        gateClose();
        intakeOff();
    }
    public void park() {
        driveStraightForwardRampDown(0.75, 0.12, 1700, 700);
    }
    public boolean DownFieldAuto() {
        grabStone();
        driveStraightBack(0.4, 250);
        bumpLeftB(0.3, 210);
        grabFoundation();
        intakeOut();
        delay(0.5);
        //foundation captured

        intakeOff();
        verticalSlide(1400);
        bumpRightFToAngle(88, 2, 0.8);

        //driveStraightForward(0.5, 100)
        correctAngle(3, 88, 0.75, 0.2);

        getNormCurrentAngle();
        if (normalizeAngle(angles.firstAngle) < 70 || normalizeAngle(angles.firstAngle) > 110) {
            return false;
            //checks to make sure we're lined up, if we're not it stops the program
        }
        servoHorizontalSlide.setPosition(0.0);
        timedDriveBackward(0.5, 1.5);
        servoHorizontalSlide.setPosition(0.5);

        releaseStone();
        releaseFoundation();
        delay (0.45);

        sSRight(0.6, 300);
        verticalSlide(0);
        horizontalSlideIn(1.5);

        correctAngle (3, 89.5, 0.3, 0.2);

        return true;
    }
}