package com.example.antitheftandroidapp.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class VideoRecordingService extends Service {

    private static final String TAG = "VideoRecordingService";
    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording = false;
    private SurfaceTexture mSurfaceTexture;

    @Override
    public void onCreate() {
        super.onCreate();
        mSurfaceTexture = new SurfaceTexture(0); // Initialize SurfaceTexture
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopRecording();
        super.onDestroy();
    }

    private void startRecording() {
        try {
            mCamera = Camera.open();
            mCamera.setPreviewTexture(mSurfaceTexture); // Set SurfaceTexture as preview surface

            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setCamera(mCamera);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            mMediaRecorder.setVideoSize(640, 480);
            mMediaRecorder.setVideoFrameRate(30);
            mMediaRecorder.setOutputFile(getOutputMediaFile().toString());

            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isRecording = true;
            Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error starting recording: " + e.getMessage());
            stopRecording();
        }
    }

    private void stopRecording() {
        if (isRecording) {
            try {
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
                mCamera.release();
                mCamera = null;
                isRecording = false;
                Toast.makeText(getApplicationContext(), "Recording stopped", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e(TAG, "Error stopping recording: " + e.getMessage());
            }
        }
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "AntiTheftVideos");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(TAG, "Failed to create directory");
                return null;
            }
        }

        String timeStamp = String.valueOf(System.currentTimeMillis());
        return new File(mediaStorageDir.getPath() + File.separator +
                "VID_" + timeStamp + ".mp4");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
