package com.matteoveroni.pomodorotime.configs;

import com.google.gson.annotations.SerializedName;

public class Config {

    @SerializedName("app_name")
    private String appName;
    @SerializedName("window_width")
    private double windowWidth;
    @SerializedName("window_height")
    private double windowHeight;

    public String getAppName() {
        return appName;
    }

    public double getWindowWidth() {
        return windowWidth;
    }

    public double getWindowHeight() {
        return windowHeight;
    }
}
