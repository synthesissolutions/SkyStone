
package org.firstinspires.ftc.teamcode.AUTO;

        import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

        import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
        import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
        import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "Blue3AutoProto", group = "Linear Opmode")
public class Blue3AutoProto extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("currentAngle", angles.firstAngle);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        Blue3Auto();
    }

    public void Blue3Auto() {
        //Stone on right
        strafeRight(0.4, 600);
        driveStraightForward(0.3, 1100);
        lowerSpat();
        delay(0.2);
        //1st skystone captured
        driveStraightBack(0.3,400);
        driveStraightForward(0.25, 50);
        raiseSpat();
        //1st skystone in position
        delay(0.1);
        strafeLeft(0.4, 600);
        driveStraightForward(0.3, 350);
        lowerSpat();
        delay(0.2);
        //2nd skystone captured
        driveStraightBack(0.3, 400);
        driveStraightForward(0.25, 50);
        raiseSpat();
        delay(0.1);
        //2nd skystone in position
        //vertical stuff
        intakeIn();
        spinRight(30, 0.3, 0.19);
        driveStraightForward(0.3, 200);
        spinRight(60, 0.3, 0.17);
        intakeOut();
        delay(0.2);
        intakeOff();
        //vertical stuff
        //LAUNCH
        driveStraightBack(1.0, 2500);
        spinRight(88, 0.5, 0.2);
        //ready to move foundation
        driveStraightBack(0.5, 300);
        bumpRightB(0.3, 120);
        grabFoundation();
        delay(0.45);
        //foundation captured
        strafeLeft(0.5, 300);
        //vertical stuff
        driveStraightForward(0.5, 800);
        hardCurveRightB(0.6, 1300);
        driveStraightBack(0.7, 300);
        strafeRight(0.6, 200);
        releaseFoundation();
        delay (0.45);
        //2nd launch
        intakeIn();
        driveStraightForward(1.0, 2500);
        //vertical stuff
        driveStraightForward(1.0, 1500);
        delay(0.2);
        intakeOut();
        delay(0.2);
        intakeOff();
        //vertical stuff
        //3rd launch
        driveStraightBack(1.0, 4000);
        //vertical stuff
        delay(1.0);
        driveStraightForward(0.5, 3000);








    }
}