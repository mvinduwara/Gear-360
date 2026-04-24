package com.example.gear360.ui;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.gear360.R;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

public class FullScreenActivity extends AppCompatActivity {

    private String imageUrl;
    private VrPanoramaView vrPanoramaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        vrPanoramaView = findViewById(R.id.vrPanoramaView);
        Button btnDownload = findViewById(R.id.btnDownload);

        imageUrl = getIntent().getStringExtra("IMAGE_URL");

        if (imageUrl != null) {
            load360Image(imageUrl);
        }

        btnDownload.setOnClickListener(v -> downloadImage());
    }

    private void load360Image(String url) {
        Toast.makeText(this, "Loading 360° Sphere...", Toast.LENGTH_SHORT).show();

        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        VrPanoramaView.Options options = new VrPanoramaView.Options();
                        options.inputType = VrPanoramaView.Options.TYPE_MONO;
                        vrPanoramaView.loadImageFromBitmap(resource, options);
                    }

                    @Override
                    public void onLoadCleared(@Nullable android.graphics.drawable.Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable android.graphics.drawable.Drawable errorDrawable) {
                        Toast.makeText(FullScreenActivity.this, "Failed to load 360 image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void downloadImage() {
        if (imageUrl == null) return;

        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Gear 360 Media");
        request.setDescription("Downloading 360 Photo from Camera");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        String fileName = "Gear360_" + System.currentTimeMillis() + ".jpg";
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            manager.enqueue(request);
        }
    }

    @Override
    protected void onPause() {
        if (vrPanoramaView != null) vrPanoramaView.pauseRendering();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (vrPanoramaView != null) vrPanoramaView.resumeRendering();
    }

    @Override
    protected void onDestroy() {
        if (vrPanoramaView != null) vrPanoramaView.shutdown();
        super.onDestroy();
    }
}