package com.example.antitheftandroidapp.Services;

import android.Manifest;
import android.app.IntentService;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.antitheftandroidapp.DBHelper.DBHelper;
import com.example.antitheftandroidapp.Receivers.MyDeviceAdminReceiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AntiTheftService extends IntentService {

    private static final String TAG = "AntiTheftService";
    private Context mContext;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mAdminComponent;
    private DBHelper dbHelper;

    public AntiTheftService() {
        super("AntiTheftService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mContext = getApplicationContext();
        dbHelper = new DBHelper(mContext);
        mDevicePolicyManager = (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminComponent = new ComponentName(mContext, MyDeviceAdminReceiver.class);

        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case "com.example.antitheftandroidapp.ACTION_LOCK_DEVICE":
                    // Lock the device
                    lockDevice();
                    break;
                case "com.example.antitheftandroidapp.ACTION_RECORD_VIDEO":
                    // Start video recording
                    startVideoRecording();
                    break;
                case "com.example.antitheftandroidapp.ACTION_CAPTURE_PHOTO":
                    // Capture photo
                    capturePhoto();
                    break;
                case "android.intent.action.SIM_STATE_CHANGED":
                    // Monitor SIM change
                    monitorSimChange();
                    break;
                case "com.example.antitheftandroidapp.ACTION_CHECK_MISSED_CALL":
                    // Check for missed call
                    String incomingNumber = intent.getStringExtra("incoming_number");
                    checkMissedCall(incomingNumber);
                    break;
                default:
                    // Capture photo and notify owner
                    capturePhoto();
                    notifyOwner();
                    break;
            }
        }
    }

    private void checkMissedCall(String incomingNumber) {
        String registeredNumber = dbHelper.getRegisteredMobile();
        if (incomingNumber != null && incomingNumber.equals(registeredNumber)) {
            Log.d(TAG, "Missed call from registered number: " + incomingNumber);
            lockDevice();
        }
    }


    private void capturePhoto() {
        Camera camera = null;
        try {
            camera = Camera.open();
            Camera.Parameters params = camera.getParameters();
            camera.setParameters(params);
            camera.startPreview();
            camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    File pictureFile = new File(Environment.getExternalStorageDirectory(), "thief_photo.jpg");
                    try (FileOutputStream fos = new FileOutputStream(pictureFile)) {
                        fos.write(data);
                        Toast.makeText(getApplicationContext(), "Photo captured", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e(TAG, "Error saving photo: " + e.getMessage());
                    } finally {
                        camera.release();
                    }
                }
            });
        } catch (Exception e) {
            if (camera != null) {
                camera.release();
            }
            Log.e(TAG, "Error capturing photo: " + e.getMessage());
        }
    }

    private void notifyOwner() {
        String registeredMobile = dbHelper.getRegisteredMobile();
        String registeredEmail = dbHelper.getRegisteredEmail();

        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String address = getAddressFromCoordinates(location.getLatitude(), location.getLongitude());
                sendSms(registeredMobile, "Your phone's location: " + address);
                sendEmail(registeredEmail, "Mobile Location", "Your phone's location: " + address);
                sendVideo(registeredMobile, registeredEmail);
                locationManager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
        } catch (SecurityException e) {
            Log.e(TAG, "Location permission not granted");
        }
    }

    private void sendVideo(String phoneNumber, String email) {
        // Get the latest recorded video file
        File videoFile = getLatestVideoFile();

        // Check if the video file exists
        if (videoFile != null && videoFile.exists()) {
            // Send the video via SMS
            sendMms(phoneNumber, videoFile);

            // Send the video via Email
            sendEmailWithAttachment(email, "Recorded Video", "Please find the recorded video attached.", videoFile);
        } else {
            Log.e(TAG, "Video file not found.");
        }
    }

    private void sendMms(String phoneNumber, File file) {
        try {
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra("address", phoneNumber);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("video/mp4");
            mContext.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error sending MMS: " + e.getMessage());
        }
    }

    private void sendEmailWithAttachment(String email, String subject, String body, File file) {
        try {
            Uri uri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mContext.startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (Exception e) {
            Log.e(TAG, "Error sending email with attachment: " + e.getMessage());
        }
    }


    private File getLatestVideoFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "AntiTheftVideos");
        File[] files = mediaStorageDir.listFiles();
        if (files != null && files.length > 0) {
            // Sort files by last modified time to get the latest one
            Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
            return files[0]; // Return the latest video file
        }
        return null; // No video file found
    }


    private void lockDevice() {
        if (mDevicePolicyManager.isAdminActive(mAdminComponent)) {
            mDevicePolicyManager.lockNow();
            mDevicePolicyManager.setPasswordQuality(mAdminComponent, DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
        } else {
            Toast.makeText(mContext, "Device admin not active", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminComponent);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Please enable device admin to allow anti-theft features.");
            mContext.startActivity(intent);
        }
    }

    private void startVideoRecording() {
        // Start the video recording service
        Intent videoIntent = new Intent(mContext, VideoRecordingService.class);
        mContext.startService(videoIntent);
    }

    private void monitorSimChange() {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    // SIM state changed, take necessary action here
                    String simState = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT ?
                            "SIM Absent" : "SIM Present";
                    Log.d(TAG, "SIM state changed: " + simState);
                    // Send information to registered mobile and email
                    sendSimChangeNotification(simState);
                }
            }
        };
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    private void sendSimChangeNotification(String message) {
        // Get registered mobile number and email
        String registeredMobile = dbHelper.getRegisteredMobile();
        String registeredEmail = dbHelper.getRegisteredEmail();

        // Send SMS with SIM change information
        sendSms(registeredMobile, "SIM change detected: " + message);

        // Send email with SIM change information
        sendEmail(registeredEmail, "SIM Change Detected", message);
    }


    private String getAddressFromCoordinates(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error getting address from coordinates: " + e.getMessage());
        }
        return "Lat: " + latitude + ", Long: " + longitude;
    }

    private void sendSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (Exception e) {
            Log.e(TAG, "Error sending SMS: " + e.getMessage());
        }
    }

    private void sendEmail(String email, String subject, String message) {
        // Code for sending email
    }
}
