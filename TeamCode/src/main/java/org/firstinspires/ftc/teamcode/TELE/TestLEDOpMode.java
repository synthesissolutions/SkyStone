package org.firstinspires.ftc.teamcode.TELE;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="LED Testing2", group="TELE")
@Disabled
public class TestLEDOpMode extends OpMode {
    private QwiicLEDStrip ledStrip;
    private ElapsedTime elapsedTime = new ElapsedTime();
    private int colorIndex = 0;
    boolean showFirst = true;

    DigitalChannel sensorFoundationLeft;

    private @ColorInt int[] colors = new int[] { Color.rgb(148, 0, 211), Color.rgb(75, 0, 130), Color.rgb(0, 0, 255),
            Color.rgb(0, 255, 0), Color.rgb(255, 0, 0), Color.rgb(255, 255, 0), Color.parseColor("purple"),
            Color.parseColor("teal"), Color.parseColor("silver"), Color.rgb(0, 0, 0) };

    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        ledStrip = hardwareMap.get(QwiicLEDStrip.class, "led_strip");
        ledStrip.setBrightness(4);
        ledStrip.setColor(Color.parseColor("yellow"));
        ledStrip.setColor(5, Color.parseColor("blue"));

        sensorFoundationLeft = hardwareMap.get(DigitalChannel.class, "SFLeft");
        sensorFoundationLeft.setMode(DigitalChannel.Mode.INPUT);
    }

    @Override
    public void start() {
        elapsedTime.reset();
        clearColorsForLED();
    }

    @Override
    public void loop() {
        /*
        if (elapsedTime.milliseconds() >= 500) {
            if (colorIndex == colors.length) {
                ledStrip.setColors(colors);
                colorIndex = 0;
            } else {
                ledStrip.setColor(colors[colorIndex]);
                colorIndex++;
            }
            elapsedTime.reset();
        }

         */

        if (elapsedTime.milliseconds() >= 100) {
            setColorsForLED();
            elapsedTime.reset();
        }
        /*
        if (elapsedTime.seconds() > 5) {
            elapsedTime.reset();
            if (showFirst) {
                int i = 1;
                ledStrip.setColor(i++, Color.rgb(0,0,255));
                ledStrip.setColor(i++, Color.rgb(0,255,0));
                ledStrip.setColor(i++, Color.rgb(255,0,0));
                ledStrip.setColor(i++, Color.rgb(255,255,0));
                ledStrip.setColor(i++, Color.rgb(255,255,255));
                ledStrip.setColor(i++, Color.rgb(0,0,0));
                ledStrip.setColor(i++, Color.rgb(0,255,255));
                ledStrip.setColor(i++, Color.rgb(255,0,0));
                ledStrip.setColor(i++, Color.rgb(255,0,0));
                ledStrip.setColor(i++, Color.rgb(128,128,128 ));
            } else {
                int i = 1;
                ledStrip.setColor(i++, Color.rgb(0,0,0));
                ledStrip.setColor(i++, Color.rgb(255,0,0));
                ledStrip.setColor(i++, Color.rgb(0,255,0));
                ledStrip.setColor(i++, Color.rgb(0,0,255));
                ledStrip.setColor(i++, Color.rgb(255,255,0));
                ledStrip.setColor(i++, Color.rgb(255,255,255));
                ledStrip.setColor(i++, Color.rgb(255,0,0));
                ledStrip.setColor(i++, Color.rgb(0,255,255));
                ledStrip.setColor(i++, Color.rgb(128,128,128 ));
                ledStrip.setColor(i++, Color.rgb(255,0,0));
            }
            showFirst = !showFirst;
        }
         */

        telemetry.addData("Touch Pressed?", isFLeftPressed());
        telemetry.update();
    }

    public boolean isFLeftPressed() {
        return !sensorFoundationLeft.getState();
    }

    public boolean isFRightPressed() {
        return false;
    }

    public void setColorsForLED() {
        if (isFLeftPressed()) {
            ledStrip.setColor(3, Color.parseColor("green"));
            ledStrip.setColor(4, Color.parseColor("green"));
        } else {
            ledStrip.setColor(3, Color.parseColor("red"));
            ledStrip.setColor(4, Color.parseColor("red"));
        }

        if (isFRightPressed()) {
            ledStrip.setColor(7, Color.parseColor("green"));
            ledStrip.setColor(8, Color.parseColor("green"));
        } else {
            ledStrip.setColor(7, Color.parseColor("red"));
            ledStrip.setColor(8, Color.parseColor("red"));
        }
    }

    public void clearColorsForLED() {
        ledStrip.setColor(Color.parseColor("black"));
    }
    @Override
    public void stop() {
        ledStrip.turnAllOff();
    }
}