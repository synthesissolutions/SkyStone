package org.firstinspires.ftc.teamcode.TELE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Phone Gyro Motorola", group="TELE")
// @Disabled
public class PhoneGyroTestMotorola extends OpMode implements SensorEventListener {
    private ElapsedTime elapsedTime = new ElapsedTime();

    //List<Float> gyroEventValues = new ArrayList<>();
    float gyroCurrentHeading = 0.0f;
    float[] orientations = new float[3];
    float[] mGravity = new float[3];
    float[] mGeomagnetic = new float[3];
    String message = "nothing";
    String message2 = "nothing";

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
        telemetry.addData("Orientaiton[0]", orientations[0]);
        telemetry.addData("Orientaiton[1]", orientations[1]);
        telemetry.addData("Orientaiton[2]", orientations[2]);
        telemetry.addData("Message", message);
        telemetry.addData("Message2", message2);
        telemetry.update();
    }

    @Override
    public void stop() {

    }

    protected void initializePhoneGyro() {
        SensorManager sensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
        Sensor magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private final float[] mMagnet = new float[3];               // magnetic field vector
    private final float[] mAcceleration = new float[3];         // accelerometer vector
    private final float[] mAccMagOrientation = new float[3];    // orientation angles from mAcceleration and mMagnet
    private float[] mRotationMatrix = new float[9];             // accelerometer and magnetometer based rotation matrix


    public void onSensorChanged(SensorEvent event) {
        /*
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
        float[] orientation = new float[3];
        SensorManager.getOrientation(rotationMatrix, orientation);
        for(int i = 0; i < 3; i++) {
            orientations[i] = (float)(Math.toDegrees(orientation[i]));
        }
        */

        /*
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values;
            message = "TYPE_ACCELEROMETER";
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagnetic = event.values;
            message = "TYPE_MAGNETIC_FIELD";
        }

        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                for(int i = 0; i < 3; i++) {
                    orientations[i] = (float)(Math.toDegrees(orientation[i]));
                }
            }
        }
         */
        /*
        float ax, ay, az;

        ax = event.values[0] / 9.81f;
        ay = event.values[1] / 9.81f;
        az = event.values[2] / 9.81f;
        float xAngle = -(float)Math.atan( ax / (Math.sqrt(ay*ay + az*az)));
        float yAngle = -(float)Math.atan( ay / (Math.sqrt(ax*ax + az*az)));
        float zAngle = (float)Math.atan( Math.sqrt(ax*ax + ay*ay) / az);

        orientations[0] = (float)Math.toDegrees(xAngle);
        orientations[1] = (float)Math.toDegrees(yAngle);
        orientations[2] = (float)Math.toDegrees(zAngle);
         */
        /*
        int x = (int)event.values[0];
        int y = (int)event.values[1];
        int angle = 0;

        if(y>=0 && x<=0) angle = x*10;
        if(x<=0 && y<=0) angle = (y*10)-90;
        if(x>=0 && y<=0) angle = (-x*10)-180;
        if(x>=0 && y>=0) angle = (-y*10)-270;

        orientations[0] = (float)angle;
        orientations[1] = (float)angle;
        orientations[2] = (float)angle;

         */

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(event.values, 0, mAcceleration, 0, 3);   // save datas
                calculateAccMagOrientation();                       // then calculate new orientation
                message = "TYPE_ACCELEROMETER";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(event.values, 0, mMagnet, 0, 3);         // save datas
                message = "TYPE_MAGNETIC_FIELD";
                break;
            default: break;
        }
    }

    public void calculateAccMagOrientation() {
        if (SensorManager.getRotationMatrix(mRotationMatrix, null, mAcceleration, mMagnet))
            SensorManager.getOrientation(mRotationMatrix, mAccMagOrientation);
        else { // Most chances are that there are no magnet datas
            double gx, gy, gz;
            gx = mAcceleration[0] / 9.81f;
            gy = mAcceleration[1] / 9.81f;
            gz = mAcceleration[2] / 9.81f;
            // http://theccontinuum.com/2012/09/24/arduino-imu-pitch-roll-from-accelerometer/
            float pitch = (float) -Math.atan(gy / Math.sqrt(gx * gx + gz * gz));
            float roll = (float) -Math.atan(gx / Math.sqrt(gy * gy + gz * gz));
            float azimuth = 0; // Impossible to guess

            mAccMagOrientation[0] = azimuth;
            mAccMagOrientation[1] = pitch;
            mAccMagOrientation[2] = roll;

            orientations[0] = (float)Math.toDegrees(mAccMagOrientation[0]);
            orientations[1] = (float)Math.toDegrees(mAccMagOrientation[1]);
            orientations[2] = (float)Math.toDegrees(mAccMagOrientation[2]);

            mRotationMatrix = getRotationMatrixFromOrientation(mAccMagOrientation);
        }
    }
    public static float[] getRotationMatrixFromOrientation(float[] o) {
        float[] xM = new float[9];
        float[] yM = new float[9];
        float[] zM = new float[9];

        float sinX = (float) Math.sin(o[1]);
        float cosX = (float) Math.cos(o[1]);
        float sinY = (float) Math.sin(o[2]);
        float cosY = (float) Math.cos(o[2]);
        float sinZ = (float) Math.sin(o[0]);
        float cosZ = (float) Math.cos(o[0]);

        // rotation about x-axis (pitch)
        xM[0] = 1.0f;xM[1] = 0.0f;xM[2] = 0.0f;
        xM[3] = 0.0f;xM[4] = cosX;xM[5] = sinX;
        xM[6] = 0.0f;xM[7] =-sinX;xM[8] = cosX;

        // rotation about y-axis (roll)
        yM[0] = cosY;yM[1] = 0.0f;yM[2] = sinY;
        yM[3] = 0.0f;yM[4] = 1.0f;yM[5] = 0.0f;
        yM[6] =-sinY;yM[7] = 0.0f;yM[8] = cosY;

        // rotation about z-axis (azimuth)
        zM[0] = cosZ;zM[1] = sinZ;zM[2] = 0.0f;
        zM[3] =-sinZ;zM[4] = cosZ;zM[5] = 0.0f;
        zM[6] = 0.0f;zM[7] = 0.0f;zM[8] = 1.0f;

        // rotation order is y, x, z (roll, pitch, azimuth)
        float[] resultMatrix = matrixMultiplication(xM, yM);
        resultMatrix = matrixMultiplication(zM, resultMatrix);
        return resultMatrix;
    }
    public static float[] matrixMultiplication(float[] A, float[] B) {
        float[] result = new float[9];

        result[0] = A[0] * B[0] + A[1] * B[3] + A[2] * B[6];
        result[1] = A[0] * B[1] + A[1] * B[4] + A[2] * B[7];
        result[2] = A[0] * B[2] + A[1] * B[5] + A[2] * B[8];

        result[3] = A[3] * B[0] + A[4] * B[3] + A[5] * B[6];
        result[4] = A[3] * B[1] + A[4] * B[4] + A[5] * B[7];
        result[5] = A[3] * B[2] + A[4] * B[5] + A[5] * B[8];

        result[6] = A[6] * B[0] + A[7] * B[3] + A[8] * B[6];
        result[7] = A[6] * B[1] + A[7] * B[4] + A[8] * B[7];
        result[8] = A[6] * B[2] + A[7] * B[5] + A[8] * B[8];

        return result;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        message2 = "onAccuracyChanged";
    }
}