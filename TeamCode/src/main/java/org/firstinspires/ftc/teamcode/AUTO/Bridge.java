package org.firstinspires.ftc.teamcode.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "Bridge", group = "Linear Opmode")
public class Bridge extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeMecanum();
        initializeCapstoneDropper();
        waitForStart();
        runtime.reset();

        driveStraightForward(0.25, 800);
    }
}

