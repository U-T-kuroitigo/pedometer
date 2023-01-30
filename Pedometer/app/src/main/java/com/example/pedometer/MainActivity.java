/*
作成者：上坂竜哉
作成内容：歩数計
 */
package com.example.pedometer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        textView = findViewById(R.id.text_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accel = sensorManager.getDefaultSensor(
                Sensor.TYPE_STEP_COUNTER);

        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
        int a = checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION);
        Log.d("sensorManager","" + a);
        int b = ContextCompat.checkSelfPermission(this,Manifest.permission.ACTIVITY_RECOGNITION) ;
        Log.d("sensorManager","" + b);
    }

    // 解除コード
    @Override
    protected void onPause() {
        super.onPause();
        // Listenerを解除
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float sensorX, sensorY;

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            sensorX = event.values[0];
            String strTmp ="X: " + sensorX + "\n";
            textView.setText(strTmp);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
