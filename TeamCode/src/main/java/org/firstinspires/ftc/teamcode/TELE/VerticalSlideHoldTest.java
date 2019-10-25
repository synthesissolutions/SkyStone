package org.firstinspires.ftc.teamcode.TELE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import static com.qualcomm.robotcore.util.Range.scale;

@TeleOp(name="VertSlideHoldTest", group="TELE")
public class VerticalSlideHoldTest extends OpMode {
    DcMotor motor1;

    final static double INTAKE_SPEED = 0.7;
    DcMotor motorVerticalSlide;

    @Override
    public void init() {
        motor1 = hardwareMap.dcMotor.get("motor1");
        initializeSlide();
    }

    // This is a comment
    @Override
    public void loop() {
        //double motor1Speed = gamepad1.left_stick_y;
        //motor1.setPower(motor1Speed);
        if (gamepad1.left_bumper) {
            motor1.setPower(INTAKE_SPEED);
        } else if (gamepad1.right_bumper) {
            motor1.setPower(INTAKE_SPEED);
        } else {
            motor1.setPower(0.0);
        }
    }
    public void initializeSlide () {
        motorVerticalSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorVerticalSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorVerticalSlide.setPower(1.0);
    }

    /*
    public int multiply(int x, int y) {
        return x * y;
    }

     */

    public void goForward(double speed, double distance) {

    }
    @Override
    public void stop() {
    }
}
