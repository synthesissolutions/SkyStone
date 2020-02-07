package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "Red Foundation BridgeR", group = "Linear Opmode")
public class RedFoundationBridgeR extends aPaprikaAutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        foundationAndBridgeRedR();
    }

    private void foundationAndBridgeRedR() {
        driveStraightBack(0.25, 1350);
        bumpLeftB(0.25, 150);
        driveBack(0.09);
        grabFoundation();
        delay (0.5);
        curveRightF (0.4, 600);
        hardCurveLeftB(0.4, 1300);
        timedDriveBackward(0.5, 0.5);
        releaseFoundation();
        delay (0.5);
        strafeLeft(0.4, 150);
        driveStraightForward(0.28, 1700);
    }
}
