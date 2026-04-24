package com.example.gear360.network;

import com.example.gear360.model.CameraInfo;
import com.example.gear360.model.CameraStateResponse;
import com.example.gear360.model.MediaListResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Gear360Api {

    @GET("/osc/info")
    Call<CameraInfo> getCameraInfo();

    @POST("/osc/state")
    Call<CameraStateResponse> getCameraState();

    @POST("/osc/commands/execute")
    Call<MediaListResponse> listFiles(@retrofit2.http.Body com.example.gear360.model.OscCommand command);
}