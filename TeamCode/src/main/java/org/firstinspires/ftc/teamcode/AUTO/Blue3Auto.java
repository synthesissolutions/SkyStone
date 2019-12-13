
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
        motorVerticalSlide.setTargetPosition(level1);
        raiseSpat();
        strafeRight(0.6, 1250);
        bumpRightF(0.3, 50);
        delay(0.1);

        driveStraightForward(0.5, 575);
        driveStraightForward(0.25, 200);
        delay(0.2);
        lowerSpat();
        delay(0.3);
        //1st skystone captured

        driveStraightBack(0.5, 600);
        driveStraightForward(0.25, 50);
        raiseSpat();
        delay(0.1);
        //1st skystone in position

        sSLeft(0.6, 960);
        driveStraightForward(0.5, 150);
        driveStraightForward(0.25, 200);
        lowerSpat();
        delay(0.2);
        //2nd skystone captured

        driveStraightBack(0.5, 600);
        driveStraightForward(0.25, 50);
        raiseSpat();
        delay(0.1);
        //2nd skystone in position

        releaseStone();
        intakeIn();
        spinRight(30, 0.3, 0.19);
        driveStraightForward(0.3, 300);
        gateClose();
        spinRight(60, 0.3, 0.19);
        motorVerticalSlide.setTargetPosition(-225);
        grabStone();
        intakeOut();
        delay(0.2);
        intakeOff();
        /*
        //LAUNCH
        driveStraightBack(1.0, 2500);
        spinRight(88, 0.5, 0.2);
        //ready to move foundation
        driveStraightBack(0.5, 300);
        bumpRightB(0.3, 120);
        grabFoundation();
        delay(0.45);
        //foundation captured
        strafeLeft(0.5, 300);
        //vertical stuff
        driveStraightForward(0.5, 800);
        hardCurveRightB(0.6, 1300);
        driveStraightBack(0.7, 300);
        strafeRight(0.6, 200);
        releaseFoundation();
        delay (0.45);
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