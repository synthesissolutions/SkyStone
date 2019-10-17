package org.firstinspires.ftc.teamcode.AUTO;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Locale;

@Autonomous(name="Turn to Angle Phone Test", group="Linear Opmode")
public class TurnToAnglePhoneTest extends LinearOpMode implements SensorEventListener {

    private ElapsedTime runtime = new ElapsedTime();

    DcMotor motorFrontLeft;
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;

    public float gyroCurrentHeading = 0.0f;

    @Override
    public void runOpMode() throws InterruptedException {



        initializeMecanum();

        telemetry.addData("currentAngle", "" + gyroCurrentHeading);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        int count = 0;
        double currentAngle = normalizeAngle(gyroCurrentHeading);
        double targetAngle = calculateTargetAngle(currentAngle, 72);
        if (targetAngle > currentAngle) {
            turnLeft (.4);
            while (opModeIsActive() && currentAngle < targetAngle) {
                count++;
                telemetry.addData("currentAngle", "" + gyroCurrentHeading);
                telemetry.addData("targetAngle", "" + targetAngle);
                telemetry.update();
                currentAngle = normalizeAngle(gyroCurrentHeading);
                Log.i("TURNTEST", "Time: " + runtime.seconds());
                Log.i("TURNTEST", "Current Angle: " + currentAngle);
            }
            stopMotors();
            while (opModeIsActive()){

                telemetry.addData("currentAngle", "" + gyroCurrentHeading);
                telemetry.addData( "realCurrentAngle", "" + currentAngle);
                telemetry.addData( "count", "" + count);
                telemetry.update();
            }
        }
    }

    public void onSensorChanged(SensorEvent event) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
        float[] orientation = new float[3];
        SensorManager.getOrientation(rotationMatrix, orientation);
        gyroCurrentHeading = (float) Math.toDegrees(orientation[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void turnLeft(double speed) {
        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(speed);
        motorBackLeft.setPower(-speed);
    }

    public void turnRight(double speed) {
        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(speed);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(speed);
    }

    public void stopMotors() {
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);
    }

    public void initializeMecanum() {
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");

        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    // force all readings to between 0 and 360
    public double normalizeAngle(double angle) {
        // calculations
        // check to see if the angle is negative
        // then add to 360
        // otherwise nothing to do
        if (angle < 0) {
            return 360 + angle;
        }
        else {
            return angle;
        }
    }

    // Left turns will have a positive turnAmount and right turns a negative turnAmount
    // all angles from 0 to 360 with angles going up counter clockwise
    double calculateTargetAngle(double currentAngle, double turnAmount) {
        // Left turns are currentAngle + targetAngle
        // if a left turn goes past 360 then subtract 360 from the result
        // Right turns are currentAngle + targetAngle
        // if a right turn goes lower than 0 add 360 to the result
        double tempAngle = currentAngle + turnAmount;
        if (tempAngle > 360) {
            return tempAngle - 360;
        }
        if (tempAngle < 0) {
            return tempAngle + 360;
        }

        return tempAngle;
    }
}
