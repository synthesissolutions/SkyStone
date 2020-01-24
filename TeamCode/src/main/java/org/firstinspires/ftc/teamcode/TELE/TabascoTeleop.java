package org.firstinspires.ftc.teamcode.TELE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static com.qualcomm.robotcore.util.Range.scale;

@TeleOp(name="TabascoTelePop", group="TELE")
//@Disabled
public class TabascoTeleop extends OpMode {

    final static double MECANUM_MAX_SPEED = 1.0;
    final static double SLOW_STRAFE_FACTOR = 1.4;
    final static double SLOW_TURN_FACTOR = 1.20;
    final static double SLOW_SPEED_FACTOR = 1.4;

    final static double SERVO_GATE_OPEN = 0.8;
    final static double SERVO_GATE_CLOSED = 0.25;
<<<<<<< HEAD
    final static double SERVO_GRABBER_OPEN = 0.0;
    final static double SERVO_GRABBER_CLOSED = 0.5;
=======
    final static double SERVO_GRABBER_OPEN = 0.25;
    final static double SERVO_GRABBER_CLOSED = 0.85;
>>>>>>> d9b6617add9feadf481a337126db8ad9140781a9
    final static double SERVO_ROTATOR_START = 0.96;
    final static double SERVO_ROTATOR_MID = 0.5;
    final static double SERVO_ROTATOR_END = 0.0;
    final static double SERVO_FOUNDATIONL_UP = 0.0;
    final static double SERVO_FOUNDATIONL_DOWN = 0.8;
    final static double SERVO_FOUNDATIONR_UP = 1.0;
    final static double SERVO_FOUNDATIONR_DOWN = 0.4;
    final static double SERVO_SPATL_UP = 0.25;
    final static double SERVO_SPATL_DOWN = 0.75;
    final static double SERVO_SPATR_UP = 0.86;
    final static double SERVO_SPATR_DOWN = 0.35;
    final static double SERVO_CAPSTONE_UP = 0.65;
    final static double SERVO_CAPSTONE_DOWN = 0.15;


    final static int VERTICAL_STEP = 30;
    int verticalTarget = 0;
    int levelCap = 0;
    int level1 = 315;
    int level2 = 600;
    int verticalMax = levelCap + 4400;

    final static double MAX_SPEED = 1.0;
    final static double FAST_SPEED = 0.8;
    final static double SLOW_SPEED = 0.6;
    double currentSpeed = MAX_SPEED;

    boolean isLiftReturning = false;
    ElapsedTime returnLiftTimer = new ElapsedTime();
    boolean isVDelayActive = false;
    ElapsedTime verticalDelay = new ElapsedTime();

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
    Servo servoFoundationL;
    Servo servoFoundationR;
    Servo servoSpatulaL;
    Servo servoSpatulaR;
    Servo servoCapstone;

    DigitalChannel touchRest;
    DigitalChannel sensorFoundationRight;
    DigitalChannel sensorFoundationLeft;

    String currentMode = "run with encoder";
    String didCallVerticalSlide = "false";
    @Override
    public void init() {
        initializeMecanum();
        initializeIntake();
        initializeDelivery();
        initializeFoundation();
        initializeCapstone();
        //initializeTouch();
    }


    @Override
    public void loop() {
        double mecanumSpeed = -gamepad1.left_stick_y * currentSpeed;
        double mecanumTurn = gamepad1.right_stick_x * currentSpeed;
        double mecanumStrafe = -gamepad1.left_stick_x* currentSpeed;

        boolean mecanumSlowStrafe = gamepad1.left_trigger>.7;
        boolean mecanumSlowSpeed = gamepad1.left_trigger>.7;
        boolean mecanumSlowTurn = gamepad1.right_trigger>.7;

<<<<<<< HEAD
        if (gamepad1.right_trigger > 0.2) {
=======
        if (gamepad1.right_bumper) {
>>>>>>> d9b6617add9feadf481a337126db8ad9140781a9
            intakeIn();
            gateOpen();
        }
        else if(gamepad1.left_trigger > 0.2) {
            intakeOut();
            gateOpen();
        }
        else {
            intakeOff();
        }
        if (gamepad1.b) {
            grabFoundation ();
        }
        else if (gamepad1.a) {
            releaseFoundation ();
        }
        if (gamepad1.dpad_up) {
            currentSpeed = MAX_SPEED;
        }
        else if (gamepad1.dpad_left) {
            currentSpeed = FAST_SPEED;
        }
        else if (gamepad1.dpad_down) {
            currentSpeed = SLOW_SPEED;
        }

        if (gamepad1.x) {
            lowerSpatR();
        }
        if (gamepad1.y) {
            lowerSpatL();
        }
        if (gamepad1.x && gamepad1.y) {
            ricePattyR();
            ricePattyL();
        }

<<<<<<< HEAD
        if(gamepad1.left_bumper) {
=======
        if(gamepad1.left_trigger > 0.1) {
>>>>>>> d9b6617add9feadf481a337126db8ad9140781a9
            gateOpen();
        }
        else if (gamepad1.right_bumper) {
            gateClose();
        }
        if (gamepad2.right_stick_y < -0.1 || gamepad2.right_stick_y > 0.1) {
            didCallVerticalSlide = "true";
            verticalSlide(gamepad2.right_stick_y);
        } else {
            didCallVerticalSlide = "false";
        }
        if (!isLiftReturning) {
            horizontalSlide(gamepad2.left_stick_y);
        }
        if(gamepad2.x) {
            stoneRotatorEnd();
        }
        else if(gamepad2.y) {
            stoneRotatorMid();
        }
        else if(gamepad2.b) {
            stoneRotatorStart();
        }
        if(gamepad2.right_bumper) {
            grabStone();
        }
        else if(gamepad2.left_bumper) {
            releaseStone();
        }
        if (gamepad2.right_trigger > 0.3) {
            servoGate.setPosition(SERVO_GATE_OPEN);
            capstoneDown();
        }
        if (gamepad2.left_trigger > 0.1) {
            capstoneUp();
        }

        //0000000000000000000000000000  Vertical Lift stuff
        if (gamepad2.dpad_left && !isLiftReturning) {
            isLiftReturning = true;
            returnS1 ();
            returnLiftTimer. reset();
        }
        else if (isLiftReturning) {
            if (returnLiftTimer.seconds() > 1.0) {
                motorHorizontalSlide.setPower(0.0);
                returnS2 ();
                isLiftReturning = false;
            }
        }
        if (gamepad2.dpad_up) {
            verticalTarget = level2;
            //very temporary, any more useful function is welcome.
        }
        else if (gamepad2.dpad_down) {
            verticalTarget = levelCap;
            verticalDelay.reset();
            isVDelayActive = true;
        }
        if (isVDelayActive && (verticalDelay.seconds() > 1.0)) {
            isVDelayActive = false;
            motorVerticalSlide.setPower(0);
        }
        if (verticalTarget > verticalMax) {
            verticalTarget = verticalMax;
        }
<<<<<<< HEAD
        if (verticalTarget < levelCap - 100) {
=======
        if (verticalTarget < levelCap) {
>>>>>>> d9b6617add9feadf481a337126db8ad9140781a9
            verticalTarget = levelCap;
        }
        //-------------------------------
        if (gamepad2.a && gamepad2.dpad_right) {
            levelCap = motorVerticalSlide.getCurrentPosition();
            level1 = levelCap + 315;
            level2 = levelCap + 600;
        }
        //00000000000000000000000000000000

        //unused buttons: GP1 dpad_right, x, y, double taps
        //unused buttons: GP2 double taps

        motorVerticalSlide.setTargetPosition(verticalTarget);

        telemetry.addData("Position", motorVerticalSlide.getCurrentPosition());
        telemetry.addData("Target", verticalTarget);
        telemetry.addData("currentMode", currentMode);
        telemetry.addData("didCallVerticalSlide", didCallVerticalSlide);
        telemetry.update();


        //MAIN DRIVE
        controlMecanumWheels(mecanumSpeed,mecanumTurn,mecanumStrafe,mecanumSlowStrafe,mecanumSlowSpeed,mecanumSlowTurn);
    }


    @Override
    public void stop() {
    }

    public void initializeMecanum()
    {
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");

        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorFrontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBackRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
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

        servoGate.setPosition(SERVO_GATE_OPEN);
    }
    public void initializeDelivery () {
        motorHorizontalSlide = hardwareMap.dcMotor.get("motorHorizontalSlide");
        motorVerticalSlide = hardwareMap.dcMotor.get("motorVerticalSlide");

        motorHorizontalSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorHorizontalSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorVerticalSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorVerticalSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        /*
        motorVerticalSlide.setTargetPosition(0);
        motorVerticalSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorVerticalSlide.setPower(1.0);
        currentMode = "run to position";
        */

        motorHorizontalSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        motorVerticalSlide.setDirection(DcMotorSimple.Direction.REVERSE);

        servoStoneGrabber = hardwareMap.servo.get("servoStoneGrabber");
        servoStoneRotator = hardwareMap.servo.get("servoStoneRotator");

        servoStoneGrabber.setPosition(SERVO_GRABBER_OPEN);
        servoStoneRotator.setPosition(SERVO_ROTATOR_START);

    }
    public void initializeFoundation() {
        servoFoundationL = hardwareMap.servo.get("servoFoundationL");
        servoFoundationR = hardwareMap.servo.get("servoFoundationR");

        releaseFoundation ();
    }
    public void initializeCapstone() {
        servoCapstone = hardwareMap.servo.get("servoCapstone");

        servoCapstone.setPosition(SERVO_CAPSTONE_UP);
    }
    /*
    public void initializeTouch(){
        sensorFoundationRight = hardwareMap.get(DigitalChannel.class, "SFRight");
        sensorFoundationLeft = hardwareMap.get(DigitalChannel.class, "SFLeft");
        sensorFoundationRight.setMode(DigitalChannel.Mode.INPUT);
        sensorFoundationLeft.setMode(DigitalChannel.Mode.INPUT);

        touchRest = hardwareMap.get(DigitalChannel.class,"touchRest");
        touchRest.setMode(DigitalChannel.Mode.INPUT);
    }
    */
    public void controlMecanumWheels(double sp,double tu, double st, boolean slowSt, boolean slowSp, boolean slowTu)
    {
        double speed = sp;
        double turn = tu;
        double strafe = st;

        strafe = slowStrafe(slowSt, strafe);

        speed = slowSpeed(slowSp, speed);

        turn = slowTurn(slowTu, turn);

        holonomic(speed, turn, strafe, MECANUM_MAX_SPEED);
    }

    public double slowSpeed(boolean active, double speed)
    {
        if (active) {
            speed=speed/ SLOW_SPEED_FACTOR;
            speed=speed/ SLOW_SPEED_FACTOR;
        }
        return speed;
    }

    public double slowStrafe(boolean active, double strafe)
    {
        if (active) {
            strafe=strafe/ SLOW_STRAFE_FACTOR;
        }
        return strafe;
    }

    public double slowTurn(boolean active, double turn)
    {
        if (active)
        {
            turn=turn/ SLOW_TURN_FACTOR;
        }
        return turn;
    }

    public void holonomic(double mecanumSpeed, double mecanumTurn, double mecanumStrafe, double mecanumMaxSpeed)
    {
        //Left Front = +mecanumSpeed + mecanumTurn - mecanumStrafe      Right Front = +mecanumSpeed - mecanumTurn + mecanumStrafe
        //Left Rear  = +mecanumSpeed + mecanumTurn + mecanumStrafe      Right Rear  = +mecanumSpeed - mecanumTurn - mecanumStrafe

        double mecanumMagnitude = Math.abs(mecanumSpeed) + Math.abs(mecanumTurn) + Math.abs(mecanumStrafe);
        mecanumMagnitude = (mecanumMagnitude > 1) ? mecanumMagnitude : 1; //Set scaling to keep -1,+1 range
        final double MECANNUM_BACK_WHEEL_MULTIPLIER = 0.95;

        if (motorFrontLeft != null)
        {
            motorFrontLeft.setPower(-scale((scaleInput(mecanumSpeed) + scaleInput(mecanumTurn) - scaleInput(mecanumStrafe)),
                    -mecanumMagnitude, +mecanumMagnitude, -mecanumMaxSpeed, +mecanumMaxSpeed));
        }
        if (motorBackLeft != null)
        {
            motorBackLeft.setPower(-scale((scaleInput(mecanumSpeed) + scaleInput(mecanumTurn) + scaleInput(mecanumStrafe* MECANNUM_BACK_WHEEL_MULTIPLIER)),
                    -mecanumMagnitude, +mecanumMagnitude, -mecanumMaxSpeed, +mecanumMaxSpeed));
        }
        if (motorFrontRight != null) {
            motorFrontRight.setPower(-(scale((scaleInput(mecanumSpeed) - scaleInput(mecanumTurn) + scaleInput(mecanumStrafe)),
                    -mecanumMagnitude, +mecanumMagnitude, -mecanumMaxSpeed, +mecanumMaxSpeed)));
        }
        if (motorBackLeft != null) {
            motorBackRight.setPower(-(scale((scaleInput(mecanumSpeed) - scaleInput(mecanumTurn) - scaleInput(mecanumStrafe* MECANNUM_BACK_WHEEL_MULTIPLIER)),
                    -mecanumMagnitude, +mecanumMagnitude, -mecanumMaxSpeed, +mecanumMaxSpeed)));
        }
    }

    double scaleInput(double dVal)
    {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
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
    public void verticalSlide (double power) {
        motorVerticalSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorVerticalSlide.setPower(power);
        /*
        motorVerticalSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        currentMode = "run to position";
        motorVerticalSlide.setPower(1.0);
        int increment = (int)Math.round(power * VERTICAL_STEP);
        verticalTarget = verticalTarget - increment;
        */
    }
    public void horizontalSlide (double power) {
        motorHorizontalSlide.setPower(power);
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
    public void capstoneDown () {
        servoCapstone.setPosition(SERVO_CAPSTONE_DOWN);
    }
    public void capstoneUp(){
        servoCapstone.setPosition(SERVO_CAPSTONE_UP);
    }
    public void returnS1 () {
        horizontalSlide(-1.0);
    }
    public void returnS2 () {
        servoStoneRotator.setPosition(SERVO_ROTATOR_START);
        motorVerticalSlide.setTargetPosition(levelCap);
    }
    /*
    public boolean isFLeftPressed() {
        return !sensorFoundationLeft.getState();
    }
    public boolean isFRightPressed() {
        return !sensorFoundationRight.getState();
    }
     */
}
