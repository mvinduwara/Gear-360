package com.example.gear360.ui;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.gear360.R;

public class FullScreenActivity extends AppCompatActivity {

    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        ImageView imgFullScreen = findViewById(R.id.imgFullScreen);
        Button btnDownload = findViewById(R.id.btnDownload);

        imageUrl = getIntent().getStringExtra("IMAGE_URL");

        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(imgFullScreen);
        }

        btnDownload.setOnClickListener(v -> downloadImage());
    }

    private void downloadImage() {
        if (imageUrl == null) return;

        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Gear 360 Media");
        request.setDescription("Downloading file from camera");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        String fileName = "Gear360_" + System.currentTimeMillis() + ".jpg";
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            manager.enqueue(request);
        }
    }
}