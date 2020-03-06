
package org.firstinspires.ftc.teamcode.AUTO;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "TRed3Auto", group = "Linear Opmode")
@Disabled
public class TRed3Auto extends LinearOpMode {

    AutoTabasco tabasco = new AutoTabasco();

    @Override
    public void runOpMode() throws InterruptedException {

        tabasco.initializeRobotA(hardwareMap, this);
        tabasco.angles = tabasco.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", tabasco.angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        tabasco.prepRobot();

        Red3Auto();

        tabasco.shutdownRobot();
    }

    public void Red3Auto() {
        AutoTabasco.SkystonePosition skystonePosition = tabasco.findSkystone("Red");

        //telemetry.addData("Position", skystonePosition);
        //telemetry.update();

        //tabasco.delay(3.0);
        skystonePosition = AutoTabasco.SkystonePosition.AwayFromWall;

        if (skystonePosition == AutoTabasco.SkystonePosition.Wall)
        {
            //Stone by wall
            tabasco.strafeRight(0.6, 150);
            tabasco.correctAngle(1, 0, 0.22, 0.19);

            captureFirstSkyStone(false);

            intakeFirstSkyStone(false);

            tabasco.sSLeft(0.6, 50);
            tabasco.correctAngle(1, 91, 0.3, 0.2);

            //LAUNCH

            tabasco.driveStraightBackRampDown(1.0, 0.12, 4125, 1600);
            tabasco.turnLeftToAngle(180, 0.6, 0.25);
            //ready to move foundation

            if (DownFieldAuto()) {
                park();
                /*/
                tabasco.driveStraightForwardRampDown(1.0, 0.12, 3000, 1500);
                tabasco.spinRight(30, 0.3, 0.15);

                intakeSecondSkyStone();

                tabasco.sSLeft(0.6, 500);
                tabasco.correctAngle(1, 89, 0.22, 0.19);

                tabasco.driveStraightBackRampDown(1.0, 0.5, 3000, 1000);

                downField2();
                park();
                tabasco.horizontalSlideStop();
                /*/
            }
            //
        }
        else if (skystonePosition == AutoTabasco.SkystonePosition.Center)
        {
            //Stone in center
            tabasco.strafeLeft(0.6, 650);
            tabasco.correctAngle(1, 0, 0.22, 0.19);

            captureFirstSkyStone(false);

            intakeFirstSkyStone(false);

            tabasco.sSLeft(0.6, 50);
            tabasco.correctAngle(1, 91, 0.3, 0.2);

            //LAUNCH

            tabasco.driveStraightBackRampDown(1.0, 0.12, 4800, 1600);
            tabasco.turnLeftToAngle(180, 0.6, 0.25);

            if (DownFieldAuto()) {
                park();
                /*/
                tabasco.driveStraightForwardRampDown(1.0, 0.12, 3000, 1500);
                tabasco.spinRight(30, 0.3, 0.15);

                intakeSecondSkyStone();

                tabasco.sSLeft(0.6, 500);
                tabasco.correctAngle(1, 89, 0.22, 0.19);

                tabasco.driveStraightBackRampDown(1.0, 0.5, 3000, 1000);

                downField2();
                park();
                tabasco.horizontalSlideStop();
                /*/
            }
            //
        }
        else
        {
            //Stone Away from Wall
            tabasco.strafeLeft(0.6, 300);
            tabasco.correctAngle(1, 0, 0.22, 0.19);

            captureFirstSkyStone(false);

            intakeFirstSkyStone(false);

            tabasco.sSLeft(0.4, 50);

            tabasco.correctAngle(1, 91, 0.3, 0.2);

            /*/LAUNCH

            tabasco.driveStraightBackRampDown(1.0, 0.12, 4450, 1600);
            tabasco.turnLeftToAngle(180, 0.6, 0.25);


            tabasco.driveStraightBack(0.35, 100);
            if (DownFieldAuto()) {
                park();
                //
                tabasco.driveStraightForwardRampDown(1.0, 0.12, 3000, 1500);
                tabasco.spinRight(30, 0.3, 0.15);

                intakeSecondSkyStone();

                tabasco.sSLeft(0.6, 500);
                tabasco.correctAngle(1, 89, 0.22, 0.19);

                tabasco.driveStraightBackRampDown(1.0, 0.5, 3000, 1000);

                downField2();
                park();
                tabasco.horizontalSlideStop();
                //
            }
            /*/
        }
    }

    public void captureFirstSkyStone(boolean onRight) {
        if (onRight == true) {
            // Don't Use
            tabasco.driveStraightForwardRampDown(0.7, 0.12, 1100, 500);
            tabasco.lowerSpatR();
            tabasco.delay(0.4);

            tabasco.driveStraightBackRampDown(0.7, 0.15, 600, 300);
            tabasco.driveStraightForward(0.25, 50);
            tabasco.ricePattyR();
            tabasco.delay(0.1);
        }
        else {
            tabasco.driveStraightForwardRampDown(0.6, 0.15, 1200, 600);
            tabasco.lowerSpatL();
            tabasco.delay(0.7);

            tabasco.driveStraightBackRampDown(0.6, 0.18, 600, 400);
            //tabasco.driveStraightForward(0.3, 50);
            tabasco.ricePattyL();
            tabasco.delay(0.1);
        }

    }
    public void intakeFirstSkyStone(boolean onRight) {
        tabasco.releaseStone();
        tabasco.intakeIn();
        if (onRight == true) {
            tabasco.spinRight(20, 0.5, 0.2);
            tabasco.driveStraightForwardRampDown(0.4, 0.12, 400, 200);
            // For now we need to double-tap the block to get it to intake properly
            // otherwise it just gets jammed on the first attempt.
            tabasco.gateClose();
            tabasco.turnLeftToAngle(90, 0.5, 0.2);
        }
        else {
            // sometimes 20 works, sometimes 15
            // battery related?
            tabasco.spinLeft(15, 0.5, 0.2);
            tabasco.driveStraightForwardRampDown(0.4, 0.12, 400, 200);
            tabasco.gateClose();
            tabasco.turnLeftToAngle(90, 0.4, 0.18);
        }
        tabasco.gateOpen();
        tabasco.delay(0.35);
        tabasco.gateClose();
        tabasco.intakeOff();
    }
    public void intakeSecondSkyStone() {
        tabasco.intakeIn();
        tabasco.releaseStone();
        tabasco.gateOpen();

        tabasco.driveStraightForward(0.4, 500);
        tabasco.intakeOut();
        tabasco.gateClose();
        tabasco.delay(0.15);

        tabasco.turnLeftToAngle(89, 0.5, 0.19);
        tabasco.gateOpen();
        tabasco.delay(0.15);
        tabasco.gateClose();
    }
    public void park() {
        tabasco.driveStraightForwardRampDown(0.75, 0.2, 1700, 800);
    }
    public boolean DownFieldAuto() {
        tabasco.grabStone();
        tabasco.driveStraightBack(0.4, 250);
        tabasco.bumpLeftB(0.3, 210);
        tabasco.grabFoundation();
        tabasco.intakeOut();
        tabasco.delay(0.5);
        //foundation captured

        tabasco.intakeOff();
        tabasco.motorVerticalSlide.setTargetPosition(-1500);
        tabasco.bumpRightFToAngle(88, 2, 0.8);

        //driveStraightForward(0.5, 100)
        tabasco.correctAngle(3, 88, 0.75, 0.2);

        tabasco.getNormCurrentAngle();
        if (tabasco.normalizeAngle(tabasco.angles.firstAngle) < 70 || tabasco.normalizeAngle(tabasco.angles.firstAngle) > 110) {
            return false;
            //checks to make sure we're lined up, if we're not it stops the program
        }
        tabasco.servoHorizontalSlide.setPosition(0.0);
        tabasco.timedDriveBackward(0.5, 1.5);
        tabasco.servoHorizontalSlide.setPosition(0.5);

        tabasco.releaseStone();
        tabasco.releaseFoundation();
        tabasco.delay (0.45);

        tabasco.sSRight(0.6, 500);
        tabasco.motorVerticalSlide.setTargetPosition(0);
        tabasco.horizontalSlideIn(1.5);

        tabasco.correctAngle (3, 89.5, 0.3, 0.2);

        return true;
    }
    public void downField2() {
        tabasco.verticalSlide(-1500);
        tabasco.servoHorizontalSlide.setPosition(0.0);
        tabasco.timedDriveBackward(0.5, 1.0);

        tabasco.releaseStone();
        tabasco.delay(0.45);

        tabasco.servoHorizontalSlide.setPosition(1.0);
    }
}