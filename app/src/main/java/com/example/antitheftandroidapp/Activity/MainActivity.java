package com.example.antitheftandroidapp.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.antitheftandroidapp.Fragment.OtherDevicesInformationFragment;
import com.example.antitheftandroidapp.Fragment.PreferencesFragment;
import com.example.antitheftandroidapp.Fragment.ProfileFragment;
import com.example.antitheftandroidapp.Fragment.UserLogFragment;
import com.example.antitheftandroidapp.R;
import com.example.antitheftandroidapp.Services.AntiTheftService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    private final ProfileFragment profileFragment = new ProfileFragment();
    private final PreferencesFragment preferencesFragment = new PreferencesFragment();
    private final OtherDevicesInformationFragment otherDevicesInformationFragment = new OtherDevicesInformationFragment();
    private final UserLogFragment userLogFragment = new UserLogFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();

        // Load the default fragment and set the corresponding menu item as selected
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.container, preferencesFragment).commit();
            bottomNavigationView.setSelectedItemId(R.id.navigation_preferences);  // Set Preferences as selected
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    selectedFragment = profileFragment;
                    break;
                case R.id.navigation_preferences:
                    selectedFragment = preferencesFragment;
                    break;
                case R.id.navigation_device_management:
                    selectedFragment = otherDevicesInformationFragment;
                    break;
                case R.id.navigation_user_log:
                    selectedFragment = userLogFragment;
                    break;
            }
            if (selectedFragment != null) {
                fragmentManager.beginTransaction().replace(R.id.container, selectedFragment).commit();
                return true;
            }
            return false;
        });

    }
}
