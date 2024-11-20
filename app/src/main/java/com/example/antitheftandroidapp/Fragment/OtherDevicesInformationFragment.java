package com.example.antitheftandroidapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.antitheftandroidapp.DBHelper.DBHelper;
import com.example.antitheftandroidapp.Model.Device;
import com.example.antitheftandroidapp.Adapter.DeviceAdapter;
import com.example.antitheftandroidapp.R;

import java.util.List;

public class OtherDevicesInformationFragment extends Fragment {

    private EditText editTextDeviceName, editTextDeviceEmail;
    private Button buttonAddDevice;
    private RecyclerView recyclerViewDevices;
    private DeviceAdapter deviceAdapter;
    private List<Device> deviceList;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_devices_information, container, false);

        editTextDeviceName = view.findViewById(R.id.editTextDeviceName);
        editTextDeviceEmail = view.findViewById(R.id.editTextDeviceEmail);
        buttonAddDevice = view.findViewById(R.id.buttonAddDevice);
        recyclerViewDevices = view.findViewById(R.id.recyclerViewDevices);
        dbHelper = new DBHelper(getActivity());

        loadDevices();

        buttonAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDevice();
            }
        });

        return view;
    }

    private void loadDevices() {
        deviceList = dbHelper.getAllDevices();
        deviceAdapter = new DeviceAdapter(getActivity(), deviceList);
        recyclerViewDevices.setAdapter(deviceAdapter);
        recyclerViewDevices.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void addDevice() {
        String deviceName = editTextDeviceName.getText().toString().trim();
        String deviceEmail = editTextDeviceEmail.getText().toString().trim();

        if (!deviceName.isEmpty() && !deviceEmail.isEmpty()) {
            Device device = new Device(deviceName, deviceEmail);
            long result = dbHelper.insertDevice(device);
            if (result != -1) {
                device.setDeviceId((int) result);
                deviceList.add(device);
                deviceAdapter.notifyDataSetChanged();
                editTextDeviceName.setText("");
                editTextDeviceEmail.setText("");
            } else {
                Toast.makeText(getActivity(), "Failed to add device. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Please enter device name and email.", Toast.LENGTH_SHORT).show();
        }
    }
}
