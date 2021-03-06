package com.matteoveroni.pomodorotime.configs;

import com.google.gson.annotations.SerializedName;

public class Config {

    @SerializedName("app_name")
    private String appName;
    @SerializedName("language")
    private String language;
    @SerializedName("window_width")
    private Double windowWidth;
    @SerializedName("window_height")
    private Double windowHeight;
    @SerializedName("pomodoro_duration_in_min")
    private Double pomodoroDuration;
    @SerializedName("pomodoro_pause_duration_in_min")
    private Double pomodoroPauseDuration;
    @SerializedName("pomodoro_long_pause_duration_in_min")
    private Double pomodoroLongPauseDuration;
    @SerializedName("number_of_sessions_before_long_pause")
    private Integer numberOfSessionBeforeLongPause;
    @SerializedName("play_alarm_sound_on_completion")
    private Boolean playAlarmSoundOnCompletion;
    @SerializedName("allow_interrupt_pomodoro")
    private Boolean allowInterruptPomodoro;
    @SerializedName("allow_abort_pomodoro")
    private Boolean allowAbortPomodoro;
    @SerializedName("is_pomodoro_loop")
    private Boolean isPomodoroLoop;
    @SerializedName("is_pomodoro_pause_alert_fullscreen")
    private Boolean isPomodoroPauseAlertFullscreen;
    @SerializedName("allow_interrupt_pause")
    private Boolean allowInterruptPause;
    @SerializedName("allow_abort_pause")
    private Boolean allowAbortPause;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public double getPomodoroDuration() {
        return pomodoroDuration;
    }

    public void setPomodoroDuration(double pomodoroDuration) {
        this.pomodoroDuration = pomodoroDuration;
    }

    public double getPomodoroPauseDuration() {
        return pomodoroPauseDuration;
    }

    public void setPomodoroPauseDuration(double pomodoroPauseDuration) {
        this.pomodoroPauseDuration = pomodoroPauseDuration;
    }

    public double getPomodoroLongPauseDuration() {
        return pomodoroLongPauseDuration;
    }

    public void setPomodoroLongPauseDuration(double pomodoroLongPauseDuration) {
        this.pomodoroLongPauseDuration = pomodoroLongPauseDuration;
    }

    public int getNumberOfSessionBeforeLongPause() {
        return numberOfSessionBeforeLongPause;
    }

    public void setNumberOfSessionBeforeLongPause(int numberOfSessionBeforeLongPause) {
        this.numberOfSessionBeforeLongPause = numberOfSessionBeforeLongPause;
    }

    public boolean isPlayAlarmSoundOnCompletion() {
        return playAlarmSoundOnCompletion;
    }

    public void setPlayAlarmSoundOnCompletion(boolean playAlarmSoundOnCompletion) {
        this.playAlarmSoundOnCompletion = playAlarmSoundOnCompletion;
    }

    public boolean isAllowInterruptPomodoro() {
        return allowInterruptPomodoro;
    }

    public void setAllowInterruptPomodoro(boolean allowInterruptPomodoro) {
        this.allowInterruptPomodoro = allowInterruptPomodoro;
    }

    public boolean isAllowAbortPomodoro() {
        return allowAbortPomodoro;
    }

    public void setAllowAbortPomodoro(boolean allowAbortPomodoro) {
        this.allowAbortPomodoro = allowAbortPomodoro;
    }

    public boolean isPomodoroLoop() {
        return isPomodoroLoop;
    }

    public void setPomodoroLoop(boolean pomodoroLoop) {
        isPomodoroLoop = pomodoroLoop;
    }

    public boolean isPomodoroPauseAlertFullscreen() {
        return isPomodoroPauseAlertFullscreen;
    }

    public void setPomodoroPauseAlertFullscreen(boolean pomodoroPauseAlertFullscreen) {
        isPomodoroPauseAlertFullscreen = pomodoroPauseAlertFullscreen;
    }

    public boolean isAllowInterruptPause() {
        return allowInterruptPause;
    }

    public void setAllowInterruptPause(boolean allowInterruptPause) {
        this.allowInterruptPause = allowInterruptPause;
    }

    public boolean isAllowAbortPause() {
        return allowAbortPause;
    }

    public void setAllowAbortPause(boolean allowAbortPause) {
        this.allowAbortPause = allowAbortPause;
    }
}
