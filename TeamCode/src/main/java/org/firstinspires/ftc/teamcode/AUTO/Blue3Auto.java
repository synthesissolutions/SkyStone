package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Locale;


@Autonomous(name = "Blue3Auto", group = "Linear Opmode")
public class Blue3Auto extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        doTheThing();
    }

    public void doTheThing() {
        //motorVerticalSlide.setTargetPosition(-315);
        driveStraightForward(0.3, 1200);
        lowerSpat();
        delay(0.2);
        //skystone grabbed
        driveStraightBack(0.3, 600);
        driveStraightBack(0.2, 100);
        driveStraightForward(0.2, 50);
        raiseSpat();
        //skystone released
        intakeIn();
        spinRight(30, 0.26, 0.19);
        driveStraightForward(0.3, 300);
        spinRight(60, 0.3, 0.17);
        intakeOff();
        //LAUNCH
        driveStraightBack(1.0, 2500);
        spinRight(88, 0.3, 0.17);
        //land
        driveStraightBack(0.5, 300);
        bumpRightB(0.25, 160);
        grabFoundation();
        driveBack(0.09);
        delay (0.45);
        //got foundation
        strafeLeft(0.5, 300);
        driveStraightForward(0.5, 800);
        hardCurveRightB(0.6, 1340);
        driveStraightBack(0.7, 300);
        //foundation pushed up against wall
        releaseFoundation();
        delay (0.5);
        //2nd Launch
        driveStraightForward(1.0, 3200);

    }
}
