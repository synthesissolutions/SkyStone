
package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.logging.Level;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "Red3Auto", group = "Linear Opmode")
public class Red3Auto extends AutoBase {

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
            prepRobot();
            telemetry.addData("levelRest", levelRest);
            telemetry.addData("levelCap", levelCap);
            telemetry.update();

            strafeLeft(0.6, 400);
            delay(0.1);

            captureFirstSkyStone();

            intakeFirstSkyStone();
            //LAUNCH

            driveStraightForward(1.0, 2300);
            driveStraightForward(0.4, 250);
            turnRightToAngle(186, 0.36, 0.18);
            //ready to move foundation

            driveStraightBack(0.6, 100);
            DownFieldAuto();
        }
        else if (skystonePosition == SkystonePosition.Center)
        {
            //Stone in center
            prepRobot();

            strafeLeft(0.6, 90);
            delay(0.1);

            captureFirstSkyStone();

            intakeFirstSkyStone();
            //LAUNCH

            driveStraightForward(1.0, 1900);
            driveStraightForward(0.4, 250);
            turnRightToAngle(180, 0.36, 0.18);

            driveStraightBack(0.6, 100);
            DownFieldAuto();
        }
        else
        {
            //Stone on right
            prepRobot();

            strafeRight(0.6, 110);
            strafeRight(0.3, 110);
            delay(0.1);

            captureFirstSkyStone();

            intakeFirstSkyStone();
            //LAUNCH

            driveStraightForward(1.0, 1700);
            driveStraightForward(0.4, 250);
            turnRightToAngle(180, 0.36, 0.18);

            driveStraightBack(0.35, 200);
            DownFieldAuto();
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
        delay(0.1);
        extendRestArm();
        delay(0.5);
        stonePosition();
    }
    public void intakeFirstSkyStone() {
        intakeIn();
        spinRight(30, 0.45, 0.15);
        driveStraightForward(0.4, 400);
        turnRightToAngle(272, 0.33, 0.17);
        gateClose();
        delay(0.2);
        motorVerticalSlide.setTargetPosition(levelRest + 10);
        grabStone();
        intakeOff();
        sSRight(0.6, 200);
    }
    private void DownFieldAuto() {
        driveStraightBack(0.35, 170);
        intakeOut();
        bumpLeftB(0.3, 150);
        grabFoundation();
        delay(0.45);
        //foundation captured

        intakeOff();
        strafeRight(0.5, 300);
        driveStraightForward(0.5, 500);
        hardCurveLeftB(0.6, 1300);
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
        sSLeft(0.6, 300);
        stonePosition();
        motorHorizontalSlide.setPower(1.0);
        driveStraightForward(1.0, 700);
        driveStraightForward(0.25, 100);
    }
}