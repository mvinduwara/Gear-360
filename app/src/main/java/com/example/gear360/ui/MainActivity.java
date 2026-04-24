package com.example.gear360.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gear360.R;
import com.example.gear360.model.CameraInfo;
import com.example.gear360.network.Gear360Api;
import com.example.gear360.network.RetrofitClient;
import com.example.gear360.network.WifiConnectionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView btnConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        LinearLayout menuGallery = findViewById(R.id.menuGallery);
        LinearLayout menuSettings = findViewById(R.id.menuSettings);
        btnConnect = findViewById(R.id.btnConnect);
        TextView tvConnectingDots = findViewById(R.id.tvConnectingDots);

        menuGallery.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GalleryActivity.class)));
        menuSettings.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SettingsActivity.class)));

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptForCameraPassword();
            }
        });
    }

    private void promptForCameraPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Camera Password");
        builder.setMessage("Enter the 8-digit password shown on your Gear 360 screen.");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Connect", (dialog, which) -> {
            String password = input.getText().toString();
            if (password.length() >= 8) {
                startWifiConnection(password);
            } else {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void startWifiConnection(String password) {
        btnConnect.setText("Connecting...");

        WifiConnectionManager.connectToGear360(this, password, new WifiConnectionManager.ConnectionCallback() {
            @Override
            public void onConnected() {
                runOnUiThread(() -> {
                    btnConnect.setText("Connected!");
                    fetchCameraInfo();
                });
            }

            @Override
            public void onFailed(String reason) {
                runOnUiThread(() -> {
                    btnConnect.setText("Get connected");
                    Toast.makeText(MainActivity.this, reason, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void fetchCameraInfo() {
        Gear360Api api = RetrofitClient.getClient().create(Gear360Api.class);
        api.getCameraInfo().enqueue(new Callback<CameraInfo>() {
            @Override
            public void onResponse(Call<CameraInfo> call, Response<CameraInfo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "Successfully linked to: " + response.body().getModel(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CameraInfo> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Wi-Fi connected, but API failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}