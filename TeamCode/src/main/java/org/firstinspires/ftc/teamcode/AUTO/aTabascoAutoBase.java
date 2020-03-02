package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.hardware.bosch.BNO055IMU;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;

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
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

public abstract class aTabascoAutoBase extends LinearOpMode {

    static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    static final String LABEL_STONE = "Stone";
    static final String LABEL_SKYSTONE = "Skystone";
    static final double MAX_TENSOR_FLOW_TIME = 1.0;

    final static double MECANUM_MAX_SPEED = 1.0;
    final static double SLOW_STRAFE_FACTOR = 1.4;
    final static double SLOW_TURN_FACTOR = 1.20;
    final static double SLOW_SPEED_FACTOR = 1.4;

    final static double SERVO_GATE_OPEN = 0.75;
    double initGate = 0.65;
    final static double SERVO_GATE_CLOSED = 0.22;

    final static double SERVO_GRABBER_OPEN = 0.1;
    final static double SERVO_GRABBER_CLOSED = 0.52;

    final static double SERVO_ROTATOR_START = 0.96;
    final static double SERVO_ROTATOR_MID = 0.42;
    final static double SERVO_ROTATOR_END = 0.0;

    final static double SERVO_FOUNDATIONL_UP = 0.48;
    final static double SERVO_FOUNDATIONL_DOWN = 0.8;
    final static double SERVO_FOUNDATIONR_UP = 0.76;
    final static double SERVO_FOUNDATIONR_DOWN = 0.44;

    final static double SERVO_SPATL_UP = 0.25;
    final static double SERVO_SPATL_DOWN = 0.85;
    final static double SERVO_SPATR_UP = 0.86;
    final static double SERVO_SPATR_DOWN = 0.35;

    final static double SERVO_CAPSTONE_UP = 0.65;
    final static double SERVO_CAPSTONE_DOWN = 0.15;

    int verticalTarget = 0;
    int level0 = 0;
    int verticalMax = level0 + 8250;

    final static double MAX_SPEED = 1.0;
    final static double FAST_SPEED = 0.8;
    final static double SLOW_SPEED = 0.6;
    double currentSpeed = MAX_SPEED;

    boolean isDriving = false;
    ElapsedTime runtime = new ElapsedTime();
    ElapsedTime driveTimer = new ElapsedTime();
    ElapsedTime liftTimer = new ElapsedTime();
    ElapsedTime slideTimer = new ElapsedTime();

    DcMotor motorFrontLeft;
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;
    DcMotor motorVerticalSlide;
    DcMotor motorIntakeLeft;
    DcMotor motorIntakeRight;

    Servo servoStoneGrabber;
    Servo servoStoneRotator;
    Servo servoGate;
    Servo servoFoundationL;
    Servo servoFoundationR;
    Servo servoSpatulaL;
    Servo servoSpatulaR;
    Servo servoCapstone;
    Servo servoHorizontalSlide;

    DigitalChannel touchRest;

    public enum SkystonePosition {
        Wall,
        Center,
        AwayFromWall,
        Unknown
    }
    // The IMU sensor object
    BNO055IMU imu;

    // State used for updating telemetry
    Orientation angles;

    private static final String VUFORIA_KEY =
            "AT1YKzT/////AAABmVMtTftG60sUq2c77lqV6TNMqKDr8xGL7jemnrVdEAbpW6YjC8sCFS86Cws5vb2U3vxQdu1UGXhAFGouNJ1Gqp4ktluBplgOCtivnHv7dQus3jkQFHd50GFPkwVuBEHW9mMNU/ZZxVU4QNqfWX+63emyUiWYu9BzBTvT7i0aSPpJMnfG9/VLcLHAbGFioQ7gM1cJvZ0gagDpxcLp3iGiN5imn3EyMhAvX8FywzBhU93b6PRCfmbsWdPpwF25tPSDIJYXVlTdl8U4T7E/Ylzn9ZJRbg/CNvNwpkfxD9f/jQ9Vll15YWACqqeNW26wiUu8C69Kve3ZByf1JUZ0S3J16abJv5rwShaFUrNAXAJGtGhG";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;


    public void initializeRobot() {
        initializeMecanum();
        initializeImu();
        initializeIntake();
        initializeDelivery();
        initializeFoundation();
        initializeCapstone();
        ///*
        initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("ERROR", "Unable to start Vuforia. Restart App");
        }
        if (tfod != null) {
            tfod.activate();
        }
        telemetry.update();
    }

    public void shutdownRobot() {
        if (tfod != null) {
            tfod.shutdown();
        }
        //*/
    }
    //Driving Section =============================


    public void strafeLeft (double speed, int distance) {
        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(speed);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void strafeRight (double speed, int distance) {
        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(speed);
        motorBackRight.setPower(speed);
        motorBackLeft.setPower(-speed);
        int startPosition = motorFrontRight.getCurrentPosition();
        while (opModeIsActive() && motorFrontRight.getCurrentPosition() > (startPosition - distance)) {
            telemetry.addData("EncoderPosition", motorFrontRight.getCurrentPosition());
            telemetry.update();
        }
        stopMotors();
    }
    public void sSLeft(double speed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double startAngle = angles.firstAngle;
        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(speed);

        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles.firstAngle > (startAngle + 0.5)) {
                motorFrontRight.setPower(speed * 0.9);
                motorFrontLeft.setPower(-speed);
                motorBackRight.setPower(-speed * 0.9);
                motorBackLeft.setPower(speed);
            }
            else if (angles.firstAngle < (startAngle - 0.5)) {
                motorFrontRight.setPower(speed);
                motorFrontLeft.setPower(-speed * 0.9);
                motorBackRight.setPower(-speed);
                motorBackLeft.setPower(speed * 0.9);
            }
            else {
                motorFrontRight.setPower(speed);
                motorFrontLeft.setPower(-speed);
                motorBackRight.setPower(-speed);
                motorBackLeft.setPower(speed);
            }
        }
        stopMotors();
    }
    public void sSRight(double speed, int distance) {
        int startPosition = motorFrontRight.getCurrentPosition();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double startAngle = angles.firstAngle;
        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(speed);
        motorBackRight.setPower(speed);
        motorBackLeft.setPower(-speed);
        while (opModeIsActive() && motorFrontRight.getCurrentPosition() > (startPosition - distance)) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles.firstAngle > (startAngle + 0.5)) {
                motorFrontRight.setPower(-speed * 0.9);
                motorFrontLeft.setPower(speed);
                motorBackRight.setPower(speed * 0.9);
                motorBackLeft.setPower(-speed);
            }
            else if (angles.firstAngle < (startAngle - 0.5)) {
                motorFrontRight.setPower(-speed);
                motorFrontLeft.setPower(speed * 0.9);
                motorBackRight.setPower(speed);
                motorBackLeft.setPower(-speed * 0.9);
            }
            else {
                motorFrontRight.setPower(-speed);
                motorFrontLeft.setPower(speed);
                motorBackRight.setPower(speed);
                motorBackLeft.setPower(-speed);
            }
        }
        stopMotors();
    }
    public void timedDriveForward(double speed, double seconds) {
        isDriving = true;
        driveTimer.reset();
        while (opModeIsActive() && (driveTimer.seconds() < seconds)) {
            motorFrontRight.setPower(speed);
            motorFrontLeft.setPower(speed);
            motorBackRight.setPower(speed);
            motorBackLeft.setPower(speed);
        }
        stopMotors();
    }
    public void timedDriveBackward(double speed, double seconds) {
        isDriving = true;
        driveTimer.reset();
        while (opModeIsActive() && (driveTimer.seconds() < seconds)) {
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

        while (opModeIsActive() && currentPosition < endEncoderDistance) {
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
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() < (startPosition + distance)) {
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

        while (opModeIsActive() && currentPosition > endEncoderDistance) {
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
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {
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
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    public void curveLeftB (double speed, int distance) {
        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(-speed * 0.5);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(-speed * 0.5);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void curveRightB (double speed, int distance) {
        motorFrontRight.setPower(-speed * 0.5);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(-speed * 0.5);
        motorBackLeft.setPower(-speed);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void curveRightF (double speed, int distance) {
        motorFrontRight.setPower(speed * 0.5);
        motorFrontLeft.setPower(speed);
        motorBackRight.setPower(speed * 0.5);
        motorBackLeft.setPower(speed);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    public void hardCurveRightB (double speed, int distance){
        motorFrontRight.setPower(speed * 0.5);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(speed * 0.5);
        motorBackLeft.setPower(-speed);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void hardCurveRightF (double speed, int distance){
        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(speed * 0.5);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(speed * 0.5);
        int startPosition = motorFrontRight.getCurrentPosition();
        while (opModeIsActive() && motorFrontRight.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void hardCurveLeftB (double speed, int distance){
        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(speed * 0.5);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(speed * 0.5);
        int startPosition = motorFrontRight.getCurrentPosition();
        while (opModeIsActive() && motorFrontRight.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void hardCurveLeftF (double speed, int distance){
        motorFrontRight.setPower(speed * 0.5);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(speed * 0.5);
        motorBackLeft.setPower(-speed);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void bumpLeftB (double speed, int distance) {
        int startPosition = motorFrontRight.getCurrentPosition();
        motorFrontRight.setPower(-speed);
        motorBackRight.setPower(-speed);
        while (opModeIsActive() && motorFrontRight.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void bumpRightB (double speed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        motorFrontLeft.setPower(-speed);
        motorBackLeft.setPower(-speed);
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void bumpLeftF (double speed, int distance) {
        int startPosition = motorFrontRight.getCurrentPosition();
        motorFrontRight.setPower(speed);
        motorBackRight.setPower(speed);
        while (opModeIsActive() && motorFrontRight.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    //Specialized
    public void bumpLeftFToAngle (double angle, int margin, double speed) {
        double currentAngle = getNormCurrentAngle();
        double targetAngle = angle;
        while (opModeIsActive() && (targetAngle - margin ) > currentAngle || currentAngle > (targetAngle + margin))  {
            currentAngle = getNormCurrentAngle();
            motorFrontRight.setPower(speed);
            motorBackRight.setPower(speed);
            telemetry.addData("currentAngle", currentAngle);
            telemetry.update();
        }
        stopMotors();

    }
    public void bumpRightF (double speed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        motorFrontLeft.setPower(speed);
        motorBackLeft.setPower(speed);
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    public double normalizeAngle(double angle) {
        if (angle < 0) {
            return 360 + angle;
        } else {
            return angle;
        }
    }
    public void correctAngle (int margin, double targetAngle, double maxSpeed, double minSpeed) {
        double normalizedAngle = getNormCurrentAngle();
        if(normalizedAngle < (targetAngle - margin)) {
            getNormCurrentAngle();
            telemetry.addData("runningLeft", "runningLeft");
            telemetry.update();

            turnLeftToAngle(targetAngle, maxSpeed, minSpeed);

        }
        else if (normalizedAngle > (targetAngle + margin)) {
            getNormCurrentAngle();
            telemetry.addData("runningRight", "runningRight");
            telemetry.update();

            turnRightToAngle(targetAngle, maxSpeed, minSpeed);

        }
    }
    public void takeCurrentAngle(){
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", normalizeAngle(angles.firstAngle));
        telemetry.update();
    }
    public double getNormCurrentAngle(){
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", normalizeAngle(angles.firstAngle));
        telemetry.update();
        return normalizeAngle(angles.firstAngle);
    }
    public void spinLeft (double turnAngle, double maxSpeed, double minSpeed) {
        double currentAngle = getNormCurrentAngle();
        double startScaling = 0.01;
        double startingAngle = currentAngle;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;


        while (opModeIsActive() && currentAngle < (startingAngle + turnAngle)) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentAngle = angles.firstAngle;
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
            while (opModeIsActive() && currentAngle > 2) {
                currentAngle = getNormCurrentAngle();
                turnLeft(maxSpeed);
            }
        }
        double startScaling = 0.01;
        double startingAngle = currentAngle;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;


        while (opModeIsActive() && currentAngle < targetAngle) {
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
        double startScaling = 0.01;
        double startingAngle = currentAngle;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;

        while (opModeIsActive() && currentAngle > (startingAngle - turnAngle)) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentAngle = angles.firstAngle;
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
            while (opModeIsActive() && (currentAngle < 358)) {
                currentAngle = getNormCurrentAngle();
                turnRight(maxSpeed);
            }
        }
        currentAngle = getNormCurrentAngle();
        double startingAngle = currentAngle;
        double startScaling = 0.01;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;

        while (opModeIsActive() && ((currentAngle > targetAngle) || (currentAngle > startingAngle))) {
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
    public SkystonePosition findSkystone(String allianceColor) {
        ElapsedTime tensorFlowTimeout = new ElapsedTime();
        SkystonePosition position;

        if (tfod != null)
        {
            while (opModeIsActive() && tensorFlowTimeout.seconds() < MAX_TENSOR_FLOW_TIME)
            {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_SKYSTONE)) {
                            telemetry.addData("label", recognition.getLabel());
                            telemetry.addData("Left", "%.03f", recognition.getLeft());
                            telemetry.addData("Right", "%.03f", recognition.getRight());
                            telemetry.addData("Width", "%.03f", recognition.getRight() - recognition.getLeft());
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
                            telemetry.addData("Position Found", position.toString());
                            telemetry.update();

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
        if (right < 275) {
            return SkystonePosition.Wall;
        } else if (right < 475) {
            return SkystonePosition.Center;
        } else {
            return SkystonePosition.AwayFromWall;
        }
    }














    //Function Section ===============================

    public void delay(double time) {
        ElapsedTime delayTimer = new ElapsedTime();
        while (opModeIsActive() && delayTimer.seconds() < time) {
        }
    }
    public void stonePosition () {
        motorVerticalSlide.setTargetPosition(level0);
        //improve later
        delay(0.5);
    }
    public void intakeIn () {
        motorIntakeLeft.setPower(1.0);
        motorIntakeRight.setPower(1.0);
    }
    public void intakeOut () {
        motorIntakeLeft.setPower(-1.0);
        motorIntakeRight.setPower(-1.0);
    }
    public void intakeOff () {
        motorIntakeLeft.setPower(0.0);
        motorIntakeRight.setPower(0.0);
    }
    public void verticalSlide (double power, double time) {
        liftTimer.reset();
        if (power < 0.0) {
            while (opModeIsActive() && liftTimer.seconds() < time) {
                //go
                motorVerticalSlide.setPower(power);
            }
        } else {
            while (opModeIsActive() && liftTimer.seconds() < time && !isLiftAtBottom()) {
                //go
                motorVerticalSlide.setPower(power);
            }
        }
        //- up + down?

        motorVerticalSlide.setPower(0.0);

    }
    public void verticalSlideUp(double time) {
        verticalSlide(-1.0, time);
    }
    public void verticalSlideDown(double time) {
        verticalSlide(0.75, time);
    }
    public void horizontalSlide (double position, double time) {
        //motorHorizontalSlide.setPower(power);
        slideTimer.reset();
        servoHorizontalSlide.setPosition(position);
        while (opModeIsActive() && slideTimer.seconds() < time) {
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
    public void grabStone () {
        servoStoneGrabber.setPosition(SERVO_GRABBER_CLOSED);
    }
    public void releaseStone () {
        servoStoneGrabber.setPosition(SERVO_GRABBER_OPEN);
    }
    public void stoneRotatorStart () {
        servoStoneRotator.setPosition(SERVO_ROTATOR_START);
    }
    public void stoneRotatorMid () {
        servoStoneRotator.setPosition(SERVO_ROTATOR_MID);
    }
    public void stoneRotatorEnd () {
        servoStoneRotator.setPosition(SERVO_ROTATOR_END);
    }
    public void gateOpen () {
        servoGate.setPosition(SERVO_GATE_OPEN);
    }
    public void gateClose () {
        servoGate.setPosition(SERVO_GATE_CLOSED);
    }
    public void grabFoundation () {
        servoFoundationL.setPosition(SERVO_FOUNDATIONL_DOWN);
        servoFoundationR.setPosition(SERVO_FOUNDATIONR_DOWN);
    }
    public void releaseFoundation () {
        servoFoundationL.setPosition(SERVO_FOUNDATIONL_UP);
        servoFoundationR.setPosition(SERVO_FOUNDATIONR_UP);
    }
    public void ricePattyL() {
        servoSpatulaL.setPosition(SERVO_SPATL_UP);
    }
    public void lowerSpatL () {
        servoSpatulaL.setPosition(SERVO_SPATL_DOWN);
    }
    public void ricePattyR() {
        servoSpatulaR.setPosition(SERVO_SPATR_UP);
    }
    public void lowerSpatR () {
        servoSpatulaR.setPosition(SERVO_SPATR_DOWN);
    }
    public boolean isLiftAtBottom() {
        return !touchRest.getState();
    }













    //Initialization Section =============================

    public void initializeMecanum()
    {
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");

        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBackLeft.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void initializeIntake () {
        motorIntakeLeft = hardwareMap.dcMotor.get("motorIntakeLeft");
        motorIntakeRight = hardwareMap.dcMotor.get("motorIntakeRight");

        motorIntakeLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorIntakeLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorIntakeRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorIntakeRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorIntakeLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        motorIntakeRight.setDirection(DcMotorSimple.Direction.REVERSE);

        servoSpatulaL = hardwareMap.servo.get("servoSpatL");
        servoSpatulaR = hardwareMap.servo.get("servoSpatR");

        servoSpatulaL.setPosition(SERVO_SPATL_UP);
        servoSpatulaR.setPosition(SERVO_SPATR_UP);

        servoGate = hardwareMap.servo.get("servoGate");

        servoGate.setPosition(initGate);
    }
    public void initializeDelivery () {
        //motorHorizontalSlide = hardwareMap.dcMotor.get("motorHorizontalSlide");
        motorVerticalSlide = hardwareMap.dcMotor.get("motorVerticalSlide");

        motorVerticalSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //motorVerticalSlide.setTargetPosition(levelCap);
        //motorVerticalSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //motorVerticalSlide.setPower(1.0);
        motorVerticalSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorVerticalSlide.setDirection(DcMotorSimple.Direction.FORWARD);

        servoStoneGrabber = hardwareMap.servo.get("servoStoneGrabber");
        servoStoneRotator = hardwareMap.servo.get("servoStoneRotator");
        servoHorizontalSlide = hardwareMap.servo.get("servoHorizontalSlide");

        servoStoneGrabber.setPosition(SERVO_GRABBER_OPEN);
        servoStoneRotator.setPosition(SERVO_ROTATOR_START);

        touchRest = hardwareMap.get(DigitalChannel.class,"touchRest");
        touchRest.setMode(DigitalChannel.Mode.INPUT);

    }
    public void initializeFoundation() {
        servoFoundationL = hardwareMap.servo.get("servoFoundationL");
        servoFoundationR = hardwareMap.servo.get("servoFoundationR");
        /*
        sensorFoundationRight = hardwareMap.get(DigitalChannel.class, "SFRight");
        sensorFoundationLeft = hardwareMap.get(DigitalChannel.class, "SFLeft");
        sensorFoundationRight.setMode(DigitalChannel.Mode.INPUT);
        sensorFoundationLeft.setMode(DigitalChannel.Mode.INPUT);
        */
        releaseFoundation ();
    }
    public void initializeCapstone() {
        servoCapstone = hardwareMap.servo.get("servoCapstone");

        servoCapstone.setPosition(SERVO_CAPSTONE_UP);
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