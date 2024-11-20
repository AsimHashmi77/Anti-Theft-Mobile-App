package com.example.antitheftandroidapp.Model;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UserLog {
    private static final String TAG = "UserLog";
    private File logFile;

    public UserLog(Context context) {
        logFile = new File(context.getExternalFilesDir(null), "user_log.txt");
    }

    public void logEvent(String message) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String level = "INFO";
        String tag = "UserLog";

        String logEntry = timestamp + "|" + level + "|" + tag + "|" + message;

        Log.d(TAG, "Writing log entry: " + logEntry);

        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(logEntry + "\n");
            Log.d(TAG, "Log entry written to file");
        } catch (IOException e) {
            Log.e(TAG, "Error writing log entry", e);
        }
    }
}
