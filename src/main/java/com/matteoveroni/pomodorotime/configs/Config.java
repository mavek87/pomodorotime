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
    private int pomodoroDuration;
    @SerializedName("pomodoro_pause_duration_in_min")
    private int pomodoroPauseDuration;
    @SerializedName("pomodoro_long_pause_duration_in_min")
    private int pomodoroLongPauseDuration;
    @SerializedName("number_of_sessions_before_pause")
    private int numberOfSessionBeforePause;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public double getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(double windowWidth) {
        this.windowWidth = windowWidth;
    }

    public double getWindowHeight() {
        return windowHeight;
    }

    public void setWindowHeight(double windowHeight) {
        this.windowHeight = windowHeight;
    }

    public int getPomodoroDuration() {
        return pomodoroDuration;
    }

    public void setPomodoroDuration(int pomodoroDuration) {
        this.pomodoroDuration = pomodoroDuration;
    }

    public int getPomodoroPauseDuration() {
        return pomodoroPauseDuration;
    }

    public void setPomodoroPauseDuration(int pomodoroPauseDurationMin) {
        this.pomodoroPauseDuration = pomodoroPauseDurationMin;
    }

    public int getPomodoroLongPauseDuration() {
        return pomodoroLongPauseDuration;
    }

    public void setPomodoroLongPauseDuration(int pomodoroLongPauseDurationMin) {
        this.pomodoroLongPauseDuration = pomodoroLongPauseDurationMin;
    }

    public int getNumberOfSessionBeforePause() {
        return numberOfSessionBeforePause;
    }

    public void setNumberOfSessionBeforeLongPause(int numberOfSessionBeforePause) {
        this.numberOfSessionBeforePause = numberOfSessionBeforePause;
    }
}
