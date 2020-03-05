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
public class TabascoTeleop extends OpMode {

    Tabasco tabasco = new Tabasco();
    @Override
    public void init() {
        tabasco.initializeRobot(hardwareMap);
    }


    @Override
    public void loop() {
        // ##### DRIVER 1 #####

        // **** Intake ****
        if (gamepad1.right_bumper) {
            tabasco.gateClose();
        } else if (gamepad1.left_bumper) {
            tabasco.gateOpen();
        }
        if (gamepad1.right_trigger > 0.1) {
            tabasco.intakeIn();
            tabasco.gateOpen();
        } else if (gamepad1.left_trigger > 0.1) {
            tabasco.intakeOut();
            tabasco.gateOpen();
        } else {
            tabasco.intakeOff();
        }

        // **** Foundation ****
        if (gamepad1.b) {
            tabasco.grabFoundation();
        } else if (gamepad1.a) {
            tabasco.releaseFoundation();
        }

        // **** Mecanum Drive ****
        double mecanumSpeed = -gamepad1.left_stick_y * tabasco.currentSpeed;
        double mecanumTurn = gamepad1.right_stick_x * tabasco.currentSpeed;
        double mecanumStrafe = -gamepad1.left_stick_x * tabasco.currentSpeed;

        boolean mecanumSlowStrafe = gamepad1.left_trigger > .7;
        boolean mecanumSlowSpeed = gamepad1.left_trigger > .7;
        boolean mecanumSlowTurn = gamepad1.right_trigger > .7;

        if (gamepad1.dpad_up) {
            tabasco.currentSpeed = tabasco.MAX_SPEED;
        } else if (gamepad1.dpad_left) {
            tabasco.currentSpeed = tabasco.FAST_SPEED;
        } else if (gamepad1.dpad_down) {
            tabasco.currentSpeed = tabasco.SLOW_SPEED;
        }

        tabasco.controlMecanumWheels(mecanumSpeed, mecanumTurn, mecanumStrafe, mecanumSlowStrafe, mecanumSlowSpeed, mecanumSlowTurn);

        // **** Block Separator - Spatula ****
        if (gamepad1.x && tabasco.rightSpatulaTimer.seconds() > 0.3) {
            tabasco.rightSpatulaTimer.reset();
            if (tabasco.isRightSpatulaUp) {
                tabasco.isRightSpatulaUp = false;
                tabasco.lowerSpatR();
            } else {
                tabasco.isRightSpatulaUp = true;
                tabasco.ricePattyR();
            }
        }

        if (gamepad1.y && tabasco.leftSpatulaTimer.seconds() > 0.3) {
            tabasco.leftSpatulaTimer.reset();
            if (tabasco.isLeftSpatulaUp) {
                tabasco.isLeftSpatulaUp = false;
                tabasco.lowerSpatL();
            } else {
                tabasco.isLeftSpatulaUp = true;
                tabasco.ricePattyL();
            }
        }

        // ##### DRIVER 2 #####
        // **** Capstone ****
        if (gamepad2.right_trigger > 0.3) {
            tabasco.gateOpen();
            tabasco.capstoneDown();
        }
        if (gamepad2.left_trigger > 0.1) {
            tabasco.capstoneUp();
        }


        // **** Stone Grabber ****
        if (gamepad2.x) {
            tabasco.stoneRotatorEnd();
        } else if (gamepad2.y) {
            tabasco.stoneRotatorMid();
        } else if (gamepad2.b) {
            tabasco.stoneRotatorStart();
        }
        if (gamepad2.right_bumper) {
            tabasco.grabStone();
        } else if (gamepad2.left_bumper) {
            tabasco.releaseStone();
        }

        // **** Vertical Lift ****
        tabasco.verticalSlide(gamepad2.left_stick_y);

        if (gamepad2.dpad_left && !tabasco.isLiftReturning) {
            tabasco.isLiftReturning = true;
            tabasco.returnS1 ();
            tabasco.returnLiftTimer. reset();
        }
        if (tabasco.isLiftReturning) {
            if (tabasco.returnLiftTimer.seconds() > 0.75) {
                tabasco.horizontalSlide(0.0);
                tabasco.returnS2 ();
                tabasco.isLiftReturning = false;
            }
        }
        if (gamepad2.dpad_up) {
            tabasco.verticalTarget = tabasco.verticalMax;
            //very temporary, any more useful function is welcome.
        }
        else if (gamepad2.dpad_down) {
            tabasco.verticalTarget = tabasco.level0;
        }
        if (tabasco.verticalTarget > tabasco.verticalMax) {
            tabasco.verticalTarget = tabasco.verticalMax;
        }
        //-----------Rarely used if ever--------------------
        if (gamepad2.a && gamepad2.dpad_right) {
            tabasco.level0 = tabasco.motorVerticalSlide.getCurrentPosition();
            tabasco.verticalMax = tabasco.level0 + 8250;
        }

        tabasco.motorVerticalSlide.setTargetPosition(tabasco.verticalTarget);

        // **** Horizontal Lift ****
        if (!tabasco.isLiftReturning) {
            tabasco.horizontalSlide(gamepad2.right_stick_y);
        }

        //unused buttons: GP1 dpad_right, combos
        //unused buttons: GP2 most combos

        telemetry.addData("Position", tabasco.motorVerticalSlide.getCurrentPosition());
        telemetry.addData("At Bottom?", tabasco.isLiftAtBottom());
        telemetry.update();
    }

    @Override
    public void stop() {

    }
}
