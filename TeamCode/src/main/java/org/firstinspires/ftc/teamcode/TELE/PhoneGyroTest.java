package org.firstinspires.ftc.teamcode.TELE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Phone Gyro", group="TELE")
// @Disabled
public class PhoneGyroTest extends OpMode implements SensorEventListener {
    private ElapsedTime elapsedTime = new ElapsedTime();

    //List<Float> gyroEventValues = new ArrayList<>();
    float gyroCurrentHeading = 0.0f;
    String message = "nothing";

    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        initializePhoneGyro();
    }

    @Override
    public void start() {
        elapsedTime.reset();
    }

    // Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    @Override
    public void loop() {
        telemetry.addData("Gryo Heading", gyroCurrentHeading);
        telemetry.update();
    }

    @Override
    public void stop() {

    }
    protected void initializePhoneGyro() {
        SensorManager sensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        Sensor gameRotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        sensorManager.registerListener(this, gameRotationSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void onSensorChanged(SensorEvent event) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
        float[] orientation = new float[3];
        SensorManager.getOrientation(rotationMatrix, orientation);
        gyroCurrentHeading = (float) ((Math.toDegrees(orientation[0]))% 360 - 360) % 360;
        message = "onSensorChanged";
        //if (Math.abs(gyroCurrentHeading) > 180) gyroCurrentHeading+=360;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        message = "onAccuracychanged";
    }
}