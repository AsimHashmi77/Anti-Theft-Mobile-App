package com.example.antitheftandroidapp.Services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationTrackingService extends Service {

    private static final String TAG = "LocationTrackingService";
    private static final int MIN_TIME_INTERVAL = 1000 * 60; // 1 minute
    private static final float MIN_DISTANCE_INTERVAL = 10; // 10 meters

    private LocationManager locationManager;
    private Geocoder geocoder;

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        geocoder = new Geocoder(this, Locale.getDefault());
        startLocationUpdates();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Location permission not granted");
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                MIN_TIME_INTERVAL, MIN_DISTANCE_INTERVAL, locationListener);
    }

    private void stopLocationUpdates() {
        locationManager.removeUpdates(locationListener);
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "Location updated: " + location.getLatitude() + ", " + location.getLongitude());
            // Send location via email or SMS
            sendLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "Location provider disabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "Location provider enabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "Location provider status changed: " + provider + ", Status: " + status);
        }
    };

    private void sendLocation(Location location) {
        // Fetch address from coordinates using Google Maps API
        String address = getAddressFromCoordinates(location.getLatitude(), location.getLongitude());

        // Prepare the message with address and coordinates
        String message = "Location: " + address + "\nLatitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude();

        // Send the SMS with the message
        sendSMS("recipientPhoneNumber", message);
    }

    private String getAddressFromCoordinates(double latitude, double longitude) {
        String addressStr = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                StringBuilder addressBuilder = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressBuilder.append(address.getAddressLine(i)).append(" ");
                }
                addressStr = addressBuilder.toString().trim();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error getting address from coordinates: " + e.getMessage());
        }
        return addressStr;
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "Location sent via SMS", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Failed to send SMS: " + e.getMessage());
        }
    }
}
