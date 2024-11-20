package com.example.antitheftandroidapp.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class SimChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.SIM_STATE_CHANGED")) {
            // SIM state changed
            String state = getSimState(intent);
            Log.d("SimChangeReceiver", "SIM state changed: " + state);
            Toast.makeText(context, "SIM state changed: " + state, Toast.LENGTH_LONG).show();

            // Check if the SIM state indicates a change
            if (state != null && (state.equals("ABSENT") || state.equals("READY"))) {
                // Notify the user or take necessary action on SIM change
                notifySimChange(context);
            }
        }
    }

    private String getSimState(Intent intent) {
        String state = null;
        if (intent.hasExtra("ss")) {
            state = intent.getStringExtra("ss");
        }
        return state;
    }

    private void notifySimChange(Context context) {
        // Example: Sending a basic notification
        Toast.makeText(context, "SIM change detected", Toast.LENGTH_LONG).show();

        // Here you can add logic to send an SMS or email if needed
        sendSimChangeNotification(context);
    }

    private void sendSimChangeNotification(Context context) {
        // Replace with the actual phone number and email of the registered user
        String phoneNumber = "registered_phone_number";
        String recipientEmail = "recipient@example.com";

        // Example SMS message
        String message = "SIM card state changed on your device.";
        sendSms(context, phoneNumber, message);

        // Example email content
        String subject = "SIM Change Alert";
        String body = "The SIM card state on your device has changed.";
        sendEmail(context, recipientEmail, subject, body);
    }

    private void sendSms(Context context, String phoneNumber, String message) {
        // Implement SMS sending logic here
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private void sendEmail(Context context, String recipientEmail, String subject, String body) {
        // Implement email sending logic here
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
