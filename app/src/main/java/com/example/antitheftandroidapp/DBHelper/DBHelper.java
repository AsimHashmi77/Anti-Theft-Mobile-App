package com.example.antitheftandroidapp.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.antitheftandroidapp.Model.Device;
import com.example.antitheftandroidapp.Model.User;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    // Database Name
    private static final String DATABASE_NAME = "AntiTheftAndroidApp.db";
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_USERS = "Users";
    public static final String TABLE_DEVICES = "Devices";
    public static final String TABLE_LOGS = "Logs";

    // Columns in the Users table
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_MOBILE_NO = "mobileNo";

    // Columns in the Devices table
    public static final String COLUMN_DEVICE_ID = "deviceId";
    public static final String COLUMN_DEVICE_NAME = "deviceName";
    public static final String COLUMN_DEVICE_EMAIL = "deviceEmail";

    // Columns in the Logs table
    public static final String COLUMN_LOG_ID = "logId";
    public static final String COLUMN_LOG_MESSAGE = "logMessage";
    public static final String COLUMN_LOG_TIMESTAMP = "logTimestamp";

    // Create statement for Users table
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_USERNAME + " TEXT NOT NULL, "
            + COLUMN_PASSWORD + " TEXT NOT NULL, "
            + COLUMN_EMAIL + " TEXT NOT NULL, "
            + COLUMN_ADDRESS + " TEXT, "
            + COLUMN_MOBILE_NO + " TEXT);";

    // Create statement for Devices table
    private static final String CREATE_DEVICES_TABLE = "CREATE TABLE " + TABLE_DEVICES + " ("
            + COLUMN_DEVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DEVICE_NAME + " TEXT NOT NULL, "
            + COLUMN_DEVICE_EMAIL + " TEXT NOT NULL);";

    // Create statement for Logs table
    private static final String CREATE_LOGS_TABLE = "CREATE TABLE " + TABLE_LOGS + " ("
            + COLUMN_LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_LOG_MESSAGE + " TEXT NOT NULL, "
            + COLUMN_LOG_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

    // Constructor for DBHelper
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // onCreate method to create the tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the Users, Devices, and Logs tables
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_DEVICES_TABLE);
        db.execSQL(CREATE_LOGS_TABLE);
    }

    // onUpgrade method to handle database upgrades (schema changes)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables and recreate them
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS);
        onCreate(db);
    }

    // Method to insert a user into the Users table
    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, user.getName());
        contentValues.put(COLUMN_USERNAME, user.getUsername());
        contentValues.put(COLUMN_PASSWORD, user.getPassword());
        contentValues.put(COLUMN_EMAIL, user.getEmail());
        contentValues.put(COLUMN_ADDRESS, user.getAddress());
        contentValues.put(COLUMN_MOBILE_NO, user.getMobileNo());
        long userId = db.insert(TABLE_USERS, null, contentValues);
        db.close(); // Close the database connection
        return userId;
    }

    // Method to fetch a user by username and password for authentication
    public User getUserByUsernameAndPassword(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{username, password}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = getUserFromCursor(cursor);
            cursor.close();
            db.close();
            return user;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null; // Return null if no user was found or an error occurred
    }

    // Method to fetch a user by ID
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            User user = getUserFromCursor(cursor);
            cursor.close();
            return user;
        }
        cursor.close();
        return null; // User not found
    }

    // Method to update a user's information
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, user.getName());
        contentValues.put(COLUMN_USERNAME, user.getUsername());
        contentValues.put(COLUMN_PASSWORD, user.getPassword());
        contentValues.put(COLUMN_EMAIL, user.getEmail());
        contentValues.put(COLUMN_ADDRESS, user.getAddress());
        contentValues.put(COLUMN_MOBILE_NO, user.getMobileNo());
        int rowsAffected = db.update(TABLE_USERS, contentValues, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(user.getUserId())});
        db.close(); // Close the database connection
        return rowsAffected;
    }

    // Helper method to create a User object from a Cursor
    private User getUserFromCursor(Cursor cursor) {
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
        String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
        String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
        String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS));
        String mobileNo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOBILE_NO));
        return new User(userId, name, username, password, email, address, mobileNo);
    }

    // Method to update the login attempts for a user
    public void updateLoginAttempts(String username, int attempts) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("loginAttempts", attempts);
        db.update(TABLE_USERS, values, "username=?", new String[]{username});
        db.close();
    }

    // Method to insert a device into the Devices table
    public long insertDevice(Device device) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DEVICE_NAME, device.getDeviceName());
        contentValues.put(COLUMN_DEVICE_EMAIL, device.getDeviceEmail());
        long deviceId = db.insert(TABLE_DEVICES, null, contentValues);
        db.close();
        return deviceId;
    }

    // Method to fetch all devices from the Devices table
    public List<Device> getAllDevices() {
        List<Device> deviceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DEVICES, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Device device = getDeviceFromCursor(cursor);
                deviceList.add(device);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return deviceList;
    }

    // Helper method to create a Device object from a Cursor
    private Device getDeviceFromCursor(Cursor cursor) {
        int deviceId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DEVICE_ID));
        String deviceName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEVICE_NAME));
        String deviceEmail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEVICE_EMAIL));
        return new Device(deviceId, deviceName, deviceEmail);
    }

    // Method to insert a log message into the Logs table
    public long insertLog(String logMessage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LOG_MESSAGE, logMessage);
        long logId = db.insert(TABLE_LOGS, null, contentValues);
        db.close(); // Close the database connection
        return logId;
    }

    // Method to retrieve all logs from the Logs table
    public List<String> getAllLogs() {
        List<String> logList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOGS, new String[]{COLUMN_LOG_MESSAGE}, null, null, null, null, COLUMN_LOG_TIMESTAMP + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String logMessage = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOG_MESSAGE));
                logList.add(logMessage);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return logList;
    }

    public String getRegisteredMobile() {
        String mobileNumber = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_MOBILE_NO}, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(COLUMN_MOBILE_NO);
                if (columnIndex >= 0) {
                    mobileNumber = cursor.getString(columnIndex);
                } else {
                    Log.e("DBHelper", "Column index for mobile number is invalid.");
                }
            }
            cursor.close();
        } else {
            Log.e("DBHelper", "Cursor is null.");
        }
        db.close();
        return mobileNumber;
    }

    public String getRegisteredEmail() {
        String email = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_EMAIL}, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                if (columnIndex >= 0) {
                    email = cursor.getString(columnIndex);
                } else {
                    Log.e("DBHelper", "Column index for email is invalid.");
                }
            }
            cursor.close();
        } else {
            Log.e("DBHelper", "Cursor is null.");
        }
        db.close();
        return email;
    }

}
