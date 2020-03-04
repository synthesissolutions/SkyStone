
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

@Autonomous(name = "TBlue3Auto", group = "Linear Opmode")
//@Disabled
public class TBlue3Auto extends aTabascoAutoBase {

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
        //delay(2.0);

        Blue3Auto();

        shutdownRobot();
    }

    public void Blue3Auto() {
        SkystonePosition skystonePosition = findSkystone("Blue");

        if (skystonePosition == SkystonePosition.Wall)
        {
            //Stone by wall
            sSRight(0.6, 150);
            //bumpRightF(0.3, 50);
            delay(0.1);

            captureFirstSkyStone(true);

            intakeFirstSkyStone(true);

            //sSRight(0.6, 100);
            //takeCurrentAngle();
            correctAngle(1, 270, 0.3, 0.2);
            takeCurrentAngle();
            delay(0.2);
            //LAUNCH

            driveStraightBackRampDown(1.0, 0.12, 3700, 1550);
            turnRightToAngle(190, 0.6, 0.25);
            //ready to move foundation

            if (DownFieldAuto()) {
                park();
            }
        }
        else if (skystonePosition == SkystonePosition.Center)
        {
            //Stone in center
            strafeRight(0.6, 840);
            //bumpRightF(0.3, 50);
            delay(0.1);

            captureFirstSkyStone(true);

            intakeFirstSkyStone(true);

            //sSRight(0.6, 100);
            //takeCurrentAngle();
            sSRight(0.6, 50);
            //should be 270 but turns to 269 to account for weirdness
            correctAngle(1, 269, 0.3, 0.2);
            takeCurrentAngle();
            delay(0.2);
            //LAUNCH

            driveStraightBackRampDown(1.0, 0.12, 4600, 1600);
            turnRightToAngle(185, 0.6, 0.25);

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
            correctAngle(1, 270, 0.22, 0.19);

            //LAUNCH

            driveStraightBackRampDown(1.0, 0.12, 3300, 1600);
            turnRightToAngle(180, 0.6, 0.25);


            driveStraightBack(0.4, 100);
            if (DownFieldAuto()) {
                park();
            }
        }
    }

    public void captureFirstSkyStone(boolean onRight) {
        if (onRight == true) {
            driveStraightForwardRampDown(0.7, 0.12, 1150, 400);
            lowerSpatR();
            delay(0.4);

            driveStraightBackRampDown(0.5, 0.12, 600, 300);
            driveStraightForward(0.25, 50);
            ricePattyR();
            delay(0.1);
        }
        else {
            driveStraightForwardRampDown(0.7, 0.12, 1050, 400);
            lowerSpatL();
            delay(0.4);

            driveStraightBackRampDown(0.5, 0.12, 600, 300);
            driveStraightForward(0.25, 50);
            ricePattyL();
            delay(0.1);
        }

    }
    public void intakeFirstSkyStone(boolean onRight) {
        releaseStone();
        intakeIn();
        if (onRight == true) {
            turnRightToAngle(330, 0.5, 0.15);
            driveStraightForwardRampDown(0.4, 0.12, 400, 200);
            // For now we need to double-tap the block to get it to intake properly
            // otherwise it just gets jammed on the first attempt.
            gateClose();
            turnRightToAngle(270, 0.4, 0.2);
        }
        else {
            turnLeftToAngle(25, 0.5, 0.15);
            driveStraightForwardRampDown(0.4, 0.12, 400, 200);
            gateClose();
            turnRightToAngle(270, 0.6, 0.2);
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
        bumpRightB(0.3, 210);
        grabFoundation();
        intakeOut();
        delay(0.5);
        //foundation captured

        intakeOff();
        motorVerticalSlide.setTargetPosition(-1500);
        bumpLeftFToAngle(270, 2, 0.8);

        //driveStraightForward(0.5, 100)
        correctAngle(3, 270, 0.75, 0.2);

        getNormCurrentAngle();
        if (normalizeAngle(angles.firstAngle) < 250 || normalizeAngle(angles.firstAngle) > 290) {
            return false;
        }
        servoHorizontalSlide.setPosition(0.0);
        timedDriveBackward(0.5, 1.0);
        servoHorizontalSlide.setPosition(0.5);

        releaseStone();
        releaseFoundation();
        delay (0.45);

        sSLeft(0.6, 300);
        motorVerticalSlide.setTargetPosition(0);
        horizontalSlideIn(1.2);

        correctAngle (3, 270, 0.3, 0.2);

        return true;
    }
}