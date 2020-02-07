package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "Red Fnd Brg & SS", group = "Linear Opmode")
@Disabled
public class RedFoundationBridgeAndSkyStone extends aPaprikaAutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        foundationBridgeAndSkyStoneRed();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();
        delay(5.0);
    }

    private void foundationBridgeAndSkyStoneRed() {
        driveStraightBack(0.25, 1300);
        bumpLeftB(0.25, 150);
        grabFoundation();
        driveBack(0.09);
        delay (0.5);

        curveRightF (0.4, 700);
        hardCurveLeftB(0.5, 1300);
        driveStraightBack(0.5, 200);
        releaseFoundation();
        delay (0.5);

        strafeLeft(0.4, 150);
        driveStraightForward(0.3, 2500);

        motorVerticalSlide.setTargetPosition(-315);
        intakeIn();
        strafeRight(0.4, 700);
        bumpRightF(0.25, 150);
        driveStraightForward(0.2, 250);
        gateClose();
        delay(0.5);
        //block inside
        motorVerticalSlide.setTargetPosition(-200);
        delay(0.2);
        grabStone();
        intakeOff();

        strafeLeft(0.4, 700);
        bumpLeftF(0.25, 150);
        driveStraightBack(0.4, 2300);

        motorVerticalSlide.setTargetPosition(-400);
        delay(0.5);
        horizontalSlide(1.0);
        delay(1.0);
        releaseStone();

        motorVerticalSlide.setTargetPosition(-600);
        horizontalSlide(-1.0);
        delay(1.0);
        motorVerticalSlide.setTargetPosition(0);
        motorHorizontalSlide.setPower(0);
        driveStraightForward(0.28, 1700);
    }
}
