package com.example.gear360.ui;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gear360.R;
import com.example.gear360.model.CameraStateResponse;
import com.example.gear360.network.Gear360Api;
import com.example.gear360.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private TextView tvBatteryLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageView btnBack = findViewById(R.id.btnBack);
        tvBatteryLevel = findViewById(R.id.tvBatteryLevel);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout rowReset = findViewById(R.id.rowResetConnection);

        rowReset.setOnClickListener(v -> {
            showResetConfirmation();
        });
        fetchCameraState();
    }

    private void fetchCameraState() {
        Gear360Api api = RetrofitClient.getClient().create(Gear360Api.class);

        api.getCameraState().enqueue(new Callback<CameraStateResponse>() {
            @Override
            public void onResponse(Call<CameraStateResponse> call, Response<CameraStateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    float batteryDecimal = response.body().getState().getBatteryLevel();
                    int batteryPercentage = (int) (batteryDecimal * 100);
                    tvBatteryLevel.setText(batteryPercentage + "%");

                } else {
                    tvBatteryLevel.setText("Error");
                    Toast.makeText(SettingsActivity.this, "Failed to fetch state", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CameraStateResponse> call, Throwable t) {
                tvBatteryLevel.setText("Offline");
                Toast.makeText(SettingsActivity.this, "Network Error: Are you connected to the camera?", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void resetSavedData() {
        getSharedPreferences("Gear360Prefs", MODE_PRIVATE).edit().clear().apply();
        Toast.makeText(this, "Connection data cleared", Toast.LENGTH_SHORT).show();
    }
    private void showResetConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Reset Connection?")
                .setMessage("This will clear the saved Wi-Fi password. You will need to re-enter it next time you connect.")
                .setPositiveButton("Reset", (dialog, which) -> {
                    performReset();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void performReset() {
        SharedPreferences prefs = getSharedPreferences("Gear360Prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Connection data cleared", Toast.LENGTH_SHORT).show();
        finish();
    }
}