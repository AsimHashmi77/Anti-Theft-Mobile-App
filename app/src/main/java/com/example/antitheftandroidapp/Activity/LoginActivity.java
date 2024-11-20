package com.example.antitheftandroidapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.antitheftandroidapp.DBHelper.DBHelper;
import com.example.antitheftandroidapp.Model.User;
import com.example.antitheftandroidapp.R;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        dbHelper = new DBHelper(this);

        buttonLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        User user = dbHelper.getUserByUsernameAndPassword(username, password);
        if (user != null) {
            SharedPreferences preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("userId", user.getUserId());
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonSignup(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();
    }
}
