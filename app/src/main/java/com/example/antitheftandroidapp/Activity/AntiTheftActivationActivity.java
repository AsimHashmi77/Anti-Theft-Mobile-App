package com.example.antitheftandroidapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.antitheftandroidapp.DBHelper.DBHelper;
import com.example.antitheftandroidapp.R;
import com.example.antitheftandroidapp.Model.User;

public class AntiTheftActivationActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private TextView textViewLoginAttempts;

    private int loginAttempts = 0;
    private static final int MAX_LOGIN_ATTEMPTS = 3; // Threshold for anti-theft activation
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_theft_activation);

        dbHelper = new DBHelper(this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewLoginAttempts = findViewById(R.id.textViewLoginAttempts);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Authenticate user using DBHelper
        User user = dbHelper.getUserByUsernameAndPassword(username, password);
        if (user != null) {
            // Successful login
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        } else {
            // Failed login
            loginAttempts++;
            textViewLoginAttempts.setText("Login Attempts: " + loginAttempts);

            if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                // Activate anti-theft measures
                activateAntiTheft();
            } else {
                // Display login failure message
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void activateAntiTheft() {
        // Implement anti-theft measures here, such as locking the device or sending notifications
        Toast.makeText(this, "Anti-Theft measures activated", Toast.LENGTH_SHORT).show();
        // For demonstration purposes, we'll just reset the login attempts
        loginAttempts = 0;
        textViewLoginAttempts.setText("Login Attempts: " + loginAttempts);
    }
}
