package com.example.gear360.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MediaListResponse {
    @SerializedName("results")
    private Results results;

    public List<MediaEntry> getEntries() {
        return results != null ? results.entries : null;
    }

    private static class Results {
        @SerializedName("entries")
        List<MediaEntry> entries;
    }

    public static class MediaEntry {
        @SerializedName("name")
        private String name;

        @SerializedName("fileUrl")
        private String fileUrl;

        public String getName() { return name; }
        public String getFileUrl() { return fileUrl; }
    }
}