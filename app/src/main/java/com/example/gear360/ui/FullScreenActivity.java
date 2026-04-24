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

import android.app.AlertDialog;
import com.example.gear360.model.OscCommand;
import com.example.gear360.network.Gear360Api;
import com.example.gear360.network.RetrofitClient;
import java.util.Arrays;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullScreenActivity extends AppCompatActivity {

    private String imageUrl;
    private VrPanoramaView vrPanoramaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        vrPanoramaView = findViewById(R.id.vrPanoramaView);
        Button btnDownload = findViewById(R.id.btnDownload);
        Button btnDelete = findViewById(R.id.btnDelete);

        imageUrl = getIntent().getStringExtra("IMAGE_URL");

        if (imageUrl != null) {
            load360Image(imageUrl);
        }

        btnDownload.setOnClickListener(v -> downloadImage());
        btnDelete.setOnClickListener(v -> confirmDeletion());
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

    private void confirmDeletion() {
        if (imageUrl == null) return;

        new AlertDialog.Builder(this)
                .setTitle("Delete File")
                .setMessage("Are you sure you want to permanently delete this photo from the Gear 360?")
                .setPositiveButton("Delete", (dialog, which) -> executeDelete())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void executeDelete() {
        Toast.makeText(this, "Deleting...", Toast.LENGTH_SHORT).show();

        Gear360Api api = RetrofitClient.getClient().create(Gear360Api.class);
        OscCommand command = new OscCommand("camera.delete");

        command.addParameter("fileUrls", Arrays.asList(imageUrl));

        api.deleteFile(command).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(FullScreenActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(FullScreenActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(FullScreenActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}