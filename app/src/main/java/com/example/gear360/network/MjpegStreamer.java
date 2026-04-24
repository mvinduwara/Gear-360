package com.example.gear360.network;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MjpegStreamer extends Thread {

    private InputStream inputStream;
    private ImageView targetImageView;
    private Activity activity;
    private volatile boolean isRunning = true;

    public MjpegStreamer(InputStream inputStream, ImageView targetImageView, Activity activity) {
        this.inputStream = inputStream;
        this.targetImageView = targetImageView;
        this.activity = activity;
    }

    public void stopStream() {
        isRunning = false;
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception e) {
            Log.e("MjpegStreamer", "Error closing stream", e);
        }
    }

    @Override
    public void run() {
        ByteArrayOutputStream frameBuffer = new ByteArrayOutputStream();
        int prevByte = -1;
        int currentByte;
        boolean isCapturingFrame = false;

        try {
            while (isRunning && (currentByte = inputStream.read()) != -1) {
                if (!isCapturingFrame) {
                    if (prevByte == 0xFF && currentByte == 0xD8) {
                        isCapturingFrame = true;
                        frameBuffer.write(prevByte);
                        frameBuffer.write(currentByte);
                    }
                } else {
                    frameBuffer.write(currentByte);
                    if (prevByte == 0xFF && currentByte == 0xD9) {
                        byte[] frameData = frameBuffer.toByteArray();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(frameData, 0, frameData.length);

                        if (bitmap != null && isRunning) {
                            activity.runOnUiThread(() -> {
                                if (isRunning) targetImageView.setImageBitmap(bitmap);
                            });
                        }
                        frameBuffer.reset();
                        isCapturingFrame = false;
                    }
                }
                prevByte = currentByte;
            }
        } catch (Exception e) {
            Log.d("MjpegStreamer", "Stream stopped: " + e.getMessage());
        } finally {
            stopStream();
        }
    }
}