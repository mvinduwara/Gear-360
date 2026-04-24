package com.example.gear360.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gear360.R;
import com.example.gear360.model.MediaListResponse;
import com.example.gear360.model.OscCommand;
import com.example.gear360.network.Gear360Api;
import com.example.gear360.network.RetrofitClient;
import com.example.gear360.utils.MediaAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayout emptyStateLayout;
    private com.example.gear360.utils.MediaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        ImageView btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerViewMedia);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);

        // Setup 3-column grid
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        btnBack.setOnClickListener(v -> finish());

        fetchCameraFiles();
    }

    private void fetchCameraFiles() {
        Gear360Api api = RetrofitClient.getClient().create(Gear360Api.class);

        OscCommand command = new OscCommand("camera.listFiles");
        command.addParameter("fileType", "all");
        command.addParameter("entryCount", 50);
        command.addParameter("maxThumbSize", 160);

        api.listFiles(command).enqueue(new Callback<MediaListResponse>() {
            @Override
            public void onResponse(Call<MediaListResponse> call, Response<MediaListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MediaListResponse.MediaEntry> entries = response.body().getEntries();

                    if (entries != null && !entries.isEmpty()) {
                        emptyStateLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        adapter = new MediaAdapter(GalleryActivity.this, entries);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(GalleryActivity.this, "Failed to load media.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MediaListResponse> call, Throwable t) {
                Toast.makeText(GalleryActivity.this, "Network error. Are you connected?", Toast.LENGTH_SHORT).show();
            }
        });
    }
}