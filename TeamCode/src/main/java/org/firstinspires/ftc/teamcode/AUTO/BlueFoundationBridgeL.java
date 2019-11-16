package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "Blue Foundation Bridge", group = "Linear Opmode")
public class BlueFoundationBridgeL extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

<<<<<<< HEAD:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/AUTO/BlueFoundationBridgeL.java
        initializeMecanum();
        initializeImu();
        initializeFoundation();
        initializeCapstoneDropper();
        //initializeTouch();
=======
        initializeRobot();

>>>>>>> 4a5ce3e630f49cfdc4b8287f7a0d79e9f5a5fecd:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/AUTO/BlueFoundationBridge.java
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        foundationAndBridgeBlue();
    }

    private void foundationAndBridgeBlue() {
        driveStraightBack(0.25, 1350);
        bumpRightB(0.25, 160);
        grabFoundation();
        delay (0.5);
        driveStraightForward(0.25, 200);
        curveLeftF (0.4, 400);
        hardCurveRightB(0.4, 1400);
        driveStraightBack(0.5, 100);
        releaseFoundation();
        delay (0.5);
        strafeRight(0.4, 150);
        driveStraightForward(0.25, 1700);
    }
}
