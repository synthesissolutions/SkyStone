package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Disabled
@Autonomous(name = "Deliver Block", group = "Linear Opmode")
public class DeliverBlock extends LinearOpMode {
    AutoTabasco tabasco = new AutoTabasco();

    @Override
    public void runOpMode() throws InterruptedException {

        tabasco.initializeRobot(hardwareMap);
        tabasco.angles = tabasco.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", tabasco.angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        tabasco.grabFoundation();
        tabasco.gateClose();
        tabasco.delay(0.5);
        tabasco.grabStone();
        tabasco.delay(0.5);
        //tabasco.verticalSlideUp(0.5);
        tabasco.delay(0.5);
        tabasco.horizontalSlideOut(1.0);
        //timedDriveBackward(0.5, 1.0);
        tabasco.releaseStone();
        tabasco.delay(0.1);
        tabasco.releaseFoundation();
        tabasco.delay (0.45);

        //driveStraightForward(0.3, 100);
        tabasco.sSLeft(0.6, 300);
        tabasco.horizontalSlideIn(1.0);
        //tabasco.verticalSlideDown(0.25);

        tabasco.shutdownRobot();
    }
}