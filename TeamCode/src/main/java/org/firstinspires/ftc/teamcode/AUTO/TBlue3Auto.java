
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

        tabasco.initializeRobot(hardwareMap);
        tabasco.angles = tabasco.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", tabasco.angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        tabasco.prepRobot();
        //delay(2.0);

        Blue3Auto();

        tabasco.shutdownRobot();
    }

    public void Blue3Auto() {
        AutoTabasco.SkystonePosition skystonePosition = tabasco.findSkystone("Blue");

        if (skystonePosition == AutoTabasco.SkystonePosition.Wall)
        {
            //Stone by wall
            tabasco.sSRight(0.6, 150);
            //bumpRightF(0.3, 50);
            tabasco.delay(0.1);

            captureFirstSkyStone(true);

            intakeFirstSkyStone(true);

            //sSRight(0.6, 100);
            //takeCurrentAngle();
            tabasco.correctAngle(1, 270, 0.3, 0.2);
            tabasco.takeCurrentAngle();
            tabasco.delay(0.2);
            //LAUNCH

            tabasco.driveStraightBackRampDown(1.0, 0.12, 3700, 1550);
            tabasco.turnRightToAngle(190, 0.6, 0.25);
            //ready to move foundation

            if (DownFieldAuto()) {
                park();
            }
        }
        else if (skystonePosition == AutoTabasco.SkystonePosition.Center)
        {
            //Stone in center
            tabasco.strafeRight(0.6, 840);
            //bumpRightF(0.3, 50);
            tabasco.delay(0.1);

            captureFirstSkyStone(true);

            intakeFirstSkyStone(true);

            //sSRight(0.6, 100);
            //takeCurrentAngle();
            tabasco.sSRight(0.6, 50);
            //should be 270 but turns to 269 to account for weirdness
            tabasco.correctAngle(1, 269, 0.3, 0.2);
            tabasco.takeCurrentAngle();
            tabasco.delay(0.2);
            //LAUNCH

            tabasco.driveStraightBackRampDown(1.0, 0.12, 4600, 1600);
            tabasco.turnRightToAngle(185, 0.6, 0.25);

            if (DownFieldAuto()) {
                park();
            }
        }
        else
        {
            //Stone on left
            /*
            sSRight(0.6, 500);
            turnLeftToAngle(0, 0.22, 0.18);
            //bumpRightF(0.3, 50);
            delay(0.1);
            */

            captureFirstSkyStone(false);

            intakeFirstSkyStone(false);

            //sSRight(0.4, 100);
            //takeCurrentAngle();
            tabasco.correctAngle(1, 270, 0.22, 0.19);

            //LAUNCH

            tabasco.driveStraightBackRampDown(1.0, 0.12, 3300, 1600);
            tabasco.turnRightToAngle(180, 0.6, 0.25);


            tabasco.driveStraightBack(0.4, 100);
            if (DownFieldAuto()) {
                park();
            }
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
            tabasco.driveStraightForwardRampDown(0.7, 0.12, 1050, 400);
            tabasco.lowerSpatL();
            tabasco.delay(0.4);

            tabasco.driveStraightBackRampDown(0.5, 0.12, 600, 300);
            tabasco.driveStraightForward(0.25, 50);
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
            tabasco.turnLeftToAngle(25, 0.5, 0.15);
            tabasco.driveStraightForwardRampDown(0.4, 0.12, 400, 200);
            tabasco.gateClose();
            tabasco.turnRightToAngle(270, 0.6, 0.2);
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