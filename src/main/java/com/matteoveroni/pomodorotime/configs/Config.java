package com.matteoveroni.pomodorotime.configs;

import com.google.gson.annotations.SerializedName;

public class Config {

    @SerializedName("app_name")
    private String appName;
    @SerializedName("window_width")
    private double windowWidth;
    @SerializedName("window_height")
    private double windowHeight;
    @SerializedName("pomodoro_duration_in_min")
    private int pomodoroDurationMin;
    @SerializedName("pomodoro_pause_duration_in_min")
    private int pomodoroPauseDurationMin;
    @SerializedName("pomodoro_long_pause_duration_in_min")
    private int pomodoroLongPauseDurationMin;
    @SerializedName("number_of_sessions_before_pause")
    private int numberOfSessionBeforePause;

    public String getAppName() {
        return appName;
    }

    public double getWindowWidth() {
        return windowWidth;
    }

    public double getWindowHeight() {
        return windowHeight;
    }

    public int getPomodoroDurationMin() {
        return pomodoroDurationMin;
    }

    public int getPomodoroPauseDurationMin() {
        return pomodoroPauseDurationMin;
    }

    public int getPomodoroLongPauseDurationMin() {
        return pomodoroLongPauseDurationMin;
    }

    public int getNumberOfSessionBeforePause() {
        return numberOfSessionBeforePause;
    }
}
