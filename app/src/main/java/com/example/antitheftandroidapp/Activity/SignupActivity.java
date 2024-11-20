package com.example.antitheftandroidapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.antitheftandroidapp.DBHelper.DBHelper;
import com.example.antitheftandroidapp.R;
import com.example.antitheftandroidapp.Model.User;

public class SignupActivity extends AppCompatActivity {

    // Declare the views for user input and the register button
    private EditText editTextName, editTextUsername, editTextPassword, editTextEmail, editTextAddress, editTextMobile;
    private Button buttonRegister;

    // Declare the DBHelper instance for database operations
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the XML layout
        setContentView(R.layout.activity_signup);

        // Initialize the views
        editTextName = findViewById(R.id.editTextName);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextMobile = findViewById(R.id.editTextMobile);
        buttonRegister = findViewById(R.id.buttonRegister);

        // Initialize the DBHelper
        dbHelper = new DBHelper(this);

        // Set an OnClickListener on the register button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the user registration
                handleRegister();
            }
        });

    }

    // Method to handle the user registration
    private void handleRegister() {
        // Get the input values from the EditText views
        String name = editTextName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String mobileNo = editTextMobile.getText().toString().trim();

        // Validate the inputs
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || address.isEmpty() || mobileNo.isEmpty()) {
            // Show an error message if any fields are empty
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new User object with the input data
        User newUser = new User(name, username, password, email, address, mobileNo);

        // Insert the new user into the database using the DBHelper
        long userId = dbHelper.insertUser(newUser);

        // Check if the insertion was successful
        if (userId != -1) {
            // Show a success message
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();

            // Clear the input fields after successful registration
            editTextName.setText("");
            editTextUsername.setText("");
            editTextPassword.setText("");
            editTextEmail.setText("");
            editTextAddress.setText("");
            editTextMobile.setText("");

            // Navigate to LoginActivity after registration
            navigateToLoginActivity();
        } else {
            // Show an error message if registration failed
            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to handle navigation to LoginActivity
    private void navigateToLoginActivity() {
        // Create an intent to start the LoginActivity
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void buttonLogin(View view) {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
