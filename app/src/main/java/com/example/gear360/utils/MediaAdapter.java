package com.example.gear360.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gear360.R;
import com.example.gear360.model.MediaListResponse;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private Context context;
    private List<MediaListResponse.MediaEntry> mediaList;

    public MediaAdapter(Context context, List<MediaListResponse.MediaEntry> mediaList) {
        this.context = context;
        this.mediaList = mediaList;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_media, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        MediaListResponse.MediaEntry item = mediaList.get(position);
        holder.tvFileName.setText(item.getName());

        Glide.with(context)
                .load(item.getFileUrl())
                .centerCrop()
                .into(holder.imgThumbnail);
    }

    @Override
    public int getItemCount() {
        return mediaList != null ? mediaList.size() : 0;
    }

    public static class MediaViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView tvFileName;

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            tvFileName = itemView.findViewById(R.id.tvFileName);
        }
    }
}