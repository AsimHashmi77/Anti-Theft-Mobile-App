package com.example.antitheftandroidapp.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.antitheftandroidapp.Services.AntiTheftService;

public class ScreenStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // Start the AntiTheftService to capture a photo when the screen is off
            Intent serviceIntent = new Intent(context, AntiTheftService.class);
            serviceIntent.setAction("com.example.antitheftandroidapp.ACTION_CAPTURE_PHOTO");
            context.startService(serviceIntent);
        }
    }
}
