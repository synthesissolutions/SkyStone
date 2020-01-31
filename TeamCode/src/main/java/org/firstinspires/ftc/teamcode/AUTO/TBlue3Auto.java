
package org.firstinspires.ftc.teamcode.AUTO;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.logging.Level;

@Autonomous(name = "TBlue3Auto", group = "Linear Opmode")
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

        Blue3Auto();

        //shutdownRobot();
    }

    public void Blue3Auto() {
        SkystonePosition skystonePosition = findSkystone("Blue");

        if (skystonePosition == SkystonePosition.Wall)
        {
            //Stone by wall

            sSRight(0.6, 150);
            bumpRightF(0.3, 50);
            delay(0.1);

            captureFirstSkyStone();

            intakeIn();
            spinRight(30, 0.45, 0.15);
            driveStraightForwardRampDown(0.4, 0.12, 400, 200);
            gateClose();
            turnRightToAngle(272, 0.33, 0.18);
            grabStone();
            intakeOff();
            sSRight(0.6, 100);
            takeCurrentAngle();
            //should be 270 but turns to 269 to account for weirdness
            correctAngle(1, 269, 0.22, 0.17);
            takeCurrentAngle();
            delay(0.2);
            //LAUNCH

            driveStraightBackRampDown(0.75, 0.12, 2900, 1000);
            turnRightToAngle(200, 0.36, 0.18);
            //ready to move foundation

            if (DownFieldAutoA()) {
                driveStraightForwardRampDown(0.5, 0.12, 1450, 500);
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
            strafeRight(0.6, 840);
            bumpRightF(0.3, 50);
            delay(0.1);

            captureFirstSkyStone();

            intakeIn();
            spinRight(30, 0.45, 0.15);
            driveStraightForwardRampDown(0.4, 0.12, 400, 200);
            gateClose();
            turnRightToAngle(272, 0.33, 0.18);
            grabStone();
            intakeOff();
            sSRight(0.6, 100);
            takeCurrentAngle();
            //should be 270 but turns to 269 to account for weirdness
            correctAngle(1, 269, 0.22, 0.17);
            takeCurrentAngle();
            delay(0.2);
            //LAUNCH

            driveStraightBackRampDown(0.75, 0.12, 3450, 1000);
            turnRightToAngle(189, 0.25, 0.18);

            if (DownFieldAutoC()) {
                driveStraightForwardRampDown(0.5, 0.12, 1450, 500);
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

            //used 300 for flat field
            //used 500 for our field
            /*
            strafeRight(0.6, 500);
            bumpRightF(0.3, 50);
            delay(0.1);
            */
            grabStone();
            intakeIn();
            captureFirstSkyStone();
            spinRight(40, 0.45, 0.15);
            releaseStone();
            driveStraightForwardRampDown(0.4, 0.12, 400, 200);
            turnRightToAngle(272, 0.33, 0.18);
            gateClose();
            sSRight(0.4, 100);
            grabStone();

            correctAngle(1, 270, 0.22, 0.19);

            //LAUNCH

            driveStraightBackRampDown(0.75, 0.12, 3250, 1000);
            //should turn to 180 but turns to 200 to adjust for field
            turnRightToAngle(200, 0.36, 0.18);


            driveStraightBack(0.35, 100);
            if (DownFieldAutoB()) {
                driveStraightForwardRampDown(0.5, 0.12, 1450, 500);

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

    public void captureFirstSkyStone() {
        driveStraightForwardRampDown(0.5, 0.12, 800, 200);
        delay(0.2);
        lowerSpatR();
        lowerSpatL();
        delay(0.17);

        driveStraightBackRampDown(0.5, 0.12, 600, 300);
        driveStraightForward(0.25, 50);
        ricePattyR();
        ricePattyL();
        delay(0.1);

    }
    public void captureWallSkystone() {
        driveStraightForwardRampDown(0.5, 0.12, 730, 100);
        delay(0.2);
        lowerSpatR();
        delay(0.17);

        driveStraightBackRampDown(0.5, 0.12, 650, 100);
        ricePattyR();
        delay(0.1);
    }
    public boolean DownFieldAutoA() {
        driveStraightBack(0.35, 250);
        bumpRightB(0.3, 180);
        grabFoundation();
        intakeOut();
        delay(0.5);
        //foundation captured

        intakeOff();
        strafeLeft(0.5, 400);
        driveStraightForward(0.5, 500);
        hardCurveRightB(0.6, 1300);
        getNormCurrentAngle();
        if (normalizeAngle(angles.firstAngle) < 250 || normalizeAngle(angles.firstAngle) > 290) {
            return false;
        }
        //vertical lift is high enough to just drop stone and come back.. hopefully
        motorVerticalSlide.setTargetPosition(-600);
        //(-1.0) power is out, (1.0) power is in
        motorHorizontalSlide.setPower(-1.0);
        timedDriveBackward(0.5, 1.0);
        releaseStone();
        delay(0.1);
        releaseFoundation();
        delay (0.45);

        driveStraightForward(0.3, 100);
        sSRight(0.6, 300);
        stonePosition();

        correctAngle(3, 270, 0.23, 0.19);

        motorHorizontalSlide.setPower(1.0);
        return true;
    }
    public boolean DownFieldAutoC() {
        driveStraightBack(0.35, 200);
        bumpRightB(0.3, 210);
        grabFoundation();
        intakeOut();
        delay(0.5);
        //foundation captured

        intakeOff();
        strafeLeft(0.5, 400);
        driveStraightForward(0.5, 500);
        hardCurveRightB(0.6, 1200);
        getNormCurrentAngle();
        if (normalizeAngle(angles.firstAngle) < 250 || normalizeAngle(angles.firstAngle) > 290) {
            return false;
        }
        //vertical lift is high enough to just drop stone and come back.. hopefully
        motorVerticalSlide.setTargetPosition(-600);
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

        correctAngle(3, 270, 0.23, 0.19);

        motorHorizontalSlide.setPower(1.0);
        return true;
    }
    public boolean DownFieldAutoB() {
        driveStraightBack(0.35, 180);
        bumpRightB(0.3, 210);
        grabFoundation();
        intakeOut();
        delay(0.5);
        //foundation captured

        intakeOff();
        strafeLeft(0.5, 400);
        driveStraightForward(0.5, 500);
        hardCurveRightB(0.6, 1100);
        getNormCurrentAngle();
        if (normalizeAngle(angles.firstAngle) < 250 || normalizeAngle(angles.firstAngle) > 290) {
            return false;
        }
        //vertical lift is high enough to just drop stone and come back.. hopefully
        motorVerticalSlide.setTargetPosition(-600);
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

        correctAngle (3, 268, 0.23, 0.19);

        motorHorizontalSlide.setPower(1.0);
        return true;
    }
}