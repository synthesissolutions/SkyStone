package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Disabled
@Autonomous(name = "Blue Foundation R Bridge", group = "Linear Opmode")
public class BlueFoundationBridgeR extends aPaprikaAutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        foundationAndBridgeBlueR();
    }

    private void foundationAndBridgeBlueR() {
        driveStraightBack(0.25, 1300);
        bumpRightB(0.25, 150);
        grabFoundation();
        driveBack(0.09);
            delay(0.5);
        driveStraightForward(0.25, 200);
        curveLeftF (0.4, 400);
        hardCurveRightB(0.4, 1400);
        timedDriveBackward(0.5, 0.5);
        releaseFoundation();
            delay(0.5);
        strafeRight(0.4, 500);
        driveStraightForward(0.25, 1700);
    }
}
