
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
        skystonePosition = AutoTabasco.SkystonePosition.Wall;

        //int SkyStandIn = 2;
        if (skystonePosition == AutoTabasco.SkystonePosition.Wall)
        {
            //Stone by wall
            tabasco.strafeRight(0.6, 230);
            tabasco.correctAngle(1, 0, 0.22, 0.19);

            captureFirstSkyStone(false);

            intakeFirstSkyStone(false);

            tabasco.sSLeft(0.6, 200);
            tabasco.correctAngle(1, 89, 0.3, 0.2);

            //LAUNCH

            tabasco.driveStraightBackRampDown(1.0, 0.12, 4200, 1600);
            tabasco.turnLeftToAngle(180, 0.6, 0.25);
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
        else if (skystonePosition == AutoTabasco.SkystonePosition.Center)
        {
            //Stone in center
            tabasco.sSLeft(0.6, 40);
            tabasco.correctAngle(1, 0, 0.22, 0.19);

            captureFirstSkyStone(true);

            intakeFirstSkyStone(true);

            tabasco.sSLeft(0.6, 300);
            tabasco.correctAngle(1, 89, 0.3, 0.2);

            //LAUNCH

            tabasco.driveStraightBackRampDown(1.0, 0.12, 4000, 1600);
            tabasco.turnLeftToAngle(180, 0.6, 0.25);

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
            tabasco.strafeLeft(0.6, 150);
            tabasco.correctAngle(5, 0, 0.22, 0.19);

            captureFirstSkyStone(false);

            intakeFirstSkyStone(false);

            tabasco.sSLeft(0.4, 300);
            //takeCurrentAngle();
            tabasco.correctAngle(1, 89, 0.22, 0.19);

            //LAUNCH

            tabasco.driveStraightBackRampDown(1.0, 0.12, 4300, 1600);
            tabasco.turnLeftToAngle(179, 0.6, 0.25);


            tabasco.driveStraightBack(0.35, 100);
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
            tabasco.driveStraightForwardRampDown(0.7, 0.12, 1100, 500);
            tabasco.lowerSpatR();
            tabasco.delay(0.4);

            tabasco.driveStraightBackRampDown(0.7, 0.15, 600, 300);
            tabasco.driveStraightForward(0.25, 50);
            tabasco.ricePattyR();
            tabasco.delay(0.1);
        }
        else {
            tabasco.driveStraightForwardRampDown(0.7, 0.12, 1050, 500);
            tabasco.lowerSpatL();
            tabasco.delay(0.4);

            tabasco.driveStraightBackRampDown(0.7, 0.15, 600, 400);
            tabasco.driveStraightForward(0.3, 50);
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
            tabasco.spinLeft(20, 0.5, 0.2);
            tabasco.driveStraightForwardRampDown(0.4, 0.12, 400, 200);
            tabasco.gateClose();
            tabasco.turnLeftToAngle(90, 0.4, 0.18);
        }
        tabasco.gateOpen();
        tabasco.delay(0.15);
        tabasco.gateClose();
        tabasco.intakeOff();
    }
    public void park() {
        tabasco.driveStraightForwardRampDown(0.75, 0.12, 1700, 700);
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
        tabasco.verticalSlide(1400);
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

        tabasco.sSRight(0.6, 300);
        tabasco.verticalSlide(0);
        tabasco.horizontalSlideIn(1.5);

        tabasco.correctAngle (3, 89.5, 0.3, 0.2);

        return true;
    }
}