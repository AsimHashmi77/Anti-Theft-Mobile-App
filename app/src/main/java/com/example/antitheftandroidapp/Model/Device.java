package com.example.antitheftandroidapp.Model;

public class Device {

    private int deviceId;
    private String deviceName;
    private String deviceEmail;

    public Device(int deviceId, String deviceName, String deviceEmail) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceEmail = deviceEmail;
    }

    public Device(String deviceName, String deviceEmail) {
        this.deviceName = deviceName;
        this.deviceEmail = deviceEmail;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceEmail() {
        return deviceEmail;
    }

    public void setDeviceEmail(String deviceEmail) {
        this.deviceEmail = deviceEmail;
    }
}
