package org.firstinspires.ftc.teamcode.TELE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static com.qualcomm.robotcore.util.Range.scale;

@TeleOp(name="RobotTeleop", group="TELE")
public class RobotTeleop extends OpMode {

    final static double MECANUM_MAX_SPEED = 1.0;
    final static double SLOW_STRAFE_FACTOR = 1.4;
    final static double SLOW_TURN_FACTOR = 1.20;
    final static double SLOW_SPEED_FACTOR = 1.4;

    final static double SERVO_GATE_OPEN = 0.8;
    final static double SERVO_GATE_CLOSED = 0.2;
    final static double SERVO_GRABBER_OPEN = 0.05;
    final static double SERVO_GRABBER_CLOSED = 0.49;
    final static double SERVO_ROTATOR_START = 0.96;
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


    @Override
    public void init() {
        initializeMecanum();
        initializeIntake();
        initializeSlide();
        initializeFoundation();
        initializeCapstoneDropper();
    }


    @Override
    public void loop() {
        double mecanumSpeed = -gamepad1.left_stick_y * currentSpeed;
        double mecanumTurn = gamepad1.right_stick_x * currentSpeed;
        double mecanumStrafe = -gamepad1.left_stick_x* currentSpeed;

        boolean mecanumSlowStrafe = gamepad1.left_trigger>.7;
        boolean mecanumSlowSpeed = gamepad1.left_trigger>.7;
        boolean mecanumSlowTurn = gamepad1.right_trigger>.7;

        if (gamepad1.right_bumper) {
            intakeIn();
        }
        else if(gamepad1.left_bumper) {
            intakeOut();
        }
        else {
            intakeOff();
        }

        verticalSlide(gamepad2.right_stick_y);
        if (!isLiftReturning) {
            horizontalSlide(gamepad2.left_stick_y);
        }

        if(gamepad1.left_trigger > 0.1) {
            gateOpen();
        }
        if(gamepad1.left_bumper) {
            gateOpen();
        }
        else if (gamepad1.right_trigger > 0.1) {
            gateClose();
        }

        if(gamepad2.b) {
            stoneRotatorEnd();
        }
        else if(gamepad2.y) {
            stoneRotatorMid();
        }
        else if(gamepad2.x) {
            stoneRotatorStart();
        }

        if(gamepad2.right_bumper) {
            grabStone();
        }
        else if(gamepad2.left_bumper) {
            releaseStone();
        }
        //======================================================================
        if (gamepad2.a && !isLiftReturning) {
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
        }//*/
        if (gamepad2.dpad_up) {
            verticalTarget = level2;
            //very temporary, any more useful function is welcome.
        }
        else if (gamepad2.dpad_left) {
            verticalTarget = level1;
            verticalDelay.reset();
            isVDelayActive = true;
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
        if (verticalTarget < VERTICAL_MAX) {
            verticalTarget = VERTICAL_MAX;
        }
        if (verticalTarget > 0) {
            verticalTarget = 0;
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
            lowerSpat ();
        }
        else if (gamepad1.y) {
            ricePatty ();
        }
        if (gamepad2.right_trigger > 0.3) {
            servoGate.setPosition(SERVO_GATE_OPEN);
        }
        if (gamepad2.right_trigger > 0.3 && !isCaptoneDropping) {
            isCaptoneDropping = true;
            capStage1 ();
            capstoneDropTimer.reset ();
        }
        else if (isCaptoneDropping) {
            if (capstoneDropTimer.seconds() > 1.0) {
                capStage3();
                isCaptoneDropping = false;
            }
            else if (capstoneDropTimer.seconds() > 0.5) {
                capStage2();
            }
        }
        //unused buttons: GP1 dpad_right, double taps
        //unused buttons: GP2 dpad_right, L trigger, double taps

        motorVerticalSlide.setTargetPosition(verticalTarget);

        telemetry.addData("Position", motorVerticalSlide.getCurrentPosition());
        telemetry.addData("Target", verticalTarget);
        telemetry.addData("servo", servoCapstone.getPosition());
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
        motorVerticalSlide.setPower(1.0);
        int increment = (int)Math.round(power * VERTICAL_STEP);
        verticalTarget = verticalTarget + increment;
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
        servoFoundation.setPosition(SERVO_FOUNDATION_DOWN);
    }
    public void releaseFoundation () {
        servoFoundation.setPosition(SERVO_FOUNDATION_UP);
    }
    public void ricePatty() {
        servoSpatula.setPosition(SERVO_SPAT_UP);
    }
    public void lowerSpat () {
        servoSpatula.setPosition(SERVO_SPAT_DOWN);
    }
    public void capStage1 () {
        servoCapstone.setPosition(SERVO_CAPSTONE_DROP);
    }
    public void capStage2 () {
        servoCapstone.setPosition(SERVO_CAPSTONE_DOWN);
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

}
