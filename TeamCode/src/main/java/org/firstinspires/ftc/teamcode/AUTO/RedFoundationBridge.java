package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "Red Foundation Bridge", group = "Linear Opmode")
public class RedFoundationBridge extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        foundationAndBridgeRed();
    }

    private void foundationAndBridgeRed() {
        driveStraightBack(0.25, 1300);
        bumpLeftB(0.25, 150);
        grabFoundation();
        delay (0.5);
        curveRightF (0.4, 600);
        hardCurveLeftB(0.4, 1300);
        driveStraightBack(0.5, 200);
        releaseFoundation();
        delay (0.5);
        strafeLeft(0.4, 150);
        driveStraightForward(0.28, 1700);
    }
}
