package com.example.gear360.model;

import com.google.gson.annotations.SerializedName;

public class CameraStateResponse {

    @SerializedName("state")
    private State state;

    public State getState() { return state; }
    public static class State {

        @SerializedName("batteryLevel")
        private float batteryLevel;

        public float getBatteryLevel() { return batteryLevel; }
    }
}