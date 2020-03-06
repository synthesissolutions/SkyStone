
package org.firstinspires.ftc.teamcode.AUTO;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "TParkingForward", group = "Linear Opmode")
@Disabled
public class TParkingForward extends LinearOpMode {

    AutoTabasco tabasco = new AutoTabasco();

    @Override
    public void runOpMode() throws InterruptedException {

        tabasco.initializeRobot(hardwareMap);
        tabasco.angles = tabasco.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", tabasco.angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        tabasco.prepRobot();
        //delay(2.0);

        tabasco.driveStraightForward(0.3, 500);

        tabasco.shutdownRobot();
    }

}