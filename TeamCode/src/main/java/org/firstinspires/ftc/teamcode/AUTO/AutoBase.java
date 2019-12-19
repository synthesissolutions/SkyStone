package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.hardware.bosch.BNO055IMU;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.Locale;

public abstract class AutoBase extends LinearOpMode {

    static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    static final String LABEL_FIRST_ELEMENT = "Stone";
    static final String LABEL_SECOND_ELEMENT = "Skystone";

    final static double MECANUM_MAX_SPEED = 1.0;
    final static double SLOW_STRAFE_FACTOR = 1.4;
    final static double SLOW_TURN_FACTOR = 1.20;
    final static double SLOW_SPEED_FACTOR = 1.4;

    final static double SERVO_GATE_OPEN = 0.8;
    final static double SERVO_GATE_CLOSED = 0.1;
    final static double SERVO_GRABBER_OPEN = 0.4;
    final static double SERVO_GRABBER_CLOSED = 0.8;
    final static double SERVO_ROTATOR_START = 0.95;
    final static double SERVO_ROTATOR_MID = 0.5;
    final static double SERVO_ROTATOR_END = 0.0;
    final static double SERVO_FOUNDATION_UP = 1.0;
    final static double SERVO_FOUNDATION_DOWN = 0.0;
    final static double SERVO_SPAT_UP = 0.0;
    final static double SERVO_SPAT_DOWN = 0.98;
    final static double SERVO_CAPSTONE_UP = 0.9;
    final static double SERVO_CAPSTONE_DROP = 0.33;
    final static double SERVO_CAPSTONE_DOWN = 0.0;
    final static double SERVO_REST_ARM_EXTEND = 0.08;
    final static double SERVO_REST_ARM_RETRACT = 1.0;


    final static int VERTICAL_STEP = 15;
    final static int VERTICAL_MAX = -3300;
    int verticalTarget = 0;
    int levelCap = 0;
    int level1 = -315;
    int level2 = -600;
    int levelRest = -350;

    final static double MAX_SPEED = 1.0;
    final static double FAST_SPEED = 0.8;
    final static double SLOW_SPEED = 0.6;
    double currentSpeed = MAX_SPEED;

    boolean isCaptoneDropping = false;
    ElapsedTime capstoneDropTimer = new ElapsedTime();
    boolean isLiftReturning = false;
    ElapsedTime returnLiftTimer = new ElapsedTime();
    boolean isVDelayActive = false;
    ElapsedTime verticalDelay = new ElapsedTime();
    ElapsedTime runtime = new ElapsedTime();
    boolean isDriving = false;
    ElapsedTime driveTimer = new ElapsedTime();
    /*boolean isLiftClear = false;
    boolean isHoming = false;
    ElapsedTime homingTimer = new ElapsedTime();*/

    DcMotor motorFrontLeft;
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;
    DcMotor motorVerticalSlide;
    DcMotor motorHorizontalSlide;
    DcMotor motorIntakeLeft;
    DcMotor motorIntakeRight;

    Servo servoStoneGrabber;
    Servo servoStoneRotator;
    Servo servoGate;
    Servo servoFoundation;
    Servo servoSpatula;
    Servo servoCapstone;
    Servo servoRestArm;

    DigitalChannel touchRest;
    DigitalChannel sensorFoundationRight;
    DigitalChannel sensorFoundationLeft;

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
        initializeSlide();
        initializeFoundation();
        initializeCapstoneDropper();
        initializeTouch();

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

    //Driving Section =============================

    public void strafeLeft (double speed, int distance) {
        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(speed);
        motorBackRight.setPower(speed);
        motorBackLeft.setPower(-speed);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    public void strafeRight (double speed, int distance) {
        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(speed);
        int startPosition = motorFrontRight.getCurrentPosition();
        while (opModeIsActive() && motorFrontRight.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    public void sSLeft(double speed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double startAngle = angles.firstAngle;
        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(speed);
        motorBackRight.setPower(speed);
        motorBackLeft.setPower(-speed);

        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() < (startPosition + distance)) {
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
    public void sSRight(double speed, int distance) {
        int startPosition = motorFrontRight.getCurrentPosition();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double startAngle = angles.firstAngle;
        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(speed);
        while (opModeIsActive() && motorFrontRight.getCurrentPosition() < (startPosition + distance)) {
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
    public void timedDriveForward(double speed, double seconds) {
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
    public void timedDriveBackward(double speed, double seconds) {
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
    public void driveStraightForward(double speed, int distance) {
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
                motorFrontRight.setPower(-speed * 0.9);
                motorFrontLeft.setPower(-speed);
                motorBackRight.setPower(-speed * 0.9);
                motorBackLeft.setPower(-speed);
            }
            else if (angles.firstAngle < (startAngle - 0.5)) {
                motorFrontRight.setPower(-speed);
                motorFrontLeft.setPower(-speed * 0.9);
                motorBackRight.setPower(-speed);
                motorBackLeft.setPower(-speed * 0.9);
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
    public void driveStraightBack(double speed, int distance) {
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
                motorFrontRight.setPower(speed);
                motorFrontLeft.setPower(speed * 0.9);
                motorBackRight.setPower(speed);
                motorBackLeft.setPower(speed * 0.9);
            }
            else if (angles.firstAngle < (startAngle - 0.5)) {
                motorFrontRight.setPower(speed * 0.9);
                motorFrontLeft.setPower(speed);
                motorBackRight.setPower(speed * 0.9);
                motorBackLeft.setPower(speed);
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
    public void stopMotors() {
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);
    }
    public void driveForward(double power) {
        motorFrontRight.setPower(-power);
        motorFrontLeft.setPower(-power);
        motorBackRight.setPower(-power);
        motorBackLeft.setPower(-power);
    }
    public void driveBack(double power) {
        motorFrontRight.setPower(power);
        motorFrontLeft.setPower(power);
        motorBackRight.setPower(power);
        motorBackLeft.setPower(power);
    }

    //Turning Section ===================================

    public void turnLeft(double speed) {
        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(speed);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(speed);
    }

    public void turnRight(double speed) {
        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(speed);
        motorBackLeft.setPower(-speed);
    }
    public void curveLeftF (double speed, int distance) {
        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(-speed * 0.5);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(-speed * 0.5);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void curveLeftB (double speed, int distance) {
        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(speed * 0.5);
        motorBackRight.setPower(speed);
        motorBackLeft.setPower(speed * 0.5);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    public void curveRightF (double speed, int distance) {
        motorFrontRight.setPower(-speed * 0.5);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(-speed * 0.5);
        motorBackLeft.setPower(-speed);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void hardCurveRightB (double speed, int distance){
        motorFrontRight.setPower(-speed * 0.5);
        motorFrontLeft.setPower(speed);
        motorBackRight.setPower(-speed * 0.5);
        motorBackLeft.setPower(speed);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    public void hardCurveRightF (double speed, int distance){
        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(-speed * 0.5);
        motorBackRight.setPower(speed);
        motorBackLeft.setPower(-speed * 0.5);
        int startPosition = motorFrontRight.getCurrentPosition();
        while (opModeIsActive() && motorFrontRight.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    public void hardCurveLeftB (double speed, int distance){
        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(-speed * 0.5);
        motorBackRight.setPower(speed);
        motorBackLeft.setPower(-speed * 0.5);
        int startPosition = motorFrontRight.getCurrentPosition();
        while (opModeIsActive() && motorFrontRight.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    public void hardCurveLeftF (double speed, int distance){
        motorFrontRight.setPower(-speed * 0.5);
        motorFrontLeft.setPower(speed);
        motorBackRight.setPower(-speed * 0.5);
        motorBackLeft.setPower(speed);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    public void bumpLeftB (double speed, int distance) {
        int startPosition = motorFrontRight.getCurrentPosition();
        motorFrontRight.setPower(speed);
        motorBackRight.setPower(speed);
        while (opModeIsActive() && motorFrontRight.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    public void bumpRightB (double speed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        motorFrontLeft.setPower(speed);
        motorBackLeft.setPower(speed);
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() < (startPosition + distance)) {

        }
        stopMotors();
    }
    public void bumpLeftF (double speed, int distance) {
        int startPosition = motorFrontRight.getCurrentPosition();
        motorFrontRight.setPower(-speed);
        motorBackRight.setPower(-speed);
        while (opModeIsActive() && motorFrontRight.getCurrentPosition() > (startPosition - distance)) {

        }
        stopMotors();
    }
    public void bumpRightF (double speed, int distance) {
        int startPosition = motorFrontLeft.getCurrentPosition();
        motorFrontLeft.setPower(-speed);
        motorBackLeft.setPower(-speed);
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() > (startPosition - distance)) {

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
        double currentAngle = angles.firstAngle;
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
        double currentAngle = angles.firstAngle;
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

        while (opModeIsActive() && (currentAngle > targetAngle)) {
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
    public int findSkystone(String allianceColor) {
        boolean skyStoneFound = false;

        if (tfod != null)
        {
            while (opModeIsActive() && !skyStoneFound)
            {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals("Skystone")) {
                            skyStoneFound = true;
                            telemetry.addData("label", recognition.getLabel());
                            telemetry.addData("Left", "%.03f", recognition.getLeft());
                            telemetry.addData("Right", "%.03f", recognition.getRight());
                            telemetry.addData("Width", "%.03f", recognition.getRight() - recognition.getLeft());
                            if (allianceColor.toLowerCase().equals("blue"))
                            {
                                return findBlueSkyStone(recognition.getLeft(), recognition.getRight());
                            }
                            else
                            {
                                return findRedSkyStone(recognition.getLeft(), recognition.getRight());
                            }
                        }
                    }
                    telemetry.update();
                }
            }

            return 0;
        }
        else
        {
            return 0;
        }
    }
    private int findBlueSkyStone(float left, float right)
    {
        if (right < 275) {
            return 3;
        } else if (right < 475) {
            return 2;
        } else {
            return 1;
        }
    }

    private int findRedSkyStone(float left, float right)
    {
        if (right < 275) {
            return 1;
        } else if (right < 475) {
            return 2;
        } else {
            return 3;
        }
    }

    //Function Section ===============================

    public void delay(double time) {
        ElapsedTime delayTimer = new ElapsedTime();
        while (opModeIsActive() && delayTimer.seconds() < time) {
        }
    }
    public void stonePosition () {
        motorVerticalSlide.setTargetPosition(-100);
        if (!touchRest.getState()) {
            levelRest = motorVerticalSlide.getCurrentPosition();
            motorVerticalSlide.setTargetPosition(levelRest);
            levelCap = levelRest + 350;
            level1 = levelRest + 35;
            level2 = levelRest - 250;
        }
    }
    public void extendRestArm () {
        servoRestArm.setPosition(SERVO_REST_ARM_EXTEND);
    }
    public void retractRestArm () {
        servoRestArm.setPosition(SERVO_REST_ARM_RETRACT);
    }
    public void grabFoundation () {
        servoFoundation.setPosition(SERVO_FOUNDATION_DOWN);
    }
    public void releaseFoundation () {
        servoFoundation.setPosition(SERVO_FOUNDATION_UP);
    }
    public void intakeOut () {
        motorIntakeLeft.setPower(1.0);
        motorIntakeRight.setPower(1.0);
    }
    public void intakeIn () {
        motorIntakeLeft.setPower(-1.0);
        motorIntakeRight.setPower(-1.0);
    }
    public void intakeOff () {
        motorIntakeLeft.setPower(0.0);
        motorIntakeRight.setPower(0.0);
    }
    public void verticalSlide (double speed, int distance) {
        int currentPosition = motorVerticalSlide.getCurrentPosition();
        motorVerticalSlide.setPower(speed);
        motorVerticalSlide.setTargetPosition(currentPosition + distance);

    }
    public void horizontalSlide (double power, double time) {
        motorHorizontalSlide.setPower(-power);
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
    public void raiseSpat () {
        servoSpatula.setPosition(SERVO_SPAT_UP);
    }
    public void lowerSpat () {
        servoSpatula.setPosition(SERVO_SPAT_DOWN);
    }
    public void capStage3 () {
        servoCapstone.setPosition(SERVO_CAPSTONE_UP);
    }
    public void returnS1 () {
        motorHorizontalSlide.setPower(1.0);
    }
    public void returnS2 () {
        verticalTarget = level1;
        servoStoneRotator.setPosition(SERVO_ROTATOR_START);
    }
    /*public boolean isTouchRestPressed() {
        return !touchRest.getState();
    }
    public void startHoming() {
        extendRestArm();
        isHoming = true;
        homingTimer.reset();
    }*/

    //Initialization Section =============================

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
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotorSimple.Direction.FORWARD);
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
    public void initializeIntake () {
        motorIntakeLeft = hardwareMap.dcMotor.get("motorIntakeLeft");
        motorIntakeRight = hardwareMap.dcMotor.get("motorIntakeRight");

        motorIntakeLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorIntakeLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorIntakeRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorIntakeRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorIntakeLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        motorIntakeRight.setDirection(DcMotorSimple.Direction.REVERSE);

        servoGate = hardwareMap.servo.get("servoGate");
        servoGate.setPosition(SERVO_GATE_OPEN);
        servoSpatula = hardwareMap.servo.get("servoSpat");
        servoSpatula.setPosition(SERVO_SPAT_UP);
    }
    public void initializeSlide () {
        motorHorizontalSlide = hardwareMap.dcMotor.get("motorHorizontalSlide");
        motorVerticalSlide = hardwareMap.dcMotor.get("motorVerticalSlide");

        motorHorizontalSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorHorizontalSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorVerticalSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorVerticalSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorVerticalSlide.setTargetPosition(0);
        motorVerticalSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorVerticalSlide.setPower(1.0);

        motorHorizontalSlide.setDirection(DcMotorSimple.Direction.FORWARD);
        motorVerticalSlide.setDirection(DcMotorSimple.Direction.FORWARD);

        servoStoneGrabber = hardwareMap.servo.get("servoStoneGrabber");
        servoStoneRotator = hardwareMap.servo.get("servoStoneRotator");
        servoRestArm = hardwareMap.servo.get("servoRest");

        servoStoneGrabber.setPosition(SERVO_GRABBER_OPEN);
        servoStoneRotator.setPosition(SERVO_ROTATOR_START);
        servoRestArm.setPosition(SERVO_REST_ARM_RETRACT);
    }
    public void initializeFoundation() {
        servoFoundation = hardwareMap.servo.get("servoFoundation");
        releaseFoundation ();
    }
    public void initializeCapstoneDropper() {
        servoCapstone = hardwareMap.servo.get("servoCapstone");
        capStage3 ();

    }
    public void initializeTouch() {
        sensorFoundationRight = hardwareMap.get(DigitalChannel.class, "SFRight");
        sensorFoundationLeft = hardwareMap.get(DigitalChannel.class, "SFLeft");
        sensorFoundationRight.setMode(DigitalChannel.Mode.INPUT);
        sensorFoundationLeft.setMode(DigitalChannel.Mode.INPUT);

        touchRest = hardwareMap.get(DigitalChannel.class,"touchRest");
        touchRest.setMode(DigitalChannel.Mode.INPUT);
    }
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }
    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }
    /*isLiftClear = (verticalTarget < levelRest + 50 && motorVerticalSlide.getCurrentPosition() < 0);

        if (isLiftClear) {
            startHoming();
        }
        if (isHoming == true && homingTimer.seconds() > 0.25) {
            motorVerticalSlide.setPower(0.0);
            verticalTarget = levelRest;
            motorVerticalSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            currentMode = "run without encoder";
        }
        if (isTouchRestPressed()) {
            isHoming = false;
            levelRest = motorVerticalSlide.getCurrentPosition();
            levelCap = levelRest + 350;
            level1 = levelRest + 35;
            level2 = levelRest - 250;
        }*/
}
