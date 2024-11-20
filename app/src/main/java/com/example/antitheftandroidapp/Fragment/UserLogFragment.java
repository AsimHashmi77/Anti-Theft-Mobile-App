package com.example.antitheftandroidapp.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.antitheftandroidapp.Adapter.LogAdapter;
import com.example.antitheftandroidapp.Model.LogItem;
import com.example.antitheftandroidapp.R;
import com.example.antitheftandroidapp.Model.UserLog;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class UserLogFragment extends Fragment {

    private RecyclerView logRecyclerView;
    private LogAdapter adapter;
    private UserLog userLog;
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("UserLogFragment", "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_user_log, container, false);

        // Initialize UserLog instance
        userLog = new UserLog(getActivity());

        // Find RecyclerView in layout
        logRecyclerView = view.findViewById(R.id.logRecyclerView);

        // Set layout manager for RecyclerView
        logRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize adapter
        adapter = new LogAdapter(new ArrayList<>());
        logRecyclerView.setAdapter(adapter);

        // Check and request permissions
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        } else {
            Log.d("UserLogFragment", "Permissions granted, displaying logs");
            // Permissions already granted, display logs
            displayLogs();
        }

        // Log an event for demonstration
        userLog.logEvent("UserLogFragment opened");

        // Display logs after logging event
        displayLogs();

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, display logs
                Log.d("UserLogFragment", "Permissions granted in onRequestPermissionsResult");
                displayLogs();
            } else {
                // Permission denied, handle appropriately
                Log.d("UserLogFragment", "Permissions denied in onRequestPermissionsResult");
            }
        }
    }

    private void displayLogs() {
        // Fetch logs from the log file
        List<LogItem> logs = fetchLogsFromFile();
        Log.d("UserLogFragment", "Fetched " + logs.size() + " logs from file");

        // Update adapter with fetched logs
        adapter.updateData(logs);
        Log.d("UserLogFragment", "Adapter updated with " + logs.size() + " logs");
    }

    private List<LogItem> fetchLogsFromFile() {
        List<LogItem> logs = new ArrayList<>();
        File logFile = new File(getActivity().getExternalFilesDir(null), "user_log.txt");

        Log.d("UserLogFragment", "Log file path: " + logFile.getAbsolutePath());
        Log.d("UserLogFragment", "Log file exists: " + logFile.exists());
        Log.d("UserLogFragment", "Log file can read: " + logFile.canRead());

        try (Scanner scanner = new Scanner(new FileReader(logFile))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Log.d("UserLogFragment", "Reading line: " + line);
                // Split the log line into timestamp, level, tag, and message
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    String timestamp = parts[0];
                    String level = parts[1];
                    String tag = parts[2];
                    String message = parts[3];
                    // Create a LogItem object and add it to the list
                    logs.add(new LogItem(timestamp, level, tag, message));
                    Log.d("UserLogFragment", "Log added: " + line);
                } else {
                    Log.d("UserLogFragment", "Log line format incorrect: " + line);
                }
            }
            if (logs.isEmpty()) {
                Log.d("UserLogFragment", "No logs found in file");
            } else {
                Log.d("UserLogFragment", "Total logs found: " + logs.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("UserLogFragment", "Error reading log file", e);
        }

        return logs;
    }


}
