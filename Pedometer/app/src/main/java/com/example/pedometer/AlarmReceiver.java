package com.example.pedometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    static int count;
    int sum = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        SensorEventThread sensorEventThread = new SensorEventThread(context);
        sensorEventThread.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        count = sensorEventThread.getCount() - sum;
        String line;
        String contents = null;
        try {
            FileInputStream fileInputStream = context.openFileInput("text.txt");
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                contents = stringBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int[] date = {calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE)};
        String s = contents + date[0] + "/" + date[1] + "/" + date[2] + " : " + count + "歩\n";
        try {
            FileOutputStream fileOutputstream = context.openFileOutput("text.txt", Context.MODE_PRIVATE);
            fileOutputstream.write(s.getBytes(StandardCharsets.UTF_8));
            fileOutputstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class SensorEventThread extends Thread implements SensorEventListener {
    Context context;
    static int count;
    private SensorManager sensorManager;
    public SensorEventThread(Context context){this.context = context;}
    public void run(){
        sensorManager = (SensorManager) (context.getSystemService(Context.SENSOR_SERVICE));
        Sensor accel = sensorManager.getDefaultSensor(
                Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public int getCount(){return count;}

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            count = (int) event.values[0];
        }
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}