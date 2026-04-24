package com.example.gear360.model;

import com.google.gson.annotations.SerializedName;

public class CameraInfo {

    @SerializedName("manufacturer")
    private String manufacturer;

    @SerializedName("model")
    private String model;

    @SerializedName("serialNumber")
    private String serialNumber;

    @SerializedName("firmwareVersion")
    private String firmwareVersion;

    public String getManufacturer() { return manufacturer; }
    public String getModel() { return model; }
    public String getSerialNumber() { return serialNumber; }
    public String getFirmwareVersion() { return firmwareVersion; }
}