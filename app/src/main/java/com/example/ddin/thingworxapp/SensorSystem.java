package com.example.ddin.thingworxapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vobis on 2017-04-14.
 */
public class SensorSystem {

    private MainActivity parent;
    private ConcurrentHashMap appProperties;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    long lastUpdate = 0;
    float[] lastDevicePosition = {0.0f, 0.0f, 0.0f};
    int index = 0;
    String[] buffer = {"0.5","0.7","0.9", "1.0"};

    private static final int SHAKE_THRESHOLD1 = 600;
    private static final int SHAKE_THRESHOLD2 = 500;
    private static final int SHAKE_THRESHOLD3 = 400;
    private static final int SHAKE_THRESHOLD4 = 300;

    public SensorSystem(MainActivity parent, ConcurrentHashMap appProperties){
        this.parent = parent;
        this.appProperties = appProperties;
        senSensorManager = (android.hardware.SensorManager) parent.getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(parent, senAccelerometer, android.hardware.SensorManager.SENSOR_DELAY_NORMAL);
    }

    private Double vecLength(float[] vec){
        return Math.sqrt(
                  Math.pow(vec[0],2)
                + Math.pow(vec[1],2)
                + Math.pow(vec[2],2)
        );
    }

    private Double[] unitVec(float[] vec){
        Double length = vecLength(vec);
        return new Double[]{vec[0]/length, vec[1]/length, vec[2]/length};
    }

    private Double unitDot(Double[] vec1, Double[] vec2){
        Double res = 0.0;
        for (int i = 0; i < 3; i++){
            res += vec1[i] * vec2[i];
        }
        return res;
    }

    private String calculateSpeed(SensorEvent sensorEvent, long diffTime){
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        float speed = Math.abs(
                x + y + z
                        - lastDevicePosition[0]
                        - lastDevicePosition[1]
                        - lastDevicePosition[2]
        ) * diffTime * 1000;
        String valueToSend = "0";
        if (speed < SHAKE_THRESHOLD4) {
            valueToSend = "0.5";
        } else if (speed < SHAKE_THRESHOLD3)
            valueToSend = "0.66";
        else if (speed < SHAKE_THRESHOLD2)
            valueToSend = "0.83";
        else if (speed > SHAKE_THRESHOLD1)
            valueToSend = "1";
        return valueToSend;
    }

    private String calculateForce(SensorEvent sensorEvent){
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        return String.valueOf(z);
    }

    public boolean processSensor(SensorEvent sensorEvent, long diffTime) {
        Sensor mySensor = sensorEvent.sensor;
            if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
                System.arraycopy(sensorEvent.values, 0, lastDevicePosition, 0, 3);
                String valueToSend = calculateForce(sensorEvent);
                appProperties.put("ShakeTreshold", valueToSend);
                //index = (index + 1) % buffer.length;
                //appProperties.put("ShakeTreshold", buffer[index]);
                System.out.println("Value: " + valueToSend);
                System.out.println("==================================================");
                System.out.println(" z:" + sensorEvent.values[2]);
                System.out.println("==================================================");
                return true;
            }
        return false;
    }
}
