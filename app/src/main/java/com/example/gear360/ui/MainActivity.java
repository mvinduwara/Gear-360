package com.example.gear360.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gear360.R;
import com.example.gear360.model.CameraInfo;
import com.example.gear360.network.Gear360Api;
import com.example.gear360.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        LinearLayout menuGallery = findViewById(R.id.menuGallery);
        LinearLayout menuSettings = findViewById(R.id.menuSettings);
        TextView btnConnect = findViewById(R.id.btnConnect);
        TextView tvConnectingDots = findViewById(R.id.tvConnectingDots);

        menuGallery.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GalleryActivity.class)));
        menuSettings.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SettingsActivity.class)));

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnConnect.setText("Connecting...");

                Gear360Api api = RetrofitClient.getClient().create(Gear360Api.class);

                api.getCameraInfo().enqueue(new Callback<CameraInfo>() {
                    @Override
                    public void onResponse(Call<CameraInfo> call, Response<CameraInfo> response) {
                        btnConnect.setText("Get connected"); 

                        if (response.isSuccessful() && response.body() != null) {
                            CameraInfo info = response.body();
                            showSuccessDialog(info);
                        } else {
                            Toast.makeText(MainActivity.this, "Error reading from camera. Is it turned on?", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CameraInfo> call, Throwable t) {
                        btnConnect.setText("Get connected");
                        Toast.makeText(MainActivity.this, "Connection failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void showSuccessDialog(CameraInfo info) {
        String details = "Manufacturer: " + info.getManufacturer() + "\n"
                + "Model: " + info.getModel() + "\n"
                + "Firmware: " + info.getFirmwareVersion() + "\n"
                + "Serial: " + info.getSerialNumber();

        new AlertDialog.Builder(this)
                .setTitle("Connected Successfully!")
                .setMessage(details)
                .setPositiveButton("OK", null)
                .show();
    }
}