package org.firstinspires.ftc.teamcode.TELE;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Motor/Servo Tester", group="TELE")
//  @Disabled
public class MotorServoTest extends OpMode {

    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;
    DcMotor motor4;
    DcMotor motor5;
    DcMotor motor6;
    DcMotor motor7;
    DcMotor motor8;

    Servo servo1;
    Servo servo2;
    Servo servo3;
    Servo servo4;
    Servo servo5;
    Servo servo6;
    double servo1pos=0.5;
    double servo2pos=0.5;
    double servo3pos=0.5;
    double servo4pos=0.5;
    double servo5pos=0.5;
    double servo6pos=0.5;
    int timer = 0;

    int motor5Timer = 0;
    boolean motor5Forward = false;
    boolean motor5Backward = false;




    @Override
    public void init() {
        servo1pos=0.5;
        servo2pos=0.5;
        servo3pos=0.5;
        servo4pos=0.5;
        servo5pos=0.5;
        servo6pos=0.5;
        timer = 0;

        if(hardwareMap.servo != null) {
            try {
                servo1 = hardwareMap.servo.get("servoBumpers");
            } catch (IllegalArgumentException e) {
                // do nothing
            }
            try {
                servo2 = hardwareMap.servo.get("servoX/Y");
            } catch (IllegalArgumentException e) {
                // do nothing
            }
            try {
                servo3 = hardwareMap.servo.get("servoA/B");
            } catch (IllegalArgumentException e) {
                // do nothing
            }
            try {
                servo4 = hardwareMap.servo.get("servoBumpers-");
            } catch (IllegalArgumentException e) {
                // do nothing
            }
            try {
                servo5 = hardwareMap.servo.get("servoX/Y-");
            } catch (IllegalArgumentException e) {
                // do nothing
            }
            try {
                servo6 = hardwareMap.servo.get("servoA/B-");
            } catch (IllegalArgumentException e) {
                // do nothing
            }
        }

        if(hardwareMap.dcMotor != null) {
            try {
                motor1 = hardwareMap.dcMotor.get("motorRight");
            } catch (IllegalArgumentException e) {
                // do nothing
            }
            try {
                motor2 = hardwareMap.dcMotor.get("motorLeft");
            } catch (IllegalArgumentException e) {
                // do nothing
            }
            try {
                motor3 = hardwareMap.dcMotor.get("motorRight-");
                motor3.setDirection(DcMotor.Direction.REVERSE);
            } catch (IllegalArgumentException e) {
                // do nothing
            }
            try {
                motor4 = hardwareMap.dcMotor.get("motorLeft-");
                motor4.setDirection(DcMotor.Direction.REVERSE);
            } catch (IllegalArgumentException e) {
                // do nothing
            }
            try {
                motor5 = hardwareMap.dcMotor.get("motorHorizontalDpad");
                //motor7.setDirection(DcMotor.Direction.REVERSE);
            } catch (IllegalArgumentException e) {
                // do nothing
            }
            try {
                motor6 = hardwareMap.dcMotor.get("motorVerticalDpad");
            } catch (IllegalArgumentException e) {
                // do nothing
            }
            try {
                motor7 = hardwareMap.dcMotor.get("motorHorizontalDpad-");
                motor7.setDirection(DcMotor.Direction.REVERSE);
            } catch (IllegalArgumentException e) {
                // do nothing
            }
            try {
                motor8 = hardwareMap.dcMotor.get("motorVerticalDpad-");
                motor8.setDirection(DcMotor.Direction.REVERSE);
            } catch (IllegalArgumentException e) {
                // do nothing
            }
        }
    }

    @Override
    public void loop() {
        // note that if y equal -1 then joystick is pushed all of the way forward.
        float left = -gamepad1.left_stick_y;
        float right = -gamepad1.right_stick_y;
        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

        // write the values to the motors
        if (motor1!=null) {
            motor1.setPower(right);
        }
        if (motor2!=null) {
            motor2.setPower(left);
        }
        if (motor3!=null) {
            motor3.setPower(right);
        }
        if (motor4!=null) {
            motor4.setPower(left);
        }

        if (gamepad1.right_bumper && motor5Timer + 150 < timer) {
            motor5Forward = !motor5Forward;
            motor5Backward = false;
            motor5Timer = timer;
        } else if (gamepad1.left_bumper && motor5Timer + 150  < timer) {
            motor5Forward = false;
            motor5Backward = !motor5Backward;
            motor5Timer = timer;
        }
        if (motor5!=null) {
            if (gamepad1.dpad_left)
            {
                motor5Forward = false;
                motor5.setPower(1);
            }
            else if (gamepad1.dpad_right)
            {
                motor5Backward = false;
                motor5.setPower(-1);
            }
            else if (motor5Forward)
            {
                motor5.setPower(1);
            }
            else if (motor5Backward)
            {
                motor5.setPower(-1);
            }
            else
            {
                motor5.setPower(0);
            }
        }
        if (motor6!=null) {
            if (gamepad1.dpad_up)
            {
                motor6.setPower(1);
            }
            else if (gamepad1.dpad_down)
            {
                motor6.setPower(-1);
            }
            else
            {
                motor6.setPower(0);
            }


        }
        if (motor7!=null) {
            if (gamepad1.dpad_left)
            {
                motor7.setPower(1);
            }
            else if (gamepad1.dpad_right)
            {
                motor7.setPower(-1);
            }
            else
            {
                motor7.setPower(0);
            }
        }
        if (motor8!=null) {
            if (gamepad1.dpad_up)
            {
                motor8.setPower(1);
            }
            if (gamepad1.dpad_down)
            {
                motor8.setPower(-1);
            }
            else
            {
                motor8.setPower(0);
            }
        }
        if (timer == 0) {
            servo1pos=0.5;
            servo2pos=0.5;
            servo3pos=0.5;
            servo4pos=0.5;
            servo5pos=0.5;
            servo6pos=0.5;
        }
        timer++;

        if (servo1!=null){
            if (gamepad1.right_bumper) {
                servo1pos += 0.01;
            }
            if (gamepad1.left_bumper) {
                servo1pos -= 0.01;
            }
            servo1pos = Range.clip(servo1pos, 0.00, 1.0);

            servo1.setPosition(servo1pos);
        }
        if (servo2!=null){
            if (gamepad1.x) {
                servo2pos += 0.01;
            }
            if (gamepad1.y) {
                servo2pos -= 0.01;
            }
            servo2pos = Range.clip(servo2pos, 0.00, 1.0);

            servo2.setPosition(servo2pos);
        }
        if (servo3!=null){
            if (gamepad1.a) {
                servo3pos += 0.01;
            }
            if (gamepad1.b) {
                servo3pos -= 0.01;
            }
            servo3pos = Range.clip(servo3pos, 0.00, 1.0);

            servo3.setPosition(servo3pos);
        }
        if (servo4!=null){
            if (gamepad1.right_bumper) {
                servo4pos -= 0.01;
            }
            if (gamepad1.left_bumper) {
                servo4pos += 0.01;
            }
            servo4pos = Range.clip(servo4pos, 0.00, 1.0);

            servo4.setPosition(servo4pos);
        }
        if (servo5!=null){
            if (gamepad1.x) {
                servo5pos -= 0.01;
            }
            if (gamepad1.y) {
                servo5pos += 0.01;
            }
            servo5pos = Range.clip(servo5pos, 0.00, 1.0);

            servo5.setPosition(servo5pos);
        }
        if (servo6!=null){
            if (gamepad1.a) {
                servo6pos -= 0.01;
            }
            if (gamepad1.b) {
                servo6pos += 0.01;
            }
            servo6pos = Range.clip(servo6pos, 0.00, 1.0);

            servo6.setPosition(servo6pos);
        }
        if (servo1!=null){
            telemetry.addData("servoBumpers", servo1.getPosition());}
        if (servo2!=null){
            telemetry.addData("servoX/Y", servo2.getPosition());}
        if (servo3!=null){
            telemetry.addData("servoA/B", servo3.getPosition());}
        if (servo4!=null){
            telemetry.addData("servoBumpers-", servo4.getPosition());}
        if (servo5!=null){
            telemetry.addData("servoX/Y-", servo5.getPosition());}
        if (servo6!=null){
            telemetry.addData("servoA/B-", servo6.getPosition());}
    }
    @Override
    public void stop() {

    }
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        }
        if (index > 16) {
            index = 16;
        }
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }
        return dScale;
    }

}