package com.example.pedometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
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

        String s = contents + "アラーム書き込み\n";
        try {
            FileOutputStream fileOutputstream = context.openFileOutput("text.txt", Context.MODE_PRIVATE);
            fileOutputstream.write(s.getBytes(StandardCharsets.UTF_8));
            fileOutputstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
