package com.example.antitheftandroidapp.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.antitheftandroidapp.Services.AntiTheftService;

public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if (TelephonyManager.EXTRA_STATE_RINGING.equals(state) && incomingNumber != null) {
                Log.d("CallReceiver", "Incoming call from: " + incomingNumber);
                Intent serviceIntent = new Intent(context, AntiTheftService.class);
                serviceIntent.setAction("com.example.antitheftandroidapp.ACTION_CHECK_MISSED_CALL");
                serviceIntent.putExtra("incoming_number", incomingNumber);
                context.startService(serviceIntent);
            }
        }
    }
}
