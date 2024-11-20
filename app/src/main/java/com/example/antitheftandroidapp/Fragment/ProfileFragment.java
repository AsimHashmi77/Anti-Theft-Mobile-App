package com.example.antitheftandroidapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.antitheftandroidapp.Activity.LoginActivity;
import com.example.antitheftandroidapp.DBHelper.DBHelper;
import com.example.antitheftandroidapp.Model.User;
import com.example.antitheftandroidapp.R;

public class ProfileFragment extends Fragment {
    private EditText editTextName, editTextUsername, editTextPassword, editTextEmail, editTextAddress, editTextMobile;
    private Button buttonUpdateProfile;
    private ImageView iconLogout;
    private DBHelper dbHelper;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editTextName = view.findViewById(R.id.editTextName);
        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        editTextMobile = view.findViewById(R.id.editTextMobile);
        buttonUpdateProfile = view.findViewById(R.id.buttonUpdateProfile);
        iconLogout = view.findViewById(R.id.iconLogout);

        dbHelper = new DBHelper(getActivity());

        SharedPreferences preferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(getActivity(), "User not logged in.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            return view;
        }

        currentUser = dbHelper.getUserById(userId);

        if (currentUser != null) {
            populateUserDetails(currentUser);
        } else {
            Toast.makeText(getActivity(), "User not found.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        buttonUpdateProfile.setOnClickListener(v -> handleUpdateProfile());
        iconLogout.setOnClickListener(v -> handleLogout());

        return view;
    }

    private void populateUserDetails(User user) {
        editTextName.setText(user.getName());
        editTextUsername.setText(user.getUsername());
        editTextPassword.setText(user.getPassword());
        editTextEmail.setText(user.getEmail());
        editTextAddress.setText(user.getAddress());
        editTextMobile.setText(user.getMobileNo());
    }

    private void handleUpdateProfile() {
        String name = editTextName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String mobileNo = editTextMobile.getText().toString().trim();

        currentUser.setName(name);
        currentUser.setUsername(username);
        currentUser.setPassword(password);
        currentUser.setEmail(email);
        currentUser.setAddress(address);
        currentUser.setMobileNo(mobileNo);

        int rowsAffected = dbHelper.updateUser(currentUser);

        if (rowsAffected > 0) {
            Toast.makeText(getActivity(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            currentUser = dbHelper.getUserById(currentUser.getUserId());
            populateUserDetails(currentUser);
        } else {
            Toast.makeText(getActivity(), "Profile update failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLogout() {
        SharedPreferences preferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("userId");
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
