package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

<<<<<<< HEAD
public class Red3Auto{
}
=======
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

        doTheThing();
    }

    public void doTheThing() {
        //motorVerticalSlide.setTargetPosition(-315);
        driveStraightForward(0.3, 1200);
        lowerSpat();
        delay(0.2);
        //skystone grabbed
        driveStraightBack(0.25, 900);
        driveStraightForward(0.2, 50);
        raiseSpat();
        //skystone released
        intakeIn();
        spinRight(30, 0.25, 0.17);
        driveStraightForward(0.25, 300);
        spinRight(60, 0.3, 0.17);
        intakeOff();
        driveStraightBack(1.0, 2500);

        spinRight(88, 0.3, 0.17);
        driveStraightBack(0.3, 300);
        bumpRightB(0.25, 160);
        grabFoundation();
        driveBack(0.09);
        delay (0.5);
        strafeLeft(0.4, 200);
        driveStraightForward(0.4, 800);
        hardCurveRightB(0.5, 1340);
        driveStraightBack(0.5, 300);
        releaseFoundation();
        delay (0.5);
        driveStraightForward(0.7, 3200);
        turnLeft(0.3);
        delay(0.5);



        /*motorVerticalSlide.setTargetPosition(-200);
        grabStone();
        intakeOut();
        delay(0.2);
        intakeOff();

        driveStraightBack(0.3, 500);
        turnLeftToAngle(-90.0, 0.4, 0.1);
        driveStraightForward(0.5, 500);

        motorVerticalSlide.setTargetPosition(-600);
        delay(0.5);
        horizontalSlide(1.0, 1.0);
        delay(1.5);
        releaseStone();
        horizontalSlide(-1.0, 1.0);
        delay(1.0);
        motorVerticalSlide.setTargetPosition(0);

        driveStraightBack(0.25, 100);
        strafeLeft(0.4, 200);
*/
    }
}
>>>>>>> c2199e337509842890f032004377caa0f70433c8
