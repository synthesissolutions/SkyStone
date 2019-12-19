
package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

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
    }

    public void Blue3Auto() {
        //Stone on right
        motorVerticalSlide.setTargetPosition(-500);
        extendRestArm();
        delay(0.5);
        stonePosition();
        raiseSpat();

        strafeRight(0.6, 1325);
        bumpRightF(0.3, 50);
        delay(0.1);

        driveStraightForward(0.5, 585);
        driveStraightForward(0.25, 200);
        delay(0.2);
        lowerSpat();
        delay(0.1);
        //1st skystone captured

        driveStraightBack(0.5, 700);
        driveStraightForward(0.25, 50);
        raiseSpat();
        delay(0.1);
        //1st skystone in position

        /*
        sSLeft(0.6, 960);
        driveStraightForward(0.5, 150);
        driveStraightForward(0.25, 200);
        lowerSpat();
        delay(0.2);
        //2nd skystone captured

        driveStraightBack(0.5, 800);
        driveStraightForward(0.25, 50);
        raiseSpat();
        delay(0.1);
        //2nd skystone in position
        */

        strafeLeft(0.6, 200);
        releaseStone();
        intakeIn();
        spinRight(45, 0.45, 0.15);
        driveStraightForward(0.4, 400);
        gateClose();
        spinRight(45, 0.45, 0.15);
        motorVerticalSlide.setTargetPosition(-80);
        grabStone();
        intakeOut();
        delay(0.2);
        intakeOff();
        //LAUNCH

        driveStraightBack(1.0, 3000);
        turnRightToAngle(180, 0.5, 0.17);
        //ready to move foundation

        driveStraightBack(0.5, 100);
        driveStraightBack(0.25, 300);
        bumpRightB(0.3, 150);
        grabFoundation();
        delay(0.45);
        //foundation captured
        strafeLeft(0.5, 300);
        driveStraightForward(0.5, 800);
        hardCurveRightB(0.6, 1300);
        motorVerticalSlide.setTargetPosition(levelRest - 100);
        //(-1.0) power is out, (1.0) power is in
        motorHorizontalSlide.setPower(-1.0);
        timedDriveBackward(0.5, 1.0);
        releaseStone();
        delay(0.1);
        releaseFoundation();
        delay (0.45);
        strafeRight(0.6, 500);
        stonePosition();
        motorHorizontalSlide.setPower(1.0);
        timedDriveForward(1.0, 1.0);
        /*
        //2nd launch
        intakeIn();
        driveStraightForward(1.0, 2500);
        //vertical stuff
        driveStraightForward(1.0, 1500);
        delay(0.2);
        intakeOut();
        delay(0.2);
        intakeOff();
        //vertical stuff
        //3rd launch
        driveStraightBack(1.0, 4000);
        //vertical stuff
        delay(1.0);
        driveStraightForward(0.5, 3000);
        */

    }
}