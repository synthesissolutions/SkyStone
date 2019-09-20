package org.firstinspires.ftc.teamcode.TELE;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import static com.qualcomm.robotcore.util.Range.scale;

@TeleOp(name="Mecanum Drive Test", group="TELE")
public class MecanumDriveTest extends OpMode {

    final static double MECANUM_MAX_SPEED = 1.0;
    final static double SLOW_STRAFE_FACTOR = 1.4;
    final static double SLOW_TURN_FACTOR = 1.20;
    final static double SLOW_SPEED_FACTOR = 1.4;

    DcMotor motorFrontLeft;
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;


    @Override
    public void init() {
        initializeMecanum();
    }

    @Override
    public void loop() {
        double mecanumSpeed = -gamepad1.left_stick_y;
        double mecanumTurn = gamepad1.right_stick_x;
        double mecanumStrafe = -gamepad1.left_stick_x;

        boolean mecanumSlowStrafe = gamepad1.left_trigger>.7;
        boolean mecanumSlowSpeed = gamepad1.left_trigger>.7;
        boolean mecanumSlowTurn = gamepad1.right_trigger>.7;


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
        motorBackLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
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
}
