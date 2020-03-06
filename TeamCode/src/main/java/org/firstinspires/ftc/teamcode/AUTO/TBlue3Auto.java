
package org.firstinspires.ftc.teamcode.AUTO;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "TBlue3Auto", group = "Linear Opmode")
@Disabled
public class TBlue3Auto extends LinearOpMode {

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

        Blue3Auto();

        tabasco.shutdownRobot();
    }

    public void Blue3Auto() {
        AutoTabasco.SkystonePosition skystonePosition = tabasco.findSkystone("Blue");

        skystonePosition = AutoTabasco.SkystonePosition.AwayFromWall;

        if (skystonePosition == AutoTabasco.SkystonePosition.Wall)
        {
            //Stone by wall

            tabasco.strafeRight(0.6, 525);
            tabasco.correctAngle(1, 0, 0.22, 0.19);

            captureFirstSkyStone(false);

            intakeFirstSkyStone(false);

            //sSRight(0.6, 100);
            tabasco.correctAngle(1, 269, 0.3, 0.2);

            /*/LAUNCH

            tabasco.driveStraightBackRampDown(1.0, 0.12, 3700, 1550);
            tabasco.turnRightToAngle(190, 0.6, 0.25);
            //ready to move foundation

            if (DownFieldAuto()) {
                park();
            }
            /*/
        }
        else if (skystonePosition == AutoTabasco.SkystonePosition.Center)
        {
            //Stone in center
            tabasco.strafeRight(0.6, 150);
            tabasco.correctAngle(1, 0, 0.22, 0.19);

            captureFirstSkyStone(false);

            intakeFirstSkyStone(false);

            //sSRight(0.6, 100);
            tabasco.correctAngle(1, 269, 0.3, 0.2);

            /*/LAUNCH

            tabasco.driveStraightBackRampDown(1.0, 0.12, 4600, 1600);
            tabasco.turnRightToAngle(185, 0.6, 0.25);

            if (DownFieldAuto()) {
                park();
            }
            /*/
        }
        else
        {
            //Stone on left
            tabasco.strafeRight(0.6, 475);
            tabasco.correctAngle(1, 0, 0.22, 0.19);

            captureFirstSkyStone(false);

            intakeFirstSkyStone(false);

            //sSRight(0.6, 100);
            tabasco.correctAngle(1, 269, 0.3, 0.2);

            /*/LAUNCH

            tabasco.driveStraightBackRampDown(1.0, 0.12, 3300, 1600);
            tabasco.turnRightToAngle(180, 0.6, 0.25);


            tabasco.driveStraightBack(0.4, 100);
            if (DownFieldAuto()) {
                park();
            }
            /*/
        }
    }

    public void captureFirstSkyStone(boolean onRight) {
        if (onRight == true) {
            tabasco.driveStraightForwardRampDown(0.7, 0.12, 1150, 400);
            tabasco.lowerSpatR();
            tabasco.delay(0.4);

            tabasco.driveStraightBackRampDown(0.5, 0.12, 600, 300);
            tabasco.driveStraightForward(0.25, 50);
            tabasco.ricePattyR();
            tabasco.delay(0.1);
        }
        else {
            tabasco.driveStraightForwardRampDown(0.6, 0.15, 1200, 600);
            tabasco.lowerSpatL();
            tabasco.delay(0.7);

            tabasco.driveStraightBackRampDown(0.6, 0.18, 600, 400);
            //tabasco.driveStraightForward(0.25, 50);
            tabasco.ricePattyL();
            tabasco.delay(0.1);
        }

    }
    public void intakeFirstSkyStone(boolean onRight) {
        tabasco.releaseStone();
        tabasco.intakeIn();
        if (onRight == true) {
            tabasco.turnRightToAngle(330, 0.5, 0.15);
            tabasco.driveStraightForwardRampDown(0.4, 0.12, 400, 200);
            // For now we need to double-tap the block to get it to intake properly
            // otherwise it just gets jammed on the first attempt.
            tabasco.gateClose();
            tabasco.turnRightToAngle(270, 0.4, 0.2);
        }
        else {
            tabasco.turnLeftToAngle(5, 0.5, 0.2);
            tabasco.driveStraightForwardRampDown(0.4, 0.12, 400, 200);
            tabasco.gateClose();
            tabasco.turnRightToAngle(270, 0.6, 0.2);
        }
        // not sure if we need to do the following or not
        // but the intake was jamming every time for a while
        tabasco.gateOpen();
        tabasco.intakeOff();
        tabasco.delay(0.1);
        tabasco.intakeIn();
        tabasco.delay(0.25);
        tabasco.gateClose();
        tabasco.intakeOff();
    }
    public void park() {
        tabasco.driveStraightForwardRampDown(0.75, 0.12, 1700, 700);
    }
    public boolean DownFieldAuto() {
        tabasco.grabStone();
        tabasco.driveStraightBack(0.4, 250);
        tabasco.bumpRightB(0.3, 210);
        tabasco.grabFoundation();
        tabasco.intakeOut();
        tabasco.delay(0.5);
        //foundation captured

        tabasco.intakeOff();
        tabasco.motorVerticalSlide.setTargetPosition(-1500);
        tabasco.bumpLeftFToAngle(270, 2, 0.8);

        //driveStraightForward(0.5, 100)
        tabasco.correctAngle(3, 270, 0.75, 0.2);

        tabasco.getNormCurrentAngle();
        if (tabasco.normalizeAngle(tabasco.angles.firstAngle) < 250 || tabasco.normalizeAngle(tabasco.angles.firstAngle) > 290) {
            return false;
        }
        tabasco.servoHorizontalSlide.setPosition(0.0);
        tabasco.timedDriveBackward(0.5, 1.0);
        tabasco.servoHorizontalSlide.setPosition(0.5);

        tabasco.releaseStone();
        tabasco.releaseFoundation();
        tabasco.delay (0.45);

        tabasco.sSLeft(0.6, 300);
        tabasco.motorVerticalSlide.setTargetPosition(0);
        tabasco.horizontalSlideIn(1.2);

        tabasco.correctAngle (3, 270, 0.3, 0.2);

        return true;
    }
}