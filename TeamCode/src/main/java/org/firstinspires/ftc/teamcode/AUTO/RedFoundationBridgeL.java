package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

<<<<<<< HEAD:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/AUTO/RedFoundationBridgeL.java
@Autonomous(name = "Red Foundation Bridge Temp", group = "Linear Opmode")
public class RedFoundationBridgeL extends AutoBase {
=======
@Autonomous(name = "Red Foundation Bridge", group = "Linear Opmode")
public class RedFoundationBridge extends AutoBase {
>>>>>>> 4a5ce3e630f49cfdc4b8287f7a0d79e9f5a5fecd:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/AUTO/RedFoundationBridge.java

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        foundationAndBridgeRedL();
    }

    private void foundationAndBridgeRedL() {
        driveStraightBack(0.25, 1300);
        bumpLeftB(0.25, 150);
        forward(0.09);
        grabFoundation();
        delay (0.5);
        curveRightF (0.4, 600);
        hardCurveLeftB(0.4, 1300);
        driveStraightBack(0.5, 200);
        releaseFoundation();
        delay (0.5);
        strafeLeft(0.4, 1000);
        driveStraightForward(0.28, 1700);
    }
}
