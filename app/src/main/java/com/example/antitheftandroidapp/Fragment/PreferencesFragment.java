package com.example.antitheftandroidapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.antitheftandroidapp.R;

public class PreferencesFragment extends Fragment {

    // Define Views for the preferences
    private SwitchCompat switchMissedCallLock;
    private EditText editTextFailedAttempts;
    private SwitchCompat switchScreenOffPictures;
    private Button buttonSavePreferences;

    // Define SharedPreferences for storing the preferences
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preferences, container, false);

        // Initialize Views
        switchMissedCallLock = view.findViewById(R.id.switchMissedCallLock);
        editTextFailedAttempts = view.findViewById(R.id.editTextFailedAttempts);
        switchScreenOffPictures = view.findViewById(R.id.switchScreenOffPictures);
        buttonSavePreferences = view.findViewById(R.id.buttonSavePreferences);

        // Initialize SharedPreferences
        preferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Load existing preferences
        loadPreferences();

        // Set up button click listener to save preferences
        buttonSavePreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
            }
        });

        return view;
    }

    // Method to load existing preferences from SharedPreferences
    private void loadPreferences() {
        boolean missedCallLock = preferences.getBoolean("missedCallLock", false);
        int failedAttemptsThreshold = preferences.getInt("failedAttemptsThreshold", 3);
        boolean screenOffPictures = preferences.getBoolean("screenOffPictures", false);

        // Set the values in the corresponding views
        switchMissedCallLock.setChecked(missedCallLock);
        editTextFailedAttempts.setText(String.valueOf(failedAttemptsThreshold));
        switchScreenOffPictures.setChecked(screenOffPictures);
    }

    // Method to save preferences to SharedPreferences
    private void savePreferences() {
        boolean missedCallLock = switchMissedCallLock.isChecked();
        int failedAttemptsThreshold;
        try {
            failedAttemptsThreshold = Integer.parseInt(editTextFailedAttempts.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Please enter a valid number for failed attempts threshold.", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean screenOffPictures = switchScreenOffPictures.isChecked();

        // Save preferences in SharedPreferences
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("missedCallLock", missedCallLock);
        editor.putInt("failedAttemptsThreshold", failedAttemptsThreshold);
        editor.putBoolean("screenOffPictures", screenOffPictures);
        editor.apply();

        // Show a confirmation message
        Toast.makeText(getActivity(), "Preferences saved successfully!", Toast.LENGTH_SHORT).show();
    }
}
