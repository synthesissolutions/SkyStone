package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.TELE.Tabasco;

public class AutoTabasco extends Tabasco {

    public enum SkystonePosition {
        Wall,
        Center,
        AwayFromWall,
        Unknown
    }
    // The IMU sensor object
    BNO055IMU imu;

    ElapsedTime driveTimer = new ElapsedTime();

    // State used for updating telemetry
    Orientation angles;

    static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    static final String LABEL_STONE = "Stone";
    static final String LABEL_SKYSTONE = "Skystone";
    static final double MAX_TENSOR_FLOW_TIME = 1.0;

    private static final String VUFORIA_KEY =
            "AT1YKzT/////AAABmVMtTftG60sUq2c77lqV6TNMqKDr8xGL7jemnrVdEAbpW6YjC8sCFS86Cws5vb2U3vxQdu1UGXhAFGouNJ1Gqp4ktluBplgOCtivnHv7dQus3jkQFHd50GFPkwVuBEHW9mMNU/ZZxVU4QNqfWX+63emyUiWYu9BzBTvT7i0aSPpJMnfG9/VLcLHAbGFioQ7gM1cJvZ0gagDpxcLp3iGiN5imn3EyMhAvX8FywzBhU93b6PRCfmbsWdPpwF25tPSDIJYXVlTdl8U4T7E/Ylzn9ZJRbg/CNvNwpkfxD9f/jQ9Vll15YWACqqeNW26wiUu8C69Kve3ZByf1JUZ0S3J16abJv5rwShaFUrNAXAJGtGhG";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    LinearOpMode autoOpMode;

    public AutoTabasco() {
    }

    public void initializeRobotA(HardwareMap ahwMap, LinearOpMode opMode) {
        autoOpMode = opMode;

        initializeRobot(ahwMap);
        initializeDeliveryAuto();
        initializeImu();
        ///*
        initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            autoOpMode.telemetry.addData("ERROR", "Unable to start Vuforia. Restart App");
        }
        if (tfod != null) {
            tfod.activate();
        }
        autoOpMode.telemetry.update();
    }

    public void shutdownRobot() {
        if (tfod != null) {
            tfod.shutdown();
        }
        //*/
    }

    public void prepRobot() {
        motorVerticalSlide.setTargetPosition(-1900);
        delay(0.5);
        motorVerticalSlide.setTargetPosition(0);
        gateOpen();
    }

    public void initializeDeliveryAuto() {
        motorVerticalSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorVerticalSlide.setTargetPosition(-20);
        motorVerticalSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorVerticalSlide.setPower(1.0);
    }

    public void delay(double time) {
        ElapsedTime delayTimer = new ElapsedTime();
        while (autoOpMode.opModeIsActive() && delayTimer.seconds() < time) {
        }
    }


    //Driving Section =============================


    public void strafeLeft (double speed, int distance) {
        double frontRightSpeed = speed;
        double frontLeftSpeed = -speed;
        double backRightSpeed = -speed;
        double backLeftSpeed = speed;

        motorFrontRight.setPower(frontRightSpeed);
        motorFrontLeft.setPower(frontLeftSpeed);
        motorBackRight.setPower(backRightSpeed);
        motorBackLeft.setPower(backLeftSpeed);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (autoOpMode.opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void strafeRight (double speed, int distance) {
        double frontRightSpeed = -speed * 0.8;
        double frontLeftSpeed = speed;
        double backRightSpeed = speed;
        double backLeftSpeed = -speed;

        motorFrontRight.setPower(frontRightSpeed);
        motorFrontLeft.setPower(frontLeftSpeed);
        motorBackRight.setPower(backRightSpeed);
        motorBackLeft.setPower(backLeftSpeed);
        int startPosition = motorFrontRight.getCurrentPosition();
        while (autoOpMode.opModeIsActive() && motorFrontRight.getCurrentPosition() > (startPosition - distance)) {
            autoOpMode.telemetry.addData("EncoderPosition", motorFrontRight.getCurrentPosition());
            autoOpMode.telemetry.update();
        }
        stopMotors();
    }
    public void sSLeft(double speed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double startAngle = angles.firstAngle;

        double frontRightSpeed = speed;
        double frontLeftSpeed = -speed;
        double backRightSpeed = -speed;
        double backLeftSpeed = speed;

        motorFrontRight.setPower(frontRightSpeed);
        motorFrontLeft.setPower(frontLeftSpeed);
        motorBackRight.setPower(backRightSpeed);
        motorBackLeft.setPower(backLeftSpeed);

        while (autoOpMode.opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles.firstAngle > (startAngle + 0.5)) {
                motorFrontRight.setPower(frontRightSpeed * 0.9);
                motorFrontLeft.setPower(frontLeftSpeed);
                motorBackRight.setPower(backRightSpeed * 0.9);
                motorBackLeft.setPower(backLeftSpeed);
            }
            else if (angles.firstAngle < (startAngle - 0.5)) {
                motorFrontRight.setPower(frontRightSpeed);
                motorFrontLeft.setPower(frontLeftSpeed * 0.9);
                motorBackRight.setPower(backRightSpeed);
                motorBackLeft.setPower(backLeftSpeed * 0.9);
            }
            else {
                motorFrontRight.setPower(frontRightSpeed);
                motorFrontLeft.setPower(frontLeftSpeed);
                motorBackRight.setPower(backRightSpeed);
                motorBackLeft.setPower(backLeftSpeed);
            }
        }
        stopMotors();
    }
    public void sSRight(double speed, int distance) {
        int startPosition = motorFrontRight.getCurrentPosition();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double startAngle = angles.firstAngle;

        double frontRightSpeed = -speed * 0.8;
        double frontLeftSpeed = speed;
        double backRightSpeed = speed;
        double backLeftSpeed = -speed;

        motorFrontRight.setPower(frontRightSpeed);
        motorFrontLeft.setPower(frontLeftSpeed);
        motorBackRight.setPower(backRightSpeed);
        motorBackLeft.setPower(backLeftSpeed);

        while (autoOpMode.opModeIsActive() && motorFrontRight.getCurrentPosition() > (startPosition - distance)) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles.firstAngle > (startAngle + 0.5)) {
                motorFrontRight.setPower(frontRightSpeed * 0.9);
                motorFrontLeft.setPower(frontLeftSpeed);
                motorBackRight.setPower(backRightSpeed * 0.9);
                motorBackLeft.setPower(backLeftSpeed);
            }
            else if (angles.firstAngle < (startAngle - 0.5)) {
                motorFrontRight.setPower(frontRightSpeed);
                motorFrontLeft.setPower(frontLeftSpeed * 0.9);
                motorBackRight.setPower(backRightSpeed);
                motorBackLeft.setPower(backLeftSpeed * 0.9);
            }
            else {
                motorFrontRight.setPower(frontRightSpeed);
                motorFrontLeft.setPower(frontLeftSpeed);
                motorBackRight.setPower(backRightSpeed);
                motorBackLeft.setPower(backLeftSpeed);
            }
        }
        stopMotors();
    }
    public void timedDriveForward(double speed, double seconds) {
        driveTimer.reset();
        while (autoOpMode.opModeIsActive() && (driveTimer.seconds() < seconds)) {
            motorFrontRight.setPower(speed);
            motorFrontLeft.setPower(speed);
            motorBackRight.setPower(speed);
            motorBackLeft.setPower(speed);
        }
        stopMotors();
    }
    public void timedDriveBackward(double speed, double seconds) {
        driveTimer.reset();
        while (autoOpMode.opModeIsActive() && (driveTimer.seconds() < seconds)) {
            motorFrontRight.setPower(-speed);
            motorFrontLeft.setPower(-speed);
            motorBackRight.setPower(-speed);
            motorBackLeft.setPower(-speed);
        }
        stopMotors();
    }
    public int driveStraightForwardRampDown(double startSpeed, double endSpeed, int distance, int rampDownDistance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        int currentPosition = startPosition;
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double startAngle = angles.firstAngle;
        double percentComplete = 0.0;
        int endEncoderDistance = startPosition + distance;
        int startRampDownEncoderDistance = endEncoderDistance - rampDownDistance; // start ramping down with 500 encoder units left

        double speed = startSpeed;

        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(speed);
        motorBackRight.setPower(speed);
        motorBackLeft.setPower(speed);

        while (autoOpMode.opModeIsActive() && currentPosition < endEncoderDistance) {
            int distanceRemaining = Math.abs(currentPosition - endEncoderDistance);

            if (distanceRemaining < rampDownDistance) {
                double rampPercent = distanceRemaining / rampDownDistance;
                speed = endSpeed + (rampPercent * (startSpeed - endSpeed));
            }

            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles.firstAngle > (startAngle + 0.5)) {
                motorFrontRight.setPower(speed * 0.9);
                motorFrontLeft.setPower(speed);
                motorBackRight.setPower(speed * 0.9);
                motorBackLeft.setPower(speed);
            }
            else if (angles.firstAngle < (startAngle - 0.5)) {
                motorFrontRight.setPower(speed);
                motorFrontLeft.setPower(speed * 0.9);
                motorBackRight.setPower(speed);
                motorBackLeft.setPower(speed * 0.9);
            }
            else {
                motorFrontRight.setPower(speed);
                motorFrontLeft.setPower(speed);
                motorBackRight.setPower(speed);
                motorBackLeft.setPower(speed);
            }

            currentPosition = motorFrontLeft.getCurrentPosition();
        }
        stopMotors();

        return currentPosition;
    }
    public void driveStraightForward(double speed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double startAngle = angles.firstAngle;
        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(speed);
        motorBackRight.setPower(speed);
        motorBackLeft.setPower(speed);
        while (autoOpMode.opModeIsActive() && motorFrontLeft.getCurrentPosition() < (startPosition + distance)) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles.firstAngle > (startAngle + 0.5)) {
                motorFrontRight.setPower(speed * 0.9);
                motorFrontLeft.setPower(speed);
                motorBackRight.setPower(speed * 0.9);
                motorBackLeft.setPower(speed);
            }
            else if (angles.firstAngle < (startAngle - 0.5)) {
                motorFrontRight.setPower(speed);
                motorFrontLeft.setPower(speed * 0.9);
                motorBackRight.setPower(speed);
                motorBackLeft.setPower(speed * 0.9);
            }
            else {
                motorFrontRight.setPower(speed);
                motorFrontLeft.setPower(speed);
                motorBackRight.setPower(speed);
                motorBackLeft.setPower(speed);
            }
        }
        stopMotors();
    }
    public int driveStraightBackRampDown(double startSpeed, double endSpeed, int distance, int rampDownDistance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        int currentPosition = startPosition;
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double startAngle = angles.firstAngle;
        double percentComplete = 0.0;
        int endEncoderDistance = startPosition - distance;
        int startRampDownEncoderDistance = endEncoderDistance - rampDownDistance; // start ramping down with 500 encoder units left

        double speed = startSpeed;

        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(-speed);

        while (autoOpMode.opModeIsActive() && currentPosition > endEncoderDistance) {
            int distanceRemaining = Math.abs(endEncoderDistance - currentPosition);
            if (distanceRemaining < rampDownDistance) {
                double rampPercent = distanceRemaining / rampDownDistance;
                speed = endSpeed + (rampPercent * (startSpeed - endSpeed));
            }
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles.firstAngle > (startAngle + 0.5)) {
                motorFrontRight.setPower(-speed);
                motorFrontLeft.setPower(-speed * 0.9);
                motorBackRight.setPower(-speed);
                motorBackLeft.setPower(-speed * 0.9);
            }
            else if (angles.firstAngle < (startAngle - 0.5)) {
                motorFrontRight.setPower(-speed * 0.9);
                motorFrontLeft.setPower(-speed);
                motorBackRight.setPower(-speed * 0.9);
                motorBackLeft.setPower(-speed);
            }
            else {
                motorFrontRight.setPower(-speed);
                motorFrontLeft.setPower(-speed);
                motorBackRight.setPower(-speed);
                motorBackLeft.setPower(-speed);
            }

            currentPosition = motorFrontLeft.getCurrentPosition();
        }
        stopMotors();

        return currentPosition;
    }
    public void driveStraightBack(double speed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double startAngle = angles.firstAngle;
        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(-speed);
        while (autoOpMode.opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles.firstAngle > (startAngle + 0.5)) {
                motorFrontRight.setPower(-speed);
                motorFrontLeft.setPower(-speed * 0.9);
                motorBackRight.setPower(-speed);
                motorBackLeft.setPower(-speed * 0.9);
            }
            else if (angles.firstAngle < (startAngle - 0.5)) {
                motorFrontRight.setPower(-speed * 0.9);
                motorFrontLeft.setPower(-speed);
                motorBackRight.setPower(-speed * 0.9);
                motorBackLeft.setPower(-speed);
            }
            else {
                motorFrontRight.setPower(-speed);
                motorFrontLeft.setPower(-speed);
                motorBackRight.setPower(-speed);
                motorBackLeft.setPower(-speed);
            }
        }
        stopMotors();
    }
    public void stopMotors() {
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);
    }
    public void driveForward(double power) {
        motorFrontRight.setPower(power);
        motorFrontLeft.setPower(power);
        motorBackRight.setPower(power);
        motorBackLeft.setPower(power);
    }
    public void driveBack(double power) {
        motorFrontRight.setPower(-power);
        motorFrontLeft.setPower(-power);
        motorBackRight.setPower(-power);
        motorBackLeft.setPower(-power);
    }















    //Turning Section ===================================

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
    public void curveLeftF (double speed, int distance) {
        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(speed * 0.7);
        motorBackRight.setPower(speed);
        motorBackLeft.setPower(speed * 0.7);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (autoOpMode.opModeIsActive() && motorFrontLeft.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    public void curveLeftB (double speed, int distance) {
        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(-speed * 0.5);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(-speed * 0.5);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (autoOpMode.opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void curveRightB (double speed, int distance) {
        motorFrontRight.setPower(-speed * 0.5);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(-speed * 0.5);
        motorBackLeft.setPower(-speed);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (autoOpMode.opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void curveRightF (double speed, int distance) {
        motorFrontRight.setPower(speed * 0.5);
        motorFrontLeft.setPower(speed);
        motorBackRight.setPower(speed * 0.5);
        motorBackLeft.setPower(speed);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (autoOpMode.opModeIsActive() && motorFrontLeft.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    public void hardCurveRightB (double speed, int distance){
        motorFrontRight.setPower(speed * 0.5);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(speed * 0.5);
        motorBackLeft.setPower(-speed);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (autoOpMode.opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void hardCurveRightF (double speed, int distance){
        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(speed * 0.5);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(speed * 0.5);
        int startPosition = motorFrontRight.getCurrentPosition();
        while (autoOpMode.opModeIsActive() && motorFrontRight.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void hardCurveLeftB (double speed, int distance){
        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(speed * 0.5);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(speed * 0.5);
        int startPosition = motorFrontRight.getCurrentPosition();
        while (autoOpMode.opModeIsActive() && motorFrontRight.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void hardCurveLeftF (double speed, int distance){
        motorFrontRight.setPower(speed * 0.5);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(speed * 0.5);
        motorBackLeft.setPower(-speed);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (autoOpMode.opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void bumpLeftB (double speed, int distance) {
        int startPosition = motorFrontRight.getCurrentPosition();
        motorFrontRight.setPower(-speed);
        motorBackRight.setPower(-speed);
        while (autoOpMode.opModeIsActive() && motorFrontRight.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void bumpRightB (double speed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        motorFrontLeft.setPower(-speed);
        motorBackLeft.setPower(-speed);
        while (autoOpMode.opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void bumpLeftF (double speed, int distance) {
        int startPosition = motorFrontRight.getCurrentPosition();
        motorFrontRight.setPower(speed);
        motorBackRight.setPower(speed);
        while (autoOpMode.opModeIsActive() && motorFrontRight.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    //Specialized
    public void bumpLeftFToAngle (double angle, int margin, double speed) {
        double currentAngle = getNormCurrentAngle();
        double targetAngle = angle;
        while (autoOpMode.opModeIsActive() && (targetAngle - margin ) > currentAngle || currentAngle > (targetAngle + margin))  {
            currentAngle = getNormCurrentAngle();
            motorFrontRight.setPower(speed);
            motorBackRight.setPower(speed);
            autoOpMode.telemetry.addData("currentAngle", currentAngle);
            autoOpMode.telemetry.update();
        }
        stopMotors();

    }
    public void bumpRightF (double speed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        motorFrontLeft.setPower(speed);
        motorBackLeft.setPower(speed);
        while (autoOpMode.opModeIsActive() && motorFrontLeft.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    //Specialized
    public void bumpRightFToAngle (double angle, int margin, double speed) {
        double currentAngle = getNormCurrentAngle();
        double targetAngle = angle;
        while (autoOpMode.opModeIsActive() && (targetAngle - margin ) > currentAngle || currentAngle > (targetAngle + margin))  {
            currentAngle = getNormCurrentAngle();
            motorFrontLeft.setPower(speed);
            motorBackLeft.setPower(speed);
            autoOpMode.telemetry.addData("currentAngle", currentAngle);
            autoOpMode.telemetry.update();
        }
        stopMotors();

    }
    public double normalizeAngle(double angle) {
        if (angle < 0) {
            return 360 + angle;
        }
        else if (angle >= 360) {
            return angle - 360;
        }
        else {
            return angle;
        }
    }
    public void correctAngle(int margin, double targetAngle, double maxSpeed, double minSpeed) {
        double currentAngle = getNormCurrentAngle();

        boolean crossingZeroFromLeft = (91 > currentAngle && 269 < targetAngle);
        boolean crossingZeroFromRight = (269 < currentAngle && 91 > targetAngle);

        if (crossingZeroFromLeft){
            while (autoOpMode.opModeIsActive() && (91 > currentAngle)) {
                currentAngle = getNormCurrentAngle();
                turnRight(maxSpeed);
            }
            stopMotors();
        }
        else if (crossingZeroFromRight) {
            while (autoOpMode.opModeIsActive() && (269 < currentAngle)) {
                currentAngle = getNormCurrentAngle();
                turnLeft(maxSpeed);
            }
            stopMotors();
        }
        if (targetAngle == 0) {
            //if its on right turn left
            //if on left turn right
            if (currentAngle < 180) {
                turnRightToAngle(targetAngle, maxSpeed, minSpeed);
            }
            else if (currentAngle > 180) {
                turnLeftToAngle(targetAngle, maxSpeed, minSpeed);
            }
        }

        else if (currentAngle < (targetAngle - margin)) {
            turnLeftToAngle(targetAngle, maxSpeed, minSpeed);
        }
        else if (currentAngle > (targetAngle + margin)) {
            turnRightToAngle(targetAngle, maxSpeed, minSpeed);

        }
    }
    public void takeCurrentAngle(){
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        autoOpMode.telemetry.addData("currentAngle", normalizeAngle(angles.firstAngle));
        autoOpMode.telemetry.update();
    }
    public double getNormCurrentAngle(){
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        autoOpMode.telemetry.addData("currentAngle", normalizeAngle(angles.firstAngle));
        autoOpMode.telemetry.update();
        return normalizeAngle(angles.firstAngle);
    }
    public void spinLeft (double turnAngle, double maxSpeed, double minSpeed) {
        double currentAngle = getNormCurrentAngle();
        boolean crossingZero = (normalizeAngle(currentAngle + turnAngle) < currentAngle);
        if (crossingZero) {
            while (autoOpMode.opModeIsActive() && currentAngle > 2) {
                currentAngle = getNormCurrentAngle();
                turnLeft(maxSpeed);
            }
        }
        currentAngle = getNormCurrentAngle();
        double startScaling = 0.01;
        double startingAngle = currentAngle;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;


        while (autoOpMode.opModeIsActive() && currentAngle < (startingAngle + turnAngle)) {
            currentAngle = getNormCurrentAngle();
            double percentComplete = (currentAngle - startingAngle) / ((startingAngle + turnAngle) - startingAngle);

            if (percentComplete > startScaling) {
                currentSpeed = (minSpeed + deltaSpeed * (1 - (percentComplete - startScaling) / (1.0 - startScaling)));
            } else {
                currentSpeed = maxSpeed;
            }
            turnLeft(currentSpeed);

        }
        stopMotors();
    }
    public void turnLeftToAngle (double targetAngle, double maxSpeed, double minSpeed) {
        double currentAngle = getNormCurrentAngle();
        boolean crossingZero = (currentAngle > targetAngle);
        if (crossingZero) {
            while (autoOpMode.opModeIsActive() && currentAngle > 2) {
                currentAngle = getNormCurrentAngle();
                turnLeft(maxSpeed);
            }
        }
        currentAngle = getNormCurrentAngle();
        double startScaling = 0.01;
        double startingAngle = currentAngle;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;


        while (autoOpMode.opModeIsActive() && currentAngle < targetAngle) {
            currentAngle = getNormCurrentAngle();
            double percentComplete = (currentAngle - startingAngle) / (targetAngle - startingAngle);

            if (percentComplete > startScaling) {
                currentSpeed = (minSpeed + deltaSpeed * (1 - (percentComplete - startScaling) / (1.0 - startScaling)));
                takeCurrentAngle();
            } else {
                currentSpeed = maxSpeed;
                takeCurrentAngle();
            }
            turnLeft(currentSpeed);
            takeCurrentAngle();

        }
        stopMotors();
    }
    public void spinRight(double turnAngle, double maxSpeed, double minSpeed) {
        double currentAngle = getNormCurrentAngle();
        boolean crossingZero = (normalizeAngle(currentAngle - turnAngle) > currentAngle);
        if (crossingZero) {
            while (autoOpMode.opModeIsActive() && (currentAngle < 358)) {
                currentAngle = getNormCurrentAngle();
                turnRight(maxSpeed);
            }
        }
        currentAngle = getNormCurrentAngle();
        double startScaling = 0.01;
        double startingAngle = currentAngle;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;

        while (autoOpMode.opModeIsActive() && currentAngle > (startingAngle - turnAngle)) {
            currentAngle = getNormCurrentAngle();
            double percentComplete = (currentAngle - startingAngle) / ((startingAngle - turnAngle) - startingAngle);

            if (percentComplete > startScaling) {
                currentSpeed = (minSpeed + deltaSpeed * (1 - (percentComplete - startScaling) / (1.0 - startScaling)));
            } else {
                currentSpeed = maxSpeed;
            }
            turnRight(currentSpeed);

        }
        stopMotors();
    }

    public void turnRightToAngle (double targetAngle, double maxSpeed, double minSpeed) {
        double currentAngle = getNormCurrentAngle();
        boolean crossingZero = (currentAngle < targetAngle);
        if (crossingZero) {
            while (autoOpMode.opModeIsActive() && (currentAngle < 358)) {
                currentAngle = getNormCurrentAngle();
                turnRight(maxSpeed);
            }
        }
        currentAngle = getNormCurrentAngle();
        double startingAngle = currentAngle;
        double startScaling = 0.01;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;

        while (autoOpMode.opModeIsActive() && currentAngle > targetAngle && currentAngle <= startingAngle) {
            currentAngle = getNormCurrentAngle();
            double percentComplete = (currentAngle - startingAngle) / (targetAngle - startingAngle);

            if (percentComplete > startScaling) {
                currentSpeed = (minSpeed + deltaSpeed * (1 - (percentComplete - startScaling) / (1.0 - startScaling)));

            } else {
                currentSpeed = maxSpeed;

            }
            turnRight(currentSpeed);
            takeCurrentAngle();
        }

        stopMotors();
    }
    public void horizontalSlide (double position, double time) {
        ElapsedTime slideTimer = new ElapsedTime();
        slideTimer.reset();
        servoHorizontalSlide.setPosition(position);
        while (autoOpMode.opModeIsActive() && slideTimer.seconds() < time) {
            //move
        }
        servoHorizontalSlide.setPosition(0.5);
    }
    public void horizontalSlideIn (double time) {
        horizontalSlide (1.0, time);
    }
    public void horizontalSlideOut (double time) {
        horizontalSlide (0.0, time);
    }
    public void horizontalSlideStop() {
        servoHorizontalSlide.setPosition(0.5);
    }
    public SkystonePosition findSkystone(String allianceColor) {
        ElapsedTime tensorFlowTimeout = new ElapsedTime();
        SkystonePosition position;

        if (tfod != null)
        {
            while (autoOpMode.opModeIsActive() && tensorFlowTimeout.seconds() < MAX_TENSOR_FLOW_TIME)
            {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    autoOpMode.telemetry.addData("# Object Detected", updatedRecognitions.size());
                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_SKYSTONE)) {
                            autoOpMode.telemetry.addData("label", recognition.getLabel());
                            autoOpMode.telemetry.addData("Left", "%.03f", recognition.getLeft());
                            autoOpMode.telemetry.addData("Right", "%.03f", recognition.getRight());
                            autoOpMode.telemetry.addData("Width", "%.03f", recognition.getRight() - recognition.getLeft());
                            if (allianceColor.toLowerCase().equals("blue"))
                            {
                                position = findBlueSkyStone(recognition.getLeft(), recognition.getRight());
                            }
                            else
                            {
                                position = findRedSkyStone(recognition.getLeft(), recognition.getRight());
                            }
                            // TODO save all of the recognition data and position detected
                            //  to a file on the phone to review after the match
                            autoOpMode.telemetry.addData("Position Found", position.toString());
                            autoOpMode.telemetry.update();

                            return position;
                        }
                    }
                }
            }

            return SkystonePosition.Unknown;
        }
        else
        {
            return SkystonePosition.Unknown;
        }
    }
    private SkystonePosition findBlueSkyStone(float left, float right)
    {
        if (right < 275) {
            return SkystonePosition.AwayFromWall;
        } else if (right < 475) {
            return SkystonePosition.Center;
        } else {
            return SkystonePosition.Wall;
        }
    }

    private SkystonePosition findRedSkyStone(float left, float right)
    {
        if (left < 50) {
            return SkystonePosition.Wall;
        } else if (left < 300) {
            return SkystonePosition.Center;
        } else {
            return SkystonePosition.AwayFromWall;
        }
    }

    public void initializeImu() {
        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = false;
        parameters.loggingTag = "IMU";
        //parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_STONE, LABEL_SKYSTONE);
    }
    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }
}