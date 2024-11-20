package com.example.antitheftandroidapp.Model;

public class User {
    // Private fields to represent user attributes
    private int userId; // System-generated user ID
    private String name;
    private String username;
    private String password;
    private String email;
    private String address;
    private String mobileNo;


    // Default constructor
    public User() {
        // Initialize with default values if needed
    }

    // Parameterized constructor to initialize user fields without user ID
    public User(String name, String username,String password, String email, String address, String mobileNo) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.mobileNo = mobileNo;

    }

    // Parameterized constructor to initialize user fields including user ID
    public User(int userId, String name, String username, String password, String email, String address, String mobileNo) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.mobileNo = mobileNo;
        this.password = password;
    }

    // Getter and setter methods for userId
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getter and setter methods for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter methods for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and setter methods for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and setter methods for address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter and setter methods for mobileNo
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    // Getter and setter methods for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
