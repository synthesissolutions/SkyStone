package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "Blue Foundation Bridge", group = "Linear Opmode")
public class BlueFoundationBridge extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeMecanum();
        initializeImu();
        initializeFoundation();
        //initializeTouch();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        foundationAndBridgeBlue();
    }

    private void foundationAndBridgeBlue() {
        driveStraightBack(0.25, 1300);
        bumpRight(0.25, 150);
        grabFoundation();
        delay (0.5);
        driveStraightForward(0.25, 200);
        curveLeftF (0.4, 400);
        hardCurveRightB(0.4, 1400);
        driveStraightBack(0.3, 200);
        releaseFoundation();
        delay (0.5);
        driveStraightForward(0.25, 1700);
    }
}
