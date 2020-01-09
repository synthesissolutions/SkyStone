
package org.firstinspires.ftc.teamcode.AUTO;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.logging.Level;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "Blue3Auto", group = "Linear Opmode")
public class Blue3Auto extends AutoBase {

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

        shutdownRobot();
    }

    public void Blue3Auto() {
        SkystonePosition skystonePosition = findSkystone("Blue");

        if (skystonePosition == SkystonePosition.Wall)
        {
            //Stone by wall
            prepRobot();
            telemetry.addData("levelRest", levelRest);
            telemetry.addData("levelCap", levelCap);
            telemetry.update();

            sSRight(0.8, 1170);
            strafeRight(0.3, 330);
            takeCurrentAngle();
            bumpRightF(0.4, 110);
            delay(0.1);

            captureFirstSkyStone();

            strafeLeft(0.5, 200);
            intakeIn();
            spinRight(45, 0.45, 0.15);
            driveStraightForward(0.4, 400);
            turnRightToAngle(270, 0.34, 0.17);
            gateClose();
            motorVerticalSlide.setTargetPosition(-80);
            grabStone();
            intakeOff();
            takeCurrentAngle();
            sSRight(0.6, 100);
            correctAngle(2, 270, 0.21, 0.19);
            //LAUNCH

            driveStraightBack(1.0, 3100);
            driveStraightBack(0.3, 100);
            turnRightToAngle(200, 0.36, 0.18);
            //ready to move foundation

            DownFieldAutoA();
        }
        else if (skystonePosition == SkystonePosition.Center)
        {
            //Stone in center
            prepRobot();

            strafeRight(0.6, 900);
            bumpRightF(0.3, 50);
            delay(0.1);

            captureFirstSkyStone();

            intakeIn();
            spinRight(30, 0.45, 0.15);
            driveStraightForward(0.4, 400);
            gateClose();
            turnRightToAngle(272, 0.33, 0.18);
            motorVerticalSlide.setTargetPosition(levelRest + 10);
            grabStone();
            intakeOff();
            takeCurrentAngle();
            sSRight(0.6, 200);
            //LAUNCH

            driveStraightBack(1.0, 3200);
            driveStraightBack(0.4, 100);
            turnRightToAngle(189, 0.25, 0.18);

            DownFieldAutoC();
            driveStraightForward(1.0, 100);

            driveStraightForward(1.0, 800);
            driveStraightForward(0.35, 400);
            motorHorizontalSlide.setPower(0.0);
            sSLeft(0.7, 500);

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
            correctAngle(2, 272, 0.22, 0.19);
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

            driveStraightForward(1.0, 700);
            driveStraightForward(0.35, 100);

        }
        else
        {
            //Stone on left
            prepRobot();

            strafeRight(0.6, 450);
            bumpRightF(0.3, 50);
            delay(0.1);

            intakeIn();
            captureFirstSkyStone();
            spinRight(45, 0.45, 0.15);
            driveStraightForward(0.4, 400);
            turnRightToAngle(272, 0.33, 0.18);
            grabStone();
            gateClose();
            sSRight(0.4, 100);

            correctAngle(1, 270, 0.22, 0.19);

            //LAUNCH

            driveStraightBack(1.0, 2950);
            driveStraightBack(0.3, 100);
            turnRightToAngle(200, 0.36, 0.18);


            driveStraightBack(0.35, 100);
            DownFieldAutoB();

            driveStraightForward(1.0, 1000);
            driveStraightForward(0.5, 800);
            spinLeft(30, 0.33, 0.18);
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
            correctAngle(2, 272, 0.22, 0.19);
            gateClose();
            takeCurrentAngle();
            delay(1.0);

            /*
            driveStraightBack(1.0, 1000);
            driveStraightBack(0.5, 500);
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

            driveStraightForward(1.0, 700);
            driveStraightForward(0.35, 100);
            */

        }
    }

    public void captureFirstSkyStone() {
        driveStraightForward(0.5, 585);
        driveStraightForward(0.25, 230);
        delay(0.2);
        lowerSpat();
        delay(0.17);

        driveStraightBack(0.5, 700);
        driveStraightForward(0.25, 50);
        raiseSpat();
        delay(0.1);

    }
    public void prepRobot() {
        motorVerticalSlide.setTargetPosition(-500);
        extendRestArm();
        delay(0.5);
        stonePosition();
    }
    public void DownFieldAutoA() {
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
        //vertical lift is high enough to just drop stone and come back.. hopefully
        motorVerticalSlide.setTargetPosition(levelRest - 600);
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
        driveStraightForward(1.0, 700);
        driveStraightForward(0.25, 100);
    }
    public void DownFieldAutoC() {
        bumpRightB(0.4, 90);
        driveStraightBack(0.35, 150);
        bumpRightB(0.3, 160);
        grabFoundation();
        intakeOut();
        delay(0.5);
        //foundation captured

        intakeOff();
        strafeLeft(0.5, 400);
        driveStraightForward(0.5, 500);
        hardCurveRightB(0.6, 1300);
        //vertical lift is high enough to just drop stone and come back.. hopefully
        motorVerticalSlide.setTargetPosition(levelRest - 600);
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
        driveStraightForward(1.0, 700);

    }
    public void DownFieldAutoB() {
        driveStraightBack(0.35, 180);
        bumpRightB(0.3, 210);
        grabFoundation();
        intakeOut();
        delay(0.5);
        //foundation captured

        intakeOff();
        strafeLeft(0.5, 400);
        driveStraightForward(0.5, 500);
        hardCurveRightB(0.6, 1300);
        //vertical lift is high enough to just drop stone and come back.. hopefully
        motorVerticalSlide.setTargetPosition(levelRest - 600);
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

        correctAngle (3, 270, 0.23, 0.19);

        motorHorizontalSlide.setPower(1.0);

    }
}