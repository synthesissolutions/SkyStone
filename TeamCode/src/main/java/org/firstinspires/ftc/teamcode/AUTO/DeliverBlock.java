package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous(name = "Deliver Block", group = "Linear Opmode")
public class DeliverBlock extends aTabascoAutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        grabFoundation();
        gateClose();
        delay(0.5);
        grabStone();
        delay(0.5);
        verticalSlideUp(0.5);
        delay(0.5);
        horizontalSlideOut(1.0);
        //timedDriveBackward(0.5, 1.0);
        releaseStone();
        delay(0.1);
        releaseFoundation();
        delay (0.45);

        //driveStraightForward(0.3, 100);
        sSLeft(0.6, 300);
        horizontalSlideIn(1.0);
        verticalSlideDown(0.25);

        shutdownRobot();
    }
}