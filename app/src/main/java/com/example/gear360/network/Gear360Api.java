package com.example.gear360.network;

import com.example.gear360.model.CameraInfo;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Gear360Api {

    // 1. Get Camera Info - Now returns our parsed CameraInfo object!
    @GET("/osc/info")
    Call<CameraInfo> getCameraInfo();

    // 2. Get Camera State (We will leave this as ResponseBody for now)
    @POST("/osc/state")
    Call<ResponseBody> getCameraState();
}