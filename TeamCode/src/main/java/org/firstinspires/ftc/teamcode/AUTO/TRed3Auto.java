
package org.firstinspires.ftc.teamcode.AUTO;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
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

        Red3Auto();

        //shutdownRobot();
    }

    public void Red3Auto() {
        SkystonePosition skystonePosition = findSkystone("Blue");

        if (skystonePosition == SkystonePosition.Wall)
        {
            //Stone by wall

            sSRight(0.6, 50);
            bumpRightF(0.3, 50);
            delay(0.1);

            capture1stSkystone();

            intake1stSkystone();

            sSLeft(0.6, 100);
            takeCurrentAngle();
            //should be 270 but turns to 269 to account for weirdness
            correctAngle(1, 90, 0.22, 0.17);
            takeCurrentAngle();
            delay(0.2);
            //LAUNCH

            driveStraightBackRampDown(0.75, 0.12, 2900, 1000);
            turnRightToAngle(200, 0.36, 0.18);
            //ready to move foundation

            if (DownFieldAuto()) {
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
            strafeLeft(0.6, 50);
            bumpRightF(0.3, 50);
            delay(0.1);

            capture1stSkystone();

            intake1stSkystone();

            sSLeft(0.6, 100);
            takeCurrentAngle();
            //should be 270 but turns to 269 to account for weirdness
            correctAngle(1, 90, 0.22, 0.17);
            takeCurrentAngle();
            delay(0.2);
            //LAUNCH

            driveStraightBackRampDown(0.75, 0.12, 3450, 1000);
            turnRightToAngle(189, 0.25, 0.18);

            if (DownFieldAuto()) {
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
            //Stone on right

            //used 300 for flat field
            //used 500 for our field
            strafeLeft(0.6, 150);
            bumpRightF(0.3, 50);
            delay(0.1);

            capture1stSkystone();

            intake1stSkystone();

            sSLeft(0.6, 100);
            takeCurrentAngle();
            //should be 270 but turns to 269 to account for weirdness
            correctAngle(1, 90, 0.22, 0.17);
            takeCurrentAngle();
            delay(0.2);

            //LAUNCH

            driveStraightBackRampDown(0.75, 0.12, 3250, 1000);
            //should turn to 180 but turns to 200 to adjust for field
            turnRightToAngle(200, 0.36, 0.18);


            driveStraightBack(0.35, 100);
            if (DownFieldAuto()) {
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

    public void capture1stSkystone() {
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
    public void intake1stSkystone() {
        intakeIn();
        spinLeft(30, 0.45, 0.15);
        driveStraightForwardRampDown(0.4, 0.12, 400, 200);
        gateClose();
        turnLeftToAngle(90, 0.33, 0.18);
        grabStone();
        intakeOff();
    }
    public boolean DownFieldAuto() {
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
        //if the following scenario occurs STOP EVERYTHING
        getNormCurrentAngle();
        if (normalizeAngle(angles.firstAngle) < 15 || normalizeAngle(angles.firstAngle) > 75) {
            return false;
        }
        //vertical lift is high enough to just drop stone and come back.. hopefully
        verticalSlide(-600);
        //(1.0) power is out, (-1.0) power is in
        horizontalSlide(1.0);
        timedDriveBackward(0.5, 1.0);
        releaseStone();
        delay(0.1);
        releaseFoundation();
        delay (0.45);
        horizontalSlide(-1.0);
        //another safty check occurs here
        getNormCurrentAngle();
        if (normalizeAngle(angles.firstAngle) < 50 || normalizeAngle(angles.firstAngle) > 130) {
            return false;
        }
        driveStraightForward(0.3, 100);
        sSLeft(0.6, 300);
        stonePosition();

        correctAngle(3, 90, 0.23, 0.19);
        //if it makes it down here its good to continue
        return true;
    }
}