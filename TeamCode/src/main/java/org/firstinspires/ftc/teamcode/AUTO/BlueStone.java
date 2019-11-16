package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "BlueStone", group = "Linear Opmode")
public class BlueStone extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeMecanum();
        initializeImu();
        initializeIntake();
        initializeSlide();
        initializeCapstoneDropper();
        //initializeTouch();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        getStone();
    }

    public void getStone() {
        intakeIn();
        motorVerticalSlide.setTargetPosition(-315);
        driveStraightForward(0.25, 1000);
        bumpRightF(0.4, 100);
        driveStraightForward(0.3, 300);

        motorVerticalSlide.setTargetPosition(-200);
        grabStone();
        intakeOut();
        delay(0.2);
        intakeOff();

        driveStraightBack(0.3, 500);
        turnRightToAngle(90.0, 0.4, 0.1);
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
        strafeRight(0.4, 200);

    }
}