package com.example.antitheftandroidapp.Receivers;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.admin.DevicePolicyManager;
import android.widget.Toast;

import com.example.antitheftandroidapp.Services.AntiTheftService;

public class MyDeviceAdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        super.onPasswordFailed(context, intent);

        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        int failedAttempts = dpm.getCurrentFailedPasswordAttempts();

        // Define the maximum number of attempts
        int maxFailedAttempts = 3;

        if (failedAttempts >= maxFailedAttempts) {
            // Activate anti-theft measures
            Intent antiTheftIntent = new Intent(context, AntiTheftService.class);
            context.startService(antiTheftIntent);
        }
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        showToast(context, "Device admin enabled");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        showToast(context, "Device admin disabled");
    }

    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
