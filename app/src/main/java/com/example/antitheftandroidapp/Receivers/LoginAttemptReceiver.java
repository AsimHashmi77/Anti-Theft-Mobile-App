package com.example.antitheftandroidapp.Receivers;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class LoginAttemptReceiver extends BroadcastReceiver {

    private static final String ACTION_LOGIN_ATTEMPT = "android.intent.action.LOGIN_ATTEMPT";
    private static final String BLOCKING_NUMBER = "+1234567890"; // Example blocking number

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(ACTION_LOGIN_ATTEMPT)) {
            // Intercept login attempt
            // Here you can implement your logic to check for incorrect login attempts
            // For demonstration purposes, let's assume an incorrect attempt
            boolean incorrectAttempt = true;

            if (incorrectAttempt) {
                // Trigger action to remotely block the device (e.g., lock the device)
                blockDevice(context);
            }
        }
    }

    private void blockDevice(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(context, MyDeviceAdminReceiver.class);

        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.lockNow();
        } else {
            Toast.makeText(context, "Device admin not active", Toast.LENGTH_SHORT).show();
        }
    }
}

