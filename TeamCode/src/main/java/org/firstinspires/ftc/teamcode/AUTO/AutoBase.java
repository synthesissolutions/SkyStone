package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.hardware.bosch.BNO055IMU;
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

public abstract class AutoBase extends LinearOpMode {

    final static double MECANUM_MAX_SPEED = 1.0;
    final static double SLOW_STRAFE_FACTOR = 1.4;
    final static double SLOW_TURN_FACTOR = 1.20;
    final static double SLOW_SPEED_FACTOR = 1.4;

    final static double SERVO_GATE_OPEN = 0.8;
    final static double SERVO_GATE_CLOSED = 0.2;
    final static double SERVO_GRABBER_OPEN = 0.15;
    final static double SERVO_GRABBER_CLOSED = 0.49;
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


    final static int VERTICAL_STEP = 15;
    final static int VERTICAL_MAX = -3300;
    int verticalTarget = 0;
    int levelCap = 0;
    int level1 = -315;
    int level2 = -600;

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

    DigitalChannel digitalTouch;

    // The IMU sensor object
    BNO055IMU imu;

    // State used for updating telemetry
    Orientation angles;

    public void initializeRobot() {
        initializeMecanum();
        initializeImu();
        initializeIntake();
        initializeSlide();
        initializeFoundation();
        initializeCapstoneDropper();
        //initializeTouch();
    }
    public void spinRight(double targetAngle, double maxSpeed, double minSpeed) {
        double currentAngle = angles.firstAngle;
        double startScaling = 0.01;
        double startingAngle = currentAngle;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;

        while (opModeIsActive() && currentAngle > (startingAngle - targetAngle)) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentAngle = angles.firstAngle;
            double percentComplete = (currentAngle - startingAngle) / ((startingAngle - targetAngle) - startingAngle);

            if (percentComplete > startScaling) {
                currentSpeed = (minSpeed + deltaSpeed * (1 - (percentComplete - startScaling) / (1.0 - startScaling)));
            } else {
                currentSpeed = maxSpeed;
            }
            turnRight(currentSpeed);

        }
        stopMotors();
    }

    public void strafeLeft (double speed, int distance) {
        motorFrontRight.setPower(-speed);
        motorFrontLeft.setPower(speed);
        motorBackRight.setPower(speed);
        motorBackLeft.setPower(-speed);
        int startPosition = motorFrontLeft.getCurrentPosition();
        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() < (startPosition + distance)) {

        }
    }
    public void strafeRight (double speed, int distance) {
        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(-speed);
        motorBackRight.setPower(-speed);
        motorBackLeft.setPower(speed);
        int startPosition = motorFrontRight.getCurrentPosition();
        while (opModeIsActive() && motorFrontRight.getCurrentPosition() < (startPosition + distance)) {

        }
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
    public void delay(double time) {
        ElapsedTime delayTimer = new ElapsedTime();
        while (opModeIsActive() && delayTimer.seconds() < time) {

        }
    }
    public void grabFoundation () {
        servoFoundation.setPosition(SERVO_FOUNDATION_DOWN);
    }
    public void releaseFoundation () {
        servoFoundation.setPosition(SERVO_FOUNDATION_UP);
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
    private void foundationAndBridgeBlue() {
        driveStraightBack(0.25, 1300);
        bumpRightB(0.25, 150);
        grabFoundation();
        delay (0.5);
        driveStraightForward(0.25, 200);
        curveLeftF (0.4, 400);
        hardCurveRightB(0.4, 1400);
        driveStraightBack(0.3, 200);
        releaseFoundation();
        delay (0.5);
        driveStraightForward(0.25, 1700);
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
    public double normalizeAngle(double angle) {
        // calculations
        // check to see if the angle is negative
        // then add to 360
        // otherwise nothing to do
        if (angle < 0) {
            return 360 + angle;
        } else {
            return angle;
        }
    }
    public void turnLeftToAngle (double targetAngle, double maxSpeed, double minSpeed) {
        double currentAngle = normalizeAngle(angles.firstAngle);
        double startScaling = 0.01;
        double startingAngle = currentAngle;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;


        while (opModeIsActive() && currentAngle < targetAngle) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentAngle = normalizeAngle(angles.firstAngle);
            double percentComplete = (currentAngle - startingAngle) / (targetAngle - startingAngle);

            if (percentComplete > startScaling) {
                currentSpeed = (minSpeed + deltaSpeed * (1 - (percentComplete - startScaling) / (1.0 - startScaling)));
            } else {
                currentSpeed = maxSpeed;
            }
            turnLeft(currentSpeed);

        }
        stopMotors();
    }
    public void turnRightToAngle (double targetAngle, double maxSpeed, double minSpeed) {
        double currentAngle = normalizeAngle(angles.firstAngle);
        double startScaling = 0.01;
        double startingAngle = currentAngle;
        double currentSpeed;
        double deltaSpeed = maxSpeed - minSpeed;


        while (opModeIsActive() && currentAngle > targetAngle) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentAngle = normalizeAngle(angles.firstAngle);
            double percentComplete = (currentAngle - startingAngle) / (targetAngle - startingAngle);

            if (percentComplete > startScaling) {
                currentSpeed = (minSpeed + deltaSpeed * (1 - (percentComplete - startScaling) / (1.0 - startScaling)));
            } else {
                currentSpeed = maxSpeed;
            }
            turnRight(currentSpeed);
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            telemetry.addData("currentAngle", angles.firstAngle);
            telemetry.update();

        }
        stopMotors();
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
        //===============================================
        servoSpatula = hardwareMap.servo.get("servoSpat");

        servoSpatula.setPosition(SERVO_SPAT_UP);
        //===============================================
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

        servoStoneGrabber.setPosition(SERVO_GRABBER_OPEN);
        servoStoneRotator.setPosition(SERVO_ROTATOR_START);
    }
    public void initializeFoundation() {
        servoFoundation = hardwareMap.servo.get("servoFoundation");

        releaseFoundation ();
    }
    public void initializeCapstoneDropper() {
        servoCapstone = hardwareMap.servo.get("servoCapstone");

        capStage3 ();

    }
}