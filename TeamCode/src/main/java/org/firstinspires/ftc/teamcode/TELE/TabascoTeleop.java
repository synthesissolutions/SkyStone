package org.firstinspires.ftc.teamcode.TELE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static com.qualcomm.robotcore.util.Range.scale;

@TeleOp(name="TabascoTeleop", group="TELE")
//@Disabled
public class TabascoTeleop extends TabascoBase {

    @Override
    public void init() {
        initializeRobot();
    }


    @Override
    public void loop() {
        // ##### DRIVER 1 #####

        // **** Intake ****
        if (gamepad1.right_bumper) {
            gateClose();
        } else if (gamepad1.left_bumper) {
            gateOpen();
        }
        if (gamepad1.right_trigger > 0.1) {
            intakeIn();
            gateOpen();
        } else if (gamepad1.left_trigger > 0.1) {
            intakeOut();
            gateOpen();
        } else {
            intakeOff();
        }

        // **** Foundation ****
        if (gamepad1.b) {
            grabFoundation();
        } else if (gamepad1.a) {
            releaseFoundation();
        }

        // **** Mecanum Drive ****
        double mecanumSpeed = -gamepad1.left_stick_y * currentSpeed;
        double mecanumTurn = gamepad1.right_stick_x * currentSpeed;
        double mecanumStrafe = -gamepad1.left_stick_x * currentSpeed;

        boolean mecanumSlowStrafe = gamepad1.left_trigger > .7;
        boolean mecanumSlowSpeed = gamepad1.left_trigger > .7;
        boolean mecanumSlowTurn = gamepad1.right_trigger > .7;

        if (gamepad1.dpad_up) {
            currentSpeed = MAX_SPEED;
        } else if (gamepad1.dpad_left) {
            currentSpeed = FAST_SPEED;
        } else if (gamepad1.dpad_down) {
            currentSpeed = SLOW_SPEED;
        }

        controlMecanumWheels(mecanumSpeed, mecanumTurn, mecanumStrafe, mecanumSlowStrafe, mecanumSlowSpeed, mecanumSlowTurn);

        // **** Block Separator - Spatula ****
        if (gamepad1.x && rightSpatulaTimer.seconds() > 0.3) {
            rightSpatulaTimer.reset();
            if (isRightSpatulaUp) {
                isRightSpatulaUp = false;
                lowerSpatR();
            } else {
                isRightSpatulaUp = true;
                ricePattyR();
            }
        }

        if (gamepad1.y && leftSpatulaTimer.seconds() > 0.3) {
            leftSpatulaTimer.reset();
            if (isLeftSpatulaUp) {
                isLeftSpatulaUp = false;
                lowerSpatL();
            } else {
                isLeftSpatulaUp = true;
                ricePattyL();
            }
        }

        // ##### DRIVER 2 #####
        // **** Capstone ****
        if (gamepad2.right_trigger > 0.3) {
            gateOpen();
            capstoneDown();
        }
        if (gamepad2.left_trigger > 0.1) {
            capstoneUp();
        }


        // **** Stone Grabber ****
        if (gamepad2.x) {
            stoneRotatorEnd();
        } else if (gamepad2.y) {
            stoneRotatorMid();
        } else if (gamepad2.b) {
            stoneRotatorStart();
        }
        if (gamepad2.right_bumper) {
            grabStone();
        } else if (gamepad2.left_bumper) {
            releaseStone();
        }

        // **** Vertical Lift ****
        verticalSlide(gamepad2.left_stick_y);

        if (gamepad2.dpad_left && !isLiftReturning) {
            isLiftReturning = true;
            returnS1 ();
            returnLiftTimer. reset();
        }
        if (isLiftReturning) {
            if (returnLiftTimer.seconds() > 0.75) {
                horizontalSlide(0.0);
                returnS2 ();
                isLiftReturning = false;
            }
        }
        if (gamepad2.dpad_up) {
            verticalTarget = verticalMax;
            //very temporary, any more useful function is welcome.
        }
        else if (gamepad2.dpad_down) {
            verticalTarget = level0;
        }
        if (verticalTarget > verticalMax) {
            verticalTarget = verticalMax;
        }
        //-----------Rarely used if ever--------------------
        if (gamepad2.a && gamepad2.dpad_right) {
            level0 = motorVerticalSlide.getCurrentPosition();
            verticalMax = level0 + 8250;
        }

        motorVerticalSlide.setTargetPosition(verticalTarget);

        // **** Horizontal Lift ****
        if (!isLiftReturning) {
            horizontalSlide(gamepad2.right_stick_y);
        }

        //unused buttons: GP1 dpad_right, combos
        //unused buttons: GP2 most combos

        telemetry.addData("Position", motorVerticalSlide.getCurrentPosition());
        telemetry.addData("At Bottom?", isLiftAtBottom());
        telemetry.update();
    }

    @Override
    public void stop() {

    }
}
