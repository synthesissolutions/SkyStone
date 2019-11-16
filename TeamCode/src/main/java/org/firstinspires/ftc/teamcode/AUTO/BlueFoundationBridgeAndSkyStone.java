package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "Blue Fnd Brg & SS", group = "Linear Opmode")
public class BlueFoundationBridgeAndSkyStone extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeMecanum();
        initializeImu();
        initializeIntake();
        initializeSlide();
        initializeFoundation();
        initializeCapstoneDropper();
        //initializeTouch();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        foundationBridgeAndSkyStoneBlue();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();
        delay(5.0);
    }

    private void foundationBridgeAndSkyStoneBlue() {
        driveStraightBack(0.25, 1200);
        driveStraightBack(0.2, 160);
        bumpRightB(0.25, 180);
        grabFoundation();
        driveBack(0.09);
        delay (0.5);

        curveLeftF (0.4, 400);
        hardCurveRightB(0.4, 1200);
        driveStraightBack(0.5, 100);
        releaseFoundation();
        delay (0.5);

        strafeRight(0.4, 200);
        driveStraightForward(0.3, 2700);

        motorVerticalSlide.setTargetPosition(-315);
        intakeOut();
        strafeLeft(0.4, 400);
        bumpLeftF(0.25, 150);
        driveStraightForward(0.2, 250);
        gateClose();
        delay(0.5);
        //block inside
        motorVerticalSlide.setTargetPosition(-200);
        delay(0.2);
        grabStone();
        intakeIn();
        delay(0.5);
        intakeOff();

        strafeRight(0.4, 1000);
        bumpRightF(0.25, 150);
        driveStraightBack(0.4, 2300);

        motorVerticalSlide.setTargetPosition(-400);
        delay(0.5);
        horizontalSlide(1.0, 0.5);
        delay(1.0);
        releaseStone();

        motorVerticalSlide.setTargetPosition(-600);
        horizontalSlide(-1.0, 0.5);
        delay(1.0);
        motorVerticalSlide.setTargetPosition(0);
        motorHorizontalSlide.setPower(0);
        driveStraightForward(0.28, 1700);
    }
}
