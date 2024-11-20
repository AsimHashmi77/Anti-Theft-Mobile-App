package com.example.antitheftandroidapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.antitheftandroidapp.R;


public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DELAY = 3000; // Duration for splash screen in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the XML layout for the splash screen
        setContentView(R.layout.activity_splash_screen);

        // Handler to delay the navigation to the main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Navigate to the main activity
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);

                // Finish the current splash screen activity
                finish();
            }
        }, SPLASH_SCREEN_DELAY);
    }
}